using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    public class EventController : CCSiteController
    {
        [HttpGet, Route("event")]
        public ActionResult Index()
        {
            return View();
        }
    }
}