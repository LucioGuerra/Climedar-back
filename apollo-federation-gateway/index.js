import {Eureka} from 'eureka-js-client';

import {ApolloGateway, RemoteGraphQLDataSource} from '@apollo/gateway';
import { ApolloServer } from 'apollo-server';


const eurekaClient = new Eureka({
    instance: {
        app: 'apollo-federation',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:4000',
        port: {
            '$': 4000,
            '@enabled': true,
        },
        vipAddress: 'apollo-federation',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        },
    },
    eureka: {
        // Cambia estos valores según la configuración de tu Eureka Server
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/'
    },
});

eurekaClient.start(error => {
    if (error) {
        console.error('Error registrando en Eureka:', error);
    } else {
        console.log('Registro en Eureka completado');
    }
});



class AuthenticatedDataSource extends RemoteGraphQLDataSource {
    willSendRequest({ request, context }) {
        // Si en el contexto existe el token, lo agrega a la cabecera Authorization
        if (context.token) {
            request.http.headers.set('Authorization', context.token);
        }
    }
}


// Configurar el gateway con los subgráficos
const gateway = new ApolloGateway({
    serviceList: [  // Se usa `serviceList` en lugar de `IntrospectAndCompose`
        { name: 'medical-service-sv', url: 'http://localhost:8081/graphql' },
        { name: 'consultation-sv', url: 'http://localhost:8086/graphql' },
        { name: 'doctor-sv', url: 'http://localhost:8083/graphql' },
        { name: 'patient-sv', url: 'http://localhost:8082/graphql' },
    ],
    buildService({ name, url }) {
        return new AuthenticatedDataSource({ url });
    },
});

const server = new ApolloServer({
    gateway,
    subscriptions: false,
    cors: true, // Permitir CORS para evitar bloqueos
    context: ({ req }) => {
        // Extraer el token de la cabecera Authorization
        const token = req.headers.authorization || '';
        return { token };
    },
});

server.listen({ port: 4000 }).then(({ url }) => {
    console.log(`🚀 Gateway listo en ${url}`);
});

