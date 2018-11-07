using System.Collections.Generic;
using System.Net.NetworkInformation;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class AppointmentsController : ControllerBase {

        [HttpGet("student/{sid}")]
        public ActionResult<List<Appointment>> GetAppointmentsByStudent(int sid) {
            var dao = new AppointmentsDao();
            return dao.GetAppointmentsByStudent(sid);
        }
        
        [HttpGet("complaint/{cid}")]
        public ActionResult<List<Appointment>> GetAppointmentsByComplaint(int cid) {
            var dao = new AppointmentsDao();
            return dao.GetAppointmentsByComplaint(cid);
        }

    }

}