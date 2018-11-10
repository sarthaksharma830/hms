CREATE TABLE "students" (
	"id" serial NOT NULL,
	"name" varchar(100) NOT NULL,
	"rollno" varchar(9) NOT NULL UNIQUE,
	"personal_contact" varchar(15) NOT NULL UNIQUE,
	"parent_contact" varchar(15) NOT NULL UNIQUE,
	"gender" bit NOT NULL,
	"email" TEXT NOT NULL UNIQUE,
	CONSTRAINT students_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "student_credentials" (
	"id" serial NOT NULL,
	"student_id" int NOT NULL UNIQUE,
	"password" TEXT NOT NULL,
	CONSTRAINT student_credentials_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "hostels" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL UNIQUE,
	"type" bit NOT NULL,
	CONSTRAINT hostels_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "student_hostels" (
	"id" serial NOT NULL,
	"student_id" int NOT NULL,
	"hostel_id" int NOT NULL,
	"room_no" TEXT NOT NULL,
	CONSTRAINT student_hostels_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "complaints" (
	"id" serial NOT NULL,
	"title" TEXT NOT NULL,
	"student_id" int NOT NULL,
	"complaint_category_id" int NOT NULL,
	"datetime" TIMESTAMP NOT NULL,
	"description" TEXT NOT NULL,
	"status" smallint NOT NULL,
	"starred" bit NOT NULL DEFAULT '0',
	"feedback" TEXT,
	"appointment_date_pref" DATE,
	"appointment_from_time_pref" TIME,
	"appointment_to_time_pref" TIME,
	CONSTRAINT complaints_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "caretakers" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"code" varchar(20) NOT NULL UNIQUE,
	"email" TEXT NOT NULL UNIQUE,
	"hostel_id" int NOT NULL UNIQUE,
	CONSTRAINT caretakers_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "complaint_categories" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"code" TEXT NOT NULL UNIQUE,
	CONSTRAINT complaint_categories_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


DROP TABLE IF EXISTS "appointments";
CREATE TABLE "appointments" (
	"id" serial NOT NULL,
	"complaint_id" int NOT NULL,
	"date" DATE NOT NULL,
	"from_time" TIME NOT NULL,
	"to_time" TIME NOT NULL,
	"status" BOOLEAN NOT NULL DEFAULT FALSE,
	CONSTRAINT appointments_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "complaint_pictures" (
	"id" serial NOT NULL,
	"complaint_id" int NOT NULL,
	"picture" TEXT NOT NULL UNIQUE,
	CONSTRAINT complaint_pictures_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


DROP TABLE IF EXISTS "default_complaint_titles";
CREATE TABLE "default_complaint_titles" (
  "id" serial NOT NULL,
  "complaint_category_id" int NOT NULL,
  "title" text NOT NULL,
  CONSTRAINT default_complaint_titles_pk PRIMARY KEY ("id")
) WITH (
  OIDS = FALSE
);


ALTER TABLE "student_credentials" ADD CONSTRAINT "student_credentials_fk0" FOREIGN KEY ("student_id") REFERENCES "students"("id");


ALTER TABLE "student_hostels" ADD CONSTRAINT "student_hostels_fk0" FOREIGN KEY ("student_id") REFERENCES "students"("id");
ALTER TABLE "student_hostels" ADD CONSTRAINT "student_hostels_fk1" FOREIGN KEY ("hostel_id") REFERENCES "hostels"("id");
ALTER TABLE "student_hostels" ADD CONSTRAINT "student_hostels_uk_room" UNIQUE (student_id, hostel_id, room_no);

ALTER TABLE "complaints" ADD CONSTRAINT "complaints_fk0" FOREIGN KEY ("student_id") REFERENCES "students"("id");
ALTER TABLE "complaints" ADD CONSTRAINT "complaints_fk1" FOREIGN KEY ("complaint_category_id") REFERENCES "complaint_categories"("id");

ALTER TABLE "caretakers" ADD CONSTRAINT "caretakers_fk0" FOREIGN KEY ("hostel_id") REFERENCES "hostels"("id");


ALTER TABLE "appointments" ADD CONSTRAINT "appointments_fk0" FOREIGN KEY ("complaint_id") REFERENCES "complaints"("id");

ALTER TABLE "complaint_pictures" ADD CONSTRAINT "complaint_pictures_fk0" FOREIGN KEY ("complaint_id") REFERENCES "complaints"("id");

ALTER TABLE "default_complaint_titles" ADD CONSTRAINT "default_complaint_titles_fk_category" FOREIGN KEY ("complaint_category_id") REFERENCES "complaint_categories"("id");