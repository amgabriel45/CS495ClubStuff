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

[assembly: OwinStartupAttribute(typeof(CrimsonClubs.Startup))]

namespace CrimsonClubs
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}