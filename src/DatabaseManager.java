import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cricmanager.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Connection to SQLite failed: " + e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        String createTeamsTable = "CREATE TABLE IF NOT EXISTS teams (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "captain_id INTEGER" +
                ");";

        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "score INTEGER DEFAULT 0," +
                "runs INTEGER DEFAULT 0," +
                "average REAL DEFAULT 0.0," +
                "strike_rate REAL DEFAULT 0.0," +
                "wickets INTEGER DEFAULT 0," +
                "economy_rate REAL DEFAULT 0.0," +
                "overs_bowled INTEGER DEFAULT 0," +
                "team_id INTEGER," +
                "FOREIGN KEY(team_id) REFERENCES teams(id)" +
                ");";

        String createMatchesTable = "CREATE TABLE IF NOT EXISTS matches (" +
                "id INTEGER PRIMARY KEY," +
                "type TEXT NOT NULL," +
                "venue TEXT NOT NULL," +
                "team1_id INTEGER," +
                "team2_id INTEGER," +
                "status TEXT DEFAULT 'Scheduled'," +
                "FOREIGN KEY(team1_id) REFERENCES teams(id)," +
                "FOREIGN KEY(team2_id) REFERENCES teams(id)" +
                ");";

        String createScoresTable = "CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "match_id INTEGER," +
                "team_id INTEGER," +
                "runs INTEGER DEFAULT 0," +
                "wickets INTEGER DEFAULT 0," +
                "overs INTEGER DEFAULT 0," +
                "balls INTEGER DEFAULT 0," +
                "FOREIGN KEY(match_id) REFERENCES matches(id)," +
                "FOREIGN KEY(team_id) REFERENCES teams(id)" +
                ");";

        String createMatchStateTable = "CREATE TABLE IF NOT EXISTS match_state (" +
                "match_id INTEGER PRIMARY KEY," +
                "toss_winner_id INTEGER," +
                "toss_decision TEXT," +
                "current_innings INTEGER DEFAULT 1," +
                "striker_id INTEGER," +
                "non_striker_id INTEGER," +
                "bowler_id INTEGER," +
                "FOREIGN KEY(match_id) REFERENCES matches(id)," +
                "FOREIGN KEY(toss_winner_id) REFERENCES teams(id)," +
                "FOREIGN KEY(striker_id) REFERENCES players(id)," +
                "FOREIGN KEY(non_striker_id) REFERENCES players(id)," +
                "FOREIGN KEY(bowler_id) REFERENCES players(id)" +
                ");";

        String createPlayingXiTable = "CREATE TABLE IF NOT EXISTS playing_xi (" +
                "match_id INTEGER," +
                "team_id INTEGER," +
                "player_id INTEGER," +
                "PRIMARY KEY(match_id, team_id, player_id)," +
                "FOREIGN KEY(match_id) REFERENCES matches(id)," +
                "FOREIGN KEY(team_id) REFERENCES teams(id)," +
                "FOREIGN KEY(player_id) REFERENCES players(id)" +
                ");";

        String createBallEventsTable = "CREATE TABLE IF NOT EXISTS ball_events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "match_id INTEGER," +
                "innings INTEGER," +
                "over_no INTEGER," +
                "ball_no INTEGER," +
                "runs INTEGER DEFAULT 0," +
                "extras INTEGER DEFAULT 0," +
                "is_wicket INTEGER DEFAULT 0," +
                "batsman_id INTEGER," +
                "bowler_id INTEGER," +
                "FOREIGN KEY(match_id) REFERENCES matches(id)," +
                "FOREIGN KEY(batsman_id) REFERENCES players(id)," +
                "FOREIGN KEY(bowler_id) REFERENCES players(id)" +
                ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(createTeamsTable);
                stmt.execute(createPlayersTable);
                stmt.execute(createMatchesTable);
                stmt.execute(createScoresTable);
                stmt.execute(createMatchStateTable);
                stmt.execute(createPlayingXiTable);
                stmt.execute(createBallEventsTable);
                System.out.println("Database successfully initialized.");
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}