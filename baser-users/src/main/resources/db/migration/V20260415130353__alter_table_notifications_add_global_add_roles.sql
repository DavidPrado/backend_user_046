
ALTER TABLE notifications ALTER COLUMN recipient_id DROP NOT NULL;

ALTER TABLE notifications ADD COLUMN is_global BOOLEAN DEFAULT FALSE;

ALTER TABLE notifications ADD COLUMN system_rule VARCHAR(100);

CREATE INDEX idx_notifications_recipient_global ON notifications(recipient_id, is_global);