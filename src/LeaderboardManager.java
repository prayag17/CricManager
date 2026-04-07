import java.sql.*;

public class LeaderboardManager {
    
    public static void viewLeaderboards() {
        System.out.println("\n--- CRICKET LEADERBOARDS ---");
        System.out.println("1. Top Run Scorers");
        System.out.println("2. Top Wicket Takers");
        System.out.print("Enter choice: ");
        
        java.util.Scanner s = new java.util.Scanner(System.in);
        String choice = s.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.println("\n--- TOP BATSMEN ---");
            String sql = "SELECT name, runs, average, strike_rate FROM players ORDER BY runs DESC LIMIT 10";
            try (Connection conn = DatabaseManager.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                System.out.printf("%-20s | %-6s | %-8s | %-8s%n", "Name", "Runs", "Average", "Strike Rate");
                while (rs.next()) {
                    System.out.printf("%-20s | %-6d | %-8.2f | %-8.2f%n", 
                        rs.getString("name"), rs.getInt("runs"), rs.getDouble("average"), rs.getDouble("strike_rate"));
                }
            } catch (SQLException e) {
                System.out.println("Error fetching leaderboards.");
            }
        } else if (choice.equals("2")) {
            System.out.println("\n--- TOP BOWLERS ---");
            String sql = "SELECT name, wickets, overs_bowled, economy_rate FROM players ORDER BY wickets DESC LIMIT 10";
            try (Connection conn = DatabaseManager.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                System.out.printf("%-20s | %-8s | %-12s | %-8s%n", "Name", "Wickets", "Overs Bowled", "Economy");
                while (rs.next()) {
                    System.out.printf("%-20s | %-8d | %-12d | %-8.2f%n", 
                        rs.getString("name"), rs.getInt("wickets"), rs.getInt("overs_bowled"), rs.getDouble("economy_rate"));
                }
            } catch (SQLException e) {
                System.out.println("Error fetching leaderboards.");
            }
        }
        System.out.println("---------------------------\n");
    }
}