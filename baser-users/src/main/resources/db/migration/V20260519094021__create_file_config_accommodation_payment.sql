DROP TABLE IF EXISTS accommodation_payment_config;

CREATE TABLE accommodation_payment_config (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    -- NULL significa que é a CONFIGURAÇÃO GLOBAL.
    -- Preenchido significa que é uma exceção customizada para este alojamento.
    id_accommodation UUID UNIQUE,
    due_day INTEGER NOT NULL DEFAULT 10 CHECK (due_day >= 1 AND due_day <= 28),
    total_amount DECIMAL(10,2) NOT NULL,
    unit_amount_per_student DECIMAL(10,2) NOT NULL,
    days_before_due_notification INTEGER DEFAULT 5,
    fine_percentage DECIMAL(5,2) DEFAULT 2.0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    CONSTRAINT fk_accommodation FOREIGN KEY (id_accommodation) REFERENCES accommodation(id) ON DELETE CASCADE
);

INSERT INTO accommodation_payment_config
(id_accommodation, due_day, total_amount, unit_amount_per_student, days_before_due_notification, fine_percentage)
VALUES
(NULL, 10, 1200.00, 300.00, 5, 2.0);