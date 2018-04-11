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
    [RoutePrefix("api/stats")]
    public class ClubStatController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(StatClubDto[]))]
        public IHttpActionResult GetClubStats(int clubId)
        {
            var stats = db.Stat_Club
                .Where(s => s.ClubId == clubId)
                .ToList()
                .Select(s => new StatClubDto(s));

            return Ok(stats);
        }

        [HttpPost, Route("club")]
        [ResponseType(typeof(StatClubDto))]
        public IHttpActionResult AddClubStat(AddStatClubDto dto)
        {
            var stat = dto.ToEntity();
            db.Stat_Club.Add(stat);
            db.SaveChanges();

            return StatusCode(HttpStatusCode.Created);
        }
    }
}
