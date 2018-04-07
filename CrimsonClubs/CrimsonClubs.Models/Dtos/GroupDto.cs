using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class GroupDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public int OrganizationId { get; set; }
        public int ClubCount { get; set; }

        public GroupDto()
        {

        }

        public GroupDto(Group dbo)
        {
            Id = dbo.Id;
            Name = dbo.Name;
            Description = dbo.Description;
            OrganizationId = dbo.OrganizationId;
            ClubCount = dbo.Clubs.Count;
        }
    }
}
