 CREATE TABLE IF NOT EXISTS internship(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_student UUID NOT NULL REFERENCES student(id) ON DELETE RESTRICT,
    id_employee_supervisor UUID NOT NULL REFERENCES employee(id) ON DELETE RESTRICT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    internship_salary NUMERIC(10,2),
    hours_per_week INT,
    hours_per_day INT,
    termination_contract BOOLEAN DEFAULT FALSE,
    termination_contract_reason VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS internship_files(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_internship UUID NOT NULL REFERENCES internship(id) ON DELETE RESTRICT,
    file_name VARCHAR(100) NOT NULL,
    file_description VARCHAR(255),
    file_size INT,
    file_data BYTEA NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS internship_file_data (
    id_file UUID PRIMARY KEY REFERENCES internship_files(id) ON DELETE RESTRICT,
    binary_content BYTEA NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_internship_student ON internship(id_student);
CREATE INDEX IF NOT EXISTS idx_internship_employee_supervisor ON internship(id_employee_supervisor);
CREATE INDEX IF NOT EXISTS idx_internship_files_internship ON internship_files(id_internship);

CREATE TABLE IF NOT EXISTS internship_assistance (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_internship UUID NOT NULL REFERENCES internship(id) ON DELETE RESTRICT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    value_assistance NUMERIC(10,2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

