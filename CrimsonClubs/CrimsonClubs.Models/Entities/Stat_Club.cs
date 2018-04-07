namespace CrimsonClubs.Models.Entities
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    public partial class Stat_Club
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int StatId { get; set; }

        public int ClubId { get; set; }

        public virtual Club Club { get; set; }

        public virtual Stat Stat { get; set; }
    }
}
