var express = require('express');
var app = express();

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true })); 

app.set('view engine', 'hbs');
app.set('views', './views');

app.get('/', function(req, res){
   res.render('form');
});

app.post('/', function(req, res){
   console.log(req.body);
   res.end();
});

app.listen(3000);
