package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.DTOPair;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.ResourceBundle;


public class MemberReadOnlyViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NOT_AVAILABLE = "N/A";

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
    @FXML private ButtonBar _options;

    private static MemberDTO _activeMemberDTO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        //clear form
        dispose();

        if (memberDTO != null) {
            _activeMemberDTO = memberDTO;
            new Thread(() -> {
                try {

                    IMemberController memberController = CommunicationFacade.lookupForMemberController();

                    List<DTOPair<DepartmentDTO, TeamDTO>> fetchedList = memberController.loadFetchedDepartmentTeamList(
                        _activeMemberDTO.getMemberId(),
                        CommunicationFacade.getActiveSession()
                    );

                    Platform.runLater(() -> {

                        if (fetchedList != null) {
                            _departmentTeamTable.setItems(FXCollections.observableList(fetchedList));
                        }

                        _lastName.setText(readOrNotAvailable(_activeMemberDTO.getLastName()));
                        _firstName.setText(readOrNotAvailable(_activeMemberDTO.getFirstName()));
                        _dateOfBirth.setText(readOrNotAvailable(_activeMemberDTO.getDateOfBirth()));
                        _gender.setText(readOrNotAvailable(_activeMemberDTO.getGender()));
                        _address.setText(readOrNotAvailable(_activeMemberDTO.getAddress()));
                        _email.setText(readOrNotAvailable(_activeMemberDTO.getEmail()));
                        _role.setText(readOrNotAvailable(_activeMemberDTO.getRole()));
                    });

                } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {

                    LOGGER.error("Error occurred while loading Member data (Departments and Teams).", e);
                    GUIHelper.showErrorAlert("Error occurred while loading Member data (Departments and Teams).");

                } catch (NotAuthorisedException e) {

                    LOGGER.error("Client load (Departments and Teams) request was rejected. Not enough permissions.", e);
                }
            }).start();
        }
    }

    @FXML
    private void onEditMember(ActionEvent actionEvent) {
        //TODO open editing dialog
    }

    @Override
    public void dispose() {
        _activeMemberDTO = null;

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
