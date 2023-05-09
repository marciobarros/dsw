var express = require('express');
var router = express.Router();

const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const mongoose = require('mongoose');
const config = require('../config/database.js');
const models = require('../model/models.js');
const auth = require("../auth/auth.js");


/*
 * Retorna a lista paginada de receitas
 */
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


/*
 * Retorna uma receita, dado o seu ID
 */
router.get('/:id', async function(req, res){
    var db = await models.connect();
    var usuario = await db.Usuario.findById(req.params.id);
    res.json(usuario);
});


/*
 * Insere ou atualiza uma receita
 */
/*router.post('/', async function(req, res){
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
});*/


/*
 * Verifica as regras de negócio em uma nova receita
 */
/*function verificaRegrasNegocio(nome, preparo, ingredientes, res) {

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
}*/


/*
 * Insere uma receita no conjunto conhecido
 */
/*async function insereReceita(nome, preparo, ingredientes, res) {
    var db = await connect();
    var receita = await db.Receita.create(receita);
    receita.nome = nome;
    receita.preparo = preparo;
    receita.ingredientes = ingredientes;
	await receita.save();
    res.json({message: "A nova receita foi criada.", location: "/receitas/" + receita._id});
}*/


/*
 * Atualiza os dados de uma receita
 */
/*async function atualizaReceita(id, nome, preparo, ingredientes, res) {
    var db = await connect();
    var receita = await db.Receita.findById(id);
    receita.nome = nome;
    receita.preparo = preparo;
    receita.ingredientes = ingredientes;
	await receita.save();
    res.json({message: "Receita ID " + id + " atualizada.", location: "/receitas/" + id});
}*/


/*
 * Remove uma receita
 */
/*router.delete('/:id', async function(req, res) {
    var db = await connect();
    var receita = await db.Receita.findById(req.params.id);
    await receita.deleteOne();
    res.json({message: "Receita ID " + req.params.id + " removida."});
});*/



















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

    const token = jwt.sign({ user_id: usuario._id, email }, config.tokenKey, { expiresIn: "2h" });
    res.json({ message: "O novo usuário foi criado.", token: token });
});


router.post('/login', async function(req, res){
    console.log("Tentativa de login");

    var email = req.body.email;
    console.log(email);
    var senha = req.body.senha;
    console.log(senha);

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

    const token = jwt.sign({ user_id: usuarioEmail._id, email }, config.tokenKey, { expiresIn: "2h" });

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

router.post("/welcome", auth, (req, res) => {
  res.status(200).json("ok");
});






router.post('/esqueci', async function(req, res) {
    console.log("Pedindo a recuperacao de senha do usuário.");

    var email = req.body.email;

    if (!verificaEmailValido(email)) {
        res.status(400).json({message: "O e-mail do usuário não está em um formato adequado."});
        return;
    }

    var usuario = await db.Usuario.findOne({ email: email }).exec();

    if (!usuario) {
        res.status(400).json({message: "Não foi encontrado um usuário com este e-mail."});
        return;
    }

    usuario.dataAtualizacao = new Date();
    usuario.tokenSenha = CryptoUtils.createToken();
    usuario.dataTokenSenha = new Date();
    await usuario.save(usuario);
    
    /*String url = hostname + "/#/login/reset?token=" + usuario.getTokenSenha() + "&email=" + usuario.getEmail();		
    String title = "Recuperação de senha";
    
    String contents = "Se você solicitou uma recuperação de senha, clique <a href='" + url + "'>aqui</a>.<br>";
    contents += "Se não tiver pedido a recuperação de senha, relaxa. Só tentaram de dar um golpe!";
    
    emailService.sendToUser(usuario.getNome(), usuario.getEmail(), title, contents);*/
    
    res.json({message: "O novo usuário foi criado.", id: usuario._id});
});





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

    if (token.length() == 0) {
        res.status(400).json({message: "O token do usuário não é válido."});
        return;
    }
    
    var usuario = await db.Usuario.findOne({ email: email }).exec();

    if (!usuario) {
        res.status(400).json({message: "Não foi encontrado um usuário com este e-mail."});
        return;
    }

    if (!verificaValidadeTokenLogin(usuario, token, 72)) {
        res.status(400).json({message: "O token de troca de senha do usuário está vencido."});
        return;
    }
    
    if (!ValidationUtils.verificaSenhaValida(senha)) {
        res.status(400).json({message: "A senha do usuário não é válida."});
        return;
    }

    if (senha != senhaRepetida) {
        res.status(400).json({message: "A confirmação de senha está diferente da senha."});
        return;
    }
    
//    String encryptedPassword = passwordEncoder.encode(form.getSenha());
    var encryptedPassword = senha;

    usuario.senha = encryptedPassword;
    await usuario.save();
    res.json({message: "Nova senha registrada."});
});



	
/*private boolean verificaValidadeTokenLogin(Usuario usuario, String token, int maximoHoras)
{
    Date dateToken = usuario.getDataTokenSenha();
    Date dateNow = new Date();
    double hours = (dateNow.getTime() - dateToken.getTime()) / (60 * 60 * 1000);
    return (hours < maximoHoras);
}*/

/*@PostMapping(value = "/trocaSenha")
public ResponseEntity<ResponseData> trocaSenha(@RequestBody TrocaSenhaForm form, BindingResult result, Locale locale)
{
    log.info("Recuperando a senha do usuário: {}", form.toString());

    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (username == null)
        return ControllerResponse.fail("senhaAntiga", "Não há um usuário logado no sistema.");

    Usuario usuario = usuarioRepositorio.findByEmail(username);

    if (usuario == null)
        return ControllerResponse.fail("senhaAntiga", "Não foi possível recuperar os dados do usuário a partir das credenciais.");
    
    if (!ValidationUtils.verificaSenhaValida(form.getSenhaAntiga()))
        return ControllerResponse.fail("senhaAntiga", "A senha atual do usuário não é válida.");
    
    if (!passwordEncoder.matches(form.getSenhaAntiga(), usuario.getSenha()))
        return ControllerResponse.fail("senhaAntiga", "A senha atual não está igual à senha registrada no sistema.");
    
    if (!ValidationUtils.verificaSenhaValida(form.getSenhaNova()))
        return ControllerResponse.fail("senhaNova", "A nova senha do usuário não é válida.");
    
    if (!form.getSenhaNova().equals(form.getSenhaNovaRepetida()))
        return ControllerResponse.fail("senhaNovaRepetida", "A confirmação de senha está diferente da senha.");

    String encryptedPassword = passwordEncoder.encode(form.getSenhaNova());
    usuario.setSenha(encryptedPassword);
    usuarioRepositorio.save(usuario);
    return ControllerResponse.success();
}*/


module.exports = router;
