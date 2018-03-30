using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    public class AccountController : CCSiteController
    {
        [HttpGet, Route("account")]
        public ActionResult Login()
        {
            return View();
        }
    }
}