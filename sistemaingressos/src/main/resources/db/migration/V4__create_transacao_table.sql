CREATE TABLE transacao (
    id              BIGSERIAL PRIMARY KEY,
    data_hora       TIMESTAMP  NOT NULL,
    valor           NUMERIC(10,2) NOT NULL,
    ingresso_id     BIGINT     NOT NULL UNIQUE
        REFERENCES ingresso(id) ON DELETE CASCADE,
    comprador_id    BIGINT     REFERENCES usuario(id),
    vendedor_id     BIGINT     REFERENCES usuario(id)
);