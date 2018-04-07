using CrimsonClubs.Models.Dtos;
using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace CrimsonClubs.Controllers.Api
{
    [RoutePrefix("api/events")]
    public class EventController : CCApiController
    {
        [HttpGet, Route("{eventId}")]
        [ResponseType(typeof(DetailedEventDto))]
        public IHttpActionResult GetEvent(int? eventId)
        {
            if (eventId == null)
            {
                return BadRequest();
            }

            var e = db.Events.Find(eventId);

            if (e == null)
            {
                return NotFound();
            }

            var dbo = db.Users.Find(CurrentUser.Id)
                .MM_User_Club.Where(m => m.IsAccepted)
                .SelectMany(m => m.Club.MM_Club_Event)
                .FirstOrDefault(m => m.EventId == eventId);

            var dto = new DetailedEventDto(dbo);

            return Ok(dto);
        }
    }
}
