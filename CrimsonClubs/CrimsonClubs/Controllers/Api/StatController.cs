using CrimsonClubs.Models;
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
    [RoutePrefix("api/stats")]
    public class StatController : CCApiController
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
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult AddClubStat(AddStatClubDto dto)
        {
            var stat = dto.ToEntity();
            db.Stat_Club.Add(stat);
            db.SaveChanges();

            return StatusCode(HttpStatusCode.Created);
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

        [HttpPut, Route]
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult EditStat(EditStatDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }

            var stat = db.Stats.Find(dto.Id);

            if (stat == null)
            {
                return NotFound();
            }

            dto.Edit(ref stat);
            db.SaveChanges();

            return Ok();
        }

        [HttpDelete, Route("{id}")]
        [ResponseType(typeof(IHttpActionResult))]
        public IHttpActionResult DeleteStat(int id)
        {
            var stat = db.Stats.Find(id);

            if (stat == null)
            {
                return NotFound();
            }

            if (stat.Type == (int)StatType.Club)
            {
                db.Stat_Club.Remove(stat.Stat_Club);
            }
            else
            {
                db.Stat_Group.Remove(stat.Stat_Group);
            }

            db.Stats.Remove(stat);
            db.SaveChanges();

            return Ok();
        }
    }
}
