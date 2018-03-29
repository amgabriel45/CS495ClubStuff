--get all clubs associated with user

DECLARE @UserId INT = 1;

SELECT
	c.Id,
	c.Name,
	c.Description,
	ISNULL(g.Name, '') AS GroupName,
	(
		SELECT COUNT(*)
		FROM MM_User_Club
		WHERE ClubId = c.Id
	) AS MemberCount
FROM [User] u
JOIN MM_User_Club m ON u.Id = m.UserId
JOIN Club c ON c.Id = m.ClubId
LEFT JOIN [Group] g ON g.Id = c.GroupId
WHERE u.Id = @UserId;

--get full calendar
DECLARE @UserId INT = 1;

SELECT
	e.Id,
	e.Name,
	e.Description,
	e.Start,
	e.Finish,
	e.IsGroupEvent,
	c.Id AS ClubId,
	c.Name AS ClubName
FROM [User] u
JOIN MM_User_Club m1 ON u.Id = m1.UserId
JOIN Club c ON c.Id = m1.ClubId
JOIN MM_Club_Event m2 ON m2.ClubId = c.Id
JOIN [Event] e ON e.Id = m2.EventId
WHERE u.Id = @UserId;

--get club calendar
DECLARE @ClubId INT = 1;

SELECT
	e.Id,
	e.Name,
	e.Description,
	e.Start,
	e.Finish,
	e.IsGroupEvent,
	c.Id AS ClubId,
	c.Name AS ClubName
FROM Club c
JOIN MM_Club_Event m2 ON m2.ClubId = c.Id
JOIN [Event] e ON e.Id = m2.EventId
WHERE c.Id = @ClubId;

--get event stats