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
    database: "cidades"
  });

  return database;
}

/*
 * Programa principal
 */
(async () => {
  try {
    db = await getConnection();
    let sql = "UPDATE Regioes SET nome = ? WHERE nome = ?";
    let data = ["Na Lua", "Lunar"];
    let result = await db.query(sql, data);
    console.log(result.affectedRows + " linha afetada.");
    console.log(result.changedRows + " linha atualizada.");
    console.log("Avisos: " + result.warningCount + " " + result.message);
    db.end();
  } 
  catch (err) {
    if (db) {
      db.end();
    }
    console.error(err);
  } 
})();
