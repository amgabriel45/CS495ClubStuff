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
        public static readonly string ConnectionString = @"data source = (localdb)\MSSQLLocalDB; initial catalog = CrimsonClubs; persist security info = True; Integrated Security = SSPI;";
        protected CrimsonClubsDbContext db = new CrimsonClubsDbContext();

        public CurrentUser CurrentUser => new CurrentUser(User);
    }
}
