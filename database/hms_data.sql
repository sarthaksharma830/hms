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

insert into caretakers (name, username, contact, email, hostel_id) VALUES ('John Doe', 'johndoe', '+911234567890', 'johndoe@example.com', 1);
insert into caretakers (name, username, contact, email, hostel_id) VALUES ('Jane Doe', 'janedoe', '+911432587690', 'janedoe@example.com', 2);

insert into caretaker_credentials (caretaker_id, password) values (1, sha256('test'));
insert into caretaker_credentials (caretaker_id, password) values (2, sha256('test'));

insert into complaint_categories (name, code) VALUES ('Carpenter', 'carp'),
                                                     ('Plumber', 'plumb'),
                                                     ('Housekeeping', 'house'),
                                                     ('Technical Support', 'tech'),
                                                     ('Electrician', 'elec');

INSERT INTO hms.complaints (id, title, student_id, complaint_category_id, datetime, description, status, starred, feedback, appointment_date_pref, appointment_from_time_pref, appointment_to_time_pref) VALUES (1, 'AC not working', 1, 5, '2018-08-29 11:00:00.972000', 'The indicator lights on the AC start blinking whenever I turn it on, and then they go off in a few minutes.', 2, '0', 'Everything works fine now', '2018-08-30', '13:00:00', '14:00:00');
INSERT INTO hms.complaints (id, title, student_id, complaint_category_id, datetime, description, status, starred, feedback, appointment_date_pref, appointment_from_time_pref, appointment_to_time_pref) VALUES (2, 'Router not working', 1, 4, '2018-09-15 12:00:00.141000', 'The Wifi disconnects within 5 minutes when I try to connect. This happens every single time. Mostly it shows "disconnected by admin", but it even disconnects without any message sometimes, even when using the Network Client on Windows. Updated the network drivers as suggest, but the problem still persists.', 1, '1', null, '2018-09-16', '12:00:00', '13:00:00');
INSERT INTO hms.complaints (id, title, student_id, complaint_category_id, datetime, description, status, starred, feedback, appointment_date_pref, appointment_from_time_pref, appointment_to_time_pref) VALUES (3, 'Door handle broken', 1, 1, '2018-10-04 15:00:00.390000', 'The door handle is jammed. Tried to oil it but didn''t work out.', 2, '0', 'The handle has been fixed now', '2018-10-06', '10:00:00', '11:00:00');
INSERT INTO hms.complaints (id, title, student_id, complaint_category_id, datetime, description, status, starred, feedback, appointment_date_pref, appointment_from_time_pref, appointment_to_time_pref) VALUES (4, 'Washing machine not working', 1, 3, '2018-10-20 19:15:00.059000', 'When I put my clothes in, the machine was working fine. But when I went to take them out after about an hour, the clothes were completely dry and it looked like the machine didn''t run at all.', 1, '0', null, null, null, null);
INSERT INTO hms.complaints (id, title, student_id, complaint_category_id, datetime, description, status, starred, feedback, appointment_date_pref, appointment_from_time_pref, appointment_to_time_pref) VALUES (5, 'Water tap leaking', 1, 2, '2018-10-24 08:45:00.037000', 'The tap has been leaking since 6 in the morning.', 0, '1', null, '2018-10-24', '12:15:00', '13:00:00');

INSERT INTO hms.appointments (id, complaint_id, date, from_time, to_time) VALUES (1, 1, '2018-08-30', '13:00:00', '14:00:00');
INSERT INTO hms.appointments (id, complaint_id, date, from_time, to_time) VALUES (2, 2, '2018-09-16', '12:00:00', '13:00:00');
INSERT INTO hms.appointments (id, complaint_id, date, from_time, to_time) VALUES (3, 3, '2018-10-06', '10:00:00', '11:00:00');