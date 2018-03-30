using CrimsonClubs.Models.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CrimsonClubs.Data.Providers
{
    public class CrimsonClubsProvider
    {
        protected readonly CrimsonClubsDbContext db;

        public CrimsonClubsProvider()
        {
            db = new CrimsonClubsDbContext();
        }
    }
}
