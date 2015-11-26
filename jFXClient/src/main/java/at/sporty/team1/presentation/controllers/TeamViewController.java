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
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Represents the View of a Team (Mannschaft)
 */
public class TeamViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUCCESSFUL_TEAM_SAVE = "Team was successfully saved.";

    @FXML
    private ListView<MemberDTO> _membersListView;
    @FXML
    private ComboBox<TeamDTO> _chooseTeamComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<TeamDTO> resultList = new LinkedList<>();

        _membersListView.setCellFactory(m -> new ListCell<MemberDTO>() {
            @Override
            protected void updateItem(MemberDTO m, boolean bln) {
                super.updateItem(m, bln);

                if (m != null) {
                    String fullName = m.getFirstName() + " " + m.getLastName();
                    setText(fullName);

                    MenuItem deleteItem = new MenuItem();
                    deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", fullName));
                    deleteItem.setOnAction(event -> _membersListView.getItems().removeAll(getItem()));

                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().add(deleteItem);

                    emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            setContextMenu(null);
                        } else {
                            setContextMenu(contextMenu);
                        }
                    });
                } else {
                    //remove caption from list
                    setText("");
                }
            }
        });

        /**
         * Load Teams
         */

        /**
         * Converter from TeamDTO to Team name (String)
         */
        StringConverter<TeamDTO> teamDTOStringConverter = new StringConverter<TeamDTO>() {
            @Override
            public String toString(TeamDTO teamDTO) {
                if (teamDTO != null) {
                    return teamDTO.getTeamName();
                }
                return null;
            }

            @Override
            public TeamDTO fromString(String string) {
                return null;
            }
        };

        _chooseTeamComboBox.setConverter(teamDTOStringConverter);

        new Thread(() -> {

            try {

                IDepartmentController departmentController = CommunicationFacade.lookupForDepartmentController();
                List<DepartmentDTO> departments = departmentController.searchAllDepartments(
                    CommunicationFacade.getActiveSession()
                );

                if (!departments.isEmpty()) {

                    for (DepartmentDTO actualDepartment : departments) {

                        resultList.addAll(departmentController.loadDepartmentTeams(
                            actualDepartment.getDepartmentId(),
                            CommunicationFacade.getActiveSession()
                        ));

                        if (!resultList.isEmpty()) {

                            //loading values to comboBox
                            Platform.runLater(() -> _chooseTeamComboBox.setItems(
                                FXCollections.observableList(resultList)
                            ));
                        }
                    }
                }
            } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {
                LOGGER.error("Error occurred while loading all Departments and their Teams.", e);
            } catch (NotAuthorisedException e) {
                LOGGER.error("Client load (Departments and Teams) request was rejected. Not enough permissions.", e);
            }
        }).start();

        _chooseTeamComboBox.setOnAction((event) -> {

            _membersListView.getItems().clear();
            displayTeamData(_chooseTeamComboBox.getSelectionModel().getSelectedItem());
        });
    }

    private static TeamDTO _activeTeamDTO;

    @Override
    public void displayDTO(IDTO idto) {
        if (idto instanceof TeamDTO) {
            displayTeamData((TeamDTO) idto);
        } else if (idto instanceof MemberDTO) {
            addNewMemberToTeam((MemberDTO) idto);
        }
    }

    /**
     * Pre-loads data into all view fields.
     *
     * @param teamDTO TeamDTO that will be preloaded.
     */
    private void displayTeamData(TeamDTO teamDTO) {
        if (teamDTO != null) {
            _activeTeamDTO = teamDTO;

            try {

                ITeamController teamController = CommunicationFacade.lookupForTeamController();

                List<MemberDTO> memberList = teamController.loadTeamMembers(
                    _activeTeamDTO.getTeamId(),
                    CommunicationFacade.getActiveSession()
                );

                if (memberList != null && !memberList.isEmpty()) {
                    _membersListView.setItems(FXCollections.observableList(memberList));
                }

            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                LOGGER.error("Error occurred while displaying team data.", e);
            } catch (UnknownEntityException e) {
                LOGGER.error("DTO was not saved in Data Storage before loading all Members from Team.", e);
            } catch (NotAuthorisedException e) {
                LOGGER.error("Client load (Team Members) request was rejected. Not enough permissions.", e);
            }
        }
    }

    public void addNewMemberToTeam(MemberDTO memberDTO) {
        if (memberDTO != null && !_membersListView.getItems().contains(memberDTO)) {
            _membersListView.getItems().add(memberDTO);
        }
    }

    @FXML
    private void removeSelectedMember(ActionEvent event) {
        MemberDTO memberDTO = _membersListView.getSelectionModel().getSelectedItem();

        try {
            IMemberController memberController = CommunicationFacade.lookupForMemberController();

            if ((_activeTeamDTO != null) && (memberDTO != null)) {

                memberController.removeMemberFromTeam(
                    memberDTO.getMemberId(),
                    _activeTeamDTO.getTeamId(),
                    CommunicationFacade.getActiveSession()
                );

                _membersListView.getItems().remove(memberDTO);

            } else if (memberDTO != null) {

                //Simply remove from list view, this part of the code
                //will be executed only when new team will be created
                _membersListView.getItems().remove(memberDTO);

            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            LOGGER.error("Error occurred while removing member from the team.", e);
        } catch (UnknownEntityException e) {
            LOGGER.error("DTO was not saved in Data Storage before removing Member from Team.", e);
        } catch (NotAuthorisedException e) {
            LOGGER.error("Client delete (Member) request was rejected. Not enough permissions.", e);
        }
    }

    @FXML
    private void saveTeam(ActionEvent event) {

        ObservableList<MemberDTO> memberList = _membersListView.getItems();

        if (memberList != null && !memberList.isEmpty()) {

            try {

                ITeamController teamController = CommunicationFacade.lookupForTeamController();
                teamController.createOrSaveTeam(
                    _activeTeamDTO,
                    CommunicationFacade.getActiveSession()
                );

                for (MemberDTO member : memberList) {
                    try {

                        CommunicationFacade.lookupForMemberController().assignMemberToTeam(
                            member.getMemberId(),
                            _activeTeamDTO.getTeamId(),
                            CommunicationFacade.getActiveSession()
                        );

                    } catch (UnknownEntityException e) {
                        LOGGER.error("Unable to assign Member to Team", e);
                    } catch (NotAuthorisedException e) {
                        LOGGER.error("Client assign (Member to Team) request was rejected. Not enough permissions.", e);
                    }
                }

                GUIHelper.showSuccessAlert(SUCCESSFUL_TEAM_SAVE);

                //Logging and closing the tab
                LOGGER.info("Team \"{}\" was successfully saved.", _activeTeamDTO.getTeamName());
                dispose();

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurred while saving the team.", e);
            } catch (ValidationException e) {
                String context = String.format("Validation exception %s while saving team.", e.getCause());

                GUIHelper.showValidationAlert(context);
                LOGGER.error(context, e);
            } catch (NotAuthorisedException e) {
                LOGGER.error("Client save (Team) request was rejected. Not enough permissions.", e);
            }
        }
    }
}
