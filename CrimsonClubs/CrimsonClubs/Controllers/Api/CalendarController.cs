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
            var a = db.Users.Find(CurrentUser.Id)
                .MM_User_Club
                .Where(m => m.IsAccepted);

            var b = a.Select(m => m.Club);

            var c = b
                .SelectMany(m => m.MM_Club_Event);

            var events = c
                .Select(m => new DetailedEventDto(m));

            return Ok(events);
        }
    }
}
