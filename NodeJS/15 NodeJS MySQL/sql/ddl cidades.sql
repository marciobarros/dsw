CREATE DATABASE cidades;

USE cidades;

--
-- REGIOES
--

CREATE TABLE Regioes 
(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL
);

INSERT INTO Regioes (nome)
VALUES ('Africa'), ('Asia'), ('Europe'), ('Oceania'), ('Americas'), ('Polar');


--
-- PAISES
--

CREATE TABLE Paises 
(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL,
  iso3 VARCHAR(3) NOT NULL,
  iso2 VARCHAR(2) NOT NULL,
  codigo_numerico VARCHAR(3) NOT NULL,
  codigo_telefone VARCHAR(20) NOT NULL,
  capital VARCHAR(255) NOT NULL,
  id_regiao INT NOT NULL,
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,

  FOREIGN KEY (id_regiao) REFERENCES Regioes(id)
);


--
-- CIDADES
--

CREATE TABLE Cidades
(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL,
  id_pais INT NOT NULL,
  latitude FLOAT NOT NULL,
  longitude FLOAT NOT NULL,
  codigo_wikidata VARCHAR(20) NULL,

  FOREIGN KEY (id_pais) REFERENCES Paises(id)
);