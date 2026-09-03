package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.StdDraw;
import engine.utilities.DVector2;
import engine.utilities.Debug;
import engine.utilities.FileInterpreter;
import engine.utilities.FilePaths;
import game.actors.Enemy;
import game.actors.Formation;
import game.actors.Player;
import game.actors.Projectile;
import ui.UIManager;

/**
 * Current game's class. Handles the game scene's initialization and the main
 * in-real-time game loop.
 */
public class Game {

    private Player player;

    private Formation enemyFormation;

    private List<Projectile> playerProjectiles;
    private List<Projectile> enemyProjectiles;

    private boolean pPressed;
    private boolean playerShouldBeHit;

    /**
     * Initializes a game with each of its elements.
     * 
     * @param formationSp    the formation's speed,
     * @param attCooldown    the cooldown between two attacks, -1 if no attacks,
     * @param bulletCooldown the cooldown between each bullet shot.
     */
    public Game(double formationSp, double attCooldown, double bulletCooldown) {
        player = new Player(0.5, 0.2);

        initFormation(formationSp);

        if (attCooldown != -1) {
            Enemy.enableAttacks(true);
            Enemy.setAttCooldown(attCooldown);
        }

        else {
            Enemy.enableAttacks(false);
        }

        Enemy.setBulletCooldown(bulletCooldown);

        playerProjectiles = new ArrayList<Projectile>(3);
        enemyProjectiles = new ArrayList<Projectile>(15);
    }

    /**
     * Initializes the enemy formation.
     * 
     * @param fSpeed the formation's speed.
     */
    public void initFormation(double fSpeed) {

        String[] content = FileInterpreter
                .ReadFile(FilePaths.lvlPath() + File.separator + "level" + GameManager.getCurrentLevel() + ".lvl")
                .split("\n");

        enemyFormation = new Formation(content.length - 1, fSpeed);

        // We get data from each line and create an enemy from it within the formation
        for (int i = content.length - 1; i > 0; i--) {
            enemyFormation.addFrom(content[i].split(" "));
        }
    }

    /** Launches in-real-time game loop */
    public void launch() {

        while (gameRunning()) {
            StdDraw.clear();

            update();
            draw();

            StdDraw.show();
            StdDraw.pause(GameManager.getFramerate());
        }

        end();
    }

    /** Destroys each of the game's elements and ends the level */
    public void end() {

        GameManager.changeLevel(player.getHp() > 0 && enemyFormation.countEnemies() == 0 || !pPressed);

        // Deletes each element
        enemyFormation = null;
        playerProjectiles = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void addEnemyToFormationFrom(String[] eStats) {
        enemyFormation.addFrom(eStats);
    }

    /**
     * Resets each of the actors' location back to their initial location when the
     * player gets hit and deletes every projectile.
     */
    public void playerHit() {
        playerProjectiles = new ArrayList<Projectile>(3);
        enemyProjectiles = new ArrayList<Projectile>(15);

        player.reset();
        enemyFormation.reset();

        StdDraw.clear();

        update();
        draw();

        StdDraw.show();

        // We wait 3s before allowing the game to continue
        StdDraw.pause(3000);
    }

    /**
     * Gets the game's running condition. The game ends if there are no enemies in
     * the enemyList or if the player's health is less or equal than 0.
     * 
     * @return false if the player is dead or if all enemies have been destroyed.
     */
    private boolean gameRunning() {
        pPressed = !StdDraw.isKeyPressed(80);
        return player.getHp() > 0 && enemyFormation.countEnemies() > 0 && pPressed;
    }

    /**
     * Creates a new projectile from an actor's location.
     * 
     * @param actorPos the actor's current position
     */
    public void shootProjectile(DVector2 actorPos, String actorID) {
        Projectile p = new Projectile(actorPos.x(), actorPos.y(), actorID);

        if ("player".equalsIgnoreCase(actorID)) {

            if (playerProjectiles.size() < 3) {
                playerProjectiles.add(p);
            }
        }

        else {
            enemyProjectiles.add(p);
        }
    }

    /** Draws all of the game's elements */
    public void draw() {

        // We draw the background first so it doesn't mask anything
        UIManager.drawBackground(StdDraw.BLACK);

        // We then draw each actor on screen
        player.draw();

        enemyFormation.draw();

        for (Projectile p : playerProjectiles) {
            if (p != null) {
                p.draw();
            }
        }

        for (Projectile p : enemyProjectiles) {
            if (p != null) {
                p.draw();
            }
        }

        // And we finally draw UI and HUD
        UIManager.drawGameUI();
    }

    /** Updates each of the game's elements parameters */
    private void update() {

        player.update();
        checkPlayerCollide();
        checkEnemyCollide();

        enemyFormation.update(playerProjectiles);

        updtProjectiles(playerProjectiles);
        updtProjectiles(enemyProjectiles);

        Debug.activateDebug();

        if (playerShouldBeHit) {
            playerShouldBeHit = false;
            playerHit();
            return;
        }
    }

    private void checkPlayerCollide() {

        for (Projectile p : enemyProjectiles) {

            player.checkCollisionWith(p);
            
            if (player.isCollidingWith(p)) {
                playerShouldBeHit = true;
            }
        }
    }

    private void checkEnemyCollide() {
        for (List<Enemy> L : enemyFormation.getFormation()) {
            for (Enemy e : L) {
                player.checkCollisionWith(e);

                if (player.isCollidingWith(e)) {
                    playerShouldBeHit = true;
                }
            }
        }
    }

    private void updtProjectiles(List<Projectile> projectiles) {
        Iterator<Projectile> it = projectiles.iterator();

        while (it.hasNext()) {

            Projectile p = it.next();
            p.update();

            if (p.getHp() <= 0) {
                it.remove();
            }
        }
    }

}
