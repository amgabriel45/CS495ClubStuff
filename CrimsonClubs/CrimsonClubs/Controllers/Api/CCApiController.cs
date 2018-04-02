using CrimsonClubs.Models;
using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace CrimsonClubs.Controllers.Api
{
    [Authorize]
    public class CCApiController : ApiController
    {
        public static readonly string ConnectionString = @"Data Source=crimsonclubs.crzjft88gkn5.us-east-2.rds.amazonaws.com;Initial Catalog=CrimsonClubs;User ID=crimson;Password=boolean2018;App=EntityFramework";
        protected CrimsonClubsDbContext db = new CrimsonClubsDbContext();

        public CurrentUser CurrentUser => new CurrentUser(User);
    }
}
