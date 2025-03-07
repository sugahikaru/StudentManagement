CREATE TABLE IF NOT EXISTS students
(
  id INT PRIMARY KEY AUTO_INCREMENT,
  fullname VARCHAR(100) NOT NULL,
  furigana VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  city VARCHAR(100),
  age INT,
  gender VARCHAR (10),
  remark TEXT,
  isDeleted boolean
);
CREATE TABLE IF NOT EXISTS  student_courses
(
   id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);
CREATE TABLE IF NOT EXISTS course_application_status (
   id INT PRIMARY KEY AUTO_INCREMENT,
   courseId INT NOT NULL,
   status VARCHAR(255) NOT NULL
);

