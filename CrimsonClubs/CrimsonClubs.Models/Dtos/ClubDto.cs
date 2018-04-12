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
        public bool IsRequestToJoin { get; set; }
        public int? GroupId { get; set; }
        public string GroupName { get; set; }
        public int MemberCount { get; set; }
        public bool IsAccepted { get; set; }
        public bool HasRequested { get; set; }
        public bool IsAllowedToJoin { get; set; }

        public ClubDto()
        {

        }

        public ClubDto(Club dbo, int userId)
        {
            Id = dbo.Id;
            Name = dbo.Name;
            Description = dbo.Description;
            IsRequestToJoin = dbo.IsRequestToJoin;
            GroupId = dbo.GroupId;
            GroupName = dbo.Group?.Name ?? "";
            MemberCount = dbo.MM_User_Club.Count(m => m.IsAccepted);
            IsAccepted = dbo.MM_User_Club.Any(m => m.UserId == userId && m.IsAccepted);
            HasRequested = dbo.MM_User_Club.Any(m => m.UserId == userId && !m.IsAccepted);
            IsAllowedToJoin = !IsAccepted && !(dbo.Group?.Clubs.SelectMany(m => m.MM_User_Club).Any(m => m.UserId == userId && m.ClubId != Id) ?? false);
        }
    }
}