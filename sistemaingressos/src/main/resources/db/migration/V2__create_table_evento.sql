CREATE TABLE evento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data DATE NOT NULL,
    local VARCHAR(255) NOT NULL,
    descricao TEXT
);
