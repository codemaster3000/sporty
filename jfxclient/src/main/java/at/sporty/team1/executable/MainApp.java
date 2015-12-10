package at.sporty.team1.executable;

import at.sporty.team1.presentation.controllers.MainViewController;
import at.sporty.team1.presentation.util.ViewLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.rmi.RMISecurityManager;

/**
 * This is Utility class which starts the whole application.
 */
public class MainApp extends Application {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final String SECURITY_PROPERTY = "java.security.policy";

	private static final String PATH_TO_SECURITY_POLICIES_FILE = "security.policy";
	private static final String PATH_TO_DEFAULT_CSS_FILE = "/at/sporty/team1/presentation/css/main.css";

	private static final String DEFAULT_TITLE = "SPORTY";
	private static final int DEFAULT_WIDTH = 1024;
	private static final int DEFAULT_HEIGHT = 600;

	/**
	 * Default (empty) constructor for this utility class.
	 */
	public MainApp() {
	}

	@Override
	public void stop() throws Exception {
	}

	@Override
	public void start(Stage initStage) {
		URL securityPoliciesURL = getClass().getClassLoader().getResource(PATH_TO_SECURITY_POLICIES_FILE);
		if (securityPoliciesURL != null) {

			System.setProperty(SECURITY_PROPERTY, securityPoliciesURL.toString());
			System.setSecurityManager(new RMISecurityManager());

			showMainStage();

		} else {
			LOGGER.error("Error occurred while starting thr client. Security policies were not found.");
		}
	}

	private void showMainStage() {
		ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
		Parent mainStage = (Parent) viewLoader.loadNode();
		
		prepareNewStage(mainStage).show();
	}

	/**
	 * Displays the new stage for the application.
	 *
	 * @param pane node to be shown.
	 * @return returns instance of the stage
	 */
	private Stage prepareNewStage(Parent pane) {
		Scene scene = new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		scene.getStylesheets().add(PATH_TO_DEFAULT_CSS_FILE);

		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.setTitle(DEFAULT_TITLE);

		return primaryStage;
	}
	
	/**
	 * Default main method. Starts "this" application.
	 *
	 * @param args the command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
