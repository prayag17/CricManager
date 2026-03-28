public abstract class Game {
    protected int gameId;
    protected String status; // Scheduled, Ongoing, Completed

    public Game(int gameId) {
        this.gameId = gameId;
        this.status = "Scheduled";
    }

    public abstract void start();
    public abstract void finish();
    public abstract Team getWinner();
}
