using CrimsonClubs.Models.Entities;
using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddStatGroupDto : AddStatDto
    {
        public int GroupId { get; set; }

        public new Stat_Group ToEntity()
        {
            var stat = new Stat_Group();
            stat.Stat = base.ToEntity();
            stat.Stat.Type = (int)StatType.Group;
            stat.GroupId = GroupId;

            return stat;
        }
    }
}
