import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    
    // Create a new match and return its ID
    public int createMatch(String type, String venue, int team1Id, int team2Id) {
        String sql = "INSERT INTO matches(type, venue, team1_id, team2_id, status) VALUES(?, ?, ?, ?, 'Ongoing')";
        int matchId = -1;
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, type);
            pstmt.setString(2, venue);
            pstmt.setInt(3, team1Id);
            pstmt.setInt(4, team2Id);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                matchId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error creating match: " + e.getMessage());
        }
        return matchId;
    }

    public void completeMatch(int matchId) {
        String sql = "UPDATE matches SET status = 'Completed' WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, matchId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error completing match: " + e.getMessage());
        }
    }

    // Insert score initialization for a team
    public void initScore(int matchId, int teamId) {
        String sql = "INSERT INTO scores(match_id, team_id, runs, wickets, overs, balls) VALUES(?, ?, 0, 0, 0, 0)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, matchId);
            pstmt.setInt(2, teamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error init score: " + e.getMessage());
        }
    }

    // Set initial match state
    public void initMatchState(int matchId, int tossWinnerId, String tossDecision, int strikerId, int nonStrikerId, int bowlerId) {
        String sql = "INSERT INTO match_state(match_id, toss_winner_id, toss_decision, current_innings, striker_id, non_striker_id, bowler_id) VALUES(?, ?, ?, 1, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, matchId);
            pstmt.setInt(2, tossWinnerId);
            pstmt.setString(3, tossDecision);
            pstmt.setInt(4, strikerId);
            pstmt.setInt(5, nonStrikerId);
            pstmt.setInt(6, bowlerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error init match state: " + e.getMessage());
        }
    }

    // Log ball event
    public void logBall(int matchId, int innings, int overNo, int ballNo, int runs, int extras, int isWicket, int batsmanId, int bowlerId) {
        String sql = "INSERT INTO ball_events(match_id, innings, over_no, ball_no, runs, extras, is_wicket, batsman_id, bowler_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, matchId);
            pstmt.setInt(2, innings);
            pstmt.setInt(3, overNo);
            pstmt.setInt(4, ballNo);
            pstmt.setInt(5, runs);
            pstmt.setInt(6, extras);
            pstmt.setInt(7, isWicket);
            pstmt.setInt(8, batsmanId);
            pstmt.setInt(9, bowlerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error logging ball: " + e.getMessage());
        }
    }

    // Update match state
    public void updateMatchState(int matchId, int strikerId, int nonStrikerId, int bowlerId, int currentInnings) {
        String sql = "UPDATE match_state SET striker_id = ?, non_striker_id = ?, bowler_id = ?, current_innings = ? WHERE match_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, strikerId);
            pstmt.setInt(2, nonStrikerId);
            pstmt.setInt(3, bowlerId);
            pstmt.setInt(4, currentInnings);
            pstmt.setInt(5, matchId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating match state: " + e.getMessage());
        }
    }

    // Update score
    public void updateScore(int matchId, int teamId, int runs, int wickets, int overs, int balls) {
        String sql = "UPDATE scores SET runs = runs + ?, wickets = wickets + ?, overs = ?, balls = ? WHERE match_id = ? AND team_id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, runs);
            pstmt.setInt(2, wickets);
            pstmt.setInt(3, overs);
            pstmt.setInt(4, balls);
            pstmt.setInt(5, matchId);
            pstmt.setInt(6, teamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating score: " + e.getMessage());
        }
    }
}
