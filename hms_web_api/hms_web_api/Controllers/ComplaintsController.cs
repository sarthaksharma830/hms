using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using hms_web_api.Dao.Impl;
using hms_web_api.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace hms_web_api.Controllers {

    [Route("api/[controller]")]
    [ApiController]
    public class ComplaintsController {

        private readonly string PICTURE_UPLOAD_PATH = "C:\\Users\\Sarthak\\hms\\complaint_pictures";
        
        [HttpGet("student/{sid}")]
        public ActionResult<List<Complaint>> GetComplaintsByStudent(int sid, [FromQuery(Name = "len")] int len) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintsByStudent(sid, len);
        }

        [HttpGet("caretaker/{cid}")]
        public ActionResult<List<Complaint>> GetComplaintsByCaretaker(int cid, [FromQuery(Name = "len")] int len) {
            var dao = new ComplaintsDao();
            return dao.GetComplaintsByCaretaker(cid, len);
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
        public ActionResult<List<string>> GetComplaintPictures(int id) {
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

        [HttpPost("pictures/{cid}")]
        public ActionResult<List<string>> UploadComplaintPictures([FromForm(Name = "pictures")] IEnumerable<IFormFile> pictures, int cid) {
            Directory.CreateDirectory(PICTURE_UPLOAD_PATH);
            var pictureNames = new List<string>();
            foreach (var picture in pictures) {
                if (picture.Length > 0) {
                    var fileName = $"CID_{cid}_{Guid.NewGuid()}{Path.GetExtension(picture.FileName)}";
                    using (var stream = new FileStream(Path.Combine(PICTURE_UPLOAD_PATH, fileName), FileMode.Create)) {
                        picture.CopyTo(stream);
                    }
                    pictureNames.Add($"/api/complaints/pictures/get/{fileName}");
                }
            }

            var dao = new ComplaintsDao();
            return dao.InsertComplaintPictures(cid, pictureNames);
        }

        [HttpGet("pictures/get/{fileName}")]
        public ActionResult GetPicture(string fileName) {
            var filePath = Path.Combine(PICTURE_UPLOAD_PATH, fileName);
            var image = File.OpenRead(filePath);
            return new FileStreamResult(image, "image/jpeg");
        }

    }

}