using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models.Dtos
{
    public class ClubDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string GroupName { get; set; }
        public int MemberCount { get; set; }

        public ClubDto()
        {

        }

        public ClubDto(Club dbo)
        {
            Id = dbo.Id;
            Name = dbo.Name;
            Description = dbo.Description;
            GroupName = dbo.Group?.Name ?? "";
            MemberCount = dbo.MM_User_Club.Count(m => m.IsAccepted);
        }
    }
}