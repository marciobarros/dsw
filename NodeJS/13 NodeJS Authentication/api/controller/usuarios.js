var express = require('express');
var router = express.Router();

const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
var nodemailer = require('nodemailer');
const crypto = require('crypto')

const mongoose = require('mongoose');
const config = require('../config/config.js');
const models = require('../model/models.js');
const auth = require("../auth/auth.js");


//
// ENDPOINT: retorna uma lista paginada de usuarios
//
router.get('/', async function(req, res){
    var page = req.query.page || 1;
    var itemsPerPage = req.query.itemsPage || 5;

    var db = await models.connect();
    var queryCount = db.Usuario.find({});
    var totalItems = await queryCount.count();

    var queryList = db.Usuario.find({}).limit(itemsPerPage).skip(itemsPerPage * (page-1));
    var items = await queryList.exec();

    res.json({ items, totalItems });
});


//
// ENDPOINT: retorna um usuário, dado o seu ID
//
router.get('/:id', async function(req, res){
    var db = await models.connect();
    var usuario = await db.Usuario.findById(req.params.id);
    res.json(usuario);
});


//
// ENDPOINT: registro de uma nova conta
//
router.post('/', async function(req, res){
    console.log("Criando um novo usuário");

    var nome = req.body.nome;
    var email = req.body.email;
    var senha = req.body.senha;
    var senhaRepetida = req.body.senhaRepetida;

    var db = await models.connect();
    var usuarioEmail = await db.Usuario.findOne({ email: email }).exec();

    if (usuarioEmail) {
        res.status(400).json({message: "Já existe um usuário registrado com este e-mail."});
        return;
    }

    if (nome.length == 0) {
        res.status(400).json({message: "O nome do usuário não pode ser vazio."});
        return;
    }

    if (!verificaEmailValido(email)) {
        res.status(400).json({message: "O e-mail do usuário não está em um formato adequado."});
        return;
    }

    if (!verificaSenhaValida(senha)) {
        res.status(400).json({message: "A senha do usuário não é válida."});
        return;
    }

    if (senha != senhaRepetida) {
        res.status(400).json({message: "A confirmação de senha está diferente da senha."});
        return;
    }

    var senhaCriptografada = await bcrypt.hash(senha, 10);

    var usuario = await db.Usuario.create({
        dataRegistro: new Date(),
        dataAtualizacao: new Date(),
        nome,
        email,
        senha: senhaCriptografada
    });

    await usuario.save();
    res.json({ message: "O novo usuário foi criado." });
});


//
// ENDPOINT: Login
//
router.post('/login', async function(req, res){
    console.log("Tentativa de login");

    var email = req.body.email;
    var senha = req.body.senha;

    var db = await models.connect();
    var usuarioEmail = await db.Usuario.findOne({ email: email }).exec();

    if (!usuarioEmail) {
        res.status(400).json({message: "Credenciais inválidas."});
        return;
    }

    if (usuarioEmail.falhasLogin >= 3) {
        res.status(400).json({message: "Usuário bloqueado."});
        return;
    }

    var senhaCorreta = await bcrypt.compare(senha, usuarioEmail.senha);

    if (!senhaCorreta) {
        usuarioEmail.falhasLogin++;
        res.status(400).json({message: "Credenciais inválidas."});
        return;
    }

    if (usuarioEmail.falhasLogin != 0) {
        usuarioEmail.falhasLogin = 0;
        await usuarioEmail.save();
    }

    const token = jwt.sign({ user_id: usuarioEmail._id, email }, config.auth.tokenKey, { expiresIn: "2h" });

    var usuario = {
        nome: usuarioEmail.nome,
        email: usuarioEmail.email,
        token: token
    };

    res.json({ message: "Login bem sucedido.", usuario: usuario });
});


//
// Verifica se um e-mail é válido
//
function verificaEmailValido(email) {
    if (!email) {
        return false;
    }
    
    return /^[A-Za-z0-9._%-]+@([A-Za-z0-9-].)+[A-Za-z]{2,4}$/.test(email);
}


//
// Verifica se uma senha é válida
//
function verificaSenhaValida(senha) {
    if (!senha) {
        return false;
    }

    if (senha.length < 8) {
        return false;
    }
    
    return /.*[a-zA-Z].*$/.test(senha) && /.*[0-9].*$/.test(senha);
}


//
// ENDPOINT: envio de token por esquecimento de senha
//
router.post('/esqueci', async function(req, res) {
    console.log("Pedindo a recuperacao de senha do usuário.");

    var email = req.body.email;

    if (!verificaEmailValido(email)) {
        res.status(400).json({message: "O e-mail do usuário não está em um formato adequado."});
        return;
    }

    var db = await models.connect();
    var usuario = await db.Usuario.findOne({ email: email }).exec();

    if (!usuario) {
        res.status(400).json({message: "Não foi encontrado um usuário com este e-mail."});
        return;
    }

    usuario.dataAtualizacao = new Date();
    usuario.tokenSenha = createRandomToken(32);
    usuario.dataTokenSenha = new Date();
    await usuario.save(usuario);

    let transporter = nodemailer.createTransport({
        host: 'smtp.sendgrid.net',
        port: 587,
        auth: {
            user: "apikey",
            pass: config.sendGrid.apiKey
        }
    });
     
    var url = config.frontend.hostname + "/login/reset?token=" + usuario.tokenSenha + "&email=" + usuario.email;
    var contents = "Se você solicitou uma recuperação de senha, clique <a href='" + url + "'>aqui</a>.<br>";
    contents += "Se não tiver pedido a recuperação de senha, relaxa. Só tentaram te dar um golpe!";

    var mailOptions = {
        from: config.sendGrid.email,
        to: usuario.email,
        subject: "Recuperação de senha",
        text: contents,
        html: contents
    };
    
    transporter.sendMail(mailOptions, function(error, info){
        if (error) {
            console.log(error);
        } else {
            console.log('Email enviado: ' + info.response);
        }
    });

    res.json({ message: "OK" });
});


//
// Gera um token aleatório para troca de senha
//
function createRandomToken(size) {  
  return crypto.randomBytes(size).toString('base64').slice(0, size);
}


//
// ENDPOINT: Recuperacao de senha do usuario
//
router.post('/reset', async function(req, res) {
    console.log("Recuperando a senha do usuário.");

    var token = req.body.token;
    var email = req.body.email;
    var senha = req.body.senha;
    var senhaRepetida = req.body.senhaRepetida;
    
    if (!verificaEmailValido(email)) {
        res.status(400).json({message: "O e-mail do usuário não está em um formato adequado."});
        return;
    }

    if (token.length == 0) {
        res.status(400).json({message: "O token do usuário não é válido."});
        return;
    }
    
    var db = await models.connect();
    var usuario = await db.Usuario.findOne({ email: email }).exec();

    if (!usuario) {
        res.status(400).json({message: "Não foi encontrado um usuário com este e-mail."});
        return;
    }

    if (!verificaValidadeTokenLogin(usuario.dataTokenSenha, 72)) {
        res.status(400).json({message: "O token de troca de senha do usuário está vencido."});
        return;
    }
    
    if (!verificaSenhaValida(senha)) {
        res.status(400).json({message: "A senha do usuário não é válida."});
        return;
    }

    if (senha != senhaRepetida) {
        res.status(400).json({message: "A confirmação de senha está diferente da senha."});
        return;
    }
    
    var senhaCriptografada = await bcrypt.hash(senha, 10);
    usuario.senha = senhaCriptografada;
    await usuario.save();

    res.json({ message: "Nova senha registrada." });
});


//
// Verifica se um token de recuperacao de senha e valido
//
function verificaValidadeTokenLogin(dateToken, maximoHoras) {
    var dateNow = new Date();
    var hours = Math.abs(dateNow - dateToken) / (60.0 * 60.0 * 1000.0);
    return hours < maximoHoras;
}


//
// ENDPOINT: troca de senha com usuário logado
//
router.post('/trocaSenha', async function(req, res) {
    console.log("Trocando a senha do usuário.");

    var claims = auth.verifyToken(req, res);

    if (!claims) {
        return res.status(401).json({ message: 'Acesso não autorizado.' });
    }

    var userId = claims.user_id;
    var senhaAntiga = req.body.senhaAntiga;
    var senhaNova = req.body.senhaNova;
    var senhaNovaRepetida = req.body.senhaNovaRepetida;
    
    var db = await models.connect();
    var usuario = await db.Usuario.findOne({ _id: userId }).exec();

    if (!usuario) {
        res.status(400).json({message: "Não foi encontrado um usuário com este e-mail."});
        return;
    }

    var senhaCorreta = await bcrypt.compare(senhaAntiga, usuario.senha);

    if (!senhaCorreta) {
        res.status(400).json({ message: "A senha antiga não confere com as credenciais do usuário." });
        return;
    }

    if (!verificaSenhaValida(senhaNova)) {
        res.status(400).json({message: "A senha do usuário não é válida."});
        return;
    }

    if (senhaNova != senhaNovaRepetida) {
        res.status(400).json({message: "A confirmação de senha está diferente da senha."});
        return;
    }

    var senhaCriptografada = await bcrypt.hash(senhaNova, 10);
    usuario.senha = senhaCriptografada;
    await usuario.save();

    res.json({ message: "Nova senha registrada." });
});


module.exports = router;
