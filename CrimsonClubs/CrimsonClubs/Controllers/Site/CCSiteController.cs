using CrimsonClubs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CrimsonClubs.Controllers.Site
{
    [Authorize]
    public class CCSiteController : Controller
    {
        public CurrentUser CurrentUser => new CurrentUser(User);
    }
}