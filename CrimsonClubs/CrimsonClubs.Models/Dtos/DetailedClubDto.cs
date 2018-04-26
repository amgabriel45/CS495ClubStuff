using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models.Dtos
{
    public class DetailedClubDto : ClubDto
    {
        public List<MemberDto> Members { get; set; }
        public List<DetailedEventDto> Events { get; set; }

        public DetailedClubDto()
        {

        }

        public DetailedClubDto(Club dbo, int userId) : base(dbo, userId)
        {
            Members = dbo.MM_User_Club.Where(m => m.IsAccepted).Select(m => new MemberDto(m)).ToList();
            Events = dbo.MM_Club_Event.Select(m => new DetailedEventDto(m, userId)).OrderBy(e => e.Start).ToList();
        }
    }
}