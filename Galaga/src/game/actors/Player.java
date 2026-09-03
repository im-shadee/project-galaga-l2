package game.actors;

import engine.StdDraw;
import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.GameManager;

/**
 * The player's class. Player can move with left/right arrows and can shoot
 * projectiles using the spacebar.
 */
public class Player extends Entity {

    private boolean spaceReleased; // Checks if the spacebar has been released.

    public Player(double x, double y) {
        super(3, 1, x, y, 0.01, new Sprite("ship"));
        spaceReleased = true;
    }

    public void incHP() {

        if (healthPoints != 3) {
            healthPoints++;
        }
    }

    /**
     * Updates the player's location depending on the arrow key pressed. Shoots
     * using spacebar.
     */
    @Override
    public void update() {

        super.update();

        // Left arrow: move the player to the left
        if (StdDraw.isKeyPressed(37) && pos.x() >= 0 + width / 2) {
            pos.setVect(pos.x() - speed, pos.y());
        }

        // Right arrow: moves the player to right
        if (StdDraw.isKeyPressed(39) && pos.x() <= 1 - width / 2) {
            pos.setVect(pos.x() + speed, pos.y());
        }

        /*
         * Spacebar: makes the player shoot, we check if the spacebar has been released
         * to avoid shooting multiple bullets from one key press
         */
        if (StdDraw.isKeyPressed(32) && spaceReleased) {

            // Shoots a bullet from the player's location
            GameManager.getGameInstance().shootProjectile(new DVector2(collider.getCenterX(), collider.getCenterY()),
                    "player");
            spaceReleased = false;
        }

        // Allows the player to shoot again only if he released space
        if (!StdDraw.isKeyPressed(32)) {
            spaceReleased = true;
        }

    }
}
