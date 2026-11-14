-- тестовые данные, демо пароль от аккаунтов: EduTrackDemo2026!

BEGIN;

-- 1 администратор, 2 преподавателя и 10 студентов
INSERT INTO users (id, email, password_hash, first_name, last_name, role, enabled) VALUES
    (1, 'admin@edutrack.test',      '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Системный', 'Администратор', 'ADMIN', TRUE),
    (2, 'teacher1@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Елена', 'Иванова', 'TEACHER', TRUE),
    (3, 'teacher2@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Алексей', 'Смирнов', 'TEACHER', TRUE),
    (4, 'student1@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Иван', 'Петров', 'STUDENT', TRUE),
    (5, 'student2@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Мария', 'Соколова', 'STUDENT', TRUE),
    (6, 'student3@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Дмитрий', 'Кузнецов', 'STUDENT', TRUE),
    (7, 'student4@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Анна', 'Морозова', 'STUDENT', TRUE),
    (8, 'student5@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Никита', 'Волков', 'STUDENT', TRUE),
    (9, 'student6@edutrack.test',   '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Ольга', 'Орлова', 'STUDENT', TRUE),
    (10, 'student7@edutrack.test',  '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Артём', 'Фёдоров', 'STUDENT', TRUE),
    (11, 'student8@edutrack.test',  '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'София', 'Михайлова', 'STUDENT', TRUE),
    (12, 'student9@edutrack.test',  '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Максим', 'Лебедев', 'STUDENT', TRUE),
    (13, 'student10@edutrack.test', '$2y$10$442J0PH.A2.IN5ePwufF7erOXvmIQ2UmmvUIxTmU/lMMiifPJ6jFG', 'Полина', 'Зайцева', 'STUDENT', TRUE);

INSERT INTO study_groups (id, code) VALUES
    (1, 'АБВГ-26-01'),
    (2, 'АБВГ-26-02');

INSERT INTO students (id, user_id, group_id) VALUES
    (1, 4, 1),
    (2, 5, 1),
    (3, 6, 1),
    (4, 7, 1),
    (5, 8, 1),
    (6, 9, 2),
    (7, 10, 2),
    (8, 11, 2),
    (9, 12, 2),
    (10, 13, 2);

INSERT INTO subjects (id, code, name) VALUES
    (1, 'MATH', 'Математика'),
    (2, 'INFO', 'Информатика'),
    (3, 'RUS', 'Русский язык');

INSERT INTO subject_assignments(id, subject_id, group_id, teacher_id, academic_year, semester) VALUES
    (1, 1, 1, 2, 2026, 'FALL'),
    (2, 2, 1, 3, 2026, 'FALL'),
    (3, 1, 2, 2, 2026, 'FALL'),
    (4, 3, 2, 3, 2026, 'FALL');

INSERT INTO lessons (id, subject_assignment_id, lesson_date, topic) VALUES
    (1, 1, '2026-09-03', 'Линейные уравнения'),
    (2, 1, '2026-09-10', 'Квадратные уравнения'),
    (3, 2, '2026-09-04', 'Основы алгоритмов'),
    (4, 2, '2026-09-11', 'Условные операторы'),
    (5, 3, '2026-09-03', 'Обыкновенные дроби'),
    (6, 3, '2026-09-10', 'Проценты'),
    (7, 4, '2026-09-05', 'Сложные предложения'),
    (8, 4, '2026-09-12', 'Орфография');

INSERT INTO attendances (id, lesson_id, student_id, status) VALUES
    (1, 1, 1, 'PRESENT'),
    (2, 1, 2, 'ABSENT'),
    (3, 1, 3, 'LATE'),
    (4, 1, 4, 'EXCUSED'),
    (5, 1, 5, 'PRESENT'),
    (6, 2, 1, 'PRESENT'),
    (7, 2, 2, 'PRESENT'),
    (8, 2, 3, 'ABSENT'),
    (9, 3, 1, 'PRESENT'),
    (10, 3, 2, 'LATE'),
    (11, 3, 3, 'PRESENT'),
    (12, 4, 4, 'ABSENT'),
    (13, 5, 6, 'PRESENT'),
    (14, 5, 7, 'PRESENT'),
    (15, 5, 8, 'EXCUSED'),
    (16, 5, 9, 'LATE'),
    (17, 5, 10, 'PRESENT'),
    (18, 6, 6, 'PRESENT'),
    (19, 6, 7, 'ABSENT'),
    (20, 7, 6, 'PRESENT'),
    (21, 7, 7, 'PRESENT'),
    (22, 7, 8, 'LATE'),
    (23, 8, 6, 'EXCUSED');

INSERT INTO assessments(id, subject_assignment_id, title, type, assessment_date, max_score) VALUES
    (1, 1, 'Домашняя работа № 1', 'HOMEWORK', '2026-09-06', 10),
    (2, 1, 'Контрольная работа № 1', 'TEST', '2026-09-13', 100),
    (3, 2, 'Практическая работа № 1', 'HOMEWORK', '2026-09-14', 20),
    (4, 3, 'Контрольная работа по математике', 'TEST', '2026-09-15', 50),
    (5, 4, 'Диктант', 'TEST', '2026-09-16', 100);

INSERT INTO grades (id, student_id, assessment_id, score) VALUES
    (1, 1, 1, 10),
    (2, 2, 1, 0),
    (3, 3, 1, 7),
    (4, 4, 1, 5),
    (5, 1, 2, 86),
    (6, 2, 2, 70),
    (7, 3, 2, 0),
    (8, 1, 3, 15),
    (9, 2, 3, 20),
    (10, 4, 3, 18),
    (11, 6, 4, 45),
    (12, 7, 4, 30),
    (13, 8, 4, 0),
    (14, 6, 5, 80),
    (15, 7, 5, 90),
    (16, 8, 5, 60);

SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('study_groups', 'id'), (SELECT MAX(id) FROM study_groups));
SELECT setval(pg_get_serial_sequence('students', 'id'), (SELECT MAX(id) FROM students));
SELECT setval(pg_get_serial_sequence('subjects', 'id'), (SELECT MAX(id) FROM subjects));
SELECT setval(pg_get_serial_sequence('subject_assignments', 'id'), (SELECT MAX(id) FROM subject_assignments));
SELECT setval(pg_get_serial_sequence('lessons', 'id'), (SELECT MAX(id) FROM lessons));
SELECT setval(pg_get_serial_sequence('attendances', 'id'), (SELECT MAX(id) FROM attendances));
SELECT setval(pg_get_serial_sequence('assessments', 'id'), (SELECT MAX(id) FROM assessments));
SELECT setval(pg_get_serial_sequence('grades', 'id'), (SELECT MAX(id) FROM grades));

COMMIT;
