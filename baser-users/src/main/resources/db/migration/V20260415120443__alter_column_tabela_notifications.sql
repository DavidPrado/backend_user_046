
ALTER TABLE notifications ALTER COLUMN category DROP DEFAULT;

ALTER TABLE notifications
  ALTER COLUMN category TYPE VARCHAR(50) USING category::text;

DROP TYPE IF EXISTS notification_category;

ALTER TABLE notifications ALTER COLUMN category SET DEFAULT 'INFO';