ALTER TABLE system_users DROP COLUMN mustChangePassword;
ALTER TABLE system_users ADD COLUMN must_change_password BOOLEAN DEFAULT FALSE;