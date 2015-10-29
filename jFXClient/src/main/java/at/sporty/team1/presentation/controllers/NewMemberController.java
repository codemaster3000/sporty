package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.stubs.CommunicationStub;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewMemberController implements IJfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String YELLOW_BACKGROUND_STYLE = "-fx-control-inner-background: lightgoldenrodyellow";

    @FXML private TextField fNameTextField;
    @FXML private TextField lNameTextField;
    @FXML private TextField birthTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField sportTextField;
    @FXML private TextField addressTextField;
    @FXML private RadioButton radioGenderFemale;
    @FXML private RadioButton radioGenderMale;

    private ToggleGroup _group = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioGenderFemale.setToggleGroup(_group);
        radioGenderMale.setToggleGroup(_group);

        Platform.runLater(fNameTextField::requestFocus);
    }


    /**
     * Save Form-Method
     * saves the member data into database
     */
    @FXML
    private void saveForm(ActionEvent actionEvent) {

        String fName = readNullOrEmpty(fNameTextField.getText());
        String lName = readNullOrEmpty(lNameTextField.getText());
        String bday = readNullOrEmpty(birthTextField.getText());
        String email = readNullOrEmpty(emailTextField.getText());
        String phone = readNullOrEmpty(phoneTextField.getText());
        String address = readNullOrEmpty(addressTextField.getText());
        String sport = readNullOrEmpty(sportTextField.getText());
        String gender = null;
        boolean filled = true;
        boolean isSaved = false;

        //Alert Box if a mandatory field is not filled
        //check if mandatory fields are filled with data
        if (fName == null) {
            filled = false;
            fNameTextField.requestFocus();
            fNameTextField.setStyle(YELLOW_BACKGROUND_STYLE);
            showValidationAlert("Please fill in First Name.");
            return;
        }

        if (lName == null) {
            filled = false;
            lNameTextField.requestFocus();
            lNameTextField.setStyle(YELLOW_BACKGROUND_STYLE);
            showValidationAlert("Please fill in Last Name.");
            return;
        }

        if (bday == null) {
            filled = false;
            birthTextField.requestFocus();
            birthTextField.setStyle(YELLOW_BACKGROUND_STYLE);
            showValidationAlert("Please fill in Date of Birth.");
            return;
        }

        if (radioGenderFemale.isSelected()) {
            gender = "female";
        } else if (radioGenderMale.isSelected()) {
            gender = "male";
        } else {
            filled = false;
            showValidationAlert("Please choose Gender.");
            return;
        }

        //FIXME: filled var is always true;
        if (filled) {

            //save
            try {

                IMemberController controller = CommunicationFacade.lookupForStub(CommunicationStub.MEMBER_CONTROLLER);
                controller.createNewMember(
                    fName,
                    lName,
                    bday,
                    email,
                    phone,
                    address,
                    sport,
                    gender
                );

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                //TODO handling
                LOGGER.error(e);
            }

        } else {
            showValidationAlert("Please fill in mandatory fields");
        }
    }

    private Optional<ButtonType> showValidationAlert(String context) {
        return showAlert(AlertType.INFORMATION, "Information", "Mandatory field not filled", context);
    }

    private Optional<ButtonType> showAlert(AlertType type, String title, String header, String context) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        return alert.showAndWait();
    }

    private String readNullOrEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
