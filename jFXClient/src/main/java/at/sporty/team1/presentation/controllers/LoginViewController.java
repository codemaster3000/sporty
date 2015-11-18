package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.util.GUIHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Represents the login view controller.
 */
public class LoginViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;

    private Consumer<UserRole> _roleConsumer;

    /*
     * initialize Controller and add function to hit enter to login
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        tfPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
    }

    public void registerLoginListener(Consumer<UserRole> proxyFunction) {
        if (proxyFunction != null) _roleConsumer = proxyFunction;
    }

    /**
     * login credential check and set screen according to userrole
     *
     * @param event ..Button "Login" is pressed
     */
    @FXML
    private void btnLogIn(ActionEvent event) {
        UserRole loginResult = login();

        if (loginResult != UserRole.UNSUCCESSFUL_LOGIN && _roleConsumer != null) {
            GUIHelper.showSuccessAlert("Login was successful. :)");
            _roleConsumer.accept(login());
        } else {
            GUIHelper.showCustomAlert(AlertType.ERROR, "Login error", null, "Invalid Username or Password.");
        }
    }


    /**
     * login: checks if the login is correct. If it´s correct the users view(=Role) will be loaded
     */
    private UserRole login() {
        try {

            ILoginController controller = CommunicationFacade.lookupForLoginController();

            UserRole userRole = controller.authorize(
                    tfUserName.toString(),
                    tfPassword.toString()
            );

            return userRole;

        } catch (RemoteException re) {
            re.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

         /* else: not authorized, return UNSUCCESSFUL_LOGIN */
        return UserRole.UNSUCCESSFUL_LOGIN;
    }
}



