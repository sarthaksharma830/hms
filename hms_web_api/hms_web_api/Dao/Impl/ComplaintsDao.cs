using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Microsoft.AspNetCore.Razor.Language.Extensions;
using Npgsql;

namespace hms_web_api.Dao.Impl {

    public class ComplaintsDao : IComplaintsDao {

        public List<Complaint> GetComplaintsByStudent(int sid) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var complaints = new List<Complaint>();
                command.Connection = connection;
                command.CommandText = "select * from getComplaintsByStudent(@sid)";
                command.Parameters.AddWithValue("@sid", sid);
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
                            DateTime.ParseExact(appDate, "dd-mm-yyyy", CultureInfo.InvariantCulture);
                    }
                    else {
                        c.AppointmentDatePreference = null;
                    }

                    var appFromTime = reader["appointment_from_time_pref"].ToString();
                    var appToTime = reader["appointment_to_time_pref"].ToString();

                    if (!string.IsNullOrEmpty(appFromTime) || !string.IsNullOrWhiteSpace(appFromTime)) {
                        c.AppointmentFromTimePreference = TimeSpan.Parse(appFromTime);
                    }
                    else {
                        c.AppointmentFromTimePreference = null;
                    }

                    if (!string.IsNullOrEmpty(appToTime) || !string.IsNullOrWhiteSpace(appToTime)) {
                        c.AppointmentToTimePreference = TimeSpan.Parse(appToTime);
                    }
                    else {
                        c.AppointmentToTimePreference = null;
                    }

                    complaints.Add(c);
                }

                return complaints;
            }
        }

    }

}