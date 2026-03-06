const swaggerAutogen = require('swagger-autogen')({ openapi: '3.0.0' });

const doc = {
    info: {
        version: "1.0.0",
        title: "Clientes API",
        description: "API que permite registrar e consultar clientes mantidos em memória."
    },
    servers: [
        { url: 'http://localhost:3000' }
    ],
    /*components: {
        securitySchemes:{
            bearerAuth: { type: 'http', scheme: 'bearer' }
        }
    }*/
};

const outputFile = './swagger-output.json';
const endpointsFiles = ['./index.js'];

swaggerAutogen(outputFile, endpointsFiles, doc).then(() => {
    require('./index');
});