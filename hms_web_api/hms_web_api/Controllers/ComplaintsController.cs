using System;
using System.Collections.Generic;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class ComplaintsController {

        [HttpGet("student/{sid}")]
        public ActionResult<List<Complaint>> GetComplaintsByStudent(int sid, [FromQuery(Name = "len")] int len) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintsByStudent(sid, len);
        }
        
    }

}