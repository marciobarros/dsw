const axios = require('axios');
const express = require('express');
const app = express();

app.get('*', function(req, res) {
  const field = req.headers.field;
  console.log("Campo: " + field);
});

app.listen(3000);

axios.get('http://localhost:3000?', { headers: { field: 'nome' } });
