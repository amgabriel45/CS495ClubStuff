using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models.Dtos
{
    public class EventDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime Start { get; set; }
        public DateTime Finish { get; set; }
        public bool IsGroupEvent { get; set; }
        public List<ClubDto> Clubs { get; set; }
        public List<StatClubDto> ClubStats { get; set; }
        public List<StatGroupDto> GroupStats { get; set; }
        public List<UserStatsDto> UsersClubStats { get; set; }
        public List<UserStatsDto> UsersGroupStats { get; set; }

        public int ClubId { get; set; }
        public string ClubName { get; set; }
    }
}
