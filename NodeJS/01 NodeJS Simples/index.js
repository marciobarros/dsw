const http = require('http');

const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('Bem-vindos ao curso de DSW!');
});

server.listen(3000, '127.0.0.1', () => {
  console.log('Por favor, acesse a url http://localhost:3000');
});
