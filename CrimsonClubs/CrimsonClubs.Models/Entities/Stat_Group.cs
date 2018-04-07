namespace CrimsonClubs.Models.Entities
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    public partial class Stat_Group
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int StatId { get; set; }

        public int GroupId { get; set; }

        public virtual Group Group { get; set; }

        public virtual Stat Stat { get; set; }
    }
}
