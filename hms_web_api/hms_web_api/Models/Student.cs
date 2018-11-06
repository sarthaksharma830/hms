namespace hms_web_api.Models
{
    public class Student
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Rollno { get; set; }
        public string PersonalContact { get; set; }
        public string ParentContact { get; set; }
        public string Email { get; set; }
        public char Gender { get; set; }
        public Hostel Hostel { get; set; }
    }
}