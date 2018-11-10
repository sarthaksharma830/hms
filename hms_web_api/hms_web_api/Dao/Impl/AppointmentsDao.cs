using System;
using System.Collections.Generic;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.IdentityModel.Tokens;
using Npgsql;

namespace hms_web_api.Dao.Impl {

    public class AppointmentsDao : IAppointmentsDao {

        public List<Appointment> GetPendingAppointmentsByStudent(int sid) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var appointments = new List<Appointment>();

                command.Connection = connection;
                command.CommandText = "select * from getPendingAppointmentsByStudent(@sid)";
                command.Parameters.AddWithValue("@sid", sid);
                var reader = command.ExecuteReader();
                while (reader.Read()) {
                    appointments.Add(new Appointment() {
                        Id = int.Parse(reader["id"].ToString()),
                        Complaint = new Complaint() {
                            Id = int.Parse(reader["complaint_id"].ToString()),
                            Title = reader["complaint_title"].ToString(),
                            ComplaintCategory = new ComplaintCategory() {
                                Id = int.Parse(reader["complaint_category_id"].ToString()),
                                Name = reader["complaint_category_name"].ToString(),
                                Code = reader["complaint_category_code"].ToString()
                            }
                        },
                        Date = DateTime.Parse(reader["date"].ToString()),
                        FromTime = TimeSpan.Parse(reader["from_time"].ToString()),
                        ToTime = TimeSpan.Parse(reader["to_time"].ToString()),
                        Status = bool.Parse(reader["status"].ToString())
                    });
                }

                return appointments;
            }
        }

        public List<Appointment> GetCompletedAppointmentsByStudent(int sid) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var appointments = new List<Appointment>();

                command.Connection = connection;
                command.CommandText = "select * from getCompletedAppointmentsByStudent(@sid)";
                command.Parameters.AddWithValue("@sid", sid);
                var reader = command.ExecuteReader();
                while (reader.Read()) {
                    appointments.Add(new Appointment() {
                        Id = int.Parse(reader["id"].ToString()),
                        Complaint = new Complaint() {
                            Id = int.Parse(reader["complaint_id"].ToString()),
                            Title = reader["complaint_title"].ToString(),
                            ComplaintCategory = new ComplaintCategory() {
                                Id = int.Parse(reader["complaint_category_id"].ToString()),
                                Name = reader["complaint_category_name"].ToString(),
                                Code = reader["complaint_category_code"].ToString()
                            }
                        },
                        Date = DateTime.Parse(reader["date"].ToString()),
                        FromTime = TimeSpan.Parse(reader["from_time"].ToString()),
                        ToTime = TimeSpan.Parse(reader["to_time"].ToString()),
                        Status = bool.Parse(reader["status"].ToString())
                    });
                }

                return appointments;
            }
        }

        public List<Appointment> GetAppointmentsByComplaint(int cid) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                var appointments = new List<Appointment>();

                command.Connection = connection;
                command.CommandText = "select * from getAppointmentsByComplaint(@cid)";
                command.Parameters.AddWithValue("@cid", cid);
                var reader = command.ExecuteReader();
                while (reader.Read()) {
                    appointments.Add(new Appointment() {
                        Id = int.Parse(reader["id"].ToString()),
                        Complaint = new Complaint() {
                            Id = int.Parse(reader["complaint_id"].ToString())
                        },
                        Date = DateTime.Parse(reader["date"].ToString()),
                        FromTime = TimeSpan.Parse(reader["from_time"].ToString()),
                        ToTime = TimeSpan.Parse(reader["to_time"].ToString()),
                        Status = bool.Parse(reader["status"].ToString())
                    });
                }

                return appointments;
            }
        }

    }

}