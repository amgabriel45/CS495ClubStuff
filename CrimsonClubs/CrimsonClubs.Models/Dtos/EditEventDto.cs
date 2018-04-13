using CrimsonClubs.Models.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Models.Dtos
{
    public class EditEventDto
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public DateTime Start { get; set; }
        public DateTime Finish { get; set; }
        public bool IsGroupEvent { get; set; }
        public int ClubId { get; set; }
        public List<int> ClubIds { get; set; }

        public void Edit(ref Event dbo)
        {
            dbo.Name = Name;
            dbo.Description = Description;
            dbo.Start = Start;
            dbo.Finish = Finish;
            dbo.IsGroupEvent = IsGroupEvent;


            foreach (var rel in dbo.MM_Club_Event.ToList())
            {
                dbo.MM_Club_Event.Remove(rel);
            }

            var relation = new MM_Club_Event();
            relation.ClubId = ClubId;

            dbo.MM_Club_Event.Add(relation);

            if (ClubIds != null)
            {
                foreach (var id in ClubIds)
                {
                    relation = new MM_Club_Event();
                    relation.ClubId = id;

                    dbo.MM_Club_Event.Add(relation);
                }
            }
        }
    }
}
