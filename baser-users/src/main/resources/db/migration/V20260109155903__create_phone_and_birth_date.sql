ALTER TABLE system_users DROP COLUMN phoneNumber;
ALTER TABLE system_users DROP COLUMN birthDate;

ALTER TABLE system_users ADD COLUMN phone_number VARCHAR(15);
ALTER TABLE system_users ADD COLUMN birth_date DATE;