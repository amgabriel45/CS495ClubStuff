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
    }
}
