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

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(createTeamsTable);
                stmt.execute(createPlayersTable);
                stmt.execute(createMatchesTable);
                stmt.execute(createScoresTable);
                System.out.println("Database successfully initialized.");
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}