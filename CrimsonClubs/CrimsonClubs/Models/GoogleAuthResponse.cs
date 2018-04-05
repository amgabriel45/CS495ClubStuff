using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CrimsonClubs.Models
{
    public class GoogleAuthResponse
    {
        [JsonProperty("email")]
        public string Email { get; set; }

        [JsonProperty("given_name")]
        public string First { get; set; }

        [JsonProperty("family_name")]
        public string Last { get; set; }

        [JsonProperty("aud")]
        public string ClientId { get; set; }
    }
}