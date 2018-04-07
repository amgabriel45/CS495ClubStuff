using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CrimsonClubs.Models.Entities;

namespace CrimsonClubs.Models.Dtos
{
    public class UserStatsDto
    {
        public int UserId { get; set; }
        public string First { get; set; }
        public string Last { get; set; }
        public Dictionary<int, int> Stats { get; set; }

        public UserStatsDto()
        {

        }

        public UserStatsDto(IGrouping<User, MMM_User_Event_Stat> stats)
        {
            var user = stats.Key;

            UserId = user.Id;
            First = user.First;
            Last = user.Last;
            Stats = new Dictionary<int, int>();

            foreach (var stat in stats)
            {
                Stats.Add(stat.StatId, stat.Value);
            }
        }
    }
}
