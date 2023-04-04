var express = require('express');
var app = express();

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var clientes = require('./clientes.js');
app.use('/clientes', clientes);

app.listen(3000);
