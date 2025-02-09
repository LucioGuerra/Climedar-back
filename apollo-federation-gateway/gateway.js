import { ApolloGateway } from '@apollo/gateway';
import { ApolloServer } from 'apollo-server';

// Configurar el gateway con los subgráficos
const gateway = new ApolloGateway({
    serviceList: [  // Se usa `serviceList` en lugar de `IntrospectAndCompose`
        { name: 'medical-service-sv', url: 'http://localhost:8081/graphql' },
        { name: 'consultation-sv', url: 'http://localhost:8086/graphql' },
        { name: 'doctor-sv', url: 'http://localhost:8083/graphql' },
        { name: 'patient-sv', url: 'http://localhost:8082/graphql' },
    ]
});

const server = new ApolloServer({
    gateway,
    subscriptions: false,
    cors: true, // Permitir CORS para evitar bloqueos
});

server.listen({ port: 4000 }).then(({ url }) => {
    console.log(`🚀 Gateway listo en ${url}`);
});
