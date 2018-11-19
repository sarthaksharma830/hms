using System.Collections.Generic;
using System.Net.NetworkInformation;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class AppointmentsController : ControllerBase {

        [HttpGet("student/{sid}/p")]
        public ActionResult<List<Appointment>> GetPendingAppointmentsByStudent(int sid) {
            var dao = new AppointmentsDao();
            return dao.GetPendingAppointmentsByStudent(sid);
        }
        
        [HttpGet("student/{sid}/c")]
        public ActionResult<List<Appointment>> GetCompletedAppointmentsByStudent(int sid) {
            var dao = new AppointmentsDao();
            return dao.GetCompletedAppointmentsByStudent(sid);
        }
        
        [HttpGet("complaint/{cid}")]
        public ActionResult<List<Appointment>> GetAppointmentsByComplaint(int cid) {
            var dao = new AppointmentsDao();
            return dao.GetAppointmentsByComplaint(cid);
        }

        [HttpPost]
        public ActionResult<List<Appointment>> CreateAppointment([FromBody] Appointment appointment) {
            var dao = new AppointmentsDao();
            return dao.CreateAppointment(appointment);
        }

        [HttpPut]
        public ActionResult<List<Appointment>> UpdateAppointment([FromBody] Appointment appointment) {
            var dao = new AppointmentsDao();
            return dao.UpdateAppointment(appointment);
        }

    }

}