import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public void addTeam(String teamName, int captainId) {
        String sql = "INSERT INTO teams(name, captain_id) VALUES(?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teamName);
            if (captainId > 0) {
                pstmt.setInt(2, captainId);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.executeUpdate();
            System.out.println("Team '" + teamName + "' added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding team: " + e.getMessage());
        }
    }

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams";

        // To assemble full Team objects, we could fetch their players,
        // but for simplicity, we will instantiate hollow Teams for CLI listings.
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Team t = new Team(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new Player[0], // Handled dynamically in matches
                        rs.getInt("captain_id")
                );
                teams.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching teams: " + e.getMessage());
        }
        return teams;
    }
}