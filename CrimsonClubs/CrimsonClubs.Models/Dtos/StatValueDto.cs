using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class StatValueDto
    {
        public int Value { get; set; }
        public int UserId { get; set; }
        public int EventId { get; set; }
        public int StatId { get; set; }

        public MMM_User_Event_Stat ToEntity()
        {
            var m = new MMM_User_Event_Stat()
            {
                Value = Value,
                UserId = UserId,
                EventId = EventId,
                StatId = StatId
            };

            return m;
        }
    }
}
