using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces {

    public interface ICaretakersDao {

        Caretaker GetCaretakerInfo(string username);

    }

}