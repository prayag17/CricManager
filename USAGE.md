# CricManager - Usage Guide

Welcome to **CricManager**, a Java-based Unix-style CLI application for managing cricket matches, teams, players, and live-scoring. This guide provides step-by-step instructions on how to compile, run, and interact with the application.

## Prerequisites
- **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed and added to your system's PATH.
- **SQLite JDBC Driver**: The required `sqlite-jdbc-3.49.1.0.jar` is already located in the `lib/` directory.

---

## 1. Compilation
Open your terminal (or Command Prompt / PowerShell) and navigate to the root directory of the project (`CricManager`).

Compile all the `.java` files from the `src` directory, ensuring the SQLite driver is in the classpath:

**Windows / Linux / macOS:**
```bash
javac -d bin -cp "lib/*" src/*.java
```

---

## 2. Running the Application

To start the CricManager CLI, you need to run the `App` main class and include both the `lib/` and the compiled classes in the `bin/` directory in your classpath.

**Windows:**
```powershell
java -cp "lib/*;bin" App
```

**Linux / macOS:**
```bash
java -cp "lib/*:bin" App
```

*Note the difference in the classpath separator: `;` for Windows and `:` for Linux/Mac.*

---

## 3. Seed Dummy Data (Optional)

If you don't want to type out players and teams manually right away, you can use the built-in seeder program to populate the database with `India`, `Australia`, and `England` along with their star players and career statistics.

Ensure you have compiled the code (`javac -d bin -cp "lib/*" src/*.java`). Then, run the `SeedData` script:

**Windows:**
```powershell
java -cp "lib/*;bin" SeedData
```

**Linux / macOS:**
```bash
java -cp "lib/*:bin" SeedData
```

This script will overwrite current arrays and inject 3 teams and 12 players instantly.

---

## 4. Application Features & Workflow

When you run the `App`, you will be greeted by the **CricManager Main Menu**. 

### Step 1: Manage Teams (Option 1)
Before playing a match, you need teams.
1. Select Option `1`.
2. Choose `1. Add New Team`.
3. Provide a team name (e.g., "India" or "Australia").
4. Repeat to add at least two teams.

### Step 2: Manage Players (Option 2)
Add players and assign them to the teams you created.
1. Select Option `2`.
2. Choose `1. Add New Player`.
3. Enter the player's name.
4. The system will display the list of available teams along with their IDs. Enter the corresponding Team ID to draft the player.
*(Note: Each team needs at least 2 players to act as Striker, Non-Striker, and Bowlers).*

### Step 3: Umpire Mode & Starting a Match (Option 3)
1. Select Option `3` from the Main Menu.
2. Enter the IDs for Team 1 and Team 2.
3. Provide a Match Type (e.g., "T20") and a Venue.
4. The Match will be created and given a unique **Match ID**. *Remember this ID for Spectator Mode.*
5. **Toss:** Specify which team won the toss and whether they chose to Bat or Bowl.
6. **Openers:** Select the Striker and Non-Striker (from the batting team) and the Opening Bowler (from the bowling team).
7. **Scoring:** The Umpire Mode begins. Enter ball-by-ball outcomes:
   - `0`, `1`, `2`, `3`, `4`, `6` - Runs off the bat.
   - `W` - Wicket.
   - `Wd` - Wide ball (adds 1 extra run, ball is re-bowled).
   - `Nb` - No-ball (adds 1 extra run, ball is re-bowled).
   - `Q` - Quit/Pause the umpire mode.

### Step 4: Spectator Mode (Option 4)
Because the application uses SQLite's WAL (Write-Ahead Logging) mode, you can spin up **multiple terminal windows simultaneously**!

1. Open a **second terminal window**.
2. Run the application again (`java -cp "lib/*;src" App`).
3. Select Option `4` (View Live Match).
4. Enter the **Match ID** of the match currently being umpired in the first terminal.
5. The screen will clear and construct a live, auto-refreshing scoreboard. As the umpire enters balls in Terminal 1, Spectator Mode in Terminal 2 will automatically update every 5 seconds.

### Step 5: Leaderboards (Option 5)
1. Select Option `5`.
2. View either the **Top Run Scorers** (batsmen ranked by total runs) or **Top Wicket Takers** (bowlers ranked by wickets). This pulls directly from the career stats saved to the database.

---

## Troubleshooting

- **"database is locked" Error**: This should be mitigated natively by `PRAGMA journal_mode=WAL;`. Make sure no other external database viewer (like DB Browser for SQLite) is holding a hard write-lock on `cricmanager.db` while you are umpiring.
- **ClassNotFoundException**: Ensure you have correctly specified the classpath (`-cp`) when running `java`, separating `lib/*` and `src` with the correct OS-specific delimiter (`;` for Windows, `:` for Unix).