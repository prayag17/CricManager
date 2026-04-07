public class Player extends Person implements Scorable {
    int score;
    int runs;
    double average;
    double strikeRate;
    int wickets;
    double economyRate;
    int oversBowled;
    int teamId;

    public Player(int id, String name, int score, int runs, double average, double strikeRate, int wickets, double economyRate, int oversBowled, int teamId) {
        super(id, name);
        this.score = score;
        this.runs = runs;
        this.average = average;
        this.strikeRate = strikeRate;
        this.wickets = wickets;
        this.economyRate = economyRate;
        this.oversBowled = oversBowled;
        this.teamId = teamId;
    }

    public Player() {
        super();
        System.out.println("[WARN] Player object created without parameters!");
    }

    @Override
    public void displayDetails() {
        System.out.println("Player: " + name + " (ID: " + id + ")");
        System.out.println("Total Runs: " + runs + " | Strike Rate: " + strikeRate);
    }

    @Override
    public void addRuns(int addedRuns) {
        this.runs += addedRuns;
        this.score += addedRuns;
    }

    @Override
    public int getTotalRuns() {
        return this.runs;
    }
}
