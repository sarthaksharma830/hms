drop function if exists sha256;
create or replace function sha256(pwd text)
  returns text as $$
select encode(digest(pwd, 'sha256'), 'HEX')
$$
language sql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists login;
create or replace function login(_rno text, _pwd text)
  returns table("login" int)
as
$$
declare
  _sid integer;
  _cnt integer;
begin
  select id into _sid from students where rollno = _rno;
  if _sid is null
  then
    return query select -1 as "login";
  else
    select count(*) into _cnt from student_credentials where student_id = _sid
                                                         and password = sha256(_pwd);
    return query select _cnt as "login";
  end if;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getStudentInfo;
create or replace function getStudentInfo(_rno varchar(9))
  returns table(
    id               int,
    name             varchar(100),
    rollno           varchar(9),
    personal_contact varchar(15),
    parent_contact   varchar(15),
    gender           bit,
    email            text,
    hostel_id        int,
    hostel_name      text,
    hostel_room_no   text
  )
as $$
begin
  return query select s.*, h.id as hostel_id, h.name as hostel_name, sh.room_no as hoste_room_no
               from students as s,
                    hostels as h,
                    student_hostels as sh
               where sh.student_id = s.id
                 and sh.hostel_id = h.id
                 and s.rollno = _rno;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getComplaintsByStudent;
create or replace function getComplaintsByStudent(_sid int, _len int)
  returns table(
    id                         int,
    title                      text,
    student_id                 int,
    complaint_category_id      int,
    datetime                   timestamp,
    description                text,
    status                     smallint,
    starred                    bit,
    feedback                   text,
    appointment_date_pref      date,
    appointment_from_time_pref time,
    appointment_to_time_pref   time,
    complaint_category_name    text,
    complaint_category_code    text
  )
as $$
begin

  if _len = 0
  then
    return query select c.*, cc.name as complaint_category_name, cc.code as complaint_category_code
                 from complaints as c,
                      complaint_categories as cc
                 where c.complaint_category_id = cc.id
                   and c.student_id = _sid
                 order by c.starred desc, c.datetime desc;
  else
    return query select c.*, cc.name as complaint_category_name, cc.code as complaint_category_code
                 from complaints as c,
                      complaint_categories as cc
                 where c.complaint_category_id = cc.id
                   and c.student_id = _sid
                 order by c.datetime desc
                 limit _len;
  end if;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getPendingAppointmentsByStudent;
create or replace function getPendingAppointmentsByStudent(_sid int)
  returns table(
    id                      int,
    complaint_id            int,
    date                    date,
    from_time               time,
    to_time                 time,
    status                  boolean,
    complaint_title         text,
    complaint_category_id   int,
    complaint_category_name text,
    complaint_category_code text
  )
as $$
begin
  return query select a.*,
                      c.title,
                      cc.id   as complaint_category_id,
                      cc.name as complaint_category_name,
                      cc.code as complaint_category_code
               from appointments as a,
                    complaints as c,
                    complaint_categories as cc
               where a.complaint_id = c.id
                 and c.student_id = _sid
                 and c.complaint_category_id = cc.id
                 and a.status = false
               order by a.date desc;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getCompletedAppointmentsByStudent;
create or replace function getCompletedAppointmentsByStudent(_sid int)
  returns table(
    id                      int,
    complaint_id            int,
    date                    date,
    from_time               time,
    to_time                 time,
    status                  boolean,
    complaint_title         text,
    complaint_category_id   int,
    complaint_category_name text,
    complaint_category_code text
  )
as $$
begin
  return query select a.*,
                      c.title,
                      cc.id   as complaint_category_id,
                      cc.name as complaint_category_name,
                      cc.code as complaint_category_code
               from appointments as a,
                    complaints as c,
                    complaint_categories as cc
               where a.complaint_id = c.id
                 and c.student_id = _sid
                 and c.complaint_category_id = cc.id
                 and a.status = true
               order by a.date desc;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getAppointmentsByComplaint;
create or replace function getAppointmentsByComplaint(_cid int)
  returns table(
    id           int,
    complaint_id int,
    date         date,
    from_time    time,
    to_time      time,
    status       boolean
  )
as $$
begin
  return query select a.*
               from appointments as a,
                    complaints as c
               where a.complaint_id = c.id
                 and c.id = _cid;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getComplaintById;
create or replace function getComplaintById(_id int)
  returns table(
    id                         int,
    title                      text,
    student_id                 int,
    complaint_category_id      int,
    datetime                   timestamp,
    description                text,
    status                     smallint,
    starred                    bit,
    feedback                   text,
    appointment_date_pref      date,
    appointment_from_time_pref time,
    appointment_to_time_pref   time,
    complaint_category_name    text,
    complaint_category_code    text
  )
as $$
begin
  return query select c.*, cc.name as complaint_category_name, cc.code as complaint_category_code
               from complaints as c,
                    complaint_categories as cc
               where c.complaint_category_id = cc.id
                 and c.id = _id
               limit 1;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists updateComplaintStarStatus;
create or replace function updateComplaintStarStatus(_id int, _star bit)
  returns table(
    c_id                       int,
    title                      text,
    student_id                 int,
    complaint_category_id      int,
    datetime                   timestamp,
    description                text,
    status                     smallint,
    c_starred                  bit,
    feedback                   text,
    appointment_date_pref      date,
    appointment_from_time_pref time,
    appointment_to_time_pref   time,
    complaint_category_name    text,
    complaint_category_code    text
  )
as $$
begin
  update complaints set starred = _star where id = _id;
  return query select * from getComplaintById(_id);
end;
$$
language plpgsql
security definer;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getComplaintPictures;
create or replace function getComplaintPictures(_cid int)
  returns table(
    id           int,
    complaint_id int,
    picture      text
  )
as $$
begin
  return query select * from complaint_pictures as c where c.complaint_id = _cid;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getAllComplaintCategories;
create or replace function getAllComplaintCategories()
  returns table(id int, name text, code text) as $$
begin
  return query select * from complaint_categories;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists getDefaultComplaintTitles;
create or replace function getDefaultComplaintTitles(_id int)
  returns table(complaint_title text) as $$
begin
  return query select title from default_complaint_titles where complaint_category_id = _id;
end;
$$
language plpgsql
strict
immutable;

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

drop function if exists createComplaint;
create or replace function createComplaint(
  _title                 text,
  _student_id            int,
  _complaint_category_id int,
  _datetime              timestamp with time zone,
  _description           text,
  _date_pref             date,
  _from_time_pref        time without time zone,
  _to_time_pref          time without time zone,
  _pictures              text []
)
  returns table(
    c_id                         int,
    c_title                      text,
    c_student_id                 int,
    c_complaint_category_id      int,
    c_datetime                   timestamp,
    c_description                text,
    c_status                     smallint,
    c_starred                    bit,
    c_feedback                   text,
    c_appointment_date_pref      date,
    c_appointment_from_time_pref time,
    c_appointment_to_time_pref   time,
    c_complaint_category_name    text,
    c_complaint_category_code    text
  ) as $$
declare
  last_id int;
  i text;
begin
  insert into complaints (title,
                          student_id,
                          complaint_category_id,
                          datetime,
                          description,
                          status,
                          appointment_date_pref,
                          appointment_from_time_pref,
                          appointment_to_time_pref)
  values (_title,
          _student_id,
          _complaint_category_id,
          _datetime,
          _description,
          0,
          _date_pref,
          _from_time_pref,
          _to_time_pref) returning id
    into last_id;

  foreach i in array _pictures
  loop
    insert into complaint_pictures (complaint_id, picture) values (last_id, i);
  end loop;
  return query select * from getComplaintById(last_id);
end;
$$
language plpgsql
security definer;