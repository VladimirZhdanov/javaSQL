DROP table if exists groups, students, courses_connection, courses;

CREATE table if not exists groups (group_id serial primary key not null, group_name varchar(250));

CREATE table if not exists students (student_id serial primary key not null,
 group_id int, first_name varchar(250), last_name varchar(250));

CREATE table if not exists courses_connection (student_id int, course_id int);

CREATE table if not exists courses (course_id serial primary key not null,
 course_name varchar(250), course_description text);