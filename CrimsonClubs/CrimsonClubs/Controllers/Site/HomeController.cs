using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    public class HomeController : CCSiteController
    {
        [HttpGet, Route]
        public ActionResult Index()
        {
            return View();
        }

        [HttpGet, Route("check")]
        public string Check()
        {
            if (Request.IsAuthenticated)
            {
                return User.Identity.Name + " " + ((ClaimsIdentity)User.Identity).FindFirst(ClaimTypes.Email).Value;
            }

            return "nope";
        }
    }
}