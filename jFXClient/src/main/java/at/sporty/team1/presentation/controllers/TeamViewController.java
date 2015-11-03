package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Represents the View of a Team (Mannschaft)
 */
public class TeamViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUCCESSFUL_TEAM_SAVE = "Team was successfully saved.";

    @FXML private ListView<MemberDTO> _membersListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _membersListView.setCellFactory(m -> new ListCell<MemberDTO>() {
            @Override
            protected void updateItem(MemberDTO m, boolean bln) {
                super.updateItem(m, bln);
                if (m != null) {
                    setText(m.getFirstName() + " " + m.getLastName());
                }

                MenuItem deleteItem = new MenuItem();
                deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", itemProperty()));
                deleteItem.setOnAction(event -> _membersListView.getItems().remove(getItem()));

                ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(deleteItem);

                emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        setContextMenu(null);
                    } else {
                        setContextMenu(contextMenu);
                    }
                });
            }
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
     * @param teamDTO TeamDTO that will be preloaded.
     */
    private void displayTeamData(TeamDTO teamDTO)  {
        if (teamDTO != null) {
            _activeTeamDTO = teamDTO;

            List<MemberDTO> memberList = _activeTeamDTO.getMemberList();
            if (memberList != null && !memberList.isEmpty())  {
                _membersListView.setItems(FXCollections.observableArrayList(
                        _activeTeamDTO.getMemberList()
                ));
            }
        }
    }

    public void addNewMemberToTeam(MemberDTO memberDTO) {
        if (memberDTO != null) {
            _membersListView.getItems().add(memberDTO);
        }
    }

    @FXML
    private void removeSelectedMember(ActionEvent event) {
        ObservableList<MemberDTO> removeList = _membersListView.getSelectionModel().getSelectedItems();

        List<MemberDTO> storedList = _activeTeamDTO.getMemberList();
        storedList.removeAll(removeList);

        _activeTeamDTO.setMemberList(storedList);
        displayTeamData(_activeTeamDTO);
    }

    @FXML
    private void saveTeam(ActionEvent event) {

        ObservableList<MemberDTO> memberList = _membersListView.getItems();

        if (memberList != null && !memberList.isEmpty()) {

            _activeTeamDTO.setMemberList(memberList);

            try {

                ITeamController teamController = CommunicationFacade.lookupForTeamController();
                teamController.createOrSaveTeam(_activeTeamDTO);

                GUIHelper.showSuccessAlert(SUCCESSFUL_TEAM_SAVE);

                //Logging and closing the tab
                LOGGER.info("Member \"{}\" was successfully saved.", _activeTeamDTO.getTeamName());
                dispose();

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurs while saving new member.", e);
            } catch (ValidationException e) {
                String context = String.format("Validation exception %s while saving member.", e.getCause());

                GUIHelper.showValidationAlert(context);
                LOGGER.error(context, e);
            }
        }
    }
}
