using System.Data.SqlClient;
using Npgsql;

namespace hms_web_api.Dao
{
    public static class SqlConnectionManager
    {
        public static NpgsqlConnection GetConnection()
        {
            var connection = new NpgsqlConnection
            {
                ConnectionString = "Host=localhost;Username=postgres;Password=dadpc;Database=hms;SearchPath=hms"
            };
            
            connection.Open();
            return connection;
        }
    }
}