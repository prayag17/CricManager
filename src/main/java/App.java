public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing CricManager...");
        
        // Connecting and initializing the SQLite database
        DatabaseManager.initializeDatabase();
        
        System.out.println("Application ready.");
    }
}
