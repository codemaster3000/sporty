package at.sporty.team1.presentation.dialogs;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.LoginMaskViewController;
import at.sporty.team1.util.GUIHelper;
import at.sporty.team1.util.SVGContainer;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Pair;

public class LoginDialog extends Dialog<Pair<String, String>> {
    private static final String DIALOG_TITLE = "Sporty Login";
    private static final String DIALOG_HEADER = "Please enter your credentials.";
    private static final String BUTTON_LOGIN_CAPTION = "Login";

    public LoginDialog() {
        prepareDialog();
    }

    private void prepareDialog() {
        setTitle(DIALOG_TITLE);
        setHeaderText(DIALOG_HEADER);
        setGraphic(GUIHelper.loadSVGGraphic(SVGContainer.LOGIN_ICON));

        //loading login mask
        ViewLoader<LoginMaskViewController> viewLoader = ViewLoader.loadView(LoginMaskViewController.class);
        Node content = viewLoader.loadNode();
        getDialogPane().setContent(content);

        LoginMaskViewController loginMaskController = viewLoader.getController();

        //preparing buttons for dialog
        ButtonType loginButtonType = new ButtonType(BUTTON_LOGIN_CAPTION, ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        //disabling login button at the start
        Node loginButton = getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        //attaching listener to username field (automatically disables login button when user name is empty)
        loginMaskController.getObservableUsernameProperty().addListener((o, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        //Setting focus to username property
        loginMaskController.setFocusToUsernameField();

        //defining the result converter for dialog return type
        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(
                    loginMaskController.getUsernameFieldValue(),
                    loginMaskController.getPasswordFieldValue()
                );
            }
            return null;
        });
    }
}



