CREATE TABLE usuario_papel (
    usuario_id BIGINT NOT NULL
        REFERENCES usuario(id) ON DELETE CASCADE,
    papel_id   BIGINT NOT NULL
        REFERENCES papel(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, papel_id)
);