using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces {

    public interface IStudentDao {

        Student GetStudentInfo(string rollno);

    }

}