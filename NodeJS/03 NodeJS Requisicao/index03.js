const axios = require('axios');
const express = require('express');
var bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.urlencoded({ extended: false }))

app.post('*', function(req, res) {
  const title = req.body.title;
  const band = req.body.band;
  console.log(band + ": " + title);
});

app.listen(3000);

const bodyString = 'title=Delicate Sound of Thunder&band=Pink Floyd';
axios.post('http://localhost:3000?', bodyString);
