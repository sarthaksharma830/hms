using System;
using System.Collections.Generic;

namespace hms_web_api.Models {

    public class Complaint {

        public int Id { get; set; }
        public string Title { get; set; }
        public Student Student { get; set; }
        public ComplaintCategory ComplaintCategory { get; set; }
        public DateTime DateTime { get; set; }
        public string Description { get; set; }
        public string Feedback { get; set; }
        public ComplaintStatus ComplaintStatus { get; set; }
        public bool Starred { get; set; }
        public DateTime? AppointmentDatePreference { get; set; }
        public DateTime? AppointmentFromTimePreference { get; set; }
        public DateTime? AppointmentToTimePreference { get; set; }
        public List<string> Pictures { get; set; }

    }

}