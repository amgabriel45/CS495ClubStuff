using System;
using System.Collections.Generic;
using System.Linq;
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
    }
}