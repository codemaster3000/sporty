package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

/**
 * Created by sereGkaluv on 30-Oct-15.
 */
public class SearchViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROGRESS = "progress";
    
    private MemberViewController _memberViewController;

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
        
        _searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
        	@Override
        	public void handle(KeyEvent keyevent){
        		if(keyevent.getCode() == KeyCode.ENTER){
        			startSearch();
        		}
        	}
		});
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
                        displayNewSearchResults(rawSearchResults);
                    });

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurs while searching.", e);
                }

            }).start();
        }
        
        _searchResultsListView.setOnMouseClicked(new EventHandler<MouseEvent>(){
        	@Override
        	public void handle(MouseEvent event){
        		if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        //TODO: open MemberView
                    	//openMemberView();
                    }
                }
        	}
        });
    }

    private void displayNewSearchResults(List<MemberDTO> resultList) {
        _searchResultsListView.setItems(FXCollections.observableArrayList(resultList));
        _searchResultsListView.requestFocus();
        _searchResultsListView.getSelectionModel().select(0);
        _searchResultsListView.getFocusModel().focus(0);
    }
    
	/*public void openMemberView() {
        new Thread(() -> {

            ViewLoader<MemberViewController> viewLoader = ViewLoader.loadView(MemberViewController.class);
            Node node = viewLoader.loadNode();

            Tab t = new Tab();
            t.setText("Member");
            t.setContent(node);
            t.setClosable(true);

            //assigning dispose function
            MemberViewController controller = viewLoader.getController();
            controller.setDisposeFunction(disposableController -> {
                Tab disposableTab = _tabControllerMap.get(disposableController);
                if (disposableTab != null) {
                    _tabPanel.getTabs().remove(disposableTab);
                }
                _tabControllerMap.remove(disposableController);
            });

            //registering child controller
            _tabControllerMap.put(controller, t);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });

        }).start();
	}	*/

}
