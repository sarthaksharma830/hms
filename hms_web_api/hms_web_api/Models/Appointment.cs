using System;

namespace hms_web_api.Models {

    public class Appointment {

        public int Id { get; set; }
        public Complaint Complaint { get; set; }
        public DateTime Date { get; set; }
        public DateTime FromTime { get; set; }
        public DateTime ToTime { get; set; }
        public bool Status { get; set; }

    }

}