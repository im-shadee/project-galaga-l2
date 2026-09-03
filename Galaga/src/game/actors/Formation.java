package game.actors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import game.GameManager;
import game.actors.enemies.Bee;
import game.actors.enemies.Butterfly;
import game.actors.enemies.Galaga;
import game.actors.enemies.Moth;

public class Formation {

    private List<ArrayList<Enemy>> columns;
    private List<Double> columnXs;

    private int updateCycles;
    private int nEnemies;

    private static boolean shouldUpdtDir;
    private double formationSpeed;

    public Formation(int n, double fSpeed) {

        columns = new ArrayList<ArrayList<Enemy>>();
        columnXs = new ArrayList<Double>();

        formationSpeed = fSpeed * 100000;
        nEnemies = 0;

        shouldUpdtDir = false;
    }

    /**
     * Gives access to the number of alive enemies within the formation.
     * 
     * @return the number of non-null enemies inside the enemy list.
     */
    public int countEnemies() {
        return nEnemies;
    }

    public List<ArrayList<Enemy>> getFormation() {
        return columns;
    }

    /**
     * Creates an enemy from a list of parameters, and adds it to the formation.
     * 
     * @param args the position, speed and score of the enemy.
     */
    public void addFrom(String[] args) {

        double x = Double.parseDouble(args[1]); // x

        // We get the enemy's position and speed value from the content sent
        double[] enemyStats = {
                x,
                Double.parseDouble(args[2]), // y
                Double.parseDouble(args[5]), // speed
        };

        // We also get the enemy's score value
        int enemyScore = Integer.parseInt(args[4]);

        // We create a new enemy with the corresponding stats
        Enemy newEnemy = getEnemy(args[0], enemyStats, enemyScore);

        int col = getOrCreateColumn(x);
        columns.get(col).add(newEnemy);

        nEnemies++;

        updateEnemyBelowFlags();
    }

    private int getOrCreateColumn(double x) {

        final double EPS = 1e-6;

        for (int i = 0; i < columnXs.size(); i++) {
            if (Math.abs(columnXs.get(i) - x) < EPS) {
                return i;
            }
        }

        columnXs.add(x);
        columns.add(new ArrayList<>());
        return columns.size() - 1;
    }

    /**
     * Creates an enemy from a specific type, depending on the name.
     * 
     * @param name  the enemy's name.
     * @param stats the enemy's stats as specified in the .lvl file.
     * @param score the enemy's score as specified in the .lvl file.
     * @return a new instance of a specific enemy, initialized with its stats.
     * @throws IllegalArgumentException if the name doesn't match any existing enemy
     *                                  subclass.
     */
    private Enemy getEnemy(String name, double[] stats, int score) throws IllegalArgumentException {

        switch (name.toLowerCase()) {
            case "bee":
                return new Bee(stats[0], stats[1], stats[2], score);

            case "butterfly":
                return new Butterfly(stats[0], stats[1], stats[2], score);

            case "moth":
                return new Moth(stats[0], stats[1], stats[2], score);

            case "galaga":
                return new Galaga(stats[0], stats[1], stats[2], score);

            default:
                throw new IllegalArgumentException("Enemy with name " + name + " does not exist!");
        }
    }

    private void updateEnemyBelowFlags() {

        for (List<Enemy> col : columns) {
            for (int i = 0; i < col.size(); i++) {

                Enemy e = col.get(i);
                e.setHasEnemyBelow(i > 0);
            }
        }
    }

    private void updateDir() {
        for (List<Enemy> col : columns) {
            for (Enemy e : col) {

                if (!e.isBoss()) {
                    e.changeDir();
                }
            }
        }

        shouldUpdtDir = false;
    }

    public static void enableDirChange() {
        shouldUpdtDir = true;
    }

    public void draw() {
        for (List<Enemy> col : columns) {
            for (Enemy e : col) {
                e.draw();
            }
        }
    }

    public void update(List<Projectile> plProj) {

        checkCollisionWithBullet(plProj);
        updateEnemies();

        if (updateCycles >= formationSpeed) {
            updateFPos();
            updateCycles = 0;
        }

        if (shouldUpdtDir) {
            updateDir();
        }

        updateCycles += GameManager.getFramerate();
    }

    private void updateFPos() {

        updateEnemyBelowFlags();

        try {
            if (columns.get(0).get(0) instanceof Galaga) {
                Galaga g = (Galaga) columns.get(0).get(0);
                g.update();
            }

            else {
                columns.get(0).get(0).updateFormationPosition();
            }

        }

        catch (IndexOutOfBoundsException oobe) {
        }

        finally {
            for (int i = 1; i < columns.size(); i++) {

                for (Enemy e : columns.get(i)) {
                    e.updateFormationPosition();
                }
            }
        }
    }

    private void updateEnemies() {

        Enemy e0;

        try {
            e0 = columns.get(0).get(0);

            if (!(e0 instanceof Galaga)) {
                e0.update();
            }

        }

        catch (IndexOutOfBoundsException oobe) {
        }

        finally {
            for (int i = 1; i < columns.size(); i++) {

                for (Enemy e : columns.get(i)) {
                    e.update();
                }
            }
        }

    }

    private void checkCollisionWithBullet(List<Projectile> plProj) {
        for (List<Enemy> col : columns) {
            Iterator<Enemy> it = col.iterator();

            while (it.hasNext()) {
                Enemy e = it.next();

                for (Projectile p : plProj) {
                    e.checkCollisionWith(p);
                }

                if (e.getHp() <= 0) {
                    it.remove();
                    nEnemies--;
                }
            }
        }
    }

    public void reset() {
        for (List<Enemy> col : columns) {
            for (Enemy e : col) {
                e.reset();
            }
        }
    }
}
