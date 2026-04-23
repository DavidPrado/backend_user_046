CREATE TABLE person (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     name VARCHAR(100) NOT NULL,
     rg VARCHAR(20),
     cpf VARCHAR(20) UNIQUE,
     email VARCHAR(100) UNIQUE,
     date_of_birth DATE,
     phone_home VARCHAR(20),
     phone_model VARCHAR(20),
     phone_work VARCHAR(20),
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

CREATE TABLE enterprise(
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     name VARCHAR(100) NOT NULL,
     cnpj VARCHAR(20) UNIQUE,
     email VARCHAR(100) UNIQUE,
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

CREATE TABLE position_enterprise(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    position VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE course(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE employee(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_person UUID NOT NULL REFERENCES person(id) ON DELETE RESTRICT,
    id_enterprise UUID NOT NULL REFERENCES enterprise(id) ON DELETE RESTRICT,
    id_position_enterprise UUID NOT NULL REFERENCES position_enterprise(id) ON DELETE RESTRICT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE student(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_person UUID NOT NULL REFERENCES person(id) ON DELETE RESTRICT,
    id_enterprise UUID REFERENCES enterprise(id) ON DELETE SET NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE student_course(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_student UUID NOT NULL REFERENCES student(id) ON DELETE RESTRICT,
    id_course UUID NOT NULL REFERENCES course(id) ON DELETE RESTRICT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE responsible_student(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_student UUID NOT NULL REFERENCES student(id) ON DELETE RESTRICT,
    id_person UUID NOT NULL REFERENCES person(id) ON DELETE RESTRICT,
    kinship VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);