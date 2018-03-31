using Microsoft.Owin.Infrastructure;
using Microsoft.Owin.Security;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;
using System.Web.Http;
using System.Web.Http.Description;

namespace CrimsonClubs.Controllers.Api
{
    [RoutePrefix("api/auth")]
    public class AuthController : CCApiController
    {
        [AllowAnonymous]
        [HttpGet, Route]
        [ResponseType(typeof(string))]
        public IHttpActionResult GetAuth()
        {
            var identity = new ClaimsIdentity(Startup.OAuthBearerOptions.AuthenticationType);
            identity.AddClaim(new Claim("UserId", "1", ClaimValueTypes.Integer));

            var currentUtc = new SystemClock().UtcNow;

            var ticket = new AuthenticationTicket(identity, new AuthenticationProperties());
            ticket.Properties.IssuedUtc = currentUtc;
            ticket.Properties.ExpiresUtc = currentUtc.Add(TimeSpan.FromMinutes(30));

            string accessToken = Startup.OAuthBearerOptions.AccessTokenFormat.Protect(ticket);

            return Ok(accessToken);
        }
    }
}
