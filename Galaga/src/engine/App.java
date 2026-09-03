package engine;

import engine.utilities.Debug;
import game.GameManager;
import ui.UIManager;

/**
 * Classe de lancement du projet
 */
public class App {
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        // Création d'un nouveau jeu et lancement de celui-ci
        UIManager UIMInstance = new UIManager();
        GameManager GMInstance = new GameManager();
        Debug DInstance = new Debug();
    }
}
