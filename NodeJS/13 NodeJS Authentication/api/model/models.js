const mongoose = require('mongoose');
const config = require('../config/database.js');


/*
 *
 */
var connection = null;
var Receita = null;
var Usuario = null;


/*
 * Conecta ao banco de dados
 */
async function connect() {
    if (!connection) {
        await mongoose.connect(config.connectionString);
        connection = mongoose.connection;
        connection.on("error", console.error.bind(console, "MongoDB connection error:"));
        
        var receitaSchema = { 
            nome: String,
            preparo: String,
            ingredientes: [{
                item : String,
                qtde : String
            }]
        };

        var usuarioSchema = { 
            dataRegistro: Date,
            dataAtualizacao: Date,
            nome: String,
            email: String,
            senha: String,
            tokenSenha: String,
            dataTokenSenha: Date,
            falhasLogin: { type: Number, default: 0 },
            bloqueado: { type: Boolean, default: false },
            administrador: { type: Boolean, default: false }
        };

        Receita = mongoose.model('Receita', receitaSchema, 'receitas');
        Usuario = mongoose.model('Usuario', usuarioSchema, 'usuarios');
    }

    return { connection, Usuario, Receita } 
}

module.exports = { connect };
