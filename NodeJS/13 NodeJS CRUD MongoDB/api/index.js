var express = require('express');
var app = express();

var cors = require('cors');
app.use(cors({ origin: '*' }));

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var receitas = require('./receitas.js');
app.use('/receitas', receitas);

app.listen(3000);
