package game.actors.enemies;

import engine.utilities.DVector2;
import engine.utilities.Sprite;
import game.actors.Enemy;

public class Butterfly extends Enemy {

    public Butterfly(double x, double y, double speed, int score) {
        super(x, y, speed, score, new Sprite("butterfly"), 1, 1);
    }

    @Override
    protected void startAtt(DVector2 trgt) {
        attPos = new DVector2(trgt);
    }

    @Override
    protected void updtAtt(DVector2 trgt) {
        moveTowards(new DVector2(pos.x(), 0.01));

        if (pos.y() <= 0.2) {
            attacking = false;
            returning = true;
        }
    }
}
