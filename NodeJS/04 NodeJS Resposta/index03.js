const axios = require('axios');
const express = require('express');
const app = express();

app.set('view engine', 'hbs');

app.get('*', function(req, res) {
  const locals = { message: 'Bem-vindos ao curso de DSW!' };
  res.render('bemvindos2', locals);
});

app.listen(3000);

axios.get('http://localhost:3000').then(response => {
  console.log(response.data);
});