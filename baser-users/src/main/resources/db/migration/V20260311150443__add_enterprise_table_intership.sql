DO $$
BEGIN
    -- 1. Adiciona a coluna id_enterprise apenas se ela não existir
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='internship' AND column_name='id_enterprise'
    ) THEN
        ALTER TABLE internship
        ADD COLUMN id_enterprise UUID NOT NULL REFERENCES enterprise(id) ON DELETE RESTRICT;
    END IF;
END $$;

-- 2. Alterar os tipos de data (O Postgres permite repetir este comando sem erro se o tipo for o mesmo)
ALTER TABLE internship
ALTER COLUMN start_date TYPE DATE,
ALTER COLUMN end_date TYPE DATE;

-- 3. Criar índice para empresa (O Postgres 16 suporta IF NOT EXISTS para índices)
CREATE INDEX IF NOT EXISTS idx_internship_enterprise
ON internship(id_enterprise);

-- 4. Criar índice para estudante
CREATE INDEX IF NOT EXISTS idx_internship_student
ON internship(id_student);