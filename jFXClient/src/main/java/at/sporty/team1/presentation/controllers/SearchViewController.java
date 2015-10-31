package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public class SearchViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROGRESS = "progress";

    @FXML private TextField _searchField;
    @FXML private ListView<MemberDTO> _searchResultsListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _searchResultsListView.setCellFactory(m -> new ListCell<MemberDTO>() {
            @Override
            protected void updateItem(MemberDTO m, boolean bln) {
                super.updateItem(m, bln);
                if (m != null) {
                    //Defining how MemberDTO will be displayed in ListView
                    setText(m.getFirstName() + " " + m.getLastName());
                }
            }
        });
    }

    @FXML
    private void startSearch(ActionEvent actionEvent) {
        String searchQuery = GUIHelper.readNullOrEmpty(_searchField.getText());

        if (searchQuery != null) {
            _searchResultsListView.getItems().clear();
            _searchResultsListView.getStyleClass().add(PROGRESS);

            new Thread(() -> {

                try {

                    IMemberController memberController = CommunicationFacade.lookupForMemberController();
                    List<MemberDTO> rawSearchResults = memberController.searchForMembers(searchQuery);

                    Platform.runLater(() -> {
                        _searchResultsListView.getStyleClass().remove(PROGRESS);
                        displayNewSearchResults(rawSearchResults);
                    });

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurs while searching.", e);
                }

            }).start();
        }
    }

    private void displayNewSearchResults(List<MemberDTO> resultList) {
        _searchResultsListView.setItems(FXCollections.observableArrayList(resultList));
        _searchResultsListView.requestFocus();
        _searchResultsListView.getSelectionModel().select(0);
        _searchResultsListView.getFocusModel().focus(0);
    }
}
