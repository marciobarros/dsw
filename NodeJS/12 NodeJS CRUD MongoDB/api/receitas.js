var express = require('express');
const mongoose = require('mongoose');

var router = express.Router();

var connectionString = 'mongodb+srv://user-test:Ab123456@cluster0.5tlslxx.mongodb.net/exemplo?retryWrites=true&w=majority';

var connection = null;

var Receita = null;


/*
 * Conecta ao banco de dados
 */
async function connect() {
    if (connection && Receita) {
        return { connection, Receita };
    }

    await mongoose.connect(connectionString);

    connection = mongoose.connection;
    connection.on("error", console.error.bind(console, "MongoDB connection error:"));
    
    Receita = mongoose.model('Receita', { 
        id: String,
        nome: String,
        preparo: String,
        ingredientes: [{
            item : String,
            qtde : String
        }]
    });
    
    return { connection, Receita } 
}


/*
 * Retorna a lista paginada de receitas
 */
router.get('/', async function(req, res){
    var page = req.query.page || 1;
    var itemsPerPage = req.query.itemsPage || 5;
    var filter = req.query.filter || '';

    var db = await connect();
    const regex = new RegExp(filter, "i");

    var queryCount = db.Receita
        .find({ nome: { $regex: regex } });

    var totalItems = await queryCount.count();

    var queryList = db.Receita
        .find({ nome: { $regex: regex } })
        .limit(itemsPerPage)
        .skip(itemsPerPage * (page-1));

    if (req.query.sortField) {
        var sortField = req.query.sortField || '';
        var sortDesc = (req.query.sortDesc == "true") ? "descending" : "ascending";
        queryList = queryList.sort([[sortField, sortDesc]]);
    }

    var items = await queryList.exec();
    res.json({ items, totalItems });
});


/*
 * Retorna uma receita, dado o seu ID
 */
router.get('/:id', async function(req, res){
    var db = await connect();
    var receita = await db.Receita.findById(req.params.id);
    res.json(receita);
});


/*
 * Insere ou atualiza uma receita
 */
router.post('/', async function(req, res){
    var id = req.body._id;
    var nome = req.body.nome;
    var preparo = req.body.preparo;
    var ingredientes = req.body.ingredientes;

    if (verificaRegrasNegocio(nome, preparo, ingredientes, res)) {
        if (id == "") {
            insereReceita(nome, preparo, ingredientes, res);
        }
        else {
            atualizaReceita(id, nome, preparo, ingredientes, res);
        }
    }
});


/*
 * Verifica as regras de negócio em uma nova receita
 */
function verificaRegrasNegocio(nome, preparo, ingredientes, res) {

    if (!nome || nome.length == 0) {
        res.status(400).json({message: "O nome da receita não pode ser vazio."});
        return false;
    }

    if (!preparo || preparo.length == 0) {
        res.status(400).json({message: "O modo de preparo da receita não pode ser vazio."});
        return false;
    }

    if (!ingredientes || ingredientes.length == 0) {
        res.status(400).json({message: "A receita deve ter ao menos um ingrediente."});
        return false;
    }

    for (var ingrediente of ingredientes) {
        var item = ingrediente.item;

        if (!item || item.length == 0) {
            res.status(400).json({message: "Ao menos um ingrediente da receita está com nome vazio."});
            return false;
        }

        var qtde = ingrediente.qtde;

        if (!qtde || qtde.length == 0) {
            res.status(400).json({message: "Ao menos um ingrediente da receita está sem quantidade especificada."});
            return false;
        }
    }

    return true;
}


/*
 * Insere uma receita no conjunto conhecido
 */
async function insereReceita(nome, preparo, ingredientes, res) {
    var db = await connect();
    var receita = await db.Receita.create(receita);
    receita.nome = nome;
    receita.preparo = preparo;
    receita.ingredientes = ingredientes;
	await receita.save();
    res.json({message: "A nova receita foi criada.", location: "/receitas/" + receita._id});
}


/*
 * Atualiza os dados de uma receita
 */
async function atualizaReceita(id, nome, preparo, ingredientes, res) {
    var db = await connect();
    var receita = await db.Receita.findById(id);
    receita.nome = nome;
    receita.preparo = preparo;
    receita.ingredientes = ingredientes;
	await receita.save();
    res.json({message: "Receita ID " + id + " atualizada.", location: "/receitas/" + id});
}


/*
 * Remove uma receita
 */
router.delete('/:id', async function(req, res) {
    var db = await connect();
    var receita = await db.Receita.findById(req.params.id);
    await receita.deleteOne();
    res.json({message: "Receita ID " + req.params.id + " removida."});
});


module.exports = router;
