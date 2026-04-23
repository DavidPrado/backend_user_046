INSERT INTO permissions (id, name, description) VALUES
(gen_random_uuid(), 'PERMISSION_VIEW', 'Permite consultar permissões existentes');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN'
AND p.name IN (
    'PERMISSION_VIEW'
);