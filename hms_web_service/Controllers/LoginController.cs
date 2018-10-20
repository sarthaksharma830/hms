using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using hms_web_service.Models;

namespace hms_web_service.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase {
        
        [HttpPost]
        public ActionResult<bool> login([FromBody] Credential credential) {
            return (credential.rollno == "101783037" && credential.password == "test123");
        }

        [HttpGet]
        public ActionResult<string> login() {
            return "Hello World";
        }

    }
}
