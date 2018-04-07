USE CrimsonClubs
GO

CREATE TABLE [Event]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Name] NVARCHAR(MAX) NOT NULL,
  [Description] NVARCHAR(MAX) NULL,
  [Start] DATETIME2(0) NOT NULL,
  [Finish] DATETIME2(0) NOT NULL,
  [IsGroupEvent] BIT NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE [Organization]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Name] NVARCHAR(MAX) NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE [Group]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Name] NVARCHAR(MAX) NOT NULL,
  [Description] NVARCHAR(MAX) NULL,
  [OrganizationId] INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (OrganizationId) REFERENCES [Organization](Id)
);

CREATE TABLE [Stat]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Name] NVARCHAR(MAX) NOT NULL,
  [Description] NVARCHAR(MAX) NULL,
  [Abbreviation] NVARCHAR(MAX) NOT NULL,
  [Type] INT NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE [User]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Email] NVARCHAR(MAX) NOT NULL,
  [First] NVARCHAR(MAX) NOT NULL,
  [Last] NVARCHAR(MAX) NOT NULL,
  [IsOrganizationAdmin] BIT NOT NULL,
  [OrganizationId] INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (OrganizationId) REFERENCES [Organization](Id)
);

CREATE TABLE [Club]
(
  [Id] INT NOT NULL IDENTITY(1,1),
  [Name] NVARCHAR(MAX) NOT NULL,
  [Description] NVARCHAR(MAX) NULL,
  [IsRequestToJoin] BIT NOT NULL,
  [GroupId] INT NULL,
  [OrganizationId] INT NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (GroupId) REFERENCES [Group](Id),
  FOREIGN KEY (OrganizationId) REFERENCES [Organization](Id)
);

CREATE TABLE [MM_User_Club]
(
  [IsAdmin] BIT NOT NULL,
  [IsAccepted] BIT NOT NULL,
  [UserId] INT NOT NULL,
  [ClubId] INT NOT NULL,
  PRIMARY KEY (UserId, ClubId),
  FOREIGN KEY (UserId) REFERENCES [User](Id),
  FOREIGN KEY (ClubId) REFERENCES [Club](Id)
);

CREATE TABLE [MMM_User_Event_Stat]
(
  [Value] INT NOT NULL,
  [UserId] INT NOT NULL,
  [EventId] INT NOT NULL,
  [StatId] INT NOT NULL,
  PRIMARY KEY (UserId, EventId, StatId),
  FOREIGN KEY (UserId) REFERENCES [User](Id),
  FOREIGN KEY (EventId) REFERENCES [Event](Id),
  FOREIGN KEY (StatId) REFERENCES [Stat](Id)
);

CREATE TABLE [MM_Club_Event]
(
  [Rank] INT NULL,
  [ClubId] INT NOT NULL,
  [EventId] INT NOT NULL,
  PRIMARY KEY (ClubId, EventId),
  FOREIGN KEY (ClubId) REFERENCES [Club](Id),
  FOREIGN KEY (EventId) REFERENCES [Event](Id)
);

CREATE TABLE [Stat_Club]
(
  [StatId] INT NOT NULL,
  [ClubId] INT NOT NULL,
  PRIMARY KEY (StatId),
  FOREIGN KEY (StatId) REFERENCES [Stat](Id),
  FOREIGN KEY (ClubId) REFERENCES [Club](Id)
);

CREATE TABLE [Stat_Group]
(
  [StatId] INT NOT NULL,
  [GroupId] INT NOT NULL,
  PRIMARY KEY (StatId),
  FOREIGN KEY (StatId) REFERENCES [Stat](Id),
  FOREIGN KEY (GroupId) REFERENCES [Group](Id)
);

INSERT INTO Organization (Name) VALUES ('Capstone University');

INSERT INTO dbo.[User]
(Email, [First], [Last], IsOrganizationAdmin, OrganizationId) VALUES
('mdmay1@crimson.ua.edu', 'Matthew-Lane', 'May', 0, 1),
('maclanemay@gmail.com', 'Maclane', 'May', 1, 1),
('mdlivingston@crimson.ua.edu', 'Max', 'Livingston', 0, 1);

INSERT INTO dbo.[Group]
([Name], [Description], OrganizationId) VALUES
('Intramural Basketball', 'Group for intramural basketball teams.', 1),
('Intramural 4v4 Football', 'Group for intramural football 4v4 teams.', 1);

INSERT INTO Club 
(Name, Description, IsRequestToJoin, GroupId, OrganizationId) VALUES
('Basketball Team A', 'Basketball Team A Description', 1, 1, 1),
('Basketball Team B', 'Basketball Team B Description', 1, 1, 1),
('Football Team A', 'Football Team A Description', 0, 2, 1),
('Football Team B', 'Football Team B Description', 0, 2, 1),
('Video Game Club', 'We play video games.', 0, NULL, 1);

INSERT INTO Stat
(Name, Description, Abbreviation, Type) VALUES
('Sportsmanship', 'Sportsmanship rating from 1 (bad) to 5 (good)', 'SMS', 1),
('Points', 'Total points player has scored', 'PTS', 2),
('3 Pointers', '3 Pointers player has made', '3P', 2),
('2 Pointers', '2 pointers player has made', '2P', 2);

INSERT INTO Stat_Club
(StatId, ClubId) VALUES
(1, 1);

INSERT INTO Stat_Group
(StatId, GroupId) VALUES
(2, 1),
(3, 1),
(4, 1);

INSERT INTO dbo.[Event]
(Name, Description, Start, Finish, IsGroupEvent) VALUES
('Practice', 'Work on the basics', '2018-04-10 12:30:00', '2018-04-10 01:30:00', 0),
('Practice Game', 'Team A vs Team B', '2018-05-10 12:30:00', '2018-05-10 01:30:00', 1),
('Game', 'Team A vs Team B', '2018-01-10 12:30:00', '2018-01-10 01:30:00', 1);

INSERT INTO MM_Club_Event
(Rank, ClubId, EventId) VALUES
(NULL, 1, 1),
(NULL, 1, 2),
(NULL, 2, 2),
(1, 1, 3),
(2, 2, 3);

INSERT INTO MM_User_Club
(IsAdmin, IsAccepted, UserId, ClubId) VALUES
(1, 1, 1, 1),
(1, 1, 1, 3),
(1, 1, 1, 5),
(0, 1, 3, 5),
(0, 0, 3, 1);

INSERT INTO MMM_User_Event_Stat
(Value, UserId, EventId, StatId) VALUES
(5, 1, 3, 1),
(5, 1, 3, 2),
(1, 1, 3, 3),
(1, 1, 3, 4);

