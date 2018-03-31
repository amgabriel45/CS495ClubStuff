using Microsoft.Owin.Security;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    [RoutePrefix("auth")]
    public class AuthController : CCSiteController
    {
        [AllowAnonymous]
        [HttpGet, Route("login")]
        public void Login()
        {
            HttpContext.GetOwinContext().Authentication.Challenge(new AuthenticationProperties { RedirectUri = "/" }, "Google");
        }

        [HttpGet, Route("logout")]
        public ActionResult Logout()
        {
            Request.GetOwinContext().Authentication.SignOut();

            return RedirectToAction("Login");
        }
    }
}