package at.sporty.team1.presentation.util;

import at.sporty.team1.presentation.dialogs.LoginDialog;
import at.sporty.team1.shared.api.entity.IDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public class GUIHelper {
    private static final String DEFAULT_SVG_FILL_COLOR = "#575757";
    private static final String YELLOW_BACKGROUND_STYLE = "-fx-control-inner-background: lightgoldenrodyellow";

    public static void highlightNotValidTextField(TextField textField) {
        textField.requestFocus();
        textField.setStyle(YELLOW_BACKGROUND_STYLE);
    }

    // login alert dialog
    public static Optional<Pair<String, String>> showLoginDialog() {
        return new LoginDialog().showAndWait();
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

    public static Optional<ButtonType> showWarningAlert(String context) {
        return showExtendedWarningAlert(null, context);
    }

    public static Optional<ButtonType> showExtendedWarningAlert(String header, String context) {
        return showCustomAlert(
            Alert.AlertType.WARNING,
            "Warning",
            header,
            context
        );
    }

    public static Optional<ButtonType> showInformationAlert(String context) {
        return showExtendedInformationAlert(null, context);
    }

    public static Optional<ButtonType> showExtendedInformationAlert(String header, String context) {
        return showCustomAlert(
            Alert.AlertType.INFORMATION,
            "Information",
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

    public static SVGPath loadSVGGraphic(SVGContainer svg) {
        SVGPath graphic = new SVGPath();
        graphic.setFill(Paint.valueOf(DEFAULT_SVG_FILL_COLOR));
        graphic.setContent(svg.getSVGString());

        return graphic;
    }

    public static <T extends IDTO> StringConverter<T> getDTOToStringConverter(Function<T, String> proxy) {
        return new StringConverter<T>() {
            @Override
            public String toString(T dto) {
                if (dto != null) {
                    return proxy.apply(dto);
                }
                return null;
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        };
    }
}
