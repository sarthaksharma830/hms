using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using NpgsqlTypes;

namespace hms_web_api.Dao.Impl {

    public class ComplaintsDao : IComplaintsDao {

        public List<Complaint> GetComplaintsByStudent(int sid, int len) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var complaints = new List<Complaint>();
                command.Connection = connection;
                command.CommandText = "select * from getComplaintsByStudent(@sid, @len)";
                command.Parameters.AddWithValue("@sid", sid);
                command.Parameters.AddWithValue("@len", len);
                var reader = command.ExecuteReader();
                while (reader.Read()) {
                    var c = new Complaint() {
                        Id = int.Parse(reader["id"].ToString()),
                        Title = reader["title"].ToString(),
                        Student = new Student() {Id = int.Parse(reader["student_id"].ToString())},
                        ComplaintCategory = new ComplaintCategory() {
                            Id = int.Parse(reader["complaint_category_id"].ToString()),
                            Name = reader["complaint_category_name"].ToString(),
                            Code = reader["complaint_category_code"].ToString()
                        },
                        DateTime = DateTime.Parse(reader["datetime"].ToString()),
                        Description = reader["description"].ToString(),
                        Starred = ((BitArray) reader["starred"]).Get(0),
                        Feedback = reader["feedback"].ToString()
                    };
                    var status = int.Parse(reader["status"].ToString());

                    switch (status) {
                        case 0:
                            c.ComplaintStatus = ComplaintStatus.Pending;
                            break;
                        case 1:
                            c.ComplaintStatus = ComplaintStatus.Scheduled;
                            break;
                        default:
                            c.ComplaintStatus = ComplaintStatus.Resolved;
                            break;
                    }

                    var appDate = reader["appointment_date_pref"].ToString();
                    if (!string.IsNullOrEmpty(appDate) || !string.IsNullOrWhiteSpace(appDate)) {
                        appDate = appDate.Substring(0, 10);
                        c.AppointmentDatePreference =
                            DateTime.ParseExact(appDate, "dd-MM-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["appointment_from_time_pref"].ToString();
                    var appToTime = reader["appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        appFromTime = $"1970-01-01T{appFromTime}";
                        c.AppointmentFromTimePreference = DateTime.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        appToTime = $"1970-01-01T{appToTime}";
                        c.AppointmentToTimePreference = DateTime.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }

                    c.Pictures = GetComplaintPictures(c.Id);

                    complaints.Add(c);
                }

                return complaints;
            }
        }

        public Complaint GetComplaintById(int id) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from getComplaintById(@id)";
                command.Parameters.AddWithValue("@id", id);
                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    var c = new Complaint() {
                        Id = int.Parse(reader["id"].ToString()),
                        Title = reader["title"].ToString(),
                        Student = new Student() {
                            Id = int.Parse(reader["student_id"].ToString()),
                            Name = reader["student_name"].ToString(),
                            Rollno = reader["student_rollno"].ToString(),
                            PersonalContact = reader["student_personal_contact"].ToString(),
                            ParentContact =  reader["student_parent_contact"].ToString(),
                            Gender =  ((BitArray) reader["student_gender"]).Get(0) ? 'M' : 'F',
                            Email = reader["student_email"].ToString(),
                            Hostel = new Hostel() {
                                Id = int.Parse(reader["student_hostel_id"].ToString()),
                                Name = reader["student_hostel_name"].ToString(),
                                Type = ((BitArray) reader["student_hostel_type"]).Get(0) ? "Boys" : "Girls",
                                RoomNumber = reader["student_room_number"].ToString()
                            }
                        },
                        ComplaintCategory = new ComplaintCategory() {
                            Id = int.Parse(reader["complaint_category_id"].ToString()),
                            Name = reader["complaint_category_name"].ToString(),
                            Code = reader["complaint_category_code"].ToString()
                        },
                        DateTime = DateTime.Parse(reader["datetime"].ToString()),
                        Description = reader["description"].ToString(),
                        Starred = ((BitArray) reader["starred"]).Get(0),
                        Feedback = reader["feedback"].ToString()
                    };
                    var status = int.Parse(reader["status"].ToString());

                    switch (status) {
                        case 0:
                            c.ComplaintStatus = ComplaintStatus.Pending;
                            break;
                        case 1:
                            c.ComplaintStatus = ComplaintStatus.Scheduled;
                            break;
                        default:
                            c.ComplaintStatus = ComplaintStatus.Resolved;
                            break;
                    }

                    var appDate = reader["appointment_date_pref"].ToString();
                    if (!string.IsNullOrEmpty(appDate) || !string.IsNullOrWhiteSpace(appDate)) {
                        appDate = appDate.Substring(0, 10);
                        c.AppointmentDatePreference =
                            DateTime.ParseExact(appDate, "dd-MM-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["appointment_from_time_pref"].ToString();
                    var appToTime = reader["appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        appFromTime = $"1970-01-01T{appFromTime}";
                        c.AppointmentFromTimePreference = DateTime.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        appToTime = $"1970-01-01T{appToTime}";
                        c.AppointmentToTimePreference = DateTime.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }
                    
                    c.Pictures = GetComplaintPictures(c.Id);

                    return c;
                }

                return null;
            }
        }

        public Complaint UpdateComplaintStarStatus(int id, bool star) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from updateComplaintStarStatus(@id, @star)";
                command.Parameters.AddWithValue("@id", id);
                command.Parameters.Add("@star", NpgsqlDbType.Bit).Value = star;

                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    var c = new Complaint() {
                        Id = int.Parse(reader["c_id"].ToString()),
                        Title = reader["title"].ToString(),
                        Student = new Student() {Id = int.Parse(reader["student_id"].ToString())},
                        ComplaintCategory = new ComplaintCategory() {
                            Id = int.Parse(reader["complaint_category_id"].ToString()),
                            Name = reader["complaint_category_name"].ToString(),
                            Code = reader["complaint_category_code"].ToString()
                        },
                        DateTime = DateTime.Parse(reader["datetime"].ToString()),
                        Description = reader["description"].ToString(),
                        Starred = ((BitArray) reader["c_starred"]).Get(0),
                        Feedback = reader["feedback"].ToString()
                    };
                    var status = int.Parse(reader["status"].ToString());

                    switch (status) {
                        case 0:
                            c.ComplaintStatus = ComplaintStatus.Pending;
                            break;
                        case 1:
                            c.ComplaintStatus = ComplaintStatus.Scheduled;
                            break;
                        default:
                            c.ComplaintStatus = ComplaintStatus.Resolved;
                            break;
                    }

                    var appDate = reader["appointment_date_pref"].ToString();
                    if (!string.IsNullOrEmpty(appDate) || !string.IsNullOrWhiteSpace(appDate)) {
                        appDate = appDate.Substring(0, 10);
                        c.AppointmentDatePreference =
                            DateTime.ParseExact(appDate, "dd-MM-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["appointment_from_time_pref"].ToString();
                    var appToTime = reader["appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        appFromTime = $"1970-01-01T{appFromTime}";
                        c.AppointmentFromTimePreference = DateTime.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        appToTime = $"1970-01-01T{appToTime}";
                        c.AppointmentToTimePreference = DateTime.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }

                    return c;
                }

                return null;
            }
        }

        public List<ComplaintCategory> GetAllComplaintCategories() {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from getAllComplaintCategories()";
                var reader = command.ExecuteReader();
                List<ComplaintCategory> categories = new List<ComplaintCategory>();
                while (reader.Read()) {
                    categories.Add(new ComplaintCategory() {
                        Id = int.Parse(reader["id"].ToString()),
                        Name = reader["name"].ToString(),
                        Code = reader["code"].ToString()
                    });
                }

                return categories;
            }
        }

        public List<string> GetDefaultComplaintTitles(int id) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from getDefaultComplaintTitles(@id)";
                command.Parameters.AddWithValue("@id", id);
                var reader = command.ExecuteReader();
                var titles = new List<string>();
                while (reader.Read()) {
                    titles.Add(reader["complaint_title"].ToString());
                }

                return titles;
            }
        }

        public Complaint CreateComplaint(Complaint complaint) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText =
                    "select * from createComplaint(@title, @studentId, @complaintCategoryId, @datetime, @description, @datePref, @fromTimePref, @toTimePref, @pictures)";

                command.Parameters.AddWithValue("@title", complaint.Title);
                command.Parameters.AddWithValue("@studentId", complaint.Student.Id);
                command.Parameters.AddWithValue("@complaintCategoryId", complaint.ComplaintCategory.Id);
                command.Parameters.AddWithValue("@description", complaint.Description);
                command.Parameters.Add("@datetime", NpgsqlDbType.TimestampTz).Value = complaint.DateTime;
                command.Parameters.Add("@datePref", NpgsqlDbType.Date).Value = complaint.AppointmentDatePreference;
                command.Parameters.Add("@pictures", NpgsqlDbType.Array | NpgsqlDbType.Text).Value = complaint.Pictures;

                if (complaint.AppointmentFromTimePreference != null) {
                    var fromTimeString =
                        complaint.AppointmentFromTimePreference.Value.ToString("HH:mm:ss",
                            CultureInfo.InvariantCulture);
                    var fromTime = TimeSpan.Parse(fromTimeString);
                    command.Parameters.Add("@fromTimePref", NpgsqlDbType.Time).Value = fromTime;
                }
                else {
                    command.Parameters.Add("@fromTimePref", NpgsqlDbType.Time).Value = null;
                }

                if (complaint.AppointmentToTimePreference != null) {
                    var toTimeString =
                        complaint.AppointmentToTimePreference.Value.ToString("HH:mm:ss", CultureInfo.InvariantCulture);
                    var toTime = TimeSpan.Parse(toTimeString);
                    command.Parameters.Add("@toTimePref", NpgsqlDbType.Time).Value = toTime;
                }
                else {
                    command.Parameters.Add("@toTimePref", NpgsqlDbType.Time).Value = null;
                }

                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    var c = new Complaint() {
                        Id = int.Parse(reader["c_id"].ToString()),
                        Title = reader["c_title"].ToString(),
                        Student = new Student() {Id = int.Parse(reader["c_student_id"].ToString())},
                        ComplaintCategory = new ComplaintCategory() {
                            Id = int.Parse(reader["c_complaint_category_id"].ToString()),
                            Name = reader["c_complaint_category_name"].ToString(),
                            Code = reader["c_complaint_category_code"].ToString()
                        },
                        DateTime = DateTime.Parse(reader["c_datetime"].ToString()),
                        Description = reader["c_description"].ToString(),
                        Starred = ((BitArray) reader["c_starred"]).Get(0),
                        Feedback = reader["c_feedback"].ToString()
                    };
                    var status = int.Parse(reader["c_status"].ToString());

                    switch (status) {
                        case 0:
                            c.ComplaintStatus = ComplaintStatus.Pending;
                            break;
                        case 1:
                            c.ComplaintStatus = ComplaintStatus.Scheduled;
                            break;
                        default:
                            c.ComplaintStatus = ComplaintStatus.Resolved;
                            break;
                    }

                    var appDate = reader["c_appointment_date_pref"].ToString();
                    if (!string.IsNullOrEmpty(appDate) || !string.IsNullOrWhiteSpace(appDate)) {
                        appDate = appDate.Substring(0, 10);
                        c.AppointmentDatePreference =
                            DateTime.ParseExact(appDate, "dd-MM-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["c_appointment_from_time_pref"].ToString();
                    var appToTime = reader["c_appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        appFromTime = $"1970-01-01T{appFromTime}";
                        c.AppointmentFromTimePreference = DateTime.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        appToTime = $"1970-01-01T{appToTime}";
                        c.AppointmentToTimePreference = DateTime.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }

                    return c;
                }

                return null;
            }
        }

        public Complaint MarkComplaintAsResolved(int id) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from markComplaintAsResolved(@cid)";
                command.Parameters.AddWithValue("@cid", id);
                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    if (reader.GetBoolean(0)) {
                        return GetComplaintById(id);
                    }
                }

                return null;
            }
        }

        public List<string> GetComplaintPictures(int id) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from getComplaintPictures(@id)";
                command.Parameters.AddWithValue("@id", id);
                var reader = command.ExecuteReader();
                var pictures = new List<string>();

                while (reader.Read()) {
                    pictures.Add(reader["picture"].ToString());
                }

                return pictures;
            }
        }

        public List<Complaint> GetComplaintsByCaretaker(int cid, int len) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var complaints = new List<Complaint>();
                command.Connection = connection;
                command.CommandText = "select * from getComplaintsByCaretaker(@cid, @len)";
                command.Parameters.AddWithValue("@cid", cid);
                command.Parameters.AddWithValue("@len", len);
                var reader = command.ExecuteReader();
                while (reader.Read()) {
                    var c = new Complaint() {
                        Id = int.Parse(reader["id"].ToString()),
                        Title = reader["title"].ToString(),
                        Student = new Student() {
                            Id = int.Parse(reader["student_id"].ToString()),
                            Name = reader["student_name"].ToString(),
                            Rollno = reader["student_rollno"].ToString(),
                            PersonalContact = reader["student_personal_contact"].ToString(),
                            ParentContact = reader["student_parent_contact"].ToString(),
                            Email = reader["student_email"].ToString(),
                            Gender = ((BitArray) reader["student_gender"]).Get(0) ? 'M' : 'F',
                            Hostel = new Hostel() {
                                Id = int.Parse(reader["hostel_id"].ToString()),
                                RoomNumber = reader["room_no"].ToString()
                            }
                        },
                        ComplaintCategory = new ComplaintCategory() {
                            Id = int.Parse(reader["complaint_category_id"].ToString()),
                            Name = reader["complaint_category_name"].ToString(),
                            Code = reader["complaint_category_code"].ToString()
                        },
                        DateTime = DateTime.Parse(reader["datetime"].ToString()),
                        Description = reader["description"].ToString(),
                        Starred = ((BitArray) reader["starred"]).Get(0),
                        Feedback = reader["feedback"].ToString()
                    };
                    var status = int.Parse(reader["status"].ToString());

                    switch (status) {
                        case 0:
                            c.ComplaintStatus = ComplaintStatus.Pending;
                            break;
                        case 1:
                            c.ComplaintStatus = ComplaintStatus.Scheduled;
                            break;
                        default:
                            c.ComplaintStatus = ComplaintStatus.Resolved;
                            break;
                    }

                    var appDate = reader["appointment_date_pref"].ToString();
                    if (!string.IsNullOrEmpty(appDate) || !string.IsNullOrWhiteSpace(appDate)) {
                        appDate = appDate.Substring(0, 10);
                        c.AppointmentDatePreference =
                            DateTime.ParseExact(appDate, "dd-MM-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["appointment_from_time_pref"].ToString();
                    var appToTime = reader["appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        appFromTime = $"1970-01-01T{appFromTime}";
                        c.AppointmentFromTimePreference = DateTime.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        appToTime = $"1970-01-01T{appToTime}";
                        c.AppointmentToTimePreference = DateTime.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }
                    
                    c.Pictures = GetComplaintPictures(c.Id);

                    complaints.Add(c);
                }

                return complaints;
            }
        }

        public List<String> InsertComplaintPictures(int cid, List<string> pictureNames) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from insertComplaintPictures(@cid, @pics)";
                command.Parameters.AddWithValue("@cid", cid);
                command.Parameters.Add("@pics", NpgsqlDbType.Array | NpgsqlDbType.Text).Value = pictureNames;

                var reader = command.ExecuteReader();
                var list = new List<string>();
                while (reader.Read()) {
                    list.Add(reader["pic"].ToString());
                }

                return list;
            }
        }

        
    }

}