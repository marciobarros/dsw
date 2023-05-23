const mongoose = require('mongoose');

module.exports = {
    database: {
        connectionString: 'mongodb://127.0.0.1:27017/dsw'
    },

    auth: {
        tokenKey: 'aiusa7s8sdjm,d0-klaj'
    },

    frontend: {
        hostname: 'http://localhost:8080/'
    },

    sendGrid: {
        apiKey: 'sendgrid-api-key',
        email: 'marcio.barros@uniriotec.br'
    }
}