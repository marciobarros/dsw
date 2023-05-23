const jwt = require("jsonwebtoken");

const config = require('../config/config.js');

//
// Recupera as "claims" de um token JWT
//
const verifyToken = function(req, res) {
  if (!req.headers) {
    return null;
  }

  if (!req.headers.authorization) {
    return null;
  }

  var authorization = req.headers.authorization.split(' ');

  if (authorization.length != 2) {
    return null;
  }

  try {
      return jwt.verify(authorization[1], config.auth.tokenKey);
    } 
  catch (e) {
    return null;
  }
}

module.exports = { verifyToken }
