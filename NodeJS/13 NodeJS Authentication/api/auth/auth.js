const jwt = require("jsonwebtoken");

const config = require('../config/database.js');

const verifyToken = function (req, res, next) {
  const token = req.headers["x-access-token"];

  if (!token) {
    return res.status(403).send("O token de autenticação não foi encontraodo.");
  }

  try {
    const decoded = jwt.verify(token, config.tokenKey);
    req.user = decoded;
    console.log(decoded);
  } catch (err) {
    return res.status(401).send("Token inválido.");
  }
  return next();
}

module.exports = verifyToken;
