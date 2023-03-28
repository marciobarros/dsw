const axios = require('axios');
const express = require('express');
const app = express();

app.get('*', function(req, res) {
  var xml = "<book><title>The Brothers Karamazov</title><author>Fyodor Dostoevsky</author></book>";
  res.set('content-type', 'application/xml').send(xml);
});

app.listen(3000);

axios.get('http://localhost:3000').then(response => {
  console.log(response.data);
});
