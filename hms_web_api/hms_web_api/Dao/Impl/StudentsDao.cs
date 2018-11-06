using System.Collections;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Npgsql;

namespace hms_web_api.Dao.Impl {

    public class StudentDao : IStudentDao {

        public Student GetStudentInfo(string rollno) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand() {Connection = connection}) {
                command.CommandText = "select * from getStudentInfo(@rollno)";
                command.Parameters.AddWithValue("@rollno", rollno);
                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    return new Student() {
                        Id = int.Parse(reader["id"].ToString()),
                        Name = reader["name"].ToString(),
                        Rollno = reader["rollno"].ToString(),
                        PersonalContact = reader["personal_contact"].ToString(),
                        ParentContact = reader["parent_contact"].ToString(),
                        Gender =  ((BitArray) reader["gender"]).Get(0) ? 'M' : 'F',
                        Email = reader["email"].ToString(),
                        Hostel = new Hostel() {
                            Id = int.Parse(reader["hostel_id"].ToString()),
                            Name = reader["hostel_name"].ToString(),
                            RoomNumber = reader["hostel_room_no"].ToString(),
                            Type = ((BitArray) reader["gender"]).Get(0) ? "Boys" : "Girls"
                        }
                    };
                }

                return null;
            }
        }

    }

}