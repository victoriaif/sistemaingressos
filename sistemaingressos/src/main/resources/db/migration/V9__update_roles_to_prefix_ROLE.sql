UPDATE papel
SET nome = CONCAT('ROLE_', nome)
WHERE nome NOT LIKE 'ROLE_%';
