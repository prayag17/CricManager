import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    
    public void addPlayer(Player player) {
        String sql = "INSERT INTO players(name, score, runs, average, strike_rate, wickets, economy_rate, overs_bowled, team_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.name);
            pstmt.setInt(2, player.score);
            pstmt.setInt(3, player.runs);
            pstmt.setDouble(4, player.average);
            pstmt.setDouble(5, player.strikeRate);
            pstmt.setInt(6, player.wickets);
            pstmt.setDouble(7, player.economyRate);
            pstmt.setInt(8, player.oversBowled);
            pstmt.setInt(9, player.teamId);
            pstmt.executeUpdate();
            System.out.println("Player " + player.name + " added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding player: " + e.getMessage());
        }
    }

    public List<Player> getPlayersByTeamId(int teamId) {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM players WHERE team_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player p = new Player(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("runs"),
                        rs.getDouble("average"),
                        rs.getDouble("strike_rate"),
                        rs.getInt("wickets"),
                        rs.getDouble("economy_rate"),
                        rs.getInt("overs_bowled"),
                        rs.getInt("team_id")
                );
                players.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching players: " + e.getMessage());
        }
        return players;
    }
    
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM players";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player p = new Player(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("score"),
                        rs.getInt("runs"),
                        rs.getDouble("average"),
                        rs.getDouble("strike_rate"),
                        rs.getInt("wickets"),
                        rs.getDouble("economy_rate"),
                        rs.getInt("overs_bowled"),
                        rs.getInt("team_id")
                );
                players.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching players: " + e.getMessage());
        }
        return players;
    }
}