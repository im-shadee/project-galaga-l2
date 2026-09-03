package engine.utilities;

import engine.StdDraw;

public class Debug {

    private static boolean debugActive;
    private static boolean debugKeyUnpressed;

    public Debug() {
        debugActive = false;
        debugKeyUnpressed = true;
    }

    public static boolean debugOn() {
        return debugActive;
    }

    private static void setDebugState(boolean debugState) {
        debugActive = debugState;
        System.out.println("Turned on debug mode: " + debugState);
    }

    public static void activateDebug() {

        // I: activates debug
        if (StdDraw.isKeyPressed(73) && debugKeyUnpressed) {

            Debug.setDebugState(!Debug.debugOn());
            debugKeyUnpressed = false;
        }

        if (!StdDraw.isKeyPressed(73)) {
            debugKeyUnpressed = true;
        }
    }
}
