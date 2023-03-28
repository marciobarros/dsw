var express = require('express');
var app = express();

app.get('/', function(req, res){
   res.send('Bem-vindo ao curso de DSW!');
});

app.listen(3000);