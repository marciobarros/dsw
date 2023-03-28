const axios = require('axios');
const express = require('express');
const app = express();

app.get('*', function(req, res) {
  const title = req.query.title;
  const band = req.query.band;
  console.log(band + ": " + title);
});

app.listen(3000);

const querystring = 'title=Delicate Sound of Thunder&band=Pink Floyd';
axios.get('http://localhost:3000?' + querystring);
