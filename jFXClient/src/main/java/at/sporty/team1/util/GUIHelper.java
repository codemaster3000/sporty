package at.sporty.team1.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

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
