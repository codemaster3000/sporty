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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
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
    @FXML private ComboBox<RoleType> roleCombobox;

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
            
            if(!departments.isEmpty() && teamController != null){
            	
            	for(DepartmentDTO actualDepartment : departments) {

                    switch(actualDepartment.getSport()){
                        case "Soccer": {
                            soccerDTO = actualDepartment;

                            List<TeamDTO> resultList = teamController.searchByDepartment(soccerDTO);
                            if (resultList != null) soccerTeams = FXCollections.observableArrayList(resultList);

                            break;
                        }

            			case "Volleyball": {
                            volleyballDTO = actualDepartment;

                            List<TeamDTO> resultList = teamController.searchByDepartment(volleyballDTO);
                            if (resultList != null) volleyballTeams = FXCollections.observableArrayList(resultList);

                            break;
                        }

                        case "Baseball": {
                            baseballDTO = actualDepartment;

                            List<TeamDTO> resultList = teamController.searchByDepartment(baseballDTO);
                            if (resultList != null) baseballTeams = FXCollections.observableArrayList(resultList);

                            break;
                        }

            			case "Football": {
                            footballDTO = actualDepartment;

                            List<TeamDTO> resultList = teamController.searchByDepartment(footballDTO);
                            if (resultList != null) footballTeams = FXCollections.observableArrayList(resultList);

                            break;
                        }
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

            if (memberSportCheckboxBaseball.isSelected()) {
                memberTeamComboBoxBaseball.setDisable(false);
            } else {
                memberTeamComboBoxBaseball.setDisable(true);
            }
        });
    	
    	memberSportCheckboxSoccer.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (memberSportCheckboxSoccer.isSelected()) {
                memberTeamComboBoxSoccer.setDisable(false);
            } else {
                memberTeamComboBoxSoccer.setDisable(true);
            }
        });
    	
    	memberSportCheckboxVolleyball.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (memberSportCheckboxVolleyball.isSelected()) {
                memberTeamComboBoxVolleyball.setDisable(false);
            } else {
                memberTeamComboBoxVolleyball.setDisable(true);
            }
        });
    	
    	memberSportCheckboxFootball.selectedProperty().addListener((observable, oldValue, newValue) -> {

            if (memberSportCheckboxFootball.isSelected()) {
                memberTeamComboBoxFootball.setDisable(false);
            } else {
                memberTeamComboBoxFootball.setDisable(true);
            }
        });
    	
    	/**
    	 * Converters from TeamDTO to Teamname (String)
    	 */  	
    	memberTeamComboBoxBaseball.setConverter(new StringConverter<TeamDTO>() {			
			@Override
			public String toString(TeamDTO object) {
				if(object != null){
					return object.getTeamName();
				}
				return null;
			}
			
			@Override
			public TeamDTO fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	
    	memberTeamComboBoxFootball.setConverter(new StringConverter<TeamDTO>() {			
			@Override
			public String toString(TeamDTO object) {
				if(object != null){
					return object.getTeamName();
				}
				return null;
			}
			
			@Override
			public TeamDTO fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	
    	memberTeamComboBoxSoccer.setConverter(new StringConverter<TeamDTO>() {			
			@Override
			public String toString(TeamDTO object) {
				if(object != null){
					return object.getTeamName();
				}
				return null;
			}
			
			@Override
			public TeamDTO fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	
    	memberTeamComboBoxVolleyball.setConverter(new StringConverter<TeamDTO>() {			
			@Override
			public String toString(TeamDTO object) {
				if(object != null){
					return object.getTeamName();
				}
				return null;
			}
			
			@Override
			public TeamDTO fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
    	  	
    	if (baseballTeams != null) memberTeamComboBoxBaseball.setItems(baseballTeams);
        if (footballTeams != null) memberTeamComboBoxFootball.setItems(footballTeams);
        if (soccerTeams != null) memberTeamComboBoxSoccer.setItems(soccerTeams);
        if (volleyballTeams != null) memberTeamComboBoxVolleyball.setItems(volleyballTeams);
		
		
		/**
		 * Role Combobox
		 */
        roleCombobox.setItems(FXCollections.observableList(Arrays.asList(RoleType.values())));
        roleCombobox.getSelectionModel().select(RoleType.MEMBER);
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
                        .setIsFeePaid(false)
                        .setRole(roleCombobox.getSelectionModel().getSelectedItem()._stringValue);
                
                setDepartementAndTeam(_activeMemberDTO);
    //TODO
//                    .setDepartmentId(department)
//                    .setTeamId(team)
//                    .setSquad(squad)


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

    private void setDepartementAndTeam(MemberDTO _memberDTO) {
		
    	ITeamController teamController = null;
    	TeamDTO teamDTO = null;   
    	 	
    	try {
			teamController = CommunicationFacade.lookupForTeamController();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	if(memberSportCheckboxBaseball.isSelected()){
    		//team holen
    		teamDTO = memberTeamComboBoxBaseball.getSelectionModel().getSelectedItem();
			try {
				teamController.assignMemberToTeam(_memberDTO, teamDTO);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	}
    	if(memberSportCheckboxFootball.isSelected()){
    		//team holen
    		teamDTO = memberTeamComboBoxFootball.getSelectionModel().getSelectedItem();
    		try {
				teamController.assignMemberToTeam(_memberDTO, teamDTO);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(memberSportCheckboxSoccer.isSelected()){
    		//team holen
    		teamDTO = memberTeamComboBoxSoccer.getSelectionModel().getSelectedItem();
    		try {
				teamController.assignMemberToTeam(_memberDTO, teamDTO);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(memberSportCheckboxVolleyball.isSelected()){
    		//team holen
    		teamDTO = memberTeamComboBoxVolleyball.getSelectionModel().getSelectedItem();
    		try {
				teamController.assignMemberToTeam(_memberDTO, teamDTO);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				
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
    
    private enum RoleType {
        MEMBER("Member"),
    	TRAINER("Trainer"),
        DEPARTMENT_HEAD("Department Head"),
        MANAGING_COMMITTEE("Managing Committee");

        private final String _stringValue;

        RoleType(String stringValue) {
            _stringValue = stringValue;
        }

        @Override
        public String toString() {
            return _stringValue;
        }
    }
}
