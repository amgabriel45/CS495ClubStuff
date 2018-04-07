using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CrimsonClubs.Models.Entities;

namespace CrimsonClubs.Models.Dtos
{
    public class StatGroupDto : StatDto
    {
        public int GroupId { get; set; }

        public StatGroupDto()
        {

        }

        public StatGroupDto(Stat_Group dbo) : base(dbo.Stat)
        {
            GroupId = dbo.Group.Id;
        }
    }
}
