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
    [RoutePrefix("api/event")]
    public class EventController : CCApiController
    {
        [HttpGet, Route("{eventId}")]
        [ResponseType(typeof(EventDto))]
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

            var dto = new EventDto()
            {
                Id = e.Id,
                Name = e.Name,
                Description = e.Description,
                Start = e.Start,
                Finish = e.Finish,
                IsGroupEvent = e.IsGroupEvent
            };

            dto.Clubs = e.MM_Club_Event
                .Select(m => m.Club)
                .Select(c => new ClubDto()
                {
                    Id = c.Id,
                    Name = c.Name,
                    Description = c.Description,
                    GroupName = c.Group.Name ?? "",
                    MemberCount = c.MM_User_Club.Count
                })
                .ToList();

            //dto.ClubStats = db.Stats
            //    .Where(s => s.Type == (int)StatType.Club)
            //    .Select(a => a.)

            return Ok(dto);
        }
    }
}
