using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Extensions
{
    public static class ClaimsIdentityExtensions
    {
        public static void AddClaims(this ClaimsIdentity identity, User user)
        {
            identity.AddClaim(new Claim("UserId", user.Id.ToString(), ClaimValueTypes.Integer));
            identity.AddClaim(new Claim("FirstName", user.First));
            identity.AddClaim(new Claim("LastName", user.Last));
        }
    }
}
