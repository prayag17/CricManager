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
- `wickets` (INTEGER DEFAULT 0) - Bowling wickets taken
- `economy_rate` (REAL DEFAULT 0.0) - Bowling economy
- `overs_bowled` (INTEGER DEFAULT 0) - Total overs bowled
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

## 5. `match_state`
Stores live state configuration to enable spectator real-time viewing and match-in-progress logic.
- `match_id` (INTEGER PRIMARY KEY) - References `matches(id)`
- `toss_winner_id` (INTEGER) - References `teams(id)`
- `toss_decision` (TEXT) - e.g., 'Bat' or 'Bowl'
- `current_innings` (INTEGER DEFAULT 1)
- `striker_id` (INTEGER) - References `players(id)`
- `non_striker_id` (INTEGER) - References `players(id)`
- `bowler_id` (INTEGER) - References `players(id)`

## 6. `playing_xi`
Associates players to a team specifically for a given match instance.
- `match_id` (INTEGER) - References `matches(id)`
- `team_id` (INTEGER) - References `teams(id)`
- `player_id` (INTEGER) - References `players(id)`
- PRIMARY KEY (`match_id`, `team_id`, `player_id`)

## 7. `ball_events`
Log of every ball bowled for generating real-time scoreboards and match history.
- `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
- `match_id` (INTEGER) - References `matches(id)`
- `innings` (INTEGER) - Which innings this ball belongs to
- `over_no` (INTEGER) - Current over (0-indexed or 1-indexed)
- `ball_no` (INTEGER) - Current ball within the over
- `runs` (INTEGER DEFAULT 0) - Runs scored off the bat
- `extras` (INTEGER DEFAULT 0) - Any extras (wides, no-balls)
- `is_wicket` (INTEGER DEFAULT 0) - 0 or 1
- `batsman_id` (INTEGER) - References `players(id)`
- `bowler_id` (INTEGER) - References `players(id)`
