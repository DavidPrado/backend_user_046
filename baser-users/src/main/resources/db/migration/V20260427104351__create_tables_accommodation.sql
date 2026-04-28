
CREATE TABLE accommodation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_number VARCHAR(20) NOT NULL,
    building_block VARCHAR(50),
    max_capacity INTEGER NOT NULL,
    square_meters DECIMAL(10,2),
    gender_type VARCHAR(20) NOT NULL DEFAULT 'MIXED',
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE TABLE accommodation_student (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_accommodation UUID NOT NULL,
    id_student UUID NOT NULL,
    entry_date DATE NOT NULL,
    exit_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_acc_room FOREIGN KEY (id_accommodation) REFERENCES accommodation(id),
    CONSTRAINT fk_acc_student FOREIGN KEY (id_student) REFERENCES student(id)
);

CREATE TABLE accommodation_payment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_student UUID NOT NULL,
    reference_month VARCHAR(7) NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_date TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_pay_student FOREIGN KEY (id_student) REFERENCES student(id)
);

CREATE TABLE accommodation_occurrence (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_accommodation UUID NOT NULL,
    id_requester UUID NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50),
    priority VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    resolved_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_occ_room FOREIGN KEY (id_accommodation) REFERENCES accommodation(id)
);

CREATE TABLE accommodation_file_binary (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_reference UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_content BYTEA NOT NULL,
    mime_type VARCHAR(100),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

CREATE INDEX idx_acc_gender ON accommodation (gender_type);
CREATE INDEX idx_acc_student_current ON accommodation_student (id_student) WHERE exit_date IS NULL;
CREATE INDEX idx_acc_binary_ref ON accommodation_file_binary (id_reference);