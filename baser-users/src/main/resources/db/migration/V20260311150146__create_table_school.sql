-- 1. Criar a tabela de escola (school)
CREATE TABLE school (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(20) NOT NULL UNIQUE, -- O código que você solicitou
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    uf VARCHAR(2),
    city VARCHAR(100),
    neighborhood VARCHAR(100),
    street VARCHAR(100),
    number VARCHAR(20),
    complement VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

-- 2. Ajustar a tabela de estudante
-- Adicionar vínculo com a escola
ALTER TABLE student ADD COLUMN id_school UUID REFERENCES school(id) ON DELETE RESTRICT;

-- Remover a coluna id_enterprise da tabela student (já que agora temos a tabela internship)
ALTER TABLE student DROP COLUMN id_enterprise;

-- 3. Ajustar a tabela de estágio (internship)
-- Adicionar id_enterprise para vínculo direto
ALTER TABLE internship ADD COLUMN id_enterprise UUID NOT NULL REFERENCES enterprise(id) ON DELETE RESTRICT;