using CrimsonClubs.Models.Entities;
using CrimsonClubs.Start;
using Microsoft.Owin.Infrastructure;
using Microsoft.Owin.Security;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Extensions
{
    public static class UserExtensions
    {
        public static string GenerateAccessToken(this User user)
        {
            var identity = new ClaimsIdentity(AuthConfig.OAuthBearerOptions.AuthenticationType);
            identity.AddClaims(user);

            var currentUtc = new SystemClock().UtcNow;

            var ticket = new AuthenticationTicket(identity, new AuthenticationProperties());
            ticket.Properties.IssuedUtc = currentUtc;
            ticket.Properties.ExpiresUtc = currentUtc.Add(TimeSpan.FromDays(30));

            string token = AuthConfig.OAuthBearerOptions.AccessTokenFormat.Protect(ticket);

            return token;
        }
    }
}
