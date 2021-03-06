package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.facades.api.IDepartmentControllerUniversal;
import at.sporty.team1.communication.facades.api.IMemberControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.presentation.controllers.core.EditViewController;
import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;


public class MemberEditViewController extends EditViewController<MemberDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final CommunicationFacade COMMUNICATION_FACADE = CommunicationFacade.getInstance();
    private static final Map<String, DepartmentDTO> SPORT_MAP = new HashMap<>();
    private static final String VIEW_TEXT_HEADER = "MEMBER EDITING VIEW";
    private static final String SUCCESSFUL_MEMBER_SAVE = "Member was successfully saved.";
    private static final String SPORT_VOLLEYBALL = "Volleyball";
    private static final String SPORT_FOOTBALL = "Football";
    private static final String SPORT_BASEBALL = "Baseball";
    private static final String SPORT_SOCCER = "Soccer";
    private static final String ROLE_MEMBER = "member";
    private static final String ROLE_TRAINER = "trainer";
    private static final String ROLE_DEPARTMENT_HEAD = "departmentHead";
    private static final String ROLE_ADMIN = "admin";

    private static final String FEMALE = "F";
    private static final String MALE = "M";

    @FXML private TextField fNameTextField;
    @FXML private TextField lNameTextField;
    @FXML private TextField birthTextField;
    @FXML private TextField emailTextField;
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
    @FXML private ComboBox<RoleType> roleComboBox;

    private static MemberDTO _activeMemberDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup _group = new ToggleGroup();
        radioGenderFemale.setToggleGroup(_group);
        radioGenderMale.setToggleGroup(_group);
        Platform.runLater(fNameTextField::requestFocus);
        
        doComboBoxAndCheckBoxInitialization();
    }

    @Override
    public String getHeaderText() {
        return VIEW_TEXT_HEADER;
    }

    /**
     * Init the Checkboxes and the Comboboxes in MemberView
     */
    private void doComboBoxAndCheckBoxInitialization() {

        /**
         * Setting checkbox listeners for comboBoxes
         */

        memberSportCheckboxBaseball.selectedProperty().addListener(
            (observable, oldValue, newValue) -> memberTeamComboBoxBaseball.setDisable(oldValue)
        );

        memberSportCheckboxSoccer.selectedProperty().addListener(
            (observable, oldValue, newValue) -> memberTeamComboBoxSoccer.setDisable(oldValue)
        );

        memberSportCheckboxVolleyball.selectedProperty().addListener(
            (observable, oldValue, newValue) -> memberTeamComboBoxVolleyball.setDisable(oldValue)
        );

        memberSportCheckboxFootball.selectedProperty().addListener(
            (observable, oldValue, newValue) -> memberTeamComboBoxFootball.setDisable(oldValue)
        );

        /**
         * Setting Converter from TeamDTO to Team name (String)
         */
        StringConverter<TeamDTO> teamDTOConverter = GUIHelper.getDTOToStringConverter(TeamDTO::getTeamName);
        memberTeamComboBoxBaseball.setConverter(teamDTOConverter);
        memberTeamComboBoxFootball.setConverter(teamDTOConverter);
        memberTeamComboBoxSoccer.setConverter(teamDTOConverter);
        memberTeamComboBoxVolleyball.setConverter(teamDTOConverter);

        /**
         * Role Combobox
         */
        roleComboBox.setItems(FXCollections.observableList(Arrays.asList(RoleType.values())));
        roleComboBox.getSelectionModel().select(RoleType.MEMBER);

        /**
         * Sportdepartment and Teams loading
         */

        //starting as a new thread for faster user feedback
        new Thread(() -> {

            try {

                IDepartmentControllerUniversal departmentController = COMMUNICATION_FACADE.lookupForDepartmentController();
                List<DepartmentDTO> departments = departmentController.searchAllDepartments();

                if (!departments.isEmpty()) {

                    for (DepartmentDTO actualDepartment : departments) {
                        Integer departmentId = actualDepartment.getDepartmentId();

                        switch (actualDepartment.getSport()) {
                            case SPORT_SOCCER: {
                                SPORT_MAP.put(SPORT_SOCCER, actualDepartment);

                                List<TeamDTO> resultList = departmentController.loadDepartmentTeams(departmentId);

                                if (resultList != null) {

                                    //loading values to comboBox
                                    Platform.runLater(() -> memberTeamComboBoxSoccer.setItems(
                                        FXCollections.observableList(resultList)
                                    ));
                                }
                                break;
                            }

                            case SPORT_VOLLEYBALL: {
                                SPORT_MAP.put(SPORT_VOLLEYBALL, actualDepartment);

                                List<TeamDTO> resultList = departmentController.loadDepartmentTeams(departmentId);

                                if (resultList != null) {

                                    //loading values to comboBox
                                    Platform.runLater(() -> memberTeamComboBoxVolleyball.setItems(
                                        FXCollections.observableList(resultList)
                                    ));
                                }
                                break;
                            }

                            case SPORT_BASEBALL: {
                                SPORT_MAP.put(SPORT_BASEBALL, actualDepartment);

                                List<TeamDTO> resultList = departmentController.loadDepartmentTeams(departmentId);

                                if (resultList != null) {

                                    //loading values to comboBox
                                    Platform.runLater(() -> memberTeamComboBoxBaseball.setItems(
                                        FXCollections.observableList(resultList)
                                    ));
                                }
                                break;
                            }

                            case SPORT_FOOTBALL: {
                                SPORT_MAP.put(SPORT_FOOTBALL, actualDepartment);

                                List<TeamDTO> resultList = departmentController.loadDepartmentTeams(departmentId);

                                if (resultList != null) {

                                    //loading values to comboBox
                                    Platform.runLater(() -> memberTeamComboBoxFootball.setItems(
                                        FXCollections.observableList(resultList)
                                    ));
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (RemoteCommunicationException | UnknownEntityException e) {
                LOGGER.error("Error occurred while loading all Departments and their Teams.", e);
            }
        }).start();
	}

    @Override
    public void loadDTO(MemberDTO memberDTO) {
        IN_WORK_PROPERTY.set(true);

        _activeMemberDTO = memberDTO;
        displayMemberDTO(memberDTO);
    }

    /**
     * Pre-loads data into all view fields.
     * @param memberDTO MemberDTO that will be preloaded.
     */
    private void displayMemberDTO(MemberDTO memberDTO) {

        if (memberDTO != null) {
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

            try {

                IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();

                List<DepartmentDTO> departments = memberController.loadMemberDepartments(
                    _activeMemberDTO.getMemberId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );

                if (departments != null) {

                    for (DepartmentDTO department : departments) {
                        switch (department.getSport()) {
                            case SPORT_VOLLEYBALL: {
                                Platform.runLater(() -> memberSportCheckboxVolleyball.setSelected(true));
                                break;
                            }

                            case SPORT_FOOTBALL: {
                                Platform.runLater(() -> memberSportCheckboxFootball.setSelected(true));
                                break;
                            }

                            case SPORT_BASEBALL: {
                                Platform.runLater(() -> memberSportCheckboxBaseball.setSelected(true));
                                break;
                            }

                            case SPORT_SOCCER: {
                                Platform.runLater(() -> memberSportCheckboxSoccer.setSelected(true));
                                break;
                            }
                        }
                    }
                }


                List<TeamDTO> teams = memberController.loadMemberTeams(
                    _activeMemberDTO.getMemberId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );

                if (teams != null) {

                    for (TeamDTO team : teams) {

                        DepartmentDTO department = COMMUNICATION_FACADE.lookupForTeamController().loadTeamDepartment(
                            team.getTeamId(),
                            COMMUNICATION_FACADE.getActiveSession()
                        );

                        if (department != null) {
                            switch (department.getSport()) {
                                case SPORT_VOLLEYBALL: {
                                    Platform.runLater(() -> {
                                        memberSportCheckboxVolleyball.setSelected(true);
                                        memberTeamComboBoxVolleyball.setDisable(false);
                                        memberTeamComboBoxVolleyball.getSelectionModel().select(team);
                                    });

                                    break;
                                }

                                case SPORT_FOOTBALL: {
                                    Platform.runLater(() -> {
                                        memberSportCheckboxFootball.setSelected(true);
                                        memberTeamComboBoxFootball.setDisable(false);
                                        memberTeamComboBoxFootball.getSelectionModel().select(team);
                                    });

                                    break;
                                }

                                case SPORT_BASEBALL: {
                                    Platform.runLater(() -> {
                                        memberSportCheckboxBaseball.setSelected(true);
                                        memberTeamComboBoxBaseball.setDisable(false);
                                        memberTeamComboBoxBaseball.getSelectionModel().select(team);
                                    });

                                    break;
                                }

                                case SPORT_SOCCER: {
                                    Platform.runLater(() -> {
                                        memberSportCheckboxSoccer.setSelected(true);
                                        memberTeamComboBoxSoccer.setDisable(false);
                                        memberTeamComboBoxSoccer.getSelectionModel().select(team);
                                    });

                                    break;
                                }
                            }
                        }
                    }
                }

            } catch (RemoteCommunicationException | UnknownEntityException e) {
                LOGGER.error("Error occurred while loading Member data (Departments and Teams).", e);
            } catch (NotAuthorisedException e) {
                String context = "Client load (Departments and Teams) request was rejected. Not enough permissions.";

                LOGGER.error(context, e);
                Platform.runLater(() -> GUIHelper.showErrorAlert(context));
            }


            String role = _activeMemberDTO.getRole();

            if (role != null) {

	            switch (role) {
                    case ROLE_MEMBER: {
                        roleComboBox.getSelectionModel().select(RoleType.MEMBER);
                        break;
                    }

                    case ROLE_TRAINER: {
                        roleComboBox.getSelectionModel().select(RoleType.TRAINER);
                        break;
                    }

                    case ROLE_DEPARTMENT_HEAD: {
                        roleComboBox.getSelectionModel().select(RoleType.DEPARTMENT_HEAD);
                        break;
                    }

                    case ROLE_ADMIN: {
                        roleComboBox.getSelectionModel().select(RoleType.ADMIN);
                        break;
                    }
	            }
            }
        }

        IN_WORK_PROPERTY.set(false);
    }

    /**
     * Save Form-Method
     * saves the member data to the data store
     */
    @Override
    public MemberDTO saveDTO() {

        String fName = GUIHelper.readNullOrEmpty(fNameTextField.getText());
        String lName = GUIHelper.readNullOrEmpty(lNameTextField.getText());
        String dateOfBirth = GUIHelper.readNullOrEmpty(birthTextField.getText());
        String email = GUIHelper.readNullOrEmpty(emailTextField.getText());
        String address = GUIHelper.readNullOrEmpty(addressTextField.getText());
        String gender = null;
        
        if (radioGenderFemale.isSelected()) {
            gender = FEMALE;
        } else if (radioGenderMale.isSelected()) {
            gender = MALE;
        }

        //check if mandatory fields are filled with data,
        //validation was moved to a separate method for refactoring convenience (IDTO)
        if(!IN_WORK_PROPERTY.get() && isValidForm(fName, lName, dateOfBirth, gender, address)) {

            //check if we are creating a new or editing an existing Member
            if (_activeMemberDTO == null) {
                //this is a new Member
                _activeMemberDTO = new MemberDTO();
            }

            try {

                //if it is an already existing member, changed member data will be simply updated.
                _activeMemberDTO.setFirstName(fName)
                        .setLastName(lName)
                        .setGender(gender)
                        .setDateOfBirth(dateOfBirth)
                        .setEmail(email)
                        .setAddress(address)
                        .setIsFeePaid(false)
                        .setRole(roleComboBox.getSelectionModel().getSelectedItem().getDSValue());

                IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();
                Integer memberId = memberController.createOrSaveMember(
                    _activeMemberDTO,
                    COMMUNICATION_FACADE.getActiveSession()
                );

                _activeMemberDTO.setMemberId(memberId);
                
                saveOrUpdateMemberDepartments(memberId);
                saveOrUpdateMemberTeams(memberId);

                //FIXME we will get success message even if teams and departments will be not assigned
                GUIHelper.showSuccessAlert(SUCCESSFUL_MEMBER_SAVE);

                //Logging and cleaning the tab
                LOGGER.info("Member \"{} {}\" was successfully saved.", fName, lName);

                return _activeMemberDTO;

            } catch (RemoteCommunicationException e) {
                LOGGER.error("Error occurred while saving the member.", e);
            } catch (ValidationException e) {
            	String context = String.format("Validation exception \"%s\" while saving member.", e.getCause());
            	
            	GUIHelper.showValidationAlert(context);
				LOGGER.error(context, e);
			} catch (NotAuthorisedException e) {
                String context = "Client save (Member) request was rejected. Not enough permissions.";

                LOGGER.error(context, e);
                GUIHelper.showErrorAlert(context);
            }
        }

        return null;
    }

    private void saveOrUpdateMemberDepartments(Integer memberId) {
        try {
            IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();

            if (memberSportCheckboxBaseball.isSelected()) {
                //Saving department for member
                memberController.assignMemberToDepartment(
                    memberId,
                    SPORT_MAP.get(SPORT_BASEBALL).getDepartmentId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );
            }

            if (memberSportCheckboxFootball.isSelected()) {
                //Saving department for member
                memberController.assignMemberToDepartment(
                    memberId,
                    SPORT_MAP.get(SPORT_FOOTBALL).getDepartmentId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );
            }

            if (memberSportCheckboxSoccer.isSelected()) {
                //Saving department for member
                memberController.assignMemberToDepartment(
                    memberId,
                    SPORT_MAP.get(SPORT_SOCCER).getDepartmentId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );
            }

            if (memberSportCheckboxVolleyball.isSelected()) {
                //Saving department for member
                memberController.assignMemberToDepartment(
                    memberId,
                    SPORT_MAP.get(SPORT_VOLLEYBALL).getDepartmentId(),
                    COMMUNICATION_FACADE.getActiveSession()
                );
            }

        } catch (RemoteCommunicationException e) {
            LOGGER.error("Error occurred while assigning Member to Department.", e);
        } catch (UnknownEntityException e) {
            LOGGER.error("DTO was not saved in Data Storage before assigning Member to Department.", e);
        } catch (NotAuthorisedException e) {
            String context = "Client save (Member Departments) request was rejected. Not enough permissions.";

            LOGGER.error(context, e);
            GUIHelper.showErrorAlert(context);
        }
    }

    private void saveOrUpdateMemberTeams(Integer memberId) {
        try {

            IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();

            if (memberSportCheckboxBaseball.isSelected()) {
                //Baseball team holen
                TeamDTO teamDTO = memberTeamComboBoxBaseball.getSelectionModel().getSelectedItem();

                //Saving team if available;
                if (teamDTO != null) {
                    memberController.assignMemberToTeam(
                        memberId,
                        teamDTO.getTeamId(),
                        COMMUNICATION_FACADE.getActiveSession()
                    );
                }
            }

            if (memberSportCheckboxFootball.isSelected()) {
                //Football team holen
                TeamDTO teamDTO = memberTeamComboBoxFootball.getSelectionModel().getSelectedItem();

                //Saving team if available;
                if (teamDTO != null) {
                    memberController.assignMemberToTeam(
                        memberId,
                        teamDTO.getTeamId(),
                        COMMUNICATION_FACADE.getActiveSession()
                    );
                }
            }

            if (memberSportCheckboxSoccer.isSelected()) {
                //Soccer team holen
                TeamDTO teamDTO = memberTeamComboBoxSoccer.getSelectionModel().getSelectedItem();

                //Saving team if available;
                if (teamDTO != null) {
                    memberController.assignMemberToTeam(
                        memberId,
                        teamDTO.getTeamId(),
                        COMMUNICATION_FACADE.getActiveSession()
                    );
                }
            }

            if (memberSportCheckboxVolleyball.isSelected()) {
                //Volleyball team holen
                TeamDTO teamDTO = memberTeamComboBoxVolleyball.getSelectionModel().getSelectedItem();

                //Saving team if available;
                if (teamDTO != null) {
                    memberController.assignMemberToTeam(
                        memberId,
                        teamDTO.getTeamId(),
                        COMMUNICATION_FACADE.getActiveSession()
                    );
                }
            }

        } catch (RemoteCommunicationException e) {
            LOGGER.error("Error occurred while assigning Member to Team.", e);
        } catch (UnknownEntityException e) {
            LOGGER.error("DTO was not saved in Data Storage before assigning Member to Team.", e);
        } catch (NotAuthorisedException e) {
            String context = "Client save (Team Members) request was rejected. Not enough permissions.";

            LOGGER.error(context, e);
            GUIHelper.showErrorAlert(context);
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
        addressTextField.clear();

        radioGenderFemale.setSelected(false);
        radioGenderMale.setSelected(false);

        memberSportCheckboxBaseball.setSelected(false);
        memberSportCheckboxFootball.setSelected(false);
        memberSportCheckboxSoccer.setSelected(false);
        memberSportCheckboxVolleyball.setSelected(false);

        memberTeamComboBoxBaseball.setDisable(true);
        memberTeamComboBoxFootball.setDisable(true);
        memberTeamComboBoxSoccer.setDisable(true);
        memberTeamComboBoxVolleyball.setDisable(true);

        memberTeamComboBoxSoccer.getSelectionModel().select(null);
        memberTeamComboBoxSoccer.setValue(null);

        memberTeamComboBoxVolleyball.getSelectionModel().select(null);
        memberTeamComboBoxSoccer.setValue(null);

        memberTeamComboBoxBaseball.getSelectionModel().select(null);
        memberTeamComboBoxSoccer.setValue(null);

        memberTeamComboBoxFootball.getSelectionModel().select(null);
        memberTeamComboBoxSoccer.setValue(null);

        roleComboBox.getSelectionModel().select(null);
        roleComboBox.setValue(null);
    }

    private enum RoleType {
        MEMBER(ROLE_MEMBER, "Member"),
    	TRAINER(ROLE_TRAINER, "Trainer"),
        DEPARTMENT_HEAD(ROLE_DEPARTMENT_HEAD, "Department Head"),
        ADMIN(ROLE_ADMIN, "Admin");

        private final String _dsValue;
        private final String _guiValue;

        RoleType(String dsValue, String guiValue) {
            _dsValue = dsValue;
            _guiValue = guiValue;
        }

        public String getDSValue() {
            return _dsValue;
        }

        @Override
        public String toString() {
            return _guiValue;
        }
    }
}
