package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.dialogs.EditViewDialog;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.DTOPair;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class MemberReadOnlyViewController extends ConsumerViewController<MemberDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NOT_AVAILABLE = "N/A";
    private static final SimpleBooleanProperty EDIT_VISIBILITY_PROPERTY = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty CREATE_VISIBILITY_PROPERTY = new SimpleBooleanProperty(false);
    private static final SimpleObjectProperty<MemberDTO> ACTIVE_DTO = new SimpleObjectProperty<>();

    @FXML private Label _lastName;
    @FXML private Label _firstName;
    @FXML private Label _dateOfBirth;
    @FXML private Label _gender;
    @FXML private Label _address;
    @FXML private Label _email;
    @FXML private TableView<DTOPair<DepartmentDTO, TeamDTO>> _departmentTeamTable;
    @FXML private TableColumn<DTOPair<DepartmentDTO, TeamDTO>, String> _departmentColumn;
    @FXML private TableColumn<DTOPair<DepartmentDTO, TeamDTO>, String> _teamColumn;
    @FXML private Label _role;
    @FXML private Button _editMemberButton;
    @FXML private Button _createMemberButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	_createMemberButton.visibleProperty().bind(CREATE_VISIBILITY_PROPERTY);
        _editMemberButton.visibleProperty().bind(EDIT_VISIBILITY_PROPERTY.and(ACTIVE_DTO.isNotNull()));

        if (CommunicationFacade.getExtendedActiveSession() != null) {
            String role = CommunicationFacade.getExtendedActiveSession().getUser().getRole();

            //enabling gui options for specific roles
            switch (role) {
                case "departmentHead": {
                    EDIT_VISIBILITY_PROPERTY.set(true);
                    CREATE_VISIBILITY_PROPERTY.set(true);
                    break;
                }

                case "admin": {
                    EDIT_VISIBILITY_PROPERTY.set(true);
                    CREATE_VISIBILITY_PROPERTY.set(true);
                    break;
                }
            }
        }

        _departmentTeamTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        _departmentColumn.setCellValueFactory(dto -> {
            DTOPair<DepartmentDTO, TeamDTO> dtoPair = dto.getValue();

            if (dtoPair != null && dtoPair.getDTO1() != null) {
                DepartmentDTO department = dtoPair.getDTO1();

                if (department != null && department.getSport() != null)
                return new SimpleStringProperty(department.getSport());
            }
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _teamColumn.setCellValueFactory(dto -> {
            DTOPair<DepartmentDTO, TeamDTO> dtoPair = dto.getValue();

            if (dtoPair != null && dtoPair.getDTO2() != null) {
                TeamDTO team = dtoPair.getDTO2();

                if (team != null && team.getTeamName() != null)
                return new SimpleStringProperty(team.getTeamName());
            }
            return new SimpleStringProperty(NOT_AVAILABLE);
        });
    }

	@Override
    public void loadDTO(MemberDTO memberDTO){
        ACTIVE_DTO.set(memberDTO);
        displayMemberDTO(memberDTO);
    }

    /**
     * Pre-loads data into all view fields.
     * @param memberDTO MemberDTO that will be preloaded.
     */
    private void displayMemberDTO(MemberDTO memberDTO) {
        if (memberDTO != null) {

            new Thread(() -> {

                try {

                    IMemberController memberController = CommunicationFacade.lookupForMemberController();

                    final List<DTOPair<DepartmentDTO, TeamDTO>> fetchedList = memberController.loadFetchedDepartmentTeamList(
                        memberDTO.getMemberId(),
                        CommunicationFacade.getActiveSession()
                    );

                    Platform.runLater(() -> {

                        //clearing old values
                        dispose();

                        _lastName.setText(readOrNotAvailable(memberDTO.getLastName()));
                        _firstName.setText(readOrNotAvailable(memberDTO.getFirstName()));
                        _dateOfBirth.setText(readOrNotAvailable(memberDTO.getDateOfBirth()));
                        _gender.setText(readOrNotAvailable(memberDTO.getGender()));
                        _address.setText(readOrNotAvailable(memberDTO.getAddress()));
                        _email.setText(readOrNotAvailable(memberDTO.getEmail()));
                        _role.setText(readOrNotAvailable(memberDTO.getRole()));

                        if (fetchedList != null && !fetchedList.isEmpty()) {
                            _departmentTeamTable.setItems(FXCollections.observableList(fetchedList));
                        }
                    });

                } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {

                    LOGGER.error("Error occurred while loading Member data (Departments and Teams).", e);
                    Platform.runLater(() ->
                        GUIHelper.showErrorAlert("Error occurred while loading Member data (Departments and Teams).")
                    );
                } catch (NotAuthorisedException e) {

                    LOGGER.error("Client load (Departments and Teams) request was rejected. Not enough permissions.", e);
                }

            }).start();
        }
    }

    @FXML
    private void onEditMember(ActionEvent actionEvent) {
        Optional<MemberDTO> result = new EditViewDialog<>(ACTIVE_DTO.get(), MemberEditViewController.class).showAndWait();
        if (result.isPresent()) {
            loadDTO(result.get());
        }
    }
    
    @FXML
    private void onCreateMember(ActionEvent actionEvent) {
        Optional<MemberDTO> result = new EditViewDialog<>(null, MemberEditViewController.class).showAndWait();
        if (result.isPresent()) {
            loadDTO(result.get());
        }
    }

    @Override
    public void dispose() {
        _lastName.setText(NOT_AVAILABLE);
        _firstName.setText(NOT_AVAILABLE);
        _dateOfBirth.setText(NOT_AVAILABLE);
        _gender.setText(NOT_AVAILABLE);
        _address.setText(NOT_AVAILABLE);
        _email.setText(NOT_AVAILABLE);
        _role.setText(NOT_AVAILABLE);

        _departmentTeamTable.getItems().clear();
    }

    private String readOrNotAvailable(String value) {
        return value != null ? value : NOT_AVAILABLE;
    }
}
