-- 1. Criar a nova tabela para os binários (Estratégia de Separação)
CREATE TABLE IF NOT EXISTS internship_file_binary (
    id_file UUID PRIMARY KEY REFERENCES internship_files(id) ON DELETE CASCADE,
    binary_content BYTEA NOT NULL
);

-- 2. Migrar os dados existentes (se houver) da internship_files para a nova tabela
-- Isso garante que você não perca arquivos já salvos antes da refatoração
INSERT INTO internship_file_binary (id_file, binary_content)
SELECT id, file_data FROM internship_files;

-- 3. Remover a coluna redundante da tabela de metadados
ALTER TABLE internship_files DROP COLUMN file_data;

-- 4. Adicionar coluna MIME_TYPE (Importante para o Angular identificar o tipo de arquivo)
ALTER TABLE internship_files ADD COLUMN mime_type VARCHAR(100);

-- 5. Ajustar o tamanho do nome do arquivo e o tipo do tamanho do arquivo
ALTER TABLE internship_files ALTER COLUMN file_name TYPE VARCHAR(255);
ALTER TABLE internship_files ALTER COLUMN file_size TYPE BIGINT;

-- 6. Adicionar Defaults de Auditoria para evitar campos nulos esquecidos
ALTER TABLE internship ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE internship_files ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE internship_assistance ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

-- 7. Remover a tabela antiga de inconsistência se ela existir e não estiver sendo usada
-- Certifique-se de que não há dados nela que você queira manter
DROP TABLE IF EXISTS internship_file_data;