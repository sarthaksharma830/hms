using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Npgsql;

namespace hms_web_api.Dao.Impl {

    public class CaretakersDao : ICaretakersDao {

        public Caretaker GetCaretakerInfo(string username) {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand()) {
                command.Connection = connection;
                command.CommandText = "select * from getCaretakerInfo(@uname)";
                command.Parameters.AddWithValue("@uname", username);
                var reader = command.ExecuteReader();
                if (reader.Read()) {
                    return new Caretaker() {
                        Id = int.Parse(reader["id"].ToString()),
                        Name = reader["name"].ToString(),
                        Username = reader["username"].ToString(),
                        Email = reader["email"].ToString(),
                        Contact = reader["contact"].ToString(),
                        Hostel = new Hostel() {
                            Id = int.Parse(reader["hostel_id"].ToString()),
                            Name = reader["hostel_name"].ToString()
                        }
                    };
                }

                return null;
            }
        }

    }

}