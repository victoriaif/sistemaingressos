-- Insere papéis
INSERT INTO papel (nome) VALUES ('ADMIN'), ('USUARIO');

-- Adiciona usuário admin
INSERT INTO usuario (cpf, nome, email, senha, ativo)
VALUES (
  '000.000.000-00',
  'Administrador',
  'admin@email.com',
  '{bcrypt}$2a$10$gHgXcd/YcEoKD43fTnK4W.1z0Bya3WBASk2ZZUOb86Vewpk8Lhz0C',
  true
);

-- Atribui papel ADMIN
INSERT INTO usuario_papel (usuario_id, papel_id)
VALUES (
  (SELECT id FROM usuario WHERE email = 'admin@email.com'),
  (SELECT id FROM papel WHERE nome = 'ADMIN')
);

-- Adiciona usuário comum
INSERT INTO usuario (cpf, nome, email, senha, ativo)
VALUES (
  '111.111.111-11',
  'Usuário Comum',
  'usuario@email.com',
  '{bcrypt}$2a$10$DCjOcDLqlkEbcKihY7QxLO6RpiUIpZxq9RmTxvw.fOozepnnbUbci',
  true
);

-- Atribui papel USUARIO
INSERT INTO usuario_papel (usuario_id, papel_id)
VALUES (
  (SELECT id FROM usuario WHERE email = 'usuario@email.com'),
  (SELECT id FROM papel WHERE nome = 'USUARIO')
);
