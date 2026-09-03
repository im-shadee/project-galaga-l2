package game.actors.enemies;

import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.GameManager;
import game.actors.Enemy;
import game.actors.Entity;
import game.actors.Player;
import game.actors.Projectile;

public class Moth extends Enemy {

    private boolean capturedPlayer;

    private enum CaptureState {
        DIVE,
        HELD,
        RETURN,
    }

    private CaptureState captureSte;
    private double holdTimer;

    public Moth(double x, double y, double speed, int score) {
        super(x, y, speed, score, new Sprite("catcher"), 1, 1);
        capturedPlayer = false;
    }

    @Override
    protected void onCollision(Entity other) {

        if (other instanceof Player) {

            capturedPlayer = true;
            other.dmgWith(damage);
            GameManager.getGameInstance().playerHit();

            return;
        }

        if (other instanceof Projectile) {

            GameManager.addPoints(score);

            if (capturedPlayer) {

                GameManager.getGameInstance().getPlayer().incHP();
                capturedPlayer = false;
            }

            this.dmgWith(other.getDmg());
        }
    }

    @Override
    protected void startAtt(DVector2 trgt) {
        captureSte = CaptureState.DIVE;
        attPos = trgt;

        holdTimer = 0;
    }

    @Override
    protected void updtAtt(DVector2 trgt) {
        switch (captureSte) {
            case DIVE:
                dive(trgt);
                break;

            case HELD:
                hold();
                break;

            case RETURN:
                attacking = false;
                returning = true;
                break;
        }
    }

    private void dive(DVector2 trgt) {
        moveTowards(new DVector2(pos.x(), 0.1));

        if (pos.y() <= 0.3) {
            captureSte = CaptureState.HELD;
        }
    }

    private void hold() {

        holdTimer += GameManager.getFramerate();

        double lowerBound = 0.3;
        double offset = 0.1;

        // Locks the y position if a moth is past a set bound
        pos.setVect(pos.x(), Math.max(pos.y(), lowerBound));

        collider.setRect(collider.getX(), collider.getY() - offset, collider.getWidth(), collider.getHeight());

        // We stay hovering for two seconds
        if (holdTimer >= 2000) {
            captureSte = CaptureState.RETURN;
            holdTimer = 0;
            collider.setRect(collider.getX(), collider.getY() + offset, collider.getWidth(), collider.getHeight());
        }
    }
}
