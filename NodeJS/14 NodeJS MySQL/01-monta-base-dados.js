const mysql = require("promise-mysql");
const fs = require("fs");

let diretorioBase = 'C:\\Users\\marci\\Desktop\\mysql';

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
 * Consulta todas as regioes do banco de dados
 */ 
async function consultaRegioes(db) {
  return await db.query("SELECT * FROM Regioes");
}

/*
 * Consulta todos os paises do banco de dados
 */
async function consultaPaises(db) {
  return await db.query("SELECT * FROM Paises");
}

/*
 * Carrega o arquivo com os paises conhecidos
 */
async function carregaArquivoPaises() {
  var contents = fs. readFileSync(diretorioBase + "\\dados\\countries.json", 'utf8');
  return JSON.parse(contents);
}

/*
 * Registra os paises do arquivo que ainda nao estao no banco de dados
 */
async function registraPaises(db, regioes, paises, paisesJson) {
  var registrou = false;

  for (var paisJson of paisesJson) {
    if (!verificaPaisCodigoIso(paisJson.iso2, paises)) {
      console.log("Registrando o pais " + paisJson.name);
      var idRegiao = pegaIdentificadorRegiao(paisJson.region, regioes);

      if (idRegiao != -1) {
        var data = [ paisJson.name, paisJson.iso3, paisJson.iso2, paisJson.numeric_code, paisJson.phone_code, paisJson.capital, idRegiao, paisJson.latitude, paisJson.longitude ];
        await db.query("INSERT INTO Paises (nome, iso3, iso2, codigo_numerico, codigo_telefone, capital, id_regiao, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", data);
        registrou = true;
      }
    }
  }

  return registrou;
}

/* 
 * Verifica o banco de dados contem um pais com determinado codigo ISO2
 */
function verificaPaisCodigoIso(iso2, paises) {
  for (var pais of paises) {
    if (pais.iso2 == iso2) {
      return true;
    }
  }

  return false;
}

/*
 * Pega o identificador de uma regiao, dado o seu nome
 */
function pegaIdentificadorRegiao(nome, regioes) {
  for (var regiao of regioes) {
    if (regiao.nome == nome) {
      return regiao.id;
    }
  }

  return -1;
}

/*
 * Consulta todas as cidades do banco de dados
 */
async function consultaCidades(db) {
  return await db.query("SELECT * FROM Cidades");
}

/*
 * Carrega o arquivo com as cidades conhecidas
 */
async function carregaArquivoCidades() {
  var contents = fs. readFileSync(diretorioBase + "\\dados\\cities.json", 'utf8');
  return JSON.parse(contents);
}

/*
 * Registra as cidades do arquivo que nao estao no banco de dados
 */
async function registraCidades(db, paises, cidades, cidadesJson) {
  for (var cidadeJson of cidadesJson) {
    var idPais = pegaIdentificadorPais(cidadeJson.country_code, paises);

    if (idPais != -1) {
      if (!verificaCidadeNomePais(cidadeJson.name, idPais, cidades)) {
        console.log("Registrando a cidade " + cidadeJson.name);
        var data = [ cidadeJson.name, idPais, cidadeJson.latitude, cidadeJson.longitude, cidadeJson.wikiDataId ];
        await db.query("INSERT INTO Cidades (nome, id_pais, latitude, longitude, codigo_wikidata) VALUES (?, ?, ?, ?, ?)", data);
      }
    }
  }
}

/* 
 * Verifica se uma cidade esta no banco de dados, dados seu nome e pais
 */
function verificaCidadeNomePais(nome, idPais, cidades) {
  for (var cidade of cidades) {
    if (cidade.nome == nome && cidade.id_pais == idPais) {
      return true;
    }
  }

  return false;
}

/*
 * Retorna o identificador de um pais, dado seu codigo ISO2
 */
function pegaIdentificadorPais(iso2, paises) {
  for (var pais of paises) {
    if (pais.iso2 == iso2) {
      return pais.id;
    }
  }

  return -1;
}

/*
 * Atualiza os paises e cidades no banco de dados
 */
async function atualizaDados(db) {
  var regioes = await consultaRegioes(db);

  var paises = await consultaPaises(db);
  var paisesJson = await carregaArquivoPaises();
  
  if (registraPaises(db, regioes, paises, paisesJson)) {
    paises = await consultaPaises(db);

    var cidades = await consultaCidades(db);
    var cidadesJson = await carregaArquivoCidades();

    await registraCidades(db, paises, cidades, cidadesJson)
  }
}

/*
 * Programa principal
 */
(async () => {
  try {
    db = await getConnection();
    atualizaDados(db);
    db.end();
  } 
  catch (err) {
    if (db) {
      db.end();
    }
    console.error(err);
  } 
})();
