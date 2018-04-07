using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class StatClubDto : StatDto
    {
        public int ClubId { get; set; }

        public StatClubDto()
        {

        }

        public StatClubDto(Stat_Club dbo) : base(dbo.Stat)
        {
            ClubId = dbo.Club.Id;
        }
    }
}
