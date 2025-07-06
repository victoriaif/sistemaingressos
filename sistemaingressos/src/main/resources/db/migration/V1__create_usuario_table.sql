CREATE TABLE usuario (
    id              BIGSERIAL PRIMARY KEY,
    cpf             VARCHAR(14)   NOT NULL UNIQUE,
    nome            VARCHAR(255)  NOT NULL,
    email           VARCHAR(255)  NOT NULL UNIQUE,
    senha           VARCHAR(255)  NOT NULL,
    data_nascimento DATE,
    ativo           BOOLEAN       NOT NULL DEFAULT true
);