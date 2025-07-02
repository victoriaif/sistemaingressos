CREATE TABLE transacao (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    ingresso_id BIGINT UNIQUE, -- OneToOne: cada ingresso só pode ser vendido uma vez
    comprador_id BIGINT,
    vendedor_id BIGINT,
    
    CONSTRAINT fk_transacao_ingresso FOREIGN KEY (ingresso_id) REFERENCES ingresso (id),
    CONSTRAINT fk_transacao_comprador FOREIGN KEY (comprador_id) REFERENCES usuario (id),
    CONSTRAINT fk_transacao_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuario (id)
);