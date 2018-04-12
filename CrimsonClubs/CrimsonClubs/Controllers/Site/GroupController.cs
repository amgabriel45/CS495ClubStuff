using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    public class GroupController : CCSiteController
    {
        [HttpGet, Route("group")]
        public ActionResult Index()
        {
            return View();
        }
    }
}