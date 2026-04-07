import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class MatchManager {
    private static Scanner scanner = new Scanner(System.in);
    private static MatchDAO matchDAO = new MatchDAO();
    private static TeamDAO teamDAO = new TeamDAO();
    private static PlayerDAO playerDAO = new PlayerDAO();

    public static void startMatchMode() {
        System.out.println("\n--- Start Match / Umpire Mode ---");
        List<Team> teams = teamDAO.getAllTeams();
        if (teams.size() < 2) {
            System.out.println("You need at least 2 teams to start a match.");
            return;
        }
        
        for (Team t : teams) {
            System.out.println(t.teamId + ". " + t.teamName);
        }
        
        System.out.print("Enter Team 1 ID: ");
        int t1 = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter Team 2 ID: ");
        int t2 = Integer.parseInt(scanner.nextLine().trim());
        
        if (t1 == t2) {
            System.out.println("A team cannot play against itself.");
            return;
        }

        System.out.print("Enter Match Type (T20/ODI/Test): ");
        String matchType = scanner.nextLine().trim();

        System.out.print("Enter Venue: ");
        String venue = scanner.nextLine().trim();

        int matchId = matchDAO.createMatch(matchType, venue, t1, t2);
        System.out.println("Match created with ID: " + matchId);

        matchDAO.initScore(matchId, t1);
        matchDAO.initScore(matchId, t2);

        System.out.print("Who won the toss? (Enter team ID): ");
        int tossWinnerId = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Toss Decision (Bat/Bowl): ");
        String tossDecision = scanner.nextLine().trim();

        int battingTeamId = (tossDecision.equalsIgnoreCase("Bat") && tossWinnerId == t1) || (!tossDecision.equalsIgnoreCase("Bat") && tossWinnerId == t2) ? t1 : t2;
        int bowlingTeamId = (battingTeamId == t1) ? t2 : t1;

        System.out.println("\nSelect opening batsmen for " + getTeamName(battingTeamId) + ":");
        List<Player> batsmen = playerDAO.getPlayersByTeamId(battingTeamId);
        for (Player p : batsmen) System.out.println(p.id + ". " + p.name);
        
        System.out.print("Striker ID: ");
        int strikerId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Non-Striker ID: ");
        int nonStrikerId = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("\nSelect opening bowler for " + getTeamName(bowlingTeamId) + ":");
        List<Player> bowlers = playerDAO.getPlayersByTeamId(bowlingTeamId);
        for (Player p : bowlers) System.out.println(p.id + ". " + p.name);
        
        System.out.print("Bowler ID: ");
        int bowlerId = Integer.parseInt(scanner.nextLine().trim());

        matchDAO.initMatchState(matchId, tossWinnerId, tossDecision, strikerId, nonStrikerId, bowlerId);

        umpireLoop(matchId, battingTeamId, bowlingTeamId, strikerId, nonStrikerId, bowlerId);
    }

    private static void umpireLoop(int matchId, int battingTeamId, int bowlingTeamId, int strikerId, int nonStrikerId, int bowlerId) {
        int innings = 1;
        int runs = 0, wickets = 0, overs = 0, balls = 0;
        
        boolean matchRunning = true;
        System.out.println("\nUmpire Scoring Mode Started for Match " + matchId + ". (Type 'q' to quit at any time)");

        while (matchRunning) {
            System.out.println("CURRENT O: " + overs + "." + balls + " | R: " + runs + " | W: " + wickets);
            System.out.print("Enter outcome for this ball (e.g. 0, 1, 2, 3, 4, 6, W, Wd, Nb): ");
            String outcome = scanner.nextLine().trim().toUpperCase();

            if (outcome.equals("Q")) {
                System.out.println("Umpire mode paused. You can resume later.");
                break;
            }

            int runsScored = 0;
            int extras = 0;
            int isWicket = 0;
            boolean legalBall = true;

            if (outcome.equals("W")) {
                isWicket = 1;
                wickets++;
            } else if (outcome.equals("WD") || outcome.equals("NB")) {
                extras = 1;
                legalBall = false;
                runs++; // Extras add 1 run
            } else {
                try {
                    runsScored = Integer.parseInt(outcome);
                    runs += runsScored;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input!");
                    continue;
                }
            }

            if (legalBall) {
                balls++;
            }

            matchDAO.logBall(matchId, innings, overs, balls, runsScored, extras, isWicket, strikerId, bowlerId);
            matchDAO.updateScore(matchId, battingTeamId, runsScored + extras, isWicket, overs, balls);

            if (balls == 6) {
                overs++;
                balls = 0;
                matchDAO.updateScore(matchId, battingTeamId, runsScored + extras, isWicket, overs, balls);
                
                // Swap batsmen usually handled here for end of over
                int temp = strikerId;
                strikerId = nonStrikerId;
                nonStrikerId = temp;

                System.out.print("End of over. Enter NEXT Bowler ID: ");
                String nextB = scanner.nextLine().trim();
                if (!nextB.equals("Q")) {
                    bowlerId = Integer.parseInt(nextB);
                    matchDAO.updateMatchState(matchId, strikerId, nonStrikerId, bowlerId, innings);
                } else break;
            } else if (runsScored % 2 != 0 && !outcome.equals("W")) {
                // Swap on odd runs
                int temp = strikerId;
                strikerId = nonStrikerId;
                nonStrikerId = temp;
                matchDAO.updateMatchState(matchId, strikerId, nonStrikerId, bowlerId, innings);
            }

            if (isWicket == 1) {
                if (wickets >= 10) {
                    System.out.println("Innings " + innings + " complete. All out!");
                    if (innings == 1) {
                        innings = 2;
                        System.out.println("Target for Team 2: " + (runs + 1));
                        runs = 0; wickets = 0; overs = 0; balls = 0;
                        int temp = battingTeamId; battingTeamId = bowlingTeamId; bowlingTeamId = temp;
                        
                        System.out.println("\nSelect opening batsmen for " + getTeamName(battingTeamId) + ":");
                        System.out.print("Striker ID: ");
                        strikerId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Non-Striker ID: ");
                        nonStrikerId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Bowler ID: ");
                        bowlerId = Integer.parseInt(scanner.nextLine().trim());
                        matchDAO.updateMatchState(matchId, strikerId, nonStrikerId, bowlerId, innings);
                    } else {
                        System.out.println("Match Finished!");
                        matchDAO.completeMatch(matchId);
                        matchRunning = false;
                    }
                } else {
                    System.out.print("Enter NEW Batsman ID (Wicket fell): ");
                    String nextBat = scanner.nextLine().trim();
                    if (!nextBat.equals("Q")) {
                        strikerId = Integer.parseInt(nextBat);
                        matchDAO.updateMatchState(matchId, strikerId, nonStrikerId, bowlerId, innings);
                    } else break;
                }
            }
        }
    }

    public static void viewSpectatorMode() {
        System.out.print("Enter Match ID to watch live: ");
        int matchId;
        try {
            matchId = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid ID.");
            return;
        }

        System.out.println("Connecting to live match stream... (Press Ctrl+C to exit spectator mode)");
        while (true) {
            try {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                
                String matchSql = "SELECT * FROM matches WHERE id = ?";
                String getScore = "SELECT * FROM scores WHERE match_id = ? AND team_id = ?";
                try (Connection conn = DatabaseManager.connect();
                     PreparedStatement pstmt = conn.prepareStatement(matchSql)) {
                    pstmt.setInt(1, matchId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String t1Name = getTeamName(rs.getInt("team1_id"));
                        String t2Name = getTeamName(rs.getInt("team2_id"));
                        String status = rs.getString("status");
                        System.out.println("=== LIVE CRICKET SCORE ===");
                        System.out.println(t1Name + " vs " + t2Name + " | Venue: " + rs.getString("venue"));
                        System.out.println("Status: " + status);
                        System.out.println("--------------------------");

                        try(PreparedStatement sPstmt = conn.prepareStatement(getScore)) {
                            sPstmt.setInt(1, matchId);
                            sPstmt.setInt(2, rs.getInt("team1_id"));
                            ResultSet srs1 = sPstmt.executeQuery();
                            if(srs1.next()) {
                                System.out.println(t1Name + ": " + srs1.getInt("runs") + "/" + srs1.getInt("wickets") + "  (" + srs1.getInt("overs") + "." + srs1.getInt("balls") + " overs)");
                            }
                            
                            sPstmt.setInt(1, matchId);
                            sPstmt.setInt(2, rs.getInt("team2_id"));
                            ResultSet srs2 = sPstmt.executeQuery();
                            if(srs2.next()) {
                                System.out.println(t2Name + ": " + srs2.getInt("runs") + "/" + srs2.getInt("wickets") + "  (" + srs2.getInt("overs") + "." + srs2.getInt("balls") + " overs)");
                            }
                        }
                    } else {
                        System.out.println("Match not found.");
                        break;
                    }
                }
                Thread.sleep(5000); // 5 sec Polling
            } catch (Exception e) {
                System.out.println("Spectator mode ended.");
                break;
            }
        }
    }

    private static String getTeamName(int teamId) {
        try(Connection conn = DatabaseManager.connect();
            PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM teams WHERE id = ?")) {
            pstmt.setInt(1, teamId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getString("name");
        } catch(SQLException ignored){}
        return "Unknown Team";
    }
}