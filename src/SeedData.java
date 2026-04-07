import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SeedData {
    public static void main(String[] args) {
        System.out.println("Initializing Database for Seeding...");
        DatabaseManager.initializeDatabase();

        String[] seedQueries = {
            // Clear existing fast for a fresh seed
            "DELETE FROM players;",
            "DELETE FROM teams;",
            
            // Insert Teams
            "INSERT INTO teams (id, name) VALUES (1, 'India');",
            "INSERT INTO teams (id, name) VALUES (2, 'Australia');",
            "INSERT INTO teams (id, name) VALUES (3, 'England');",

            // Insert India Players
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (1, 'Rohit Sharma', 1, 10709, 49.1, 139.5);",
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (2, 'Virat Kohli', 1, 13848, 58.7, 138.0);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (3, 'Jasprit Bumrah', 1, 149, 6.5, 800);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (4, 'Ravindra Jadeja', 1, 220, 7.1, 950);",

            // Insert Australia Players
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (5, 'David Warner', 2, 6932, 45.3, 142.2);",
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (6, 'Steve Smith', 2, 5356, 42.1, 125.5);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (7, 'Pat Cummins', 2, 210, 6.8, 920);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (8, 'Mitchell Starc', 2, 236, 7.3, 850);",

            // Insert England Players
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (9, 'Jos Buttler', 3, 5022, 40.5, 145.8);",
            "INSERT INTO players (id, name, team_id, runs, average, strike_rate) VALUES (10, 'Joe Root', 3, 6522, 48.8, 128.9);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (11, 'Jofra Archer', 3, 120, 7.4, 500);",
            "INSERT INTO players (id, name, team_id, wickets, economy_rate, overs_bowled) VALUES (12, 'Adil Rashid', 3, 190, 7.0, 800);"
        };

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Seeding dummy data...");
            for (String query : seedQueries) {
                stmt.execute(query);
            }
            System.out.println("Dummy data seeded successfully! You now have 3 Teams and 12 Players.");

        } catch (SQLException e) {
            System.out.println("Error seeding data: " + e.getMessage());
        }
    }
}