package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public class SearchViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROGRESS = "progress";
    
    private Consumer<MemberDTO> _targetDelegator;

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
        
        _searchField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                startSearch();
            }
        });

        _searchResultsListView.setOnMouseClicked(e -> {
            if (isDoubleClick(e, MouseButton.PRIMARY) && _targetDelegator != null) {

                MemberDTO selectedMember = _searchResultsListView.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    _targetDelegator.accept(selectedMember);
                }
            }
        });

        _searchResultsListView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.RIGHT && _targetDelegator != null) {

                MemberDTO selectedMember = _searchResultsListView.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    _targetDelegator.accept(selectedMember);
                }
            }
        });
    }
    
    public void setTargetDelegator(Consumer<MemberDTO> consumerFunction) {
        _targetDelegator = consumerFunction;
    }

    @FXML
    private void startSearch() {
    	
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
                        _searchResultsListView.setItems(FXCollections.observableArrayList(rawSearchResults));
                        _searchResultsListView.requestFocus();
                        _searchResultsListView.getSelectionModel().select(0);
                        _searchResultsListView.getFocusModel().focus(0);
                    });

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurs while searching.", e);
                }

            }).start();
        }
    }

    private boolean isDoubleClick(MouseEvent event, MouseButton mouseButton) {
        return event.getButton().equals(mouseButton) && event.getClickCount() == 2;
    }
}
