CREATE OR REPLACE VIEW v_internship_complete AS
SELECT
    i.*,
    p.name AS student_name,
    p.cpf AS student_cpf,
    ent.name AS enterprise_name,
    ent.cnpj AS enterprise_cnpj
FROM internship i
JOIN student s ON i.id_student = s.id
JOIN person p ON p.id = s.id_person
JOIN enterprise ent ON i.id_enterprise = ent.id;