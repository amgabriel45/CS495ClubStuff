using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class UserDto
    {
        public int Id { get; set; }
        public string Email { get; set; }
        public string First { get; set; }
        public string Last { get; set; }
        public bool IsOrganizationAdmin { get; set; }
        public int OrganizationId { get; set; }

        public UserDto()
        {

        }

        public UserDto(User dbo)
        {
            Id = dbo.Id;
            Email = dbo.Email;
            First = dbo.First;
            Last = dbo.Last;
            IsOrganizationAdmin = dbo.IsOrganizationAdmin;
            OrganizationId = dbo.OrganizationId;
        }
    }
}
