const axios = require('axios');
const express = require('express');
const app = express();

app.get('*', function(req, res) {
  res.status(400).send("Humm, esta requisicao nao vai funcionar ...");
});

app.listen(3000);

axios.get('http://localhost:3000').then(response => {
  console.log("Ora, isto nao deveria acontecer ...")
}).catch(error => {
  console.log(error.response.status);
  console.log(error.response.data);
});
