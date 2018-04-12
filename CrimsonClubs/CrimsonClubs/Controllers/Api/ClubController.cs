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
    [RoutePrefix("api/clubs")]
    public class ClubController : CCApiController
    {
        [HttpGet, Route("{id}")]
        [ResponseType(typeof(DetailedClubDto))]
        public IHttpActionResult GetClub(int id)
        {
            var club = db.Clubs.Find(id);

            if (club == null)
            {
                return NotFound();
            }

            bool hasPermission = club.MM_User_Club.Any(m => m.UserId == CurrentUser.Id && m.IsAccepted == true);

            if (!hasPermission)
            {
                return StatusCode(HttpStatusCode.Forbidden);
            }

            var dto = new DetailedClubDto(club, CurrentUser.Id);

            return Ok(dto);
        }

        [HttpGet, Route]
        [ResponseType(typeof(ClubDto[]))]
        public IHttpActionResult GetUserClubs()
        {
            var clubs = db.Users.Find(CurrentUser.Id)
                .MM_User_Club.Select(m => m.Club)
                .Select(c => new ClubDto(c, CurrentUser.Id));

            return Ok(clubs);
        }

        [HttpGet, Route("all")]
        [ResponseType(typeof(ClubDto[]))]
        public IHttpActionResult GetAllClubs()
        {
            var clubs = db.Clubs
                .OrderBy(c => c.Group.Name)
                .ToList()
                .Select(c => new ClubDto(c, CurrentUser.Id));

            return Ok(clubs);
        }

        [HttpGet, Route]
        [ResponseType(typeof(ClubDto[]))]
        public IHttpActionResult GetClubsInGroup(int groupId)
        {
            var group = db.Groups.Find(groupId);

            if (group == null)
            {
                return NotFound();
            }

            var clubs = group.Clubs
                .ToList()
                .Select(c => new ClubDto(c, CurrentUser.Id));

            return Ok(clubs);
        }

        [HttpPost, Route]
        [ResponseType(typeof(ClubDto))]
        public IHttpActionResult AddClub(AddClubDto dto)
        {
            var club = new Club()
            {
                Name = dto.Name,
                Description = dto.Description,
                IsRequestToJoin = dto.IsRequestToJoin,
                GroupId = dto.GroupId,
                OrganizationId = 1
            };

            var relation = new MM_User_Club();
            relation.Club = club;
            relation.UserId = CurrentUser.Id;
            relation.IsAccepted = true;
            relation.IsAdmin = true;

            db.Clubs.Add(club);
            db.MM_User_Club.Add(relation);

            db.SaveChanges();

            return Created($"api/clubs/{club.Id}", new ClubDto(club, CurrentUser.Id));
        }

        [HttpPut, Route]
        [ResponseType(typeof(ClubDto))]
        public IHttpActionResult EditClub(EditClubDto dto)
        {
            var club = db.Clubs.Find(dto.Id);

            if (club == null)
            {
                return BadRequest();
            }
            
            club.Name = dto.Name;
            club.Description = dto.Description;
            club.IsRequestToJoin = dto.IsRequestToJoin;
            club.GroupId = dto.GroupId;

            if (!club.IsRequestToJoin)
            {
                var joinRequests = club.MM_User_Club.Where(m => !m.IsAccepted);

                foreach (var request in joinRequests)
                {
                    request.IsAccepted = true;
                }
            }

            db.SaveChanges();

            return Ok(new ClubDto(club, CurrentUser.Id));
        }

        [HttpDelete, Route("{id}")]
        [ResponseType(typeof(string))]
        public IHttpActionResult DeleteClub(int id)
        {
            var club = db.Clubs.Find(id);

            if (club == null)
            {
                return NotFound();
            }

            bool HasPermission = club.MM_User_Club.FirstOrDefault(m => m.UserId == CurrentUser.Id)?.IsAdmin ?? false;

            if (!HasPermission)
            {
                return StatusCode(HttpStatusCode.Forbidden);
            }

            db.MM_User_Club.RemoveRange(club.MM_User_Club);
            db.Clubs.Remove(club);
            db.SaveChanges();

            return Ok();
        }

        [HttpPost, Route("{id}/join")]
        [ResponseType(typeof(string))]
        public IHttpActionResult JoinClub(int id)
        {
            var club = db.Clubs.Find(id);

            if (club == null)
            {
                return NotFound();
            }

            bool isInClub = club.MM_User_Club.Any(m => m.UserId == CurrentUser.Id);
            bool isInGroup = db.MM_User_Club.Any(m => m.UserId == CurrentUser.Id && m.Club.GroupId == club.GroupId && m.ClubId != club.Id);

            if (isInClub)
            {
                return BadRequest("Already in club");
            }

            if (isInGroup)
            {
                return BadRequest("Already in group");
            }

            var relation = new MM_User_Club()
            {
                UserId = CurrentUser.Id,
                ClubId = club.Id,
                IsAdmin = false,
                IsAccepted = !club.IsRequestToJoin
            };

            db.MM_User_Club.Add(relation);
            db.SaveChanges();

            var message = relation.IsAccepted ? "joined" : "requested";

            return Ok(message);
        }

        [HttpPost, Route("{id}/leave")]
        [ResponseType(typeof(string))]
        public IHttpActionResult LeaveClub(int id)
        {
            var club = db.Clubs.Find(id);

            if (club == null)
            {
                return NotFound();
            }

            var relation = club.MM_User_Club.FirstOrDefault(m => m.UserId == CurrentUser.Id);

            if (relation == null)
            {
                return BadRequest();
            }

            db.MM_User_Club.Remove(relation);
            db.SaveChanges();

            return Ok();
        }
    }
}
