package game.actors;

import engine.utilities.Sprite;

/**
 * The projectiles' class. Projectiles are sent from either the player or an
 * enemy, and move along the y axis from the sender's position. They inflict
 * damage and get destroyed when they hit something other than their sender or
 * themselves.
 */
public class Projectile extends Entity {

    private String sender; // The actor that sent the projectile

    /**
     * Creates a projectile from an actor's location at a t instant.
     * Initializes a projectile with 1 hp, 1 dmg and a set speed.
     * 
     * @param x      the projectile value on the horizontal axis.
     * @param y      the projectile value on the vertical axis.
     * @param sender the actor that sent the projectile.
     */
    public Projectile(double x, double y, String sender) {
        super(1, 1, x, y, 0.07, new Sprite("bullet"));

        this.sender = sender;

        if (sender.contains("laser")) {
            changeBulletType();
        }
    }

    private void changeBulletType() {
        sprite = new Sprite("laser", 0.015f);
    }

    /**
     * Gives access to a specific projectile's sender.
     * 
     * @return this projectile's instance's sender
     */
    public String getSender() {
        return sender;
    }

    /** Updates this object's y value depending on speed and sender */
    public void update() {

        super.update();

        if (pos.y() <= 1 && pos.y() >= 0) {

            // The projectile goes up from the player's location
            if (sender.equals("player")) {
                pos.setVect(pos.x(), pos.y() + speed);
            }

            // The projectile goes down from the enemy's location
            else {
                pos.setVect(pos.x(), pos.y() - speed);
            }
        }

        // The object is now off-screen and needs to be destroyed
        else {
            healthPoints = 0;
        }
    }

    @Override
    public void onCollision(Entity other) {

        // Projectiles get destroyed as soon as they hit something other than themselves
        if (!(other instanceof Projectile)) {
            super.onCollision(other);
        }
    }
}
