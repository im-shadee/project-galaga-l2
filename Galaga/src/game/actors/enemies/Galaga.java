package game.actors.enemies;

import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.GameManager;
import game.actors.Enemy;

import java.awt.Color;
import java.util.Random;

public class Galaga extends Enemy {

    private double laserTimer;
    private double spawnTimer;

    private double laserCooldown;
    private double spawnCooldown;

    private boolean passedHalfHp;

    private int maxHealth;
    private int localDirection;

    private Random rand;

    public Galaga(double x, double y, double speed, int score) {
        super(x, y, speed, score, new Sprite("galaga", 0.02), 300, 1);

        maxHealth = 300;
        laserTimer = 0;
        spawnTimer = 0;

        spawnCooldown = 700;
        laserCooldown = 300;

        localDirection = 1;

        isBoss = true;
        passedHalfHp = false;

        rand = new Random();
    }

    @Override
    public void update() {
        super.update();

        move();

        if (spawnTimer >= spawnCooldown) {
            spawnMoths();
            spawnTimer = 0;
        }

        if (healthPoints <= maxHealth / 2) {
            halfHpEvents();

            if (laserTimer >= laserCooldown) {
                shootLaser();
                laserTimer = 0;
            }

        }

        spawnTimer += GameManager.getFramerate();
        laserTimer += GameManager.getFramerate();
    }

    public void move() {
        if (collider.getX() <= 0.05 && localDirection == -1 || collider.getMaxX() >= 0.95 && localDirection == 1) {
            localDirection *= -1;
            spawnTimer = 0;
        }

        pos.setVect(pos.x() + speed * localDirection, fPos.y());
    }

    private void halfHpEvents() {
        if (passedHalfHp) {
            return;
        }

        sprite.replaceColor(Color.YELLOW, Color.MAGENTA);
        sprite.replaceColor(Color.RED, Color.YELLOW);

        spawnCooldown = 500;

        spawnTimer = 0;
        laserTimer = 250;

        passedHalfHp = true;
    }

    private void spawnMoths() {
        int nMoths = rand.nextInt(3) + 3;
        double spacing = 0.07;

        for (int i = 0; i < nMoths; i++) {
            double spawnX = fPos.x() - (nMoths / 2.0 - i) * spacing;
            double spawnY = fPos.y() - 0.08;

            GameManager.SpawnEnemyWith(new String[] {
                    "Moth",
                    String.valueOf(spawnX),
                    String.valueOf(spawnY),
                    "0.06",
                    "300",
                    "0.002"
            });
        }
    }

    private void shootLaser() {
        GameManager.getGameInstance().shootProjectile(new DVector2(collider.getCenterX(), collider.getCenterY()),
                "enemy_laser");
    }

    @Override
    protected void startAtt(DVector2 trgt) {
        // No attack towards the player
    }

    @Override
    protected void updtAtt(DVector2 trgt) {
        // No attack towards the player
    }

    @Override
    protected void onCollision(game.actors.Entity other) {
        super.onCollision(other);
    }
}