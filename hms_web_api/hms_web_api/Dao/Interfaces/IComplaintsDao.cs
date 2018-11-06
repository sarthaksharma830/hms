using System.Collections.Generic;
using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces {

    public interface IComplaintsDao {

        List<Complaint> GetComplaintsByStudent(int sid);

    }

}