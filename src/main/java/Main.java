import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        // Creates main fridge object and runs it
        Fridge fridge = new Fridge();
        fridge.runFridge(args);

        // new StorageManager().saveStorage();

        // Intake intake = new Intake();
        // intake.doLaunch(args);
    }
}
