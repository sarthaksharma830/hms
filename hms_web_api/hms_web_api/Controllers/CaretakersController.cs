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
    public class CaretakersController : ControllerBase {

        [HttpGet("{username}")]
        public ActionResult<Caretaker> Get(string username) {
            var dao = new CaretakersDao();
            return dao.GetCaretakerInfo(username);
        }

    }

}