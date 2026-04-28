CREATE OR REPLACE VIEW v_accommodation_student_details AS
SELECT
    t.id AS id,
    a.room_number AS room_number,
    a.building_block as building_block,
    a.max_capacity as max_capacity,
    a.gender_type as a.gender_type,
    p."name" AS student_name,
    t.entry_date AS entry_date,
    t.exit_date AS exit_date,
    a.status AS status_accommodation
FROM accommodation_student t
INNER JOIN accommodation a ON (a.id = t.id_accommodation)
INNER JOIN student s ON (s.id = t.id_student)
INNER JOIN person p ON (p.id = s.id_person);