package game;

import ui.UIManager;
import engine.StdDraw;
import engine.utilities.DVector2;
import engine.utilities.FileInterpreter;
import engine.utilities.FilePaths;

/**
 * The game's manager class. Initializes systems that are external to the
 * current game e.g canvas, level, etc, and start/correctly initialize each
 * game.
 */
public class GameManager {

    private static int currentLevel; // The current level in play
    private static int currentScore; // The player's current score
    private static int framerate; // The game's framerate

    private static DVector2 canvasSz; // The canvas' global size

    private static Game currentGameInstance; // The game's instance

    public GameManager() {
        currentLevel = 1;
        currentScore = 0;

        canvasSz = new DVector2(700, 700);
        framerate = 30;

        init();
    }

    /**
     * Initializes the game scene and launches the game.
     */
    public static void init() {

        // Init. canvas
        StdDraw.setCanvasSize(GameManager.getCanvasSize().xToInt(), GameManager.getCanvasSize().yToInt());
        StdDraw.enableDoubleBuffering();

        launchLevel();
    }

    public static Game getGameInstance() {
        return currentGameInstance;
    }

    public static void SpawnEnemyWith(String[] eStats) {
        currentGameInstance.addEnemyToFormationFrom(eStats);
    }

    /**
     * Draws the level screen and starts the game.
     */
    public static void launchLevel() {

        // Draws the current level's name on the screen
        UIManager.drawLevel(currentLevel);

        String[] lvlSettings = FileInterpreter.ReadFile(FilePaths.lvlPath() + "\\level" + currentLevel + ".lvl")
                .split("\n")[0].split(" ");

        currentGameInstance = new Game(
                Double.parseDouble(lvlSettings[1]),
                Double.parseDouble(lvlSettings[2]),
                Double.parseDouble(lvlSettings[3]));

        currentGameInstance.launch();
    }

    /**
     * Sets a new size for the game window.
     * 
     * @param newSz the canvas' new size
     */
    public static void changeCanvasSz(DVector2 newSz) {
        canvasSz = newSz;
    }

    /**
     * Gives access to the number of the current level in play.
     * 
     * @return the current level
     */
    public static int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gives access to the global framerate.
     * 
     * @return the game's framerate
     */
    public static int getFramerate() {
        return framerate;
    }

    /**
     * Gives access to the current score.
     * 
     * @return the current score
     */
    public static int getCurrentScore() {
        return currentScore;
    }

    /**
     * Gives access to the window's height and length.
     * 
     * @return the canvas' size
     */
    public static DVector2 getCanvasSize() {
        return canvasSz;
    }

    /**
     * Adds a number of point to the current score and updates it in the UI.
     * 
     * @param points the number of points to add
     */
    public static void addPoints(int points) {
        currentScore += points;
        UIManager.updtScoreUI(currentScore);
    }

    /**
     * Sets the current level to the next one. If there are no levels left, we end
     * the game.
     * In that case, the winning screen gets drawn,
     * otherwise, we initialize the next level.
     */
    public static void changeLevel(boolean cond) {

        if (!cond) {
            endGame(false);
        }

        currentLevel++;

        // Checks if a levelX file exists in the level directory
        boolean existsNextLevel = FileInterpreter.existsFileIn(FilePaths.lvlPath(),
                ("\\level" + currentLevel + ".lvl"));

        if (!existsNextLevel) {
            endGame(true); // We reached the end of the game whilst still alive, we draw the winning screen
            return;
        }

        // A file was found, so we launch the next level.
        launchLevel();
    }

    /**
     * If the game was won, we draw the winning screen, otherwise the Game Over
     * screen.
     */
    public static void endGame(boolean won) {

        UIManager.drawEnd(won);

        // We update and save the highscore value if needed
        UIManager.updHiScore();

        // We then reset the global values to prepare for a restart
        reset();

        // Waits for a spacebar press before proceeding further
        while (!StdDraw.isKeyPressed(32)) {
        }

        init();
    }

    /**
     * Resets the manager's global parameters.
     */
    public static void reset() {
        currentLevel = 1;
        currentScore = 0;
    }
}
