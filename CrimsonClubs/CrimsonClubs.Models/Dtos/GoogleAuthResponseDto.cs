using CrimsonClubs.Models.Entities;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models.Dtos
{
    public class GoogleAuthResponseDto
    {
        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("given_name")]
        public string First { get; set; }

        [JsonProperty("family_name")]
        public string Last { get; set; }

        [JsonProperty("aud")]
        public string ClientIdWeb { get; set; }

        [JsonProperty("azp")]
        public string ClientIdApp { get; set; }

        public User ToEntity()
        {
            var user = new User();
            user.Email = Email;
            user.First = First;
            user.Last = Last;
            user.IsOrganizationAdmin = false;
            user.OrganizationId = 1;

            return user;
        }
    }
}