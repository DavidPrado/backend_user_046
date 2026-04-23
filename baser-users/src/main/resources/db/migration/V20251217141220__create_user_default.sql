INSERT INTO system_users (id, name, email, password, active)
VALUES (
    gen_random_uuid(),
    'Administrador Sistema',
    'admin@etec.sp.gov.com.br',
    '$2a$10$czB39bfCFYHNMYo6TPiYn.UFaO.D43PyPC1KwZxV6RHcTVJUagprW', -- Etec046@!
    true
);

INSERT INTO system_users (id, name, email, password, active)
VALUES (
    gen_random_uuid(),
    'Desenvolvedor TI',
    'dev@etec.sp.gov.com.br',
    '$2a$10$VV8ThiAN6qBzDhDNOHHbyeGSR/2TF5ivJNZQZP0D9lF/9Ss3xr3Oy', -- TI046Etec@!
    true
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM system_users u, roles r
WHERE u.email = 'admin@etec.sp.gov.com.br' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM system_users u, roles r
WHERE u.email = 'dev@etec.sp.gov.com.br' AND r.name = 'ROLE_ADMIN';