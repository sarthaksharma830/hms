using System;
using System.Collections.Generic;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.IdentityModel.Tokens;
using Npgsql;
using NpgsqlTypes;

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
                var dt = new DateTime(1970, 01, 01);
                while (reader.Read()) {
                    var a = new Appointment() {
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
                        Status = bool.Parse(reader["status"].ToString())
                    };
                    var ft = TimeSpan.Parse(reader["from_time"].ToString());
                    var tt = TimeSpan.Parse(reader["to_time"].ToString());
                    a.FromTime = dt + ft;
                    a.ToTime = dt + tt;
                    appointments.Add(a);
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
                var dt = new DateTime(1970, 01, 01);
                while (reader.Read()) {
                    var a = new Appointment() {
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
                        Status = bool.Parse(reader["status"].ToString())
                    };
                    var ft = TimeSpan.Parse(reader["from_time"].ToString());
                    var tt = TimeSpan.Parse(reader["to_time"].ToString());
                    a.FromTime = dt + ft;
                    a.ToTime = dt + tt;
                    appointments.Add(a);
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
                var dt = new DateTime(1970, 01, 01);
                while (reader.Read()) {
                    var a = new Appointment() {
                        Id = int.Parse(reader["id"].ToString()),
                        Complaint = new Complaint() {
                            Id = int.Parse(reader["complaint_id"].ToString())
                        },
                        Date = DateTime.Parse(reader["date"].ToString()),
                        Status = bool.Parse(reader["status"].ToString())
                    };
                    var ft = TimeSpan.Parse(reader["from_time"].ToString());
                    var tt = TimeSpan.Parse(reader["to_time"].ToString());
                    a.FromTime = dt + ft;
                    a.ToTime = dt + tt;
                    appointments.Add(a);
                }

                return appointments;
            }
        }

        public List<Appointment> CreateAppointment(Appointment appointment) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from createAppointment(@cid, @d, @ft, @tt)";
                command.Parameters.AddWithValue("@cid", appointment.Complaint.Id);
                
                var fromTimeString = appointment.FromTime.ToString("hh:mm:ss");
                var fromTimeSpan = TimeSpan.Parse(fromTimeString);
                var toTimeString = appointment.ToTime.ToString("hh:mm:ss");
                var toTimeSpan = TimeSpan.Parse(toTimeString);
                
                command.Parameters.Add("@d", NpgsqlDbType.Date).Value = appointment.Date;
                command.Parameters.Add("@ft", NpgsqlDbType.Time).Value = fromTimeSpan;
                command.Parameters.Add("@tt", NpgsqlDbType.Time).Value = toTimeSpan;
                var reader = command.ExecuteReader();
                var dt = new DateTime(1970, 01, 01);
                if (reader.Read()) {
                    var a = new Appointment() {
                        Id = int.Parse(reader["a_id"].ToString()),
                        Complaint = new Complaint() {Id = int.Parse(reader["a_complaint_id"].ToString())},
                        Date = DateTime.Parse(reader["a_date"].ToString()),
                        Status = bool.Parse(reader["a_status"].ToString())
                    };
                    var ft = TimeSpan.Parse(reader["a_from_time"].ToString());
                    var tt = TimeSpan.Parse(reader["a_to_time"].ToString());
                    a.FromTime = dt + ft;
                    a.ToTime = dt + tt;
                    var appointments = new List<Appointment> {a};
                    return appointments;
                }

                return new List<Appointment>();
            }
        }

        public List<Appointment> UpdateAppointment(Appointment appointment) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from updateAppointment(@aid, @d, @ft, @tt)";
                command.Parameters.AddWithValue("@aid", appointment.Id);

                var fromTimeString = appointment.FromTime.ToString("hh:mm:ss");
                var fromTimeSpan = TimeSpan.Parse(fromTimeString);
                var toTimeString = appointment.ToTime.ToString("hh:mm:ss");
                var toTimeSpan = TimeSpan.Parse(toTimeString);
                
                command.Parameters.Add("@d", NpgsqlDbType.Date).Value = appointment.Date;
                command.Parameters.Add("@ft", NpgsqlDbType.Time).Value = fromTimeSpan;
                command.Parameters.Add("@tt", NpgsqlDbType.Time).Value = toTimeSpan;
                var reader = command.ExecuteReader();
                var dt = new DateTime(1970, 01, 01);
                if (reader.Read()) {
                    var a = new Appointment() {
                        Id = int.Parse(reader["a_id"].ToString()),
                        Complaint = new Complaint() {Id = int.Parse(reader["a_complaint_id"].ToString())},
                        Date = DateTime.Parse(reader["a_date"].ToString()),
                        Status = bool.Parse(reader["a_status"].ToString())
                    };
                    var ft = TimeSpan.Parse(reader["a_from_time"].ToString());
                    var tt = TimeSpan.Parse(reader["a_to_time"].ToString());
                    a.FromTime = dt + ft;
                    a.ToTime = dt + tt;
                    return  new List<Appointment> {a};
                }

                return new List<Appointment>();
            }
        }

    }

}