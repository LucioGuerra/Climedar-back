import {Eureka} from 'eureka-js-client';
import {ApolloGateway, IntrospectAndCompose, RemoteGraphQLDataSource} from '@apollo/gateway';
import {ApolloServer} from 'apollo-server';


const eurekaClient = new Eureka({
    instance: {
        app: 'apollo-federation',
        hostName: 'apollo-federation',
        ipAddr: 'apollo-federation',
        statusPageUrl: 'http://apollo-federation:4000',
        port: {
            '$': 4000,
            '@enabled': true,
        },
        preferIpAddress: true,
        vipAddress: 'apollo-federation',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        // Cambia estos valores según la configuración de tu Eureka Server
        host: 'eureka-sv',
        port: 8761,
        servicePath: '/eureka/apps/'
    },
});

class AuthenticatedDataSource extends RemoteGraphQLDataSource {
    willSendRequest({request, context}) {
        if (context.token) {
            request.http.headers.set('Authorization', context.token);
        }
    }
}

eurekaClient.start(async (error) => {
    if (error) {
        console.error('❌ Error registrando en Eureka:', error);
        process.exit(1);
    } else {
        console.log('✅ Registro en Eureka completado');

        // Lista de servicios a descubrir en Eureka
        const servicesToDiscover = [
            {name: 'medical-service-sv', path: '/graphql'},
            {name: 'consultation-sv', path: '/graphql'},
            {name: 'doctor-sv', path: '/graphql'},
            {name: 'patient-sv', path: '/graphql'},
        ];

        // Función para obtener la URL de los servicios desde Eureka
        const serviceList = servicesToDiscover.map(service => {
            const instances = eurekaClient.getInstancesByAppId(service.name.toUpperCase());

            if (instances && instances.length > 0) {
                const instance = instances[0]; // Tomar la primera instancia disponible
                const host = instance.ipAddr || instance.hostName;
                const port = instance.port && instance.port['$'];
                const url = `http://${host}:${port}${service.path}`;
                console.log(`🔗 Servicio ${service.name} encontrado en ${url}`);
                return {name: service.name, url};
            } else {
                console.error(`⚠️ No se encontró instancia para ${service.name} en Eureka`);
                return null;
            }
        }).filter(s => s !== null); // Filtrar servicios no encontrados

        if (serviceList.length === 0) {
            console.error('🚨 No se encontraron servicios disponibles en Eureka. Cerrando servidor.');
            process.exit(1);
        }

        // Configurar ApolloGateway con introspección automática
        const gateway = new ApolloGateway({
            supergraphSdl: new IntrospectAndCompose({
                subgraphs: serviceList,
            }),
            buildService({name, url}) {
                return new AuthenticatedDataSource({url});
            },
        });

        // Iniciar Apollo Server
        const server = new ApolloServer({
            gateway,
            subscriptions: false,
            introspection: true,
            context: ({req, res}) => {
                if (res && res.removeHeader) {
                    res.removeHeader("Access-Control-Allow-Origin");
                }
                const token = req.headers.authorization || '';
                return {token};
            },
            plugins: [{
                requestDidStart: async () => ({
                    willSendResponse: async ({response}) => {
                        // 🔥 Borra manualmente los encabezados antes de enviar la respuesta
                        if (response.http && response.http.headers) {
                            response.http.headers.delete("Access-Control-Allow-Origin");
                        }
                    }
                })
            }]
        });

        server.listen({port: 4000}).then(({url}) => {
            console.log(`🚀 Gateway listo en ${url}`);
        });
    }
});

