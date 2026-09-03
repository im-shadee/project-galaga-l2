package game.actors;

import java.awt.geom.Rectangle2D;

import engine.StdDraw;
import engine.utilities.DVector2;
import engine.utilities.Debug;
import engine.utilities.Sprite;

/**
 * This class represent's an entity with healthpoints, damage, speed, a
 * position, a sprite and a collider.
 */
public class Entity {

    protected int healthPoints; // The entity's health's points
    protected int damage; // The entity's input damage

    protected DVector2 pos; // The entity's x and y position
    protected DVector2 initPos; // The entity's initial position

    protected double width; // The entity's length
    protected double height; // The entity's height
    protected double speed; // The entity's global speed

    protected Sprite sprite; // The entity's sprite

    protected Rectangle2D collider; // The entity's collider

    /**
     * Creates a entity object with set properties.
     * All actors inherit from this class.
     * 
     * @param hp  the entity's current status,
     * @param dmg the entity's input damage,
     * @param x   the entity's upper left corner x coordinate,
     * @param y   the entity's upper left corner y coordinate,
     * @param sp  the entity's speed,
     * @param s   the entity's sprite.
     */
    public Entity(int hp, int dmg, double x, double y, double sp, Sprite s) {

        healthPoints = hp;
        damage = dmg;
        speed = sp;
        sprite = s;

        pos = new DVector2(x, y);
        initPos = new DVector2(x, y);

        width = sprite.width() * sprite.pixelSize();
        height = sprite.height() * sprite.pixelSize();
        collider = new Rectangle2D.Double(pos.x(), pos.y(), width, height);
    }

    /**
     * Gives access to the entity's status.
     * 
     * @return the entity's hp value.
     */
    public int getHp() {
        return this.healthPoints;
    }

    /**
     * Gives access to the entity's damage stat.
     * 
     * @return the entity's damage value.
     */
    public int getDmg() {
        return this.damage;
    }

    /**
     * Gives access to the entity's position.
     * 
     * @return the entity's position as a DVector2.
     */
    public DVector2 getPos() {
        return pos;
    }

    /**
     * Decreases the current entity's health by the inflicting enemy's damage
     * value.
     * 
     * @param otherDmg the other entity's damage value.
     */
    public void dmgWith(int otherDmg) {
        healthPoints -= otherDmg;
    }

    /**
     * Changes the position of the collider each update accordingly to the new
     * entity's position.
     */
    private void updateCollider() {

        collider.setRect(pos.x(), pos.y(), width, height);
    }

    /**
     * Checks if this entity's is colliding with an other entity's collider.
     * 
     * @param other the entity we check the collision with.
     */
    public void checkCollisionWith(Entity other) {

        if (collider.intersects(other.getCollider())) {
            onCollision(other);
        }
    }

    public boolean isCollidingWith(Entity other) {
        return collider.intersects(other.getCollider());
    }

    /**
     * Gives access to an entity's collider.
     * 
     * @return this entity's collider
     */
    public Rectangle2D getCollider() {
        return this.collider;
    }

    /** Draws an entity's collider rectangle on screen. !USED IN DEBUG MODE! */
    private void drawCollider() {

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.rectangle(collider.getCenterX(), collider.getCenterY(), width / 2, height / 2);
    }

    /**
     * Inflicts damage to both this and the other entities.
     * Can be overwritten.
     * 
     * @param other the other entity colliding with this entity.
     */
    protected void onCollision(Entity other) {
        this.dmgWith(other.getDmg());
        other.dmgWith(damage);
    }

    /** Draws the entity's sprite on the canvas */
    public void draw() {
        sprite.draw(pos);

        if (Debug.debugOn()) {
            drawCollider();
        }
    }

    /**
     * Updates this entity's collider.
     * Specific behaviors are defined within Entity's subclasses.
     * Can be overwritten.
     */
    public void update() {
        updateCollider();
    }

    /** Resets this entity's position back to their default location */
    public void reset() {
        pos.setVect(initPos);
    }
}
