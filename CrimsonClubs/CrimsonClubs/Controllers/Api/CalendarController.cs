using CrimsonClubs.Models;
using CrimsonClubs.Models.Dtos;
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
    [RoutePrefix("api/calendar")]
    public class CalendarController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(DetailedEventDto[]))]
        public IHttpActionResult GetUserCalendar()
        {
            var events = db.Users.Find(CurrentUser.Id)
                .MM_User_Club
                .Where(m => m.IsAccepted)
                .SelectMany(m => m.Club.MM_Club_Event)
                .Select(m => new DetailedEventDto(m));

            return Ok(events);
        }
    }
}
