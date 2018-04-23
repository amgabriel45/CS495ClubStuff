using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CrimsonClubs.Models.Entities;

namespace CrimsonClubs.Models.Dtos
{
    public class JoinRequestDto
    {
        public int UserId { get; set; }
        public string First { get; set; }
        public string Last { get; set; }
        public JoinRequestDto(MM_User_Club relation)
        {
            var user = relation.User;

            UserId = user.Id;
            First = user.First;
            Last = user.Last;
        }
    }
}
