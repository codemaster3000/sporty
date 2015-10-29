package at.sporty.team1.presentation.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class NewMemberController implements IJfxController {

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
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fNameTextField.requestFocus();
            }
        });
    }
    
    
    /**
     * Save Form-Method
     * saves the member data into database
     */
    @FXML
    private void saveForm(ActionEvent actionEvent){
    	
    	MemberDTO memberDTO = new MemberDTO();
    	String fName = fNameTextField.getText();
    	String lName = lNameTextField.getText();
    	String bday = birthTextField.getText();
    	String email = emailTextField.getText();
    	String phone = phoneTextField.getText();
    	String address = addressTextField.getText();
    	String sport = sportTextField.getText();
    	String gender = null;
    	boolean filled = true;
    	boolean isSaved = false;
    	
    	//Alert Box if a mandatory field is not filled
    	Alert alert = new Alert(AlertType.INFORMATION);
    	
    	alert.setTitle("Information");
    	alert.setHeaderText("Mandatory field not filled");
    	   	
    	//check if mandatory fields are filled with data
        if(fName.isEmpty()){
            filled = false;
            fNameTextField.requestFocus();
            fNameTextField.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
            alert.setContentText("Please fill in First Name.");
            alert.showAndWait();
            return;
        }
        
        if(lName.isEmpty()){
            filled = false;
            lNameTextField.requestFocus();
            lNameTextField.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
            alert.setContentText("Please fill in Last Name.");
            alert.showAndWait();
            return;
        }
        
        if(bday.isEmpty()){
            filled = false;
            birthTextField.requestFocus();
            birthTextField.setStyle("-fx-control-inner-background: lightgoldenrodyellow");
            alert.setContentText("Please fill in Date of Birth.");
            alert.showAndWait();
            return;
        }
        
        if(radioGenderFemale.isSelected()){
            gender = "female";
        }else if(radioGenderMale.isSelected()){
            gender = "male";
        }else{
            filled = false;
            alert.setContentText("Please choose Gender.");
            alert.showAndWait();
            return;
        }

        if(!filled){
        	alert.setContentText("Please fill in mandatory fields");
            alert.showAndWait();
        }else{
        	
            //save
        }
        
    }
	
}
