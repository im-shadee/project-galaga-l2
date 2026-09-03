package ui;

import engine.StdDraw;
import engine.utilities.FileInterpreter;
import engine.utilities.FilePaths;
import engine.utilities.Sprite;
import game.GameManager;

/**
 * Classe du manager de l'interface utilisateur. Permet d'initialiser et gérer
 * les systèmes propres à l'UI qui ne sont pas concernés par le jeu.
 */
public class UIManager {

    private static String highscore;
    private static String score;

    public UIManager() {
        score = "0";

        InitHighscore();
    }

    private static void InitHighscore() {
        highscore = FileInterpreter.ReadFile(FilePaths.hiScorePath());
    }

    public static String getHighscore() {
        return highscore;
    }

    public static String getScore() {
        return score;
    }

    /** Updates highscore and saves it in highscore.sc if the end score is higher */
    public static void updHiScore() {

        if (Double.parseDouble(score) > Double.parseDouble(highscore)) {

            highscore = score;
            FileInterpreter.writeInFile(FilePaths.hiScorePath(), highscore);
        }

        reset();
    }

    public static void updtScoreUI(int newScore) {
        score = String.valueOf(newScore);
    }

    public static void drawGameUI() {

        // Draws values
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.1, 0.95, "Score: " + score);
        StdDraw.text(0.5, 0.95, highscore);

        // Draws titles
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(0.5, 0.98, "High Score");

        // Draws life icon
        double x0 = 0.07;
        Sprite s = new Sprite("ship");

        for (int i = 0; i < GameManager.getGameInstance().getPlayer().getHp(); i++) {
            s.draw(x0 + i * 0.08, 0.08);
        }

        // Draws level icon
        x0 = 0.96;
        s = new Sprite("level");

        for (int i = GameManager.getCurrentLevel(); i > 0; i--) {
            s.draw(x0 - (GameManager.getCurrentLevel() - i) * 0.05, 0.08);
        }

    }

    public static void drawLevel(int levelID) {

        drawTitle("Level " + levelID);
    }

    public static void drawEnd(boolean gameWon) {

        String endText = "Game won!\nCongratulations!\nScore: " + score + "\n\nPress space to play.";

        if (!gameWon) {
            endText = "Game Over\nScore: " + score + "\n\nPress space to play.";
        }

        drawTitle(endText);
    }

    public static void drawTitle(String text) {

        // We draw the title and background
        drawBackground(StdDraw.BLACK);

        StdDraw.setPenColor(StdDraw.WHITE);
        drawMultiline(text, 0.5, 0.1);

        StdDraw.show();
        StdDraw.pause(2000);
    }

    public static void drawBackground(java.awt.Color bckgColor) {

        StdDraw.setPenColor(bckgColor);
        StdDraw.filledSquare(0.5, 0.5, 1);
    }

    private static void drawMultiline(String text, double x, double lineHeight) {

        if (!text.contains("\n")) {
            StdDraw.text(x, 0.5, text);
            return;
        }

        String[] lines = text.split("\n");

        double y = ((double) (lines.length - 1)) / ((double) lines.length);

        for (int i = 0; i < lines.length; i++) {
            StdDraw.text(x, y - i * lineHeight, lines[i]);
        }
    }

    public static void reset() {
        score = "0";
    }
}
