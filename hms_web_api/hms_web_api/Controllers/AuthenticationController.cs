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

        [HttpPost("login")]
        [Consumes("application/json")]
        public ActionResult<LoginResult> Get([FromBody] Credential credential) {
            var dao = new AuthenticationDao();
            return dao.Login(credential.Rollno, credential.Password);
        }

    }

}