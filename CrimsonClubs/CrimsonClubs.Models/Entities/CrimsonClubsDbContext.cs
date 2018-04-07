namespace CrimsonClubs.Models.Entities
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;

    public partial class CrimsonClubsDbContext : DbContext
    {
        public CrimsonClubsDbContext()
            : base("name=CrimsonClubsDbContext")
        {
        }

        public virtual DbSet<Club> Clubs { get; set; }
        public virtual DbSet<Event> Events { get; set; }
        public virtual DbSet<Group> Groups { get; set; }
        public virtual DbSet<MM_Club_Event> MM_Club_Event { get; set; }
        public virtual DbSet<MM_User_Club> MM_User_Club { get; set; }
        public virtual DbSet<MMM_User_Event_Stat> MMM_User_Event_Stat { get; set; }
        public virtual DbSet<Organization> Organizations { get; set; }
        public virtual DbSet<Stat> Stats { get; set; }
        public virtual DbSet<Stat_Club> Stat_Club { get; set; }
        public virtual DbSet<Stat_Group> Stat_Group { get; set; }
        public virtual DbSet<User> Users { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Club>()
                .HasMany(e => e.MM_Club_Event)
                .WithRequired(e => e.Club)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Club>()
                .HasMany(e => e.MM_User_Club)
                .WithRequired(e => e.Club)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Club>()
                .HasMany(e => e.Stat_Club)
                .WithRequired(e => e.Club)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Event>()
                .Property(e => e.Start)
                .HasPrecision(0);

            modelBuilder.Entity<Event>()
                .Property(e => e.Finish)
                .HasPrecision(0);

            modelBuilder.Entity<Event>()
                .HasMany(e => e.MM_Club_Event)
                .WithRequired(e => e.Event)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Event>()
                .HasMany(e => e.MMM_User_Event_Stat)
                .WithRequired(e => e.Event)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Group>()
                .HasMany(e => e.Stat_Group)
                .WithRequired(e => e.Group)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Organization>()
                .HasMany(e => e.Clubs)
                .WithRequired(e => e.Organization)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Organization>()
                .HasMany(e => e.Groups)
                .WithRequired(e => e.Organization)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Organization>()
                .HasMany(e => e.Users)
                .WithRequired(e => e.Organization)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Stat>()
                .HasMany(e => e.MMM_User_Event_Stat)
                .WithRequired(e => e.Stat)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<Stat>()
                .HasOptional(e => e.Stat_Club)
                .WithRequired(e => e.Stat);

            modelBuilder.Entity<Stat>()
                .HasOptional(e => e.Stat_Group)
                .WithRequired(e => e.Stat);

            modelBuilder.Entity<User>()
                .HasMany(e => e.MM_User_Club)
                .WithRequired(e => e.User)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<User>()
                .HasMany(e => e.MMM_User_Event_Stat)
                .WithRequired(e => e.User)
                .WillCascadeOnDelete(false);
        }
    }
}
