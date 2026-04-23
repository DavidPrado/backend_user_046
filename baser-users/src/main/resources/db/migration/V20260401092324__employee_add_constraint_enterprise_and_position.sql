ALTER TABLE employee
ADD CONSTRAINT uk_person_enterprise_position
UNIQUE (id_person, id_enterprise, id_position_enterprise);