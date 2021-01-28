import java.util.Set;

public class Main {
    public static Fridge fridge;

    public static void main(String[] args) throws InterruptedException {
        // Creates main fridge object and runs it
        fridge = new Fridge();
        fridge.runFridge(args);

        // Uncomment for thread debugging
        // Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        // while (threadSet.size() > 1) {
        //     System.out.println(threadSet);
        //     Thread.sleep(1000);
        // }
    }
}
