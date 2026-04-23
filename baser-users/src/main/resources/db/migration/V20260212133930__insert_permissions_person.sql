INSERT INTO permissions (id, name, description) VALUES
(gen_random_uuid(), 'PESSOA_CREATE', 'Permite criar novos pessoas'),
(gen_random_uuid(), 'PESSOA_READ',   'Permite visualizar detalhes do cadastro de pessoas'),
(gen_random_uuid(), 'PESSOA_UPDATE', 'Permite editar perfis de acesso existentes'),
(gen_random_uuid(), 'PESSOA_DELETE', 'Permite excluir pessoas do cadastro');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN'
AND p.name IN (
    'PESSOA_CREATE',
    'PESSOA_READ',
    'PESSOA_UPDATE',
    'PESSOA_DELETE'
);