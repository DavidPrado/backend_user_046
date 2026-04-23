DELETE FROM student_course
WHERE id_student IN (
    SELECT id FROM (
        SELECT id,
               row_number() OVER (PARTITION BY id_person, id_school ORDER BY created_at DESC) as row_num
        FROM student
    ) t
    WHERE t.row_num > 1
);


DELETE FROM responsible_student
WHERE id_student IN (
    SELECT id FROM (
        SELECT id,
               row_number() OVER (PARTITION BY id_person, id_school ORDER BY created_at DESC) as row_num
        FROM student
    ) t
    WHERE t.row_num > 1
);


DELETE FROM student
WHERE id IN (
    SELECT id FROM (
        SELECT id,
               row_number() OVER (PARTITION BY id_person, id_school ORDER BY created_at DESC) as row_num
        FROM student
    ) t
    WHERE t.row_num > 1
);

ALTER TABLE student
ADD CONSTRAINT uk_student_person_school UNIQUE (id_person, id_school);