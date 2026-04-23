-- Remove a restrição se ela já existir para evitar o erro "already exists"
ALTER TABLE responsible_student
DROP CONSTRAINT IF EXISTS uk_person_kinship_student;

-- Cria a nova restrição de unicidade
ALTER TABLE responsible_student
ADD CONSTRAINT uk_person_kinship_student
UNIQUE (id_student, id_person, kinship);