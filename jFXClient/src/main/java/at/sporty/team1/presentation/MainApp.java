package at.sporty.team1.presentation;

import at.sporty.team1.presentation.controllers.LoginViewController;
import at.sporty.team1.presentation.controllers.MainViewController;
import at.sporty.team1.rmi.enums.UserRole;
import javafx.application.Application;
import javafx.scene.Node;
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
	private static final int DEFAULT_HEIGHT = 700;
	
	private Stage _loginStage;

	/**
	 * Default (empty) constructor for this utility class.
	 */
	public MainApp() {
	}

	@Override
	public void stop() throws Exception {
	}

	/**
	 * TODO comment
	 *
	 * @param initStage
	 *            ...the FX-initial-Stage
	 */
	@Override
	public void start(Stage initStage) {
		URL securityPoliciesURL = getClass().getClassLoader().getResource(PATH_TO_SECURITY_POLICIES_FILE);
		if (securityPoliciesURL != null) {

			System.setProperty(SECURITY_PROPERTY, securityPoliciesURL.toString());
			System.setSecurityManager(new RMISecurityManager());

			/* handle the login */
//			showLoginStage();
            showMainStage(UserRole.MEMBER);

		} else {
			LOGGER.error("Error occurs while starting a client. Security policies were not found.");
		}
	}
	
	private void showLoginStage() {
		ViewLoader<LoginViewController> viewLoader = ViewLoader.loadView(LoginViewController.class);
		Parent loginStage = (Parent) viewLoader.loadNode();
		viewLoader.getController().registerLoginListener(this::showMainStage);
		
		prepareNewStage(loginStage).show();
//		_loginStage.show();
	}


	private void showMainStage(UserRole userRole) {	
		// should close login stage when main stage is opened
		if (_loginStage != null) _loginStage.close();
		
		ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
		Parent mainStage = (Parent) viewLoader.loadNode();
		viewLoader.getController().setUserRole(userRole);
		
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
	 * @param args
	 *            the command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
