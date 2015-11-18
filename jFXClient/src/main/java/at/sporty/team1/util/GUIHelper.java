package at.sporty.team1.util;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public class GUIHelper {
    private static final String YELLOW_BACKGROUND_STYLE = "-fx-control-inner-background: lightgoldenrodyellow";

    public static void highlightNotValidTextField(TextField textField) {
        textField.requestFocus();
        textField.setStyle(YELLOW_BACKGROUND_STYLE);
    }

    // login alert window
    public static Optional<Pair<String, String>> showLoginForm() {
    	
    	// Create the custom dialog.
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
    	dialog.setTitle("Sporty Login");
    	dialog.setHeaderText("Please enter your credentials.");

    	// Set the button types.
    	ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

    	// Create the username and password labels and fields.
    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	TextField username = new TextField();
    	username.setPromptText("Username");
    	PasswordField password = new PasswordField();
    	password.setPromptText("Password");

    	grid.add(new Label("Username:"), 0, 0);
    	grid.add(username, 1, 0);
    	grid.add(new Label("Password:"), 0, 1);
    	grid.add(password, 1, 1);

    	// Enable/Disable login button depending on whether a username was entered.
    	Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
    	loginButton.setDisable(true);

    	// Do some validation (using the Java 8 lambda syntax).
    	username.textProperty().addListener((observable, oldValue, newValue) -> {
    	    loginButton.setDisable(newValue.trim().isEmpty());
    	});

    	dialog.getDialogPane().setContent(grid);

    	// Request focus on the username field by default.
    	Platform.runLater(() -> username.requestFocus());

    	// Convert the result to a username-password-pair when the login button is clicked.
    	dialog.setResultConverter(dialogButton -> {
    	    if (dialogButton == loginButtonType) {
    	        return new Pair<>(username.getText(), password.getText());
    	    }
    	    return null;
    	});

    	return dialog.showAndWait();    	
    }
    
    public static Optional<ButtonType> showValidationAlert(String context) {
        return showExtendedValidationAlert(null, context);
    }

    public static Optional<ButtonType> showExtendedValidationAlert(String header, String context) {
        return showCustomAlert(
            Alert.AlertType.WARNING,
            "Validation problem",
            header,
            context
        );
    }
    
    public static Optional<ButtonType> showSuccessAlert(String context) {
        return showExtendedSuccessAlert(null, context);
    }

    public static Optional<ButtonType> showExtendedSuccessAlert(String header, String context) {
        return showCustomAlert(
            Alert.AlertType.INFORMATION,
            "Success",
            header,
            context
        );
    }

    public static Optional<ButtonType> showErrorAlert(String context) {
        return showExtendedErrorAlert(null, context);
    }

    public static Optional<ButtonType> showExtendedErrorAlert(String header, String context) {
        return showCustomAlert(
            Alert.AlertType.ERROR,
            "Error",
            header,
            context
        );
    }

    public static Optional<ButtonType> showCustomAlert(Alert.AlertType type, String title, String header, String context) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        return alert.showAndWait();
    }

    public static String readNullOrEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
    
}
