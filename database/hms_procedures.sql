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
create function getComplaintsByStudent(_sid int)
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
                 and c.student_id = _sid;
end;
$$
language plpgsql
strict
immutable;