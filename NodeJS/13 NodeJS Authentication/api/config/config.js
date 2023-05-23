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
        apiKey: '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',
        email: 'marcio.barros@uniriotec.br'
    }
}