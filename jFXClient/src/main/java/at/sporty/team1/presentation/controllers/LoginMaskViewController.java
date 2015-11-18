package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.JfxController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Represents the login mask view controller.
 */
public class LoginMaskViewController extends JfxController {

    @FXML
    private TextField _usernameField;
    @FXML
    private PasswordField _passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _usernameField.requestFocus();
    }

    public StringProperty getObservableUsernameProperty() {
        return _usernameField.textProperty();
    }

    public String getUsernameFieldValue() {
        return _usernameField.getText();
    }

    public String getPasswordFieldValue() {
        return _passwordField.getText();
    }
}
