using CrimsonClubs.Models.Enums;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class StatDto
    {
        public int Id { get; set; }
        public int Name { get; set; }
        public int Description { get; set; }
        public int Abbreviation { get; set; }
        public StatType Type { get; set; }
    }
}
