package at.sporty.team1.presentation;

import at.sporty.team1.presentation.controllers.MainViewController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static final int DEFAULT_WIDTH = 960;
    private static final int DEFAULT_HEIGHT = 540;
    private static final String DEFAULT_TITLE = "SPORTY";

    public MainApp() {
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void start(Stage initStage) throws Exception {
    	showMainStage(new Stage());
    }

    private void showMainStage(Stage primaryStage) {
        ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
        Parent pane = (Parent) viewLoader.loadNode();

        primaryStage.setScene(new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT));
        primaryStage.setTitle(DEFAULT_TITLE);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
	}
}
