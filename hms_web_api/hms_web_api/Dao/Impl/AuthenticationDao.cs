using System;
using System.Data;
using hms_web_api.Dao.Interfaces;
using hms_web_api.Models;
using Npgsql;
using NpgsqlTypes;

namespace hms_web_api.Dao.Impl
{
    public class AuthenticationDao : IAuthenticationDao
    {
        public LoginResult Login(string rollno, string password)
        {
            using (var connection = SqlConnectionManager.GetConnection())
            using (var command = new NpgsqlCommand())
            {
                command.Connection = connection;
                command.CommandText = "select login(@rno, @pwd)";
                command.Parameters.AddWithValue("@rno", rollno);
                command.Parameters.AddWithValue("@pwd", password);
                var reader = command.ExecuteReader();
                var result = LoginResult.InvalidRollNumber;
                if (reader.Read())
                {
                    int x = (int) reader.GetValue(0);
                    if (x == 1) result = LoginResult.Successful;
                    else if (x == 0) result = result = LoginResult.InvalidPassword; 
                }
                
                return result;
            }
        }
    }
}