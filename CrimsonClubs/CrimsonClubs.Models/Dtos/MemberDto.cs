using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class MemberDto
    {
        public int Id { get; set; }
        public string Email { get; set; }
        public string First { get; set; }
        public string Last { get; set; }
        public bool IsGroupAdmin { get; set; }

        public MemberDto(MM_User_Club dbo)
        {
            Id = dbo.User.Id;
            Email = dbo.User.Email;
            First = dbo.User.First;
            Last = dbo.User.Last;
            IsGroupAdmin = dbo.IsAdmin;
        }
    }
}
