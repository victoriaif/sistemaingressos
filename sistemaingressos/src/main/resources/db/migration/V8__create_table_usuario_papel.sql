CREATE TABLE usuario_papel (
    usuario_id BIGINT NOT NULL,
    papel_id INTEGER NOT NULL,
    PRIMARY KEY (usuario_id, papel_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (papel_id) REFERENCES papel(id)
);
