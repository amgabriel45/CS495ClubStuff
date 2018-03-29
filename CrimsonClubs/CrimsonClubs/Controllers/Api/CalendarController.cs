using CrimsonClubs.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace CrimsonClubs.Controllers.Api
{
    [RoutePrefix("api/calendar")]
    public class CalendarController : CCApiController
    {
        [HttpGet, Route]
        [ResponseType(typeof(Event[]))]
        public IHttpActionResult GetUserCalendar()
        {
            int userId = 1;

            var events = new List<Event>();

            string sql = "SELECT e.Id, e.Name, e.Description, e.Start, e.Finish, e.IsGroupEvent, c.Id AS ClubId, c.Name AS ClubName FROM [User] u JOIN MM_User_Club m1 ON u.Id = m1.UserId JOIN Club c ON c.Id = m1.ClubId JOIN MM_Club_Event m2 ON m2.ClubId = c.Id JOIN [Event] e ON e.Id = m2.EventId WHERE u.Id = @UserId;";
            using (var conn = new SqlConnection(ConnectionString))
            using (var cmd = new SqlCommand(sql, conn))
            {
                cmd.Parameters.AddWithValue("@UserId", userId);

                conn.Open();

                var reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    var e = new Event();
                    e.Id = (int)reader["Id"];
                    e.Name = (string)reader["Name"];
                    e.Description = (string)reader["Description"];
                    e.Start = (DateTime)reader["Start"];
                    e.Finish = (DateTime)reader["Finish"];
                    e.IsGroupEvent = (bool)reader["IsGroupEvent"];
                    e.ClubId = (int)reader["ClubId"];
                    e.ClubName = (string)reader["ClubName"];

                    events.Add(e);
                }
            }

            return Ok(events);
        }
    }
}
