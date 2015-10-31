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
        return GUIHelper.showAlert(
            Alert.AlertType.INFORMATION,
            "Validation problem",
            "Validation problem occurs.",
            context
        );
    }

    public static Optional<ButtonType> showSuccessAlert(String context) {
        return GUIHelper.showAlert(
            Alert.AlertType.INFORMATION,
            "Success",
            null,
            context
        );
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String context) {
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
