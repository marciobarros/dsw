var express = require('express');
var router = express.Router();

var clientes = [
   { id: 1, nome: "Fulano", credito: 1000 },
   { id: 2, nome: "Beltrano", credito: 1200 },
   { id: 3, nome: "Ciclano", credito: 2008 }
];


/*
 * Retorna a lista de clientes
 */
router.get('/', function(req, res){
    res.json(clientes);
});


/*
 * Retorna um cliente, dado o seu ID
 */
router.get('/:id([0-9]+)', function(req, res){
    var indice = pegaIndiceCliente(req.params.id);

    if (indice == -1) {
        res.status(404);
        res.json({message: "Not Found"});
        return;
    }

    res.json(clientes[indice]);
});


/*
 * Insere um cliente
 */
router.post('/', function(req, res){
    if(!req.body.nome || !req.body.credito.toString().match(/^[0-9]+$/g)) {
       res.status(400);
       res.json({message: "Bad Request"});
       return;
    }

    var newId = clientes[clientes.length-1].id+1;

    clientes.push({
        id: newId,
        nome: req.body.nome,
        credito: parseFloat(req.body.credito)
    });

    res.json({message: "O novo cliente foi criado.", location: "/clientes/" + newId});
});


/*
 * Atualiza os dados de um cliente
 */
router.put('/:id', function(req, res){
    if(!req.body.nome || !req.body.credito.toString().match(/^[0-9]+$/g)){
       res.status(400);
       res.json({message: "Bad Request"});
       return;
    }

    var indice = pegaIndiceCliente(req.params.id);

    if(indice == -1) {
        res.json({message: "Not found"});
        return;
    }
 
    var clienteSelecionado = clientes[indice];
    clienteSelecionado.nome = req.body.nome;
    clienteSelecionado.credito = parseFloat(req.body.credito);

    res.json({message: "Cliente ID " + req.params.id + " atualizado.", 
        location: "/clientes/" + req.params.id});
});


/*
 * Remove um cliente
 */
router.delete('/:id', function(req, res) {
    var indice = pegaIndiceCliente(req.params.id);

    if(indice == -1) {
       res.json({message: "Not found"});
       return;
    }

    clientes.splice(indice, 1);
    res.send({message: "Cliente ID " + req.params.id + " removido."});
});


/*
 * Retorna um cliente, dado o seu ID
 */
function pegaIndiceCliente(id) {
    var len = clientes.length;

    for (var i = 0; i < len; i++) {
        if (clientes[i].id == id) {
            return i;
        }
    }

    return -1;
}

module.exports = router;
