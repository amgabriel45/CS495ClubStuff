using CrimsonClubs.Extensions;
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
            var auth = JsonConvert.DeserializeObject<GoogleAuthResponseDto>(json);

            //string androidClientId = "77421544828-33rdp50mrdtpeje5dpbal37s63e5ojco.apps.googleusercontent.com";

            if (auth.ClientIdWeb != AuthConfig.ClientId) // && data.ClientIdApp != androidClientId
            {
                return BadRequest("Invalid Token Origin");
            }

            var user = db.Users.FirstOrDefault(u => u.Email == auth.Email);

            if (user == null)
            {
                user = auth.ToEntity();

                db.Users.Add(user);
                db.SaveChanges();
            }

            string accessToken = user.GenerateAccessToken();

            var dto = new ApiAuthDto(accessToken, user);

            return Ok(dto);
        }

        [AllowAnonymous]
        [HttpGet, Route("test/{userId}")]
        [ResponseType(typeof(ApiAuthDto))]
        public IHttpActionResult GetAuthTest(int userId)
        {
            var user = db.Users.Find(userId);

            if (user == null)
            {
                return NotFound();
            }

            string token = user.GenerateAccessToken();

            var dto = new ApiAuthDto(token, user);

            return Ok(dto);
        }
    }
}
