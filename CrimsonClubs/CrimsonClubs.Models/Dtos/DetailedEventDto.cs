using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models.Dtos
{
    public class DetailedEventDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime Start { get; set; }
        public DateTime Finish { get; set; }
        public bool IsGroupEvent { get; set; }

        public int ClubId { get; set; }
        public string ClubName { get; set; }

        public List<ClubDto> Clubs { get; set; }
        public List<StatClubDto> ClubStats { get; set; }
        public List<StatGroupDto> GroupStats { get; set; }
        public List<UserStatsDto> UsersClubStats { get; set; }
        public List<UserStatsDto> UsersGroupStats { get; set; }

        public DetailedEventDto()
        {

        }

        public DetailedEventDto(MM_Club_Event dbo)
        {
            var e = dbo.Event;
            var club = dbo.Club;

            Id = e.Id;
            Name = e.Name;
            Description = e.Description;
            Start = e.Start;
            Finish = e.Finish;
            IsGroupEvent = e.IsGroupEvent;
            ClubId = club.Id;
            ClubName = club.Name;
            Clubs = e.MM_Club_Event.Select(m => new ClubDto(m.Club)).ToList();
        }
    }
}
