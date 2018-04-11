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
        [HttpGet, Route("~/api/clubs/{id}/calendar")]
        [ResponseType(typeof(DetailedEventDto[]))]
        public IHttpActionResult GetClubEvents(int id)
        {
            var club = db.Clubs.Find(id);

            if (club == null)
            {
                return NotFound();
            }

            var hasPermission = club.MM_User_Club.Any(m => m.UserId == CurrentUser.Id && m.IsAccepted);

            if (!hasPermission)
            {
                return StatusCode(HttpStatusCode.Forbidden);
            }

            var events = club.MM_Club_Event.Select(m => new DetailedEventDto(m));

            return Ok(events);
        }

        [HttpGet, Route]
        [ResponseType(typeof(DetailedEventDto[]))]
        public IHttpActionResult GetUserEvents()
        {
            var events = db.Users.Find(CurrentUser.Id)
                .MM_User_Club
                .Where(m => m.IsAccepted)
                .SelectMany(m => m.Club.MM_Club_Event)
                .Select(m => new DetailedEventDto(m));

            return Ok(events);
        }

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

        [HttpPost, Route]
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult AddEvent(AddEventDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }

            var e = dto.ToEntity();
            db.Events.Add(e);
            db.SaveChanges();

            return Content(HttpStatusCode.Created, $"api/events/{e.Id}");
        }

        [HttpPut, Route]
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult EditEvent(EditEventDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }

            var e = db.Events.Find(dto.Id);

            if (e == null)
            {
                return NotFound();
            }

            dto.Edit(ref e);
            db.SaveChanges();

            return Ok();
        }

        [HttpDelete, Route("{id}")]
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult DeleteEvent(int id)
        {
            var e = db.Events.Find(id);

            if (e == null)
            {
                return NotFound();
            }


            db.MMM_User_Event_Stat.RemoveRange(e.MMM_User_Event_Stat);
            db.MM_Club_Event.RemoveRange(e.MM_Club_Event);
            db.Events.Remove(e);
            db.SaveChanges();

            return Ok();
        }
    }
}
