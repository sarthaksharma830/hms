namespace hms_web_api.Models {

    public class ComplaintPicture {

        public int Id { get; set; }

        public Complaint Complaint { get; set; }

        public string Path { get; set; }

    }

}