public class Match extends Game {
    Team team1;
    Team team2;
    String venue;
    Score score1;
    Score score2;
    
    public Match(int matchId, Team team1, Team team2, String venue, Score score1, Score score2) {
        super(matchId);
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.score1 = score1;
        this.score2 = score2;
    }

    @Override
    public void start() {
        this.status = "Ongoing";
        System.out.println("Match " + gameId + " started at " + venue);
    }

    @Override
    public void finish() {
        this.status = "Completed";
        System.out.println("Match " + gameId + " finished.");
    }

    @Override
    public Team getWinner() {
        if (score1 != null && score2 != null) {
            if (score1.runs > score2.runs) {
                return team1;
            } else if (score2.runs > score1.runs) {
                return team2;
            }
        }
        return null; // Draw or unfinished
    }
}
