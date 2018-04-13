using CrimsonClubs.Models.Entities;
using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddStatClubDto : AddStatDto
    {
        public int ClubId { get; set; }

        public new Stat_Club ToEntity()
        {
            var stat = new Stat_Club();
            stat.Stat = base.ToEntity();
            stat.Stat.Type = (int)StatType.Club;
            stat.ClubId = ClubId;

            return stat;
        }
    }
}
