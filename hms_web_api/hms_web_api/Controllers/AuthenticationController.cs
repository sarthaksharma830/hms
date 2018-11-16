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
    public class AuthenticationController : ControllerBase {

        [HttpPost("login/s")]
        [Consumes("application/json")]
        public ActionResult<LoginResult> StudentLogin([FromBody] Credential credential) {
            var dao = new AuthenticationDao();
            return dao.StudentLogin(credential.Rollno, credential.Password);
        }
        
        [HttpPost("login/c")]
        [Consumes("application/json")]
        public ActionResult<LoginResult> CaretakerLogin([FromBody] Credential credential) {
            var dao = new AuthenticationDao();
            return dao.CaretakerLogin(credential.Rollno, credential.Password);
        }

    }

}