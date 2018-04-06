using CrimsonClubs.Models;
using CrimsonClubs.Models.Dtos;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace CrimsonClubs.Controllers.Api
{
    [RoutePrefix("api/stat")]
    public class StatController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(int))]
        public IHttpActionResult Get()
        {
            return Ok();
        }
    }
}
