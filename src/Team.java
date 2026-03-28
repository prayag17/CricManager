public class Team implements Scorable {
    int teamId;
    String teamName;
    Player[] players;
    int captainIndex;
    static int totalRuns;
    static int totalWickets;
    static int totalMatches;

    public Team(int teamId, String teamName, Player[] players, int captainIndex) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.players = players;
        this.captainIndex = captainIndex;
    }

    public Team() {
        System.out.println("[WARN] Team object created without parameters!");
    }
    
    @Override
    public void addRuns(int runs) {
        totalRuns += runs;
    }

    @Override
    public int getTotalRuns() {
        return totalRuns;
    }
}
