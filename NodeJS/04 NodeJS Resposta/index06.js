const axios = require('axios');
const express = require('express');
const app = express();

app.get('/ponto1', (req, res) => {
  res.redirect('/ponto2');
});

app.get('/ponto2', (req, res) => {
  res.send('Bemvindos ao curso de DSW!')
});

app.listen(3000);

axios.get('http://localhost:3000/ponto1').then(response => {
  console.log(response.data);
});