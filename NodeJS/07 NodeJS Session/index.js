var express = require('express');
var cookieParser = require('cookie-parser');
var session = require('express-session');

var app = express();
app.use(cookieParser());
app.use(session({secret: "senha-secreta"}));

app.get('/', function(req, res){
   if (req.session.page_views){
      req.session.page_views++;
      res.send("Página visitada " + req.session.page_views + " vezes.");
   } else {
      req.session.page_views = 1;
      res.send("Bem-vindo a esta página pela primeira vez!");
   }
});

app.listen(3000);
