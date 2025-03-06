-- students テーブルにデータを挿入
INSERT INTO students (fullname, furigana, email, city, age, gender, remark, isDeleted)
VALUES
('Taro Yamada', 'やまだ たろう', 'taro.yamada@example.com', 'Tokyo', 25, 'Male', 'Enrolled in Data Science', 0),
('Hanako Suzuki', 'すずき はなこ', 'hanako.suzuki@example.com', 'Osaka', 22, 'Female', 'Interested in AI', 0),
('Kenta Tanaka', 'たなか けんた', 'kenta.tanaka@example.com', 'Nagoya', 30, 'Male', 'Part-time student', 0);

-- student_courses テーブルにデータを挿入
INSERT INTO student_courses (student_id, course_name, start_date, end_date)
VALUES
(1, 'Java', '2024-01-10 00:00:00', '2024-01-17 00:00:00'),
(2, 'AWS', '2024-02-15 00:00:00', '2024-02-22 00:00:00'),
(3, 'Web制作', '2024-03-20 00:00:00', '2024-03-27 00:00:00');

-- データ挿入: ID 1
INSERT INTO course_application_status (courseId, status)
VALUES
(1, '受講中'),
(2, '仮申込み'),
(3, '受講終了');
