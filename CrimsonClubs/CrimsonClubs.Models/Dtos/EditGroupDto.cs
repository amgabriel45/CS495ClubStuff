using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class EditGroupDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }

        public EditGroupDto()
        {

        }

        public void Edit(ref Group dbo)
        {
            dbo.Name = Name;
            dbo.Description = Description;
        }
    }
}
