public class TestMatch extends Match {
    public static final int MAX_DAYS = 5;

    public TestMatch(int matchId, Team team1, Team team2, String venue, Score score1, Score score2) {
        super(matchId, team1, team2, venue, score1, score2);
    }

    @Override
    public void start() {
        this.status = "Ongoing";
        System.out.println("Test Match " + gameId + " started at " + venue + ". Scheduled for " + MAX_DAYS + " Days.");
    }
}
