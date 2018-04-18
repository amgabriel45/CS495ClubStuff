using CrimsonClubs.Models;
using CrimsonClubs.Models.Dtos;
using CrimsonClubs.Models.Entities;
using CrimsonClubs.Start;
using Microsoft.Owin.Infrastructure;
using Microsoft.Owin.Security;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Http.Description;

namespace CrimsonClubs.Controllers.Api
{
    [RoutePrefix("api/auth")]
    public class AuthController : CCApiController
    {
        private static readonly HttpClient client = new HttpClient();

        [AllowAnonymous]
        [HttpGet, Route]
        [ResponseType(typeof(ApiAuthDto))]
        public async Task<IHttpActionResult> GetAuth(string token)
        {
            string verifyUrl = $"https://www.googleapis.com/oauth2/v3/tokeninfo?id_token={token}";

            var response = await client.GetAsync(verifyUrl);

            if (response.StatusCode != HttpStatusCode.OK)
            {
                return BadRequest("Invalid Token");
            }

            string json = await response.Content.ReadAsStringAsync();
            var data = JsonConvert.DeserializeObject<GoogleAuthResponse>(json);

            //string androidClientId = "77421544828-33rdp50mrdtpeje5dpbal37s63e5ojco.apps.googleusercontent.com";

            if (data.ClientIdWeb != AuthConfig.ClientId) // && data.ClientIdApp != androidClientId
            {
                return BadRequest("Invalid Token Origin");
            }

            var user = db.Users.FirstOrDefault(u => u.Email == data.Email);

            if (user == null)
            {
                user = new User();
                user.Email = data.Email;
                user.First = data.First;
                user.Last = data.Last;
                user.IsOrganizationAdmin = false;
                user.OrganizationId = 1;

                db.Users.Add(user);
                db.SaveChanges();
            }

            var identity = new ClaimsIdentity(AuthConfig.OAuthBearerOptions.AuthenticationType);
            identity.AddClaim(new Claim("UserId", user.Id.ToString(), ClaimValueTypes.Integer));
            identity.AddClaim(new Claim("FirstName", user.First));
            identity.AddClaim(new Claim("LastName", user.Last));

            var currentUtc = new SystemClock().UtcNow;

            var ticket = new AuthenticationTicket(identity, new AuthenticationProperties());
            ticket.Properties.IssuedUtc = currentUtc;
            ticket.Properties.ExpiresUtc = currentUtc.Add(TimeSpan.FromDays(1));

            string accessToken = AuthConfig.OAuthBearerOptions.AccessTokenFormat.Protect(ticket);

            var dto = new ApiAuthDto();
            dto.Token = accessToken;
            dto.User = new UserDto()
            {
                Id = user.Id,
                Email = user.Email,
                First = user.First,
                Last = user.Last,
                IsOrganizationAdmin = user.IsOrganizationAdmin,
                OrganizationId = user.OrganizationId
            };

            return Ok(dto);
        }

        [AllowAnonymous]
        [HttpGet, Route("test/{userId}")]
        [ResponseType(typeof(ApiAuthDto))]
        public IHttpActionResult GetAuthTest(int userId)
        {
            var identity = new ClaimsIdentity(AuthConfig.OAuthBearerOptions.AuthenticationType);
            identity.AddClaim(new Claim("UserId", userId.ToString(), ClaimValueTypes.Integer));

            var currentUtc = new SystemClock().UtcNow;

            var ticket = new AuthenticationTicket(identity, new AuthenticationProperties());
            ticket.Properties.IssuedUtc = currentUtc;
            ticket.Properties.ExpiresUtc = currentUtc.Add(TimeSpan.FromMinutes(30));

            string accessToken = AuthConfig.OAuthBearerOptions.AccessTokenFormat.Protect(ticket);

            var user = db.Users.Find(userId);

            if (user == null)
            {
                return NotFound();
            }

            var dto = new ApiAuthDto();
            dto.Token = accessToken;
            dto.User = new UserDto()
            {
                Id = user.Id,
                Email = user.Email,
                First = user.First,
                Last = user.Last,
                IsOrganizationAdmin = user.IsOrganizationAdmin,
                OrganizationId = user.OrganizationId
            };

            return Ok(dto);
        }
    }
}
