CREATE SEQUENCE IF NOT EXISTS users_codigo_seq START WITH 1;

ALTER TABLE system_users ADD COLUMN code BIGINT;

UPDATE system_users
SET code = nextval('users_codigo_seq')
WHERE code IS NULL;

ALTER TABLE system_users ALTER COLUMN code SET NOT NULL;

ALTER TABLE system_users ALTER COLUMN code SET DEFAULT nextval('users_codigo_seq');

ALTER TABLE system_users ADD CONSTRAINT uk_system_users_codigo UNIQUE (code);