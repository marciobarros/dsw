const axios = require('axios');
const express = require('express');
const app = express();

app.get('/:band/:year', function(req, res) {
  const band = req.params.band;
  const year = req.params.year;
  console.log(band + ": " + year);
});

app.listen(3000);

axios.get('http://localhost:3000/Pink-Floyd/1988');
