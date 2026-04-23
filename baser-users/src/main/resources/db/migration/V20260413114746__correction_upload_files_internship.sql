TRUNCATE TABLE internship_file_binary CASCADE;
TRUNCATE TABLE internship_files CASCADE;

DO $$
BEGIN
    ALTER TABLE internship_file_binary DROP CONSTRAINT IF EXISTS internship_file_binary_id_file_fkey;


    ALTER TABLE internship_files DROP CONSTRAINT IF EXISTS internship_files_id_internship_fkey;
EXCEPTION
    WHEN undefined_object THEN

        NULL;
END $$;


ALTER TABLE internship_files DROP COLUMN IF EXISTS file_data;


ALTER TABLE internship_files
    ALTER COLUMN file_size TYPE BIGINT,
    ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100);


ALTER TABLE internship_files
    ADD CONSTRAINT internship_files_id_internship_fkey
    FOREIGN KEY (id_internship) REFERENCES internship(id) ON DELETE RESTRICT;


ALTER TABLE internship_file_binary
    ADD CONSTRAINT internship_file_binary_id_file_fkey
    FOREIGN KEY (id_file) REFERENCES internship_files(id)
    ON DELETE CASCADE;


CREATE INDEX IF NOT EXISTS idx_internship_files_internship ON internship_files(id_internship);