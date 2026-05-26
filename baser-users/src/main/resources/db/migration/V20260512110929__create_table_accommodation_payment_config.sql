-- 1. Tabela de Parametrização
CREATE TABLE accommodation_payment_config (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    days_before_due_notification INTEGER DEFAULT 5, -- Avisar X dias antes
    fine_percentage DECIMAL(5,2) DEFAULT 2.0,       -- Multa por atraso (opcional)
    active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE accommodation_payment
ADD COLUMN IF NOT EXISTS amount_paid DECIMAL(12,2),
ADD COLUMN IF NOT EXISTS justification TEXT,
ADD COLUMN IF NOT EXISTS email_notification_sent BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS email_confirmation_sent BOOLEAN DEFAULT FALSE;