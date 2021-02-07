public class Intake {
    // Creates sensors
    private final Camera rCamera;
    private final Scale rScale;

    public Intake() {
        rCamera = new Camera();
        rScale = new Scale();
    }

    public Camera getCamera() {
        return rCamera;
    }

    public Scale getScale() {
        return rScale;
    }
}
