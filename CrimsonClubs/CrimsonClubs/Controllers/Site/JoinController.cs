using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    public class JoinController : CCSiteController
    {
        [HttpGet, Route("join")]
        public ActionResult Index()
        {
            return View();
        }
    }
}