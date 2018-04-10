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
        public int? GroupId { get; set; }
        public string GroupName { get; set; }
        public int MemberCount { get; set; }
        public bool IsAccepted { get; set; }

        public ClubDto()
        {

        }

        public ClubDto(Club dbo)
        {
            Id = dbo.Id;
            Name = dbo.Name;
            Description = dbo.Description;
            GroupId = dbo.GroupId;
            GroupName = dbo.Group?.Name ?? "";
            MemberCount = dbo.MM_User_Club.Count(m => m.IsAccepted);
            IsAccepted = false; // TODO
        }
    }
}