# Database Schema 

This project uses SQLite. The schema structure maps the OOP model into the following relational tables.

## 1. `teams`
Stores information about the cricket teams.
- `id` (INTEGER PRIMARY KEY) - Corresponds to `teamId`
- `name` (TEXT NOT NULL) - Corresponds to `teamName`
- `captain_id` (INTEGER) - Foreign key linking to a player ID

## 2. `players`
Stores individual player stats and their team affiliation.
- `id` (INTEGER PRIMARY KEY) - Player ID
- `name` (TEXT NOT NULL) - Name of the player
- `score` (INTEGER DEFAULT 0)
- `runs` (INTEGER DEFAULT 0)
- `average` (REAL DEFAULT 0.0)
- `strike_rate` (REAL DEFAULT 0.0)
- `team_id` (INTEGER) - Foreign key referencing `teams(id)`

## 3. `matches`
Stores the high level match details.
- `id` (INTEGER PRIMARY KEY) - Match ID
- `type` (TEXT NOT NULL) - Match type (e.g., "T20", "ODI", "Test")
- `venue` (TEXT NOT NULL)
- `team1_id` (INTEGER) - Foreign key referencing `teams(id)`
- `team2_id` (INTEGER) - Foreign key referencing `teams(id)`
- `status` (TEXT DEFAULT 'Scheduled') - Match status (Scheduled, Ongoing, Completed)

## 4. `scores`
Stores the innings score for each team per match.
- `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
- `match_id` (INTEGER) - Foreign key referencing `matches(id)`
- `team_id` (INTEGER) - Foreign key referencing `teams(id)`
- `runs` (INTEGER DEFAULT 0)
- `wickets` (INTEGER DEFAULT 0)
- `overs` (INTEGER DEFAULT 0)
- `balls` (INTEGER DEFAULT 0)
