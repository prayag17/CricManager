import java.util.Scanner;
import java.util.List;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    private static PlayerDAO playerDAO = new PlayerDAO();
    private static TeamDAO teamDAO = new TeamDAO();

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing CricManager...");
        
        // Connecting and initializing the SQLite database
        DatabaseManager.initializeDatabase();
        
        System.out.println("Application ready.\n");

        boolean running = true;
        while (running) {
            System.out.println("=====================================");
            System.out.println("        CRICMANAGER MAIN MENU        ");
            System.out.println("=====================================");
            System.out.println("1. Manage Teams & Squads");
            System.out.println("2. Manage Players");
            System.out.println("3. Start Match / Umpire Mode");
            System.out.println("4. View Live Match (Spectator Mode)");
            System.out.println("5. View Leaderboards");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manageTeamsMenu();
                    break;
                case "2":
                    managePlayersMenu();
                    break;
                case "3":
                    MatchManager.startMatchMode();
                    break;
                case "4":
                    MatchManager.viewSpectatorMode();
                    break;
                case "5":
                    LeaderboardManager.viewLeaderboards();
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting CricManager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void manageTeamsMenu() {
        System.out.println("\n--- Manage Teams ---");
        System.out.println("1. Add New Team");
        System.out.println("2. View All Teams");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter Team Name: ");
            String name = scanner.nextLine().trim();
            // Note: captain_id is set to 0 (null) initially
            teamDAO.addTeam(name, 0); 
        } else if (choice.equals("2")) {
            List<Team> teams = teamDAO.getAllTeams();
            System.out.println("\n--- Registered Teams ---");
            for (Team t : teams) {
                System.out.println("ID: " + t.teamId + " | Name: " + t.teamName);
            }
            System.out.println("------------------------\n");
        }
    }

    private static void managePlayersMenu() {
        System.out.println("\n--- Manage Players ---");
        System.out.println("1. Add New Player");
        System.out.println("2. View All Players");
        System.out.println("3. View Players by Team");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter Player Name: ");
            String name = scanner.nextLine().trim();
            
            System.out.println("Available Teams:");
            List<Team> teams = teamDAO.getAllTeams();
            for (Team t : teams) {
                System.out.println(t.teamId + ". " + t.teamName);
            }
            System.out.print("Enter Team ID for the player (or 0 for no team): ");
            int teamId;
            try {
                teamId = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                teamId = 0;
            }

            // Create new player with default 0 stats
            Player p = new Player(0, name, 0, 0, 0.0, 0.0, 0, 0.0, 0, teamId);
            playerDAO.addPlayer(p);

        } else if (choice.equals("2")) {
            List<Player> players = playerDAO.getAllPlayers();
            printPlayers(players);
        } else if (choice.equals("3")) {
            System.out.print("Enter Team ID: ");
            try {
                int tId = Integer.parseInt(scanner.nextLine().trim());
                List<Player> players = playerDAO.getPlayersByTeamId(tId);
                printPlayers(players);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Team ID.");
            }
        }
    }

    private static void printPlayers(List<Player> players) {
        System.out.println("\n--- Players List ---");
        System.out.printf("%-5s | %-20s | %-8s | %-10s%n", "ID", "Name", "Team ID", "Avg/SR");
        System.out.println("---------------------------------------------------------");
        for (Player p : players) {
            System.out.printf("%-5d | %-20s | %-8d | %-5.2f / %-5.2f%n", 
                p.id, p.name, p.teamId, p.average, p.strikeRate);
        }
        System.out.println("---------------------------------------------------------\n");
    }
}
