-- 1) Insere papéis
INSERT INTO papel (nome) VALUES ('ADMIN'), ('USUARIO');

-- 2) Insere usuário administrador com senha “admin”
INSERT INTO usuario (
    cpf, nome, email, senha, data_nascimento, ativo
) VALUES (
    '000.000.000-00',
    'Administrador',
    'admin@email.com',
    '{noop}admin',
    '2000-01-01',
    true
);

-- Associa ADMIN ao administrador
INSERT INTO usuario_papel (usuario_id, papel_id)
SELECT u.id, p.id
FROM usuario u
JOIN papel p ON p.nome = 'ADMIN'
WHERE u.email = 'admin@email.com';

-- 3) Insere usuário comum com senha “usuario”
INSERT INTO usuario (
    cpf, nome, email, senha, data_nascimento, ativo
) VALUES (
    '111.111.111-11',
    'Usuário Comum',
    'usuario@email.com',
    '{noop}usuario',
    '2000-01-02',
    true
);

-- Associa USUARIO ao usuário comum
INSERT INTO usuario_papel (usuario_id, papel_id)
SELECT u.id, p.id
FROM usuario u
JOIN papel p ON p.nome = 'USUARIO'
WHERE u.email = 'usuario@email.com';