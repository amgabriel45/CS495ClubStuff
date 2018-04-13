using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Security.Principal;
using System.Web;

namespace CrimsonClubs.Models
{
    public class CurrentUser : ClaimsPrincipal
    {
        public CurrentUser(IPrincipal principal) : base(principal as ClaimsPrincipal) { }
        
        public int Id => int.Parse(FindFirst("UserId").Value);
        public string First => FindFirst("FirstName").Value;
        public string Last => FindFirst("LastName").Value;
    }
}