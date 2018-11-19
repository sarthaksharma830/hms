using System.Collections.Generic;
using hms_web_api.Models;

namespace hms_web_api.Dao.Interfaces {

    public interface IAppointmentsDao {

        List<Appointment> GetPendingAppointmentsByStudent(int sid);
        List<Appointment> GetCompletedAppointmentsByStudent(int sid);
        List<Appointment> GetAppointmentsByComplaint(int cid);
        List<Appointment> CreateAppointment(Appointment appointment);
        List<Appointment> UpdateAppointment(Appointment appointment);

    }

}