namespace hms_web_api.Models {

    public class Caretaker {

        public int Id { get; set; }
        public string Name { get; set; }
        public string Username { get; set; }
        public string Contact { get; set; }
        public string Email { get; set; }
        public Hostel Hostel { get; set; }

    }

}