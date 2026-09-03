package game.actors.enemies;

import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.GameManager;
import game.actors.Enemy;

public class Bee extends Enemy {

    private double zigzagTime;

    public Bee(double x, double y, double speed, int score) {
        super(x, y, speed, score, new Sprite("bee"), 1, 1);
    }

    @Override
    protected void startAtt(DVector2 trgt) {
        zigzagTime = 0;
    }

    @Override
    protected void updtAtt(DVector2 trgt) {
        zigzagTime += GameManager.getFramerate();

        double zigzagX = Math.sin(zigzagTime * 600000);
        attPos = new DVector2(pos.x() + zigzagX, pos.y());

        moveTowards(new DVector2(attPos.x(), 0.01));

        if (pos.y() <= 0.2) {
            attacking = false;
            returning = true;
        }
    }
}
