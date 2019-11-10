CREATE table if not exists groups (
group_id serial primary key not null,
group_name varchar(250));

CREATE table if not exists students (
student_id serial primary key not null,
group_id int,
first_name varchar(250),
last_name varchar(250));

CREATE table if not exists courses (
course_id serial primary key not null,
course_name varchar(250),
course_description text);

CREATE table if not exists courses_connection (
student_id int NOT NULL,
course_id int NOT NULL,
FOREIGN KEY (student_id) REFERENCES students(student_id),
FOREIGN KEY (course_id) REFERENCES courses(course_id),
UNIQUE (student_id, course_id));