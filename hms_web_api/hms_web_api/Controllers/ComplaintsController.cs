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

        [HttpGet("{id}")]
        public ActionResult<Complaint> GetComplaintById(int id) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintById(id);
        }

        [HttpPut("star/{id}/{star}")]
        public ActionResult<Complaint> UpdateComplaintStarStatus(int id, int star) {
            var dao = new ComplaintsDao();
            return dao.UpdateComplaintStarStatus(id, star == 1);
        }

        [HttpGet("pictures/{id}")]
        public ActionResult<List<ComplaintPicture>> GetComplaintPictures(int id) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintPictures(id);
        }

        [HttpGet("categories")]
        public ActionResult<List<ComplaintCategory>> GetAllComplaintCategories() {
            var dao = new ComplaintsDao();
            return dao.GetAllComplaintCategories();
        }

        [HttpGet("titles/{id}")]
        public ActionResult<List<string>> GetDefaultComplaintTitles(int id) {
            var dao = new ComplaintsDao();
            return dao.GetDefaultComplaintTitles(id);
        }

        [HttpPost]
        [Consumes("application/json")]
        public ActionResult<Complaint> CreateComplaint([FromBody] Complaint complaint) {
            var dao = new ComplaintsDao();
            return dao.CreateComplaint(complaint);
        }

    }

}