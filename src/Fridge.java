public class Fridge {

    // Controls running of main loop (Dashboard go blank if not true)
    private boolean running = true;

    // Creates Subclass objects
    // ------------------------
    private Dashboard dash = new Dashboard();
    private Intake intake = new Intake();
    private Database db = new Database();

    // Constructor
    public Fridge() {

    }

    // Main method for fridge
    public void runFridge() {
        while (running) {

        }
    }

    // Stops main loop
    public void stopFridge() {
        running = false;
    }

}
