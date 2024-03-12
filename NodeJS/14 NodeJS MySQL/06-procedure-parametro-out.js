const mysql = require("promise-mysql");
const fs = require("fs");

/*
 * Pega a conexao com o banco de dados
 */ 
async function getConnection() {
  let database = await mysql.createConnection({
    host: "localhost",
    port: "3306",
    user: "marcio",
    password: "marcio",
    database: "cidades",
    multipleStatements: true
  });

  return database;
}

/*
 * Programa principal
 */
(async () => {
  try {
    db = await getConnection();
    let sql = "CALL RegistraRegiao ('Marte', @id); SELECT @id as id;";
    let result = await db.query(sql);
    console.log(result);
    console.log(result[1][0].id);
    db.end();
  } 
  catch (err) {
    if (db) {
      db.end();
    }
    console.error(err);
  } 
})();
