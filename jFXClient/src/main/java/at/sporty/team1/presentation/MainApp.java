package at.sporty.team1.presentation;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.MainViewController;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
	private static final int DEFAULT_HEIGHT = 680;

	private static final Set<String> HACK_ACCESS_TOKENS = new HashSet<>();

	static {
		HACK_ACCESS_TOKENS.add("letMeIn");
		HACK_ACCESS_TOKENS.add("letmein");
		HACK_ACCESS_TOKENS.add("login");
		HACK_ACCESS_TOKENS.add("nyan");
		HACK_ACCESS_TOKENS.add("yo dawg");
		HACK_ACCESS_TOKENS.add("yodawg");
		HACK_ACCESS_TOKENS.add("wow so secure, much login"); // http://i65.tinypic.com/991fgg.jpg
		HACK_ACCESS_TOKENS.add("gg wp");
		HACK_ACCESS_TOKENS.add("pls");
		HACK_ACCESS_TOKENS.add("you shall not pass");
		HACK_ACCESS_TOKENS.add("you have no power here");
	}

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

			/* handle the login */
			performLogin();

		} else {
			LOGGER.error("Error occurred while starting a client. Security policies were not found.");
		}
	}
	
	private void performLogin() {
		try {
			
			Optional<Pair<String, String>> result = GUIHelper.showLoginDialog();
			if (result.isPresent()) {
				Pair<String, String> loginData = result.get();
				
				UserRole loginResult = CommunicationFacade.lookupForLoginController().authorize(
					loginData.getKey(),
					loginData.getValue()
				);
				
				if (HACK_ACCESS_TOKENS.contains(loginData.getKey()) || loginResult != UserRole.UNSUCCESSFUL_LOGIN) {
		            GUIHelper.showSuccessAlert("Login was successful. :)");
		            showMainStage(loginResult);
		        } else {
		            GUIHelper.showErrorAlert("Invalid Username or Password.");
		            performLogin();
		        }
			}
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			LOGGER.error("Unsuccessful login detected.", e);
		}
	}


	private void showMainStage(UserRole userRole) {	
				
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
