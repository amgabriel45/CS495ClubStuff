using Microsoft.Owin;
using Microsoft.Owin.Security.Google;
using Microsoft.Owin.Security.Cookies;
using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;
using Microsoft.AspNet.Identity;

[assembly: OwinStartupAttribute(typeof(CrimsonClubs.Start.Startup))]

namespace CrimsonClubs.Start
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            AuthConfig.ConfigureAuth(app);
        }
    }
}