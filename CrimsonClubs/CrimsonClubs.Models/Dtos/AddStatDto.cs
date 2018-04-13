using CrimsonClubs.Models.Entities;
using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddStatDto
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public string Abbreviation { get; set; }

        public Stat ToEntity()
        {
            var stat = new Stat();
            stat.Name = Name;
            stat.Description = Description;
            stat.Abbreviation = Abbreviation;

            return stat;
        }
    }
}
