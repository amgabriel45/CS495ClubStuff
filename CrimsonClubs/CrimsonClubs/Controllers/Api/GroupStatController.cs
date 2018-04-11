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
    public class GroupStatController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(StatGroupDto[]))]
        public IHttpActionResult GetGroupStats(int groupId)
        {
            var stats = db.Stat_Group
                .Where(s => s.GroupId == groupId)
                .ToList()
                .Select(s => new StatGroupDto(s));

            return Ok(stats);
        }

        [HttpPost, Route("group")]
        [ResponseType(typeof(StatClubDto))]
        public IHttpActionResult AddGroupStat(AddStatGroupDto dto)
        {
            var stat = dto.ToEntity();
            db.Stat_Group.Add(stat);
            db.SaveChanges();

            return StatusCode(HttpStatusCode.Created);
        }
    }
}
