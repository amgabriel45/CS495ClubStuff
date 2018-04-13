using CrimsonClubs.Models.Dtos;
using CrimsonClubs.Models.Entities;
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
    [RoutePrefix("api/groups")]
    public class GroupController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(GroupDto[]))]
        public IHttpActionResult GetGroups()
        {
            var groups = db.Groups.ToList().Select(g => new GroupDto(g));

            return Ok(groups);
        }

        [HttpGet, Route("{id}")]
        [ResponseType(typeof(GroupDto))]
        public IHttpActionResult GetGroup(int id)
        {
            var group = db.Groups.Find(id);

            if (group == null)
            {
                return NotFound();
            }

            var dto = new GroupDto(group);

            return Ok(dto);
        }

        [HttpPost, Route]
        [ResponseType(typeof(GroupDto))]
        public IHttpActionResult AddGroup(AddGroupDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }

            var group = dto.ToEntity();

            db.Groups.Add(group);
            db.SaveChanges();

            return Created($"api/groups/{group.Id}", new GroupDto(group));
        }

        [HttpPut, Route]
        [ResponseType(typeof(GroupDto))]
        public IHttpActionResult EditGroup(EditGroupDto dto)
        {
            if (dto == null)
            {
                return BadRequest();
            }

            var group = db.Groups.Find(dto.Id);

            if (group == null)
            {
                return NotFound();
            }

            dto.Edit(ref group);
            db.SaveChanges();

            return Ok(new GroupDto(group));
        }

        [HttpDelete, Route("{id}")]
        [ResponseType(typeof(GroupDto))]
        public IHttpActionResult DeleteGroup(int id)
        {
            var group = db.Groups.Find(id);

            if (group == null)
            {
                return NotFound();
            }

            foreach (var club in group.Clubs)
            {
                club.GroupId = null;
            }

            db.Groups.Remove(group);
            db.SaveChanges();

            return Ok(new GroupDto(group));
        }
    }
}
