using System;
using System.Collections.Generic;
using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces {

    public interface IComplaintsDao {

        List<Complaint> GetComplaintsByStudent(int sid, int len);
        
        List<Complaint> GetComplaintsByCaretaker(int cid, int len);

        Complaint GetComplaintById(int id);

        Complaint UpdateComplaintStarStatus(int id, bool star);

        List<ComplaintCategory> GetAllComplaintCategories();

        List<string> GetDefaultComplaintTitles(int id);

        Complaint CreateComplaint(Complaint complaint);

        Complaint MarkComplaintAsResolved(int id);

    }

}