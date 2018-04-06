using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddClubDto
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public bool IsRequestToJoin { get; set; }
        public int? GroupId { get; set; }
    }
}
