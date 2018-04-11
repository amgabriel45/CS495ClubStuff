using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class AddEventDto
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime Start { get; set; }
        public DateTime Finish { get; set; }
        public bool IsGroupEvent { get; set; }
        public int ClubId { get; set; }
        public List<int> ClubIds { get; set; }

        public Event ToEntity()
        {
            var e = new Event();
            e.Name = Name;
            e.Description = Description;
            e.Start = Start;
            e.Finish = Finish;
            e.IsGroupEvent = IsGroupEvent;
            e.MM_Club_Event = new List<MM_Club_Event>();

            var relation = new MM_Club_Event();
            relation.ClubId = ClubId;

            e.MM_Club_Event.Add(relation);

            if(ClubIds == null)
            {
                return e;
            }
            foreach (var id in ClubIds)
            {
                relation = new MM_Club_Event();
                relation.ClubId = id;

                e.MM_Club_Event.Add(relation);
            }

            return e;
        }
    }
}
