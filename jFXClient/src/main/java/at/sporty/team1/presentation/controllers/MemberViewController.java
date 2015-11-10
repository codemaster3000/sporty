package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;


public class MemberViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUCCESSFUL_MEMBER_SAVE = "Member was successfully saved.";
    private static final String FEMALE = "F";
    private static final String MALE = "M";

    @FXML private TextField fNameTextField;
    @FXML private TextField lNameTextField;
    @FXML private TextField birthTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField addressTextField;
    @FXML private RadioButton radioGenderFemale;
    @FXML private RadioButton radioGenderMale;
    @FXML private CheckBox memberSportCheckboxSoccer;
    @FXML private CheckBox memberSportCheckboxVolleyball;
    @FXML private CheckBox memberSportCheckboxBaseball;
    @FXML private CheckBox memberSportCheckboxFootball;
    @FXML private ComboBox<TeamDTO> memberTeamComboboxSoccer;
    @FXML private ComboBox<TeamDTO> memberTeamComboboxVolleyball;
    @FXML private ComboBox<TeamDTO> memberTeamComboboxBaseball;
    @FXML private ComboBox<TeamDTO> memberTeamComboboxFootball;

    private static MemberDTO _activeMemberDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup _group = new ToggleGroup();
        radioGenderFemale.setToggleGroup(_group);
        radioGenderMale.setToggleGroup(_group);
        ObservableList<String> values = null;
        Platform.runLater(fNameTextField::requestFocus);
        
       doComboBoxAndCheckBoxInitialization();
    }

    /**
     * Init the Checkboxes and the Comboboxes in MemberView
     */
    private void doComboBoxAndCheckBoxInitialization() {
    	
//    	DepartmentDTO department = null;
//    	TeamDTO team = new TeamDTO();
    	
    	ObservableList<TeamDTO> soccerTeams = null;
    	ObservableList<TeamDTO> volleyballTeams = null;
    	ObservableList<TeamDTO> footballTeams = null;
    	ObservableList<TeamDTO> baseballTeams = null;
    	
		try {
			soccerTeams = (ObservableList<TeamDTO>) CommunicationFacade.lookupForTeamController().searchBySport("soccer");
			volleyballTeams = (ObservableList<TeamDTO>) CommunicationFacade.lookupForTeamController().searchBySport("volleyball");
			footballTeams = (ObservableList<TeamDTO>) CommunicationFacade.lookupForTeamController().searchBySport("football");
			baseballTeams = (ObservableList<TeamDTO>) CommunicationFacade.lookupForTeamController().searchBySport("baseball");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
    	
    	
    	memberTeamComboboxBaseball.setDisable(true);
    	memberTeamComboboxFootball.setDisable(true);
    	memberTeamComboboxSoccer.setDisable(true);
    	memberTeamComboboxVolleyball.setDisable(true);
    	
    	memberSportCheckboxBaseball.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(memberSportCheckboxBaseball.isSelected()){
					memberTeamComboboxBaseball.setDisable(false);
				}else{
					memberTeamComboboxBaseball.setDisable(true);
				}
				
			}	
    	});
    	
    	memberSportCheckboxSoccer.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(memberSportCheckboxSoccer.isSelected()){
					memberTeamComboboxSoccer.setDisable(false);
				}else{
					memberTeamComboboxSoccer.setDisable(true);
				}
				
			}	
    	});
    	
    	memberSportCheckboxVolleyball.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(memberSportCheckboxVolleyball.isSelected()){
					memberTeamComboboxVolleyball.setDisable(false);
				}else{
					memberTeamComboboxVolleyball.setDisable(true);
				}
				
			}	
    	});
    	
    	memberSportCheckboxFootball.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if(memberSportCheckboxFootball.isSelected()){
					memberTeamComboboxFootball.setDisable(false);
				}else{
					memberTeamComboboxFootball.setDisable(true);
				}
				
			}	
    	});
    	
    	memberTeamComboboxBaseball.setItems(baseballTeams);
		memberTeamComboboxFootball.setItems(footballTeams);
		memberTeamComboboxSoccer.setItems(soccerTeams);
		memberTeamComboboxVolleyball.setItems(volleyballTeams);
	}

	@Override
    public void displayDTO(IDTO idto){
        if (idto instanceof MemberDTO) {
            displayMemberDTO((MemberDTO) idto);
        }
    }

    /**
     * Pre-loads data into all view fields.
     * @param memberDTO MemberDTO that will be preloaded.
     */
    private void displayMemberDTO(MemberDTO memberDTO) {
        if (memberDTO != null) {
            _activeMemberDTO = memberDTO;

            fNameTextField.setText(_activeMemberDTO.getFirstName());
            lNameTextField.setText(_activeMemberDTO.getLastName());

            if (FEMALE.equals(_activeMemberDTO.getGender())) {
                radioGenderFemale.setSelected(true);
            } else if (MALE.equals(_activeMemberDTO.getGender())) {
                radioGenderMale.setSelected(true);
            }

            birthTextField.setText(_activeMemberDTO.getDateOfBirth());
            emailTextField.setText(_activeMemberDTO.getEmail());
            addressTextField.setText(_activeMemberDTO.getAddress());
        }
    }

    /**
     * Save Form-Method
     * saves the member data into the database
     */
    @FXML
    private void saveForm(ActionEvent actionEvent) {
        String fName = GUIHelper.readNullOrEmpty(fNameTextField.getText());
        String lName = GUIHelper.readNullOrEmpty(lNameTextField.getText());
        String bday = GUIHelper.readNullOrEmpty(birthTextField.getText());
        String email = GUIHelper.readNullOrEmpty(emailTextField.getText());
        String address = GUIHelper.readNullOrEmpty(addressTextField.getText());
        String phone = GUIHelper.readNullOrEmpty(phoneTextField.getText());


        String gender = null;
        if (radioGenderFemale.isSelected()) {
            gender = FEMALE;
        } else if (radioGenderMale.isSelected()) {
            gender = MALE;
        }

        //check if mandatory fields are filled with data,
        //validation was moved to a separate method for refactoring convenience (IDTO)
        if(isValidForm(fName, lName, bday, gender, address)) {
            try {

                //check if we are creating a new or editing an existing Member
                if (_activeMemberDTO == null) {
                    //this is a new Member
                    _activeMemberDTO = new MemberDTO();
                }

                //if it is an already existing member, changed member data will be simply updated.
                _activeMemberDTO.setFirstName(fName)
                        .setLastName(lName)
                        .setGender(gender)
                        .setDateOfBirth(bday)
                        .setEmail(email)
                        .setAddress(address);
    //TODO
    //                .setDepartmentId(department)
    //                .setTeamId(team)
    //                .setSquad(squad)
    //                .setRole(role)
    //                .setUsername(username);

                IMemberController imc = CommunicationFacade.lookupForMemberController();
                imc.createOrSaveMember(_activeMemberDTO);

                GUIHelper.showSuccessAlert(SUCCESSFUL_MEMBER_SAVE);

                //Logging and cleaning the tab
                LOGGER.info("Member \"{} {}\" was successfully saved.", fName, lName);
                dispose();

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurs while saving the member.", e);
            } catch (ValidationException e) {
            	String context = String.format("Validation exception %s while saving member.", e.getCause());
            	
            	GUIHelper.showValidationAlert(context);
				LOGGER.error(context, e);
			}
        }
    }

    private boolean isValidForm(String fName, String lName, String bday, String gender, String address) {
        //Alert Box if a mandatory field is not filled
        if (fName == null) {
            GUIHelper.highlightNotValidTextField(fNameTextField);
            GUIHelper.showValidationAlert("Please fill in First Name field.");

            return false;
        }

        if (lName == null) {
            GUIHelper.highlightNotValidTextField(lNameTextField);
            GUIHelper.showValidationAlert("Please fill in Last Name field.");

            return false;
        }

        if (bday == null) {
            GUIHelper.highlightNotValidTextField(birthTextField);
            GUIHelper.showValidationAlert("Please fill in Date of Birth field.");

            return false;
        }

        if (gender == null) {
            GUIHelper.showValidationAlert("Please choose Gender.");

            return false;
        }
        
        if (address == null) {
            GUIHelper.highlightNotValidTextField(addressTextField);
            GUIHelper.showValidationAlert("Please fill in Address.");

            return false;
        }

        if (address == null) {
            GUIHelper.showValidationAlert("Please fill in Address field.");

            return false;
        }

        return true;
    }

    @Override
    public void dispose() {
        fNameTextField.clear();
        lNameTextField.clear();
        birthTextField.clear();
        emailTextField.clear();
        phoneTextField.clear();

        addressTextField.clear();
        radioGenderFemale.setSelected(false);
        radioGenderMale.setSelected(false);
    }
}
