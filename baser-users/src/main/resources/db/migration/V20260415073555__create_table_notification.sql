CREATE TYPE notification_category AS ENUM (
    'INFO',
    'SUCCESS',
    'WARNING',
    'ERROR',
    'URGENT'
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    category notification_category DEFAULT 'INFO',
    action_url VARCHAR(255),
    recipient_id UUID NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_notifications_recipient
        FOREIGN KEY (recipient_id)
        REFERENCES system_users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_notifications_recipient_unread ON notifications(recipient_id) WHERE is_read = FALSE;
CREATE INDEX idx_notifications_created_at ON notifications(created_at DESC);