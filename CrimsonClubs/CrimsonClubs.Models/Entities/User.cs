namespace CrimsonClubs.Models.Entities
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("User")]
    public partial class User
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public User()
        {
            MM_User_Club = new HashSet<MM_User_Club>();
            MMM_User_Event_Stat = new HashSet<MMM_User_Event_Stat>();
        }

        public int Id { get; set; }

        [Required]
        public string Email { get; set; }

        [Required]
        public string First { get; set; }

        [Required]
        public string Last { get; set; }

        public bool IsOrganizationAdmin { get; set; }

        public int OrganizationId { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<MM_User_Club> MM_User_Club { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<MMM_User_Event_Stat> MMM_User_Event_Stat { get; set; }

        public virtual Organization Organization { get; set; }
    }
}
