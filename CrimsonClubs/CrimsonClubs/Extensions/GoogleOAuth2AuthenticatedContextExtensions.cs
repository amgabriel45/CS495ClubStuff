using CrimsonClubs.Models.Entities;
using Microsoft.Owin.Security.Google;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Extensions
{
    public static class GoogleOAuth2AuthenticatedContextExtensions
    {
        public static User NewUser(this GoogleOAuth2AuthenticatedContext context)
        {
            var user = new User()
            {
                Email = context.Email,
                First = context.GivenName,
                Last = context.FamilyName,
                IsOrganizationAdmin = false,
                OrganizationId = 1
            };

            return user;
        }
    }
}