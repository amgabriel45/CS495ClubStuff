using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddGroupDto
    {
        public string Name { get; set; }
        public string Description { get; set; }

        public AddGroupDto()
        {

        }

        public Group ToEntity()
        {
            var group = new Group()
            {
                Name = Name,
                Description = Description,
                OrganizationId = 1
            };

            return group;
        }
    }
}
