package at.sporty.team1.presentation;

import at.sporty.team1.presentation.controllers.MainViewController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is Utility class which starts the whole application.
 */
public class MainApp extends Application {
    private static final int DEFAULT_WIDTH = 960;
    private static final int DEFAULT_HEIGHT = 540;
    private static final String DEFAULT_TITLE = "SPORTY";

    /**
     * Default (empty) constructor for this utility class.
     */
    public MainApp() {
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void start(Stage initStage) throws Exception {
    	showMainStage(new Stage());
    }

    /**
     * Displays the main stage of the application.
     * @param primaryStage stage to be shown.
     */
    private void showMainStage(Stage primaryStage) {
        ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
        Parent pane = (Parent) viewLoader.loadNode();

        primaryStage.setScene(new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT));
        primaryStage.setTitle(DEFAULT_TITLE);

        primaryStage.show();
    }

    /**
     * Default main method. Starts "this" application.
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
	}
}
