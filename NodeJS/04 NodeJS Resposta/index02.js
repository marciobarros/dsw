const axios = require('axios');
const express = require('express');
const app = express();

app.set('view engine', 'hbs');

app.get('*', function(req, res) {
  res.render('bemvindos');
});

app.listen(3000);

axios.get('http://localhost:3000').then(response => {
  console.log(response.data);
});