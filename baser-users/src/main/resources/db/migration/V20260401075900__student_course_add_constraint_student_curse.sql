DELETE FROM student_course a
USING student_course b
WHERE a.id > b.id
  AND a.id_student = b.id_student
  AND a.id_course = b.id_course;


 ALTER TABLE student_course
  ADD CONSTRAINT unique_student_course
  UNIQUE (id_student, id_course);