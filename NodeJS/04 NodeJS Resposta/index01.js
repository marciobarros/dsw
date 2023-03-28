const axios = require('axios');
const express = require('express');
const app = express();

app.get('*', function(req, res) {
  res.send('Bem-vindo ao curso de DSW!');
});

app.listen(3000);

axios.get('http://localhost:3000').then(response => {
  console.log(response.data);
});
