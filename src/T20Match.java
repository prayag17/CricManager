public class T20Match extends Match {
    public static final int MAX_OVERS = 20;

    public T20Match(int matchId, Team team1, Team team2, String venue, Score score1, Score score2) {
        super(matchId, team1, team2, venue, score1, score2);
    }

    @Override
    public void start() {
        this.status = "Ongoing";
        System.out.println("T20 Match " + gameId + " started at " + venue + ". Max Overs: " + MAX_OVERS);
    }
}
