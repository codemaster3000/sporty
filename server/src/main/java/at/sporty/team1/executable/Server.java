package at.sporty.team1.executable;

import at.sporty.team1.application.controller.real.InitializationController;

/**
 * Created by sereGkaluv on 23-Oct-15.
 * Sporty server starter class.
 */
public class Server {

    /**
     * Default main method. Starts "this" application.
     *
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        InitializationController.initialize();
    }
}
