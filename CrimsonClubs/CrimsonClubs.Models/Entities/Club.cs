namespace CrimsonClubs.Models.Entities
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("Club")]
    public partial class Club
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Club()
        {
            MM_Club_Event = new HashSet<MM_Club_Event>();
            MM_User_Club = new HashSet<MM_User_Club>();
            Stat_Club = new HashSet<Stat_Club>();
        }

        public int Id { get; set; }

        [Required]
        public string Name { get; set; }

        public string Description { get; set; }

        public bool IsRequestToJoin { get; set; }

        public int? GroupId { get; set; }

        public int OrganizationId { get; set; }

        public virtual Group Group { get; set; }

        public virtual Organization Organization { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<MM_Club_Event> MM_Club_Event { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<MM_User_Club> MM_User_Club { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Stat_Club> Stat_Club { get; set; }
    }
}
