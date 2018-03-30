using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class UserStatsDto
    {
        public string UserName { get; set; }
        public Dictionary<int, int> Stats { get; set; }
    }
}
