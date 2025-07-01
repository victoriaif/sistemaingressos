CREATE TABLE ingresso (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    local VARCHAR(255) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL CHECK (preco > 0),
    status VARCHAR(50),
    evento_id BIGINT REFERENCES evento(id) ON DELETE CASCADE,
    usuario_id BIGINT REFERENCES usuario(id) ON DELETE CASCADE
);
