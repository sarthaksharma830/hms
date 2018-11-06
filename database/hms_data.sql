insert into students (name, rollno, personal_contact, parent_contact, gender, email)
values ('Sarthak Sharma', '101783037', '+916280611919', '+918288007880', B'1', 'ssharma60_be17@thapar.edu');
insert into students (name, rollno, personal_contact, parent_contact, gender, email)
values ('Shruti', '101783040', '+919780384083', '+919780084080', B'0', 'sshruti60_be17@thapar.edu'),
       ('Gurpreet Singh Walia', '101783015', '+911234561235', '+91234567890', B'1', 'gwalia60_be17@thapar.edu')

insert into student_credentials(student_id, password) VALUES (1, sha256('test'));

insert into hostels(name, type) VALUES ('M', B'1');
insert into hostels(name, type) VALUES ('I', B'0');

insert into student_hostels (student_id, hostel_id, room_no) VALUES (1, 1, 'D 627');
insert into student_hostels (student_id, hostel_id, room_no) VALUES (4, 1, 'D 627');
insert into student_hostels (student_id, hostel_id, room_no) VALUES (3, 2, 'A 123');

insert into caretakers (name, code, email, hostel_id) VALUES ('John Doe', 'john_doe', 'johndoe@example.com', 1);
insert into caretakers (name, code, email, hostel_id) VALUES ('Jane Doe', 'jane_doe', 'janedoe@example.com', 2);

insert into complaint_categories (name, code) VALUES ('Carpenter', 'carp'),
                                                     ('Plumber', 'plumb'),
                                                     ('Housekeeping', 'house'),
                                                     ('Technical Support', 'tech'),
                                                     ('Electrician', 'elec');

