using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    [RoutePrefix("account")]
    public class AccountController : CCSiteController
    {
        [HttpGet, Route("login")]
        public ActionResult Login()
        {
            return View();
        }
    }
}