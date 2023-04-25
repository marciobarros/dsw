var express = require('express');
var router = express.Router();

const fs = require('fs');
let rawdata = fs.readFileSync('banco-dados/receitas.json');
var receitas = JSON.parse(rawdata);


/*
 * Retorna a lista de receitas
 */
router.get('/', function(req, res){
    var page = req.query.page || 1;
    var itemsPerPage = req.query.itemsPage || 5;
    var sortField = req.query.sortField || '';
    var sortDesc = req.query.sortDesc || "false";
    var filter = req.query.filter || '';

    var receitasFiltradas = aplicaFiltro(receitas, filter);
    ordenaReceitas(receitasFiltradas, sortField, sortDesc);
    receitasFiltradas = selecionaPagina(receitasFiltradas, page, itemsPerPage);

    res.json({ items: receitasFiltradas, totalItems: receitas.length });
});


/*
 * Aplica o filtro sobre uma lista de receitas
 */
function aplicaFiltro(receitas, filter) {
    var receitasFiltradas = [];

    for (var receita of receitas) {
        if (receita.nome.includes(filter)) {
            receitasFiltradas.push(receita);
        }
    }

    return receitasFiltradas;
}


/*
 * Ordena as receitas pelo campo selecionado
 */
function ordenaReceitas(receitas, sortField, sortDesc) {
    if (sortField != '') {
        receitas.sort((item1, item2) => {
            var valor1 = item1[sortField]
            var valor2 = item2[sortField]
            var signal = (sortDesc == "true") ? 1 : -1;
    
            if (valor1 < valor2) return signal * 1;
            if (valor1 > valor2) return signal * -1;
            return 0;
        });
    }
}


/* 
 * Seleciona a pagina escolhida de uma lista de receitas
 */
function selecionaPagina(receitas, pagina, itensPorPagina) {
    return receitas.slice((pagina - 1) * itensPorPagina, pagina * itensPorPagina);
}


/*
 * Retorna uma receita, dado o seu ID
 */
router.get('/:id', function(req, res){
    var indice = pegaIndiceReceita(req.params.id);

    if (indice == -1) {
        res.status(404).json({message: "A receita com identificador " + req.params.id + " não foi encontrada."});
        return;
    }

    res.json(receitas[indice]);
});


/*
 * Insere ou atualiza uma receita
 */
router.post('/', function(req, res){
    var id = req.body.id;
    var nome = req.body.nome;
    var preparo = req.body.preparo;
    var ingredientes = req.body.ingredientes;

    if (verificaRegrasNegocio(id, nome, preparo, ingredientes, res)) {
        if (id == -1) {
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
function verificaRegrasNegocio(id, nome, preparo, ingredientes, res) {

    if (!nome || nome.length == 0) {
        res.status(400).json({message: "O nome da receita não pode ser vazio."});
        return false;
    }

    // TODO: receitas com nome duplicado

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
function insereReceita(nome, preparo, ingredientes, res) {
    var newId = pegaProximoIdentificador();

    receitas.push({
        id: newId,
        nome: nome,
        preparo: preparo,
        ingredientes: ingredientes
    });

    res.json({message: "A nova receita foi criada.", location: "/receitas/" + newId});
}


/*
 * Pega o próximo identificador de receitas
 */
function pegaProximoIdentificador() {
    var maximo = 0;

    for (var receita of receitas) {
        if (receita.id > maximo) {
            maximo = receita.id;
        }
    }

    return maximo + 1;
}


/*
 * Atualiza os dados de uma receita
 */
function atualizaReceita(id, nome, preparo, ingredientes, res) {
    var indice = pegaIndiceReceita(id);

    if (indice == -1) {
        res.status(404).json({message: "A receita com identificador " + id + " não foi encontrada."});
        return;
    }
 
    var receitaSelecionada = receitas[indice];
    receitaSelecionada.nome = nome;
    receitaSelecionada.preparo = preparo;
    receitaSelecionada.ingredientes = ingredientes;
    res.json({message: "Receita ID " + id + " atualizada.", location: "/receitas/" + id});
}


/*
 * Remove uma receita
 */
router.delete('/:id', function(req, res) {
    var indice = pegaIndiceReceita(req.params.id);
    console.log("Removendo a receita de ID " + req.params.id + " e índice " + indice + ".");

    if (indice == -1) {
        res.status(404).json({message: "A receita com identificador " + req.params.id + " não foi encontrada."});
        return;
    }

    receitas.splice(indice, 1);
    res.send({message: "Receita ID " + req.params.id + " removida."});
});


/*
 * Retorna uma receita, dado o seu ID
 */
function pegaIndiceReceita(id) {
    var len = receitas.length;

    for (var i = 0; i < len; i++) {
        if (receitas[i].id == id) {
            return i;
        }
    }

    return -1;
}


module.exports = router;
