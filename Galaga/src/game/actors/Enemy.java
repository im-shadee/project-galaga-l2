package game.actors;

import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.GameManager;

/**
 * The enemy's class. Enemies move according to a certain constant pattern
 * following a specific direction. They can die and increment the player's score
 * when hit, fly towards the player and shoot bullets.
 */
public abstract class Enemy extends Entity {

    protected int score;
    protected int direction;

    private static double instanceTime;
    private static double bulletTimer;

    protected static double attCooldown;
    protected static double bulletCooldown;

    protected boolean hasEnemiesBelow;
    protected boolean isBoss;
    protected boolean canAttack;
    protected boolean attacking;
    protected boolean returning;
    protected static boolean enAttack;

    protected DVector2 fPos;
    protected DVector2 attPos;

    /**
     * Creates an enemy object.
     * 
     * @param x     the enemy's upper left x coordinate,
     * @param y     the enemy's upper left y coordinate,
     * @param speed the enemy's speed,
     * @param score the score associated with the enemy,
     * @param s     the enemy's sprite.
     */
    public Enemy(double x, double y, double speed, int score, Sprite s, int hp, int dmg) {
        super(hp, dmg, x, y, speed, s);
        this.score = score;

        direction = 1;
        instanceTime = 0;
        bulletTimer = 0;

        hasEnemiesBelow = true;

        canAttack = false;
        isBoss = false;

        attacking = false;
        returning = false;

        fPos = new DVector2(initPos);
    }

    public static void enableAttacks(boolean en) {
        enAttack = en;
    }

    public static void setAttCooldown(double cooldown) {
        attCooldown = cooldown * 100;
    }

    public static void setBulletCooldown(double cooldown) {
        bulletCooldown = cooldown * 10;
    }

    public void setHasEnemyBelow(boolean foundEnemy) {
        hasEnemiesBelow = foundEnemy;
    }

    protected boolean canStartAttack() {
        return enAttack && instanceTime >= attCooldown && !attacking && !returning && !hasEnemiesBelow;
    }

    public void changeDir() {
        direction *= -1;
    }

    public boolean isBoss() {
        return isBoss;
    }

    @Override
    protected void onCollision(Entity other) {

        super.onCollision(other);

        /*
         * Increments the score only if hit by the player's projectile
         * 
         * NB: we don't need to check the projectile's sender since enemies
         * won't be shooting eachother.
         */
        if (other instanceof Projectile && healthPoints <= 0) {
            GameManager.addPoints(score);
        }
    }

    @Override
    public void update() {

        super.update();
        attack();

        if (!attacking && !returning) {
            instanceTime += GameManager.getFramerate();
        }

        bulletTimer += GameManager.getFramerate();
    }

    public void updateFormationPosition() {
        if (bulletTimer >= bulletCooldown && enAttack && !hasEnemiesBelow) {
            GameManager.getGameInstance().shootProjectile(new DVector2(collider.getCenterX(), collider.getCenterY()),
                    "enemy");
            bulletTimer = 0;
        }

        if (collider.getX() <= 0.05 && direction == -1 || collider.getMaxX() >= 0.95 && direction == 1) {
            Formation.enableDirChange();
        }

        fPos.setVect(fPos.x() + speed * direction, fPos.y());
    }

    private void attack() {
        if (canStartAttack()) {
            attacking = true;
            instanceTime = 0;

            startAtt(GameManager.getGameInstance().getPlayer().getPos());
        }

        if (attacking) {
            updtAtt(GameManager.getGameInstance().getPlayer().getPos());
        }

        else if (returning) {
            returnToFormation();
        }

        else {
            this.pos = fPos;
        }
    }

    protected void moveTowards(DVector2 trgt) {

        DVector2 dir = trgt.substract(pos).normalize();
        pos = pos.add(dir.multiply(speed * 2));
    }

    protected void returnToFormation() {
        moveTowards(fPos);

        if (pos.squaredDist(fPos) < 0.05 * 0.05) {

            pos.setVect(fPos);

            returning = false;
            canAttack = false;
        }
    }

    protected abstract void startAtt(DVector2 trgt);

    protected abstract void updtAtt(DVector2 trgt);
}
