INSERT INTO roles (name) VALUES ('ROLE_MONITOR');

INSERT INTO system_users (id, name, email, password, active)
VALUES (
    gen_random_uuid(),
    'Monitoramento Sistema',
    'monitoramento@etec.sp.gov.br',
    '$2a$10$gs.Ua6LuyyQrR0P/s0XjVevz9XLpYu1NjRGmYA9zg74m9PAniCrnq',
    true
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM system_users u, roles r
WHERE u.email = 'monitoramento@etec.sp.gov.br' AND r.name = 'ROLE_MONITOR';