using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class ApiAuthDto
    {
        public string Token { get; set; }
        public UserDto User { get; set; }
        
        public ApiAuthDto()
        {

        }

        public ApiAuthDto(string token, User user)
        {
            Token = token;
            User = new UserDto(user);
        }
    }
}
