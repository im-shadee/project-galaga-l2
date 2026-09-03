package engine.utilities;

import java.awt.Color;
import java.io.File;

import engine.StdDraw;

/**
 * The sprites' class. A sprite is represented with a matrix of pixels. You can
 * draw a sprite with a given width, length and pixel size.
 */
public class Sprite {

    private Color[][] sprite; // The pixel matrix representing the sprite

    private String spriteFile; // The sprite file's name
    private double pixelSize; // The pixels size

    private int width; // The sprite's width
    private int height; // The sprites height

    /**
     * Initializes the sprite from a .spr file data and stores it in the pixel grid.
     * 
     * @param spriteName the entity name corresponding to the correct .spr file.
     */
    public Sprite(String spriteName) {
        spriteFile = spriteName + ".spr";
        pixelSize = 0.004f;

        store();
    }

    /**
     * Initializes the sprite from a .spr file data and stores it in the pixel grid.
     * 
     * @param spriteName the entity name corresponding to the correct .spr file.
     * @param pxSz       sets a specific pixel size for that sprite.
     */
    public Sprite(String spriteName, double pxSz) {
        spriteFile = spriteName + ".spr";
        pixelSize = pxSz;

        store();
    }

    public void store() {

        String filePath = FilePaths.sprPath() + File.separator + spriteFile;
        String[] pixels = FileInterpreter.ReadFile(filePath).split("\n");

        height = pixels.length;
        width = pixels[0].length();

        sprite = new Color[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Color c = FileInterpreter.charToColor(pixels[i].charAt(j));

                if (c != null) {
                    // Fills the pixel matrix with the color corresponding to the character
                    sprite[i][j] = c;
                }
            }
        }
    }

    /**
     * Gives access to the sprite's height.
     * 
     * @return this sprite's height.
     */
    public int height() {
        return height;
    }

    /**
     * Gives access to the sprite's width.
     * 
     * @return this sprite's width.
     */
    public int width() {
        return width;
    }

    /**
     * Gives access to the sprite's pixel size.
     * 
     * @return this sprite's pixel size.
     */
    public double pixelSize() {
        return pixelSize;
    }

    public void replaceColor(Color colorToReplace, Color newColor) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (sprite[i][j] == null) {
                    continue;
                }

                if (sprite[i][j].equals(colorToReplace)) {
                    sprite[i][j] = newColor;
                }
            }
        }
    }

    public void draw(double x, double y) {

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                Color c = sprite[row][col];

                if (c != null) {
                    StdDraw.setPenColor(c);

                    double px = x + col * pixelSize + pixelSize / 2;

                    // NB: we use height - 1 - row because StdDraw draws from bottom to top
                    double py = y + (height - 1 - row) * pixelSize + pixelSize / 2;

                    StdDraw.filledSquare(px, py, pixelSize / 2f);
                }
            }
        }
    }

    /**
     * Draws a sprite from a set position. Takes in a DVector instead of two double
     * parameters, and executes the draw function using the DVector's x and y value.
     * 
     * @param pos the vector containing the position at which the sprite is drawn.
     */
    public void draw(DVector2 pos) {
        draw(pos.x(), pos.y());
    }
}
