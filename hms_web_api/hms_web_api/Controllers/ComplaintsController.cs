using System.Collections.Generic;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class ComplaintsController {

        [HttpGet]
        public ActionResult<List<Complaint>> GetComplaintsByStudent([FromQuery(Name = "sid")] int sid) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintsByStudent(sid);
        }
        
    }

}