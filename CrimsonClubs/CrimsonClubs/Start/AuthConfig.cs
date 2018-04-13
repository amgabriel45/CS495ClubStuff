using CrimsonClubs.Models.Entities;
using Microsoft.AspNet.Identity;
using Microsoft.Owin;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.Google;
using Microsoft.Owin.Security.OAuth;
using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Web;

namespace CrimsonClubs.Start
{
    public static class AuthConfig
    {
        public static OAuthBearerAuthenticationOptions OAuthBearerOptions { get; private set; }

        public static readonly string ClientId = "77421544828-k57594dl8a1rgmitclu6e0rj8970ved1.apps.googleusercontent.com";

        public static void ConfigureAuth(IAppBuilder app)
        {
            OAuthBearerOptions = new OAuthBearerAuthenticationOptions();
            app.UseOAuthBearerAuthentication(OAuthBearerOptions);

            app.UseCookieAuthentication(new CookieAuthenticationOptions
            {
                AuthenticationType = DefaultAuthenticationTypes.ExternalCookie,
                LoginPath = new PathString("/auth/login"),
                CookieName = "Auth",
                Provider = new CookieAuthenticationProvider
                {
                    OnApplyRedirect = context =>
                    {
                        if (!IsApiRequest(context))
                        {
                            context.Response.Redirect(context.RedirectUri);
                        }
                    },
                }
            });
            app.UseExternalSignInCookie(DefaultAuthenticationTypes.ExternalCookie);
            app.UseTwoFactorSignInCookie(DefaultAuthenticationTypes.TwoFactorCookie, TimeSpan.FromMinutes(5));
            app.UseTwoFactorRememberBrowserCookie(DefaultAuthenticationTypes.TwoFactorRememberBrowserCookie);

            var authenticationOptions = new GoogleOAuth2AuthenticationOptions()
            {
                ClientId = ClientId,
                ClientSecret = "m_3UUpAcinahC7R7m-4nzNZq",
                CallbackPath = new PathString("/auth/signin-google")
            };

            authenticationOptions.AuthorizationEndpoint += "?prompt=select_account";

            authenticationOptions.Provider = new GoogleOAuth2AuthenticationProvider()
            {
                OnAuthenticated = context =>
                {
                    using (var db = new CrimsonClubsDbContext())
                    {
                        var user = db.Users.FirstOrDefault(u => u.Email == context.Email);

                        if (user == null)
                        {
                            user = new User();
                            user.Email = context.Email;
                            user.First = context.GivenName;
                            user.Last = context.FamilyName;
                            user.IsOrganizationAdmin = false;
                            user.OrganizationId = 1;

                            db.Users.Add(user);
                            db.SaveChanges();
                        }

                        context.Identity.AddClaim(new Claim("UserId", user.Id.ToString(), ClaimValueTypes.Integer));
                        context.Identity.AddClaim(new Claim("FirstName", user.First));
                        context.Identity.AddClaim(new Claim("LastName", user.Last));
                    }

                    return Task.FromResult(0);
                }
            };

            app.UseGoogleAuthentication(authenticationOptions);
        }

        private static bool IsApiRequest(CookieApplyRedirectContext context)
        {
            return context.Request.Path.StartsWithSegments(new PathString("/api"));
        }
    }
}
