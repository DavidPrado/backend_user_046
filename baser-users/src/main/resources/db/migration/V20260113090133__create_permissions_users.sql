CREATE TABLE IF NOT EXISTS permissions(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS role_permissions(
    role_id UUID REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

INSERT INTO permissions (name, description) VALUES
('USER_READ', 'Permite visualizar usuários'),
('USER_CREATE', 'Permite criar novos usuários'),
('USER_UPDATE', 'Permite editar usuários'),
('USER_DELETE', 'Permite excluir usuários');
