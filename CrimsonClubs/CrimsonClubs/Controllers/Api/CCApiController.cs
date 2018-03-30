using CrimsonClubs.Models.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace CrimsonClubs.Controllers.Api
{
    public class CCApiController : ApiController
    {
        public static readonly string ConnectionString = @"data source = (localdb)\MSSQLLocalDB; initial catalog = CrimsonClubs; persist security info = True; Integrated Security = SSPI;";
        protected CrimsonClubsDbContext db = new CrimsonClubsDbContext();
    }
}
