using System;

namespace hms_web_api.Models {

    public class Appointment {

        public int Id { get; set; }
        public Complaint Complaint { get; set; }
        public DateTime Date { get; set; }
        public TimeSpan FromTime { get; set; }
        public TimeSpan ToTime { get; set; }
        public bool Status { get; set; }

    }

}