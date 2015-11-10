package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * Represents the login view controller.
 */
public class LoginViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static MemberDTO loginUser;

    /**
     * FXML
     */

    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;

    /**
     * Controller
     */
//    private MainController _mainController;
    private ViewLoader viewLoader;
    private ILoginController loginController;
    private MainViewController mainViewController;

//    //TODO
//    @Override
//    public void setScreenParent(ViewLoader screenPage) {
//        viewLoader = screenPage;
//    }

    /*
     * initialize Controller and add function to hit enter to login
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            ILoginController loginController = CommunicationFacade.lookupForLoginController();

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }

//        _mainController = MainController.getInstance();
//        _loginController = LoginController.getInstance();
        tfPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    login();
                }
            }
        });
    }


    /**
     * login credential check and set screen according to userrole
     *
     * @param event ..Button "Login" is pressed
     */
    public void btnLogIn(ActionEvent event) {
        login();
    }

    /*
     * login: checks if the login is correct. If it´s correct the receptionist view will be loaded
     */
    private void login() {
        // TODO set correct screens

        //workaround for empty login --- fuuuuu, autorize requires this to be NOT "" ...... :<
//        if (tfPassword.getText().equals("")&&tfUserName.getText().equals(""))
//            _screenController.setScreen(ScreensFramework.screen2ID);

        // user
//        if (loginController.authorize(tfUserName.getText(), tfPassword.getText()) == 0)
//            mainViewController.setScreen(viewLoade.screen2ID);
//
////        // abteilungsleiter
////        if (_loginController.authorize(tfUserName.getText(), tfPassword.getText()) == 1)
////            _screenController.setScreen(ScreensFramework.screen3ID); // TODO set correct screen
//
////        // trainer
////        if(_loginController.authorize(tfUserName.getText(), tfPassword.getText()) == 2)
////             _screenController.setScreen(ScreensFramework.screen2ID); // TODO set correct screen
////
//        // login unsucessfull
//        if (loginController.authorize(tfUserName.getText(), tfPassword.getText()) == -1)
//            mainViewController.setScreen(ScreensFramework.screen1ID);
//    }
//
//    public void minimize(ActionEvent actionEvent) {
//        mainViewController.getPrimaryStage().setIconified(true);
//    }
//
//    public void closeApp(ActionEvent actionEvent) {
//        mainViewController.getPrimaryStage().close();
//    }
    }

}

