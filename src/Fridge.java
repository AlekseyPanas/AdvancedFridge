public class Fridge {
    // Creates Subclass objects
    // ------------------------
    private Dashboard dash = new Dashboard();
    private Intake intake = new Intake();
    private Database db = new Database();

    // Constructor
    public Fridge() {
    }

    // Main method for fridge
    public void runFridge(String[] args) {
        dash.doLaunch(args);
    }

}
