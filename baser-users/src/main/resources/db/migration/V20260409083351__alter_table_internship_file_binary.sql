
DROP TABLE IF EXISTS internship_file_binary;

CREATE TABLE internship_file_binary (
    id UUID PRIMARY KEY,
    id_file UUID NOT NULL,
    binary_content BYTEA NOT NULL,
    sequence_order INTEGER DEFAULT 1,
    CONSTRAINT fk_binary_to_file
        FOREIGN KEY (id_file) REFERENCES internship_files(id) ON DELETE RESTRICT
);

CREATE INDEX idx_binary_id_file ON internship_file_binary(id_file);