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
import at.sporty.team1.util.Roles;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
    @FXML private ComboBox<TeamDTO> memberTeamComboBoxSoccer;
    @FXML private ComboBox<TeamDTO> memberTeamComboBoxVolleyball;
    @FXML private ComboBox<TeamDTO> memberTeamComboBoxBaseball;
    @FXML private ComboBox<TeamDTO> memberTeamComboBoxFootball;
    @FXML private ComboBox<String> roleComboBox;

    private static MemberDTO _activeMemberDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup _group = new ToggleGroup();
        radioGenderFemale.setToggleGroup(_group);
        radioGenderMale.setToggleGroup(_group);
        Platform.runLater(fNameTextField::requestFocus);
        
        doComboBoxAndCheckBoxInitialization();
    }

    /**
     * Init the Checkboxes and the Comboboxes in MemberView
     */
    private void doComboBoxAndCheckBoxInitialization() {
    	
    	List<DepartmentDTO> departments = null;
    	ObservableList<TeamDTO> soccerTeams = null;
    	ObservableList<TeamDTO> volleyballTeams = null;
    	ObservableList<TeamDTO> footballTeams = null;
    	ObservableList<TeamDTO> baseballTeams = null;
    	DepartmentDTO soccerDTO = null;
    	DepartmentDTO volleyballDTO = null;
    	DepartmentDTO footballDTO = null;
    	DepartmentDTO baseballDTO = null;
    	ITeamController teamController = null;
    	IDepartmentController departmentController = null;
    	
    	ObservableList<String> roles = FXCollections.observableArrayList("Trainer", "Department Head", "Managing Committee");
    	
    	/**
    	 * Sportdepartment and Teams
    	 */
		try {
			teamController = CommunicationFacade.lookupForTeamController();
		} catch (RemoteException | MalformedURLException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		try {
           
            departmentController = CommunicationFacade.lookupForDepartmentController();         
            departments = departmentController.searchAllDepartments();
            
            if(!departments.isEmpty()){
            	
            	while(departments.iterator().hasNext()){
            		DepartmentDTO actualDepartment = departments.iterator().next();
            		
            		switch(actualDepartment.getSport()){
            			case "Soccer": 
            					soccerDTO = actualDepartment;
            					soccerTeams = FXCollections.observableList(teamController.searchByDepartment(soccerDTO));
            					break;
            			case "Volleyball": 
            					volleyballDTO = actualDepartment;
            					volleyballTeams = FXCollections.observableList(teamController.searchByDepartment(volleyballDTO));
            					break;
            			case "Baseball": 
            					baseballDTO = actualDepartment;
            					baseballTeams = FXCollections.observableList(teamController.searchByDepartment(baseballDTO));
            					break;
            			case "Football": 
            					footballDTO = actualDepartment;
            					footballTeams = FXCollections.observableList(teamController.searchByDepartment(footballDTO));
            					break;
            			default: break;
            		}
            	}
            }
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  	
    	memberTeamComboBoxBaseball.setDisable(true);
    	memberTeamComboBoxFootball.setDisable(true);
    	memberTeamComboBoxSoccer.setDisable(true);
    	memberTeamComboBoxVolleyball.setDisable(true);
    	
    	memberSportCheckboxBaseball.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if(memberSportCheckboxBaseball.isSelected()){
                memberTeamComboBoxBaseball.setDisable(false);
            }else{
                memberTeamComboBoxBaseball.setDisable(true);
            }
        });
    	
    	memberSportCheckboxSoccer.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if(memberSportCheckboxSoccer.isSelected()){
                memberTeamComboBoxSoccer.setDisable(false);
            }else{
                memberTeamComboBoxSoccer.setDisable(true);
            }
        });
    	
    	memberSportCheckboxVolleyball.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if(memberSportCheckboxVolleyball.isSelected()){
                memberTeamComboBoxVolleyball.setDisable(false);
            }else{
                memberTeamComboBoxVolleyball.setDisable(true);
            }
        });
    	
    	memberSportCheckboxFootball.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if(memberSportCheckboxFootball.isSelected()){
                memberTeamComboBoxFootball.setDisable(false);
            }else{
                memberTeamComboBoxFootball.setDisable(true);
            }
        });
    	
    	memberTeamComboBoxBaseball.setItems(baseballTeams);
		memberTeamComboBoxFootball.setItems(footballTeams);
		memberTeamComboBoxSoccer.setItems(soccerTeams);
		memberTeamComboBoxVolleyball.setItems(volleyballTeams);
		
		
		/**
		 * Role Combobox
		 */
		roleComboBox.setItems(roles);
		
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
                        .setAddress(address)
                        .setIsFeePaid(false);
    //TODO
//                    .setDepartmentId(department)
//                    .setTeamId(team)
//                    .setSquad(squad)
//                    .setRole(role);

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
