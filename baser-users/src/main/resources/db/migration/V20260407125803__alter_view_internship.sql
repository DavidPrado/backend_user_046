DROP VIEW IF EXISTS v_internship_complete;

CREATE VIEW v_internship_complete AS
SELECT
    i.id,
    i.id_student,
    i.id_enterprise,
    i.id_employee_supervisor,
    i.start_date,
    i.end_date,
    i.internship_salary,
    i.hours_per_week,
    i.hours_per_day,
    i.termination_contract,
    i.termination_contract_reason,
    i.created_at,
    i.updated_at,      -- nova
    i.created_by,      -- nova
    i.updated_by,      -- nova
    p.name AS student_name,
    p.cpf AS student_cpf,
    ent.name AS enterprise_name,
    ent.cnpj AS enterprise_cnpj
FROM internship i
JOIN student s ON i.id_student = s.id
JOIN person p ON p.id = s.id_person
JOIN enterprise ent ON i.id_enterprise = ent.id;