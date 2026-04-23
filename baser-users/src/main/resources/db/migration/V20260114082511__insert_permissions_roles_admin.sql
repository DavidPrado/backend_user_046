INSERT INTO permissions (id, name, description) VALUES
(gen_random_uuid(), 'ROLE_CREATE', 'Permite criar novos perfis de acesso'),
(gen_random_uuid(), 'ROLE_VIEW',   'Permite visualizar detalhes dos perfis de acesso'),
(gen_random_uuid(), 'ROLE_UPDATE', 'Permite editar perfis de acesso existentes'),
(gen_random_uuid(), 'ROLE_DELETE', 'Permite excluir perfis de acesso'),
(gen_random_uuid(), 'ROLE_ADMIN',  'Permite listar e gerenciar todos os perfis de acesso');


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN'
AND p.name IN (
    'ROLE_CREATE',
    'ROLE_VIEW',
    'ROLE_UPDATE',
    'ROLE_DELETE',
    'ROLE_ADMIN'
);