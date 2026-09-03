package engine.utilities;

import java.io.File;

public class FilePaths {

    // Highscore file
    private static final String hiScPath =
            "ressources" + File.separator + "highscore" + File.separator + "highscore.sc";

    // Levels folder
    private static final String lvlPath =
            "ressources" + File.separator + "levels";

    // Sprites folder
    private static final String sprPath =
            "ressources" + File.separator + "sprites";

    public static String hiScorePath() {
        return hiScPath;
    }

    public static String lvlPath() {
        return lvlPath;
    }

    public static String sprPath() {
        return sprPath;
    }

}
