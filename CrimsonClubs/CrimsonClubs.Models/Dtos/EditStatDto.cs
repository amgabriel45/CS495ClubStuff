using CrimsonClubs.Models.Entities;
using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class EditStatDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string Abbreviation { get; set; }

        public void Edit(ref Stat dbo)
        {
            dbo.Name = Name;
            dbo.Description = Description;
            dbo.Abbreviation = Abbreviation;
        }
    }
}
