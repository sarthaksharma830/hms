using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class StudentsController : ControllerBase {

        [HttpGet("{rollno}")]
        public ActionResult<Student> Get(string rollno) {
            var dao = new StudentDao();
            return dao.GetStudentInfo(rollno);
        }

    }

}