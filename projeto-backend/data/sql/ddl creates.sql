CREATE DATABASE UnirioCompartilhamento
CHARACTER SET UTF8 
COLLATE utf8_general_ci;

USE UnirioCompartilhamento;

CREATE TABLE Usuario
(
    id INT NOT NULL AUTO_INCREMENT,
    data_registro TIMESTAMP NULL,
    data_atualizacao TIMESTAMP NULL,
    nome VARCHAR(80) NOT NULL,
    email VARCHAR(80) NOT NULL,
    senha VARCHAR(80) NOT NULL,
    token_senha VARCHAR(256) DEFAULT NULL,
    data_token_senha TIMESTAMP NULL,
    falhas_login INT NOT NULL DEFAULT 0,
    data_ultimo_login TIMESTAMP NULL,
    bloqueado BIT NOT NULL DEFAULT 0,
    administrador BIT NOT NULL DEFAULT 0,

    CONSTRAINT pkUsuario PRIMARY KEY(id),
    CONSTRAINT ctEmailUnico UNIQUE(email)
);

CREATE TABLE ItemCompartilhado
(
    id INT NOT NULL AUTO_INCREMENT,
    data_registro TIMESTAMP NULL,
    data_atualizacao TIMESTAMP NULL,
    usuario_id INT NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao VARCHAR(4096) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    removido BIT NOT NULL,
    
    CONSTRAINT pkItemCompartilhado PRIMARY KEY(id),
    CONSTRAINT fkUsuarioDono FOREIGN KEY(usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE Compartilhamento
(
    id INT NOT NULL AUTO_INCREMENT,
    data_registro TIMESTAMP NULL,
    data_atualizacao TIMESTAMP NULL,
    usuario_id INT NOT NULL,
    item_id INT NOT NULL,
    data_inicio TIMESTAMP NULL,
    data_termino TIMESTAMP NULL,
    aceito BIT NOT NULL DEFAULT 0,
    rejeitado BIT NOT NULL DEFAULT 0,
    cancelado_dono BIT NOT NULL DEFAULT 0,
    cancelado_usuario BIT NOT NULL DEFAULT 0,
    
    CONSTRAINT pkCompartilhamento PRIMARY KEY(id),
    CONSTRAINT fkUsuarioCompartilhamento FOREIGN KEY(usuario_id) REFERENCES Usuario(id),
    CONSTRAINT fkItemCompartilhado FOREIGN KEY(item_id) REFERENCES ItemCompartilhado(id)
);
