using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces
{
    public interface IAuthenticationDao
    {
        LoginResult StudentLogin(string rollno, string password);
    }
}