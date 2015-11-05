package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NO_RESULTS_TITLE = "No results";
    private static final String NO_RESULTS_CONTEXT = "No results were found.";
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "MEMBER";
    private static final Map<Tab, IJfxController> CONTROLLER_TO_TAB_MAP = new HashMap<>();
    private static final Map<IJfxController, Tab> TAB_TO_CONTROLLER_MAP = new HashMap<>();
    
    @FXML private TextField _searchField;
    @FXML private TabPane _tabPanel;
    @FXML private BorderPane _borderPanel;

    private SearchResultViewController _searchResultViewController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        //loading left search results panel
        new Thread(() -> {

            ViewLoader<SearchResultViewController> viewLoader = ViewLoader.loadView(SearchResultViewController.class);
            Node node = viewLoader.loadNode();
            _searchResultViewController = viewLoader.getController();
            _searchResultViewController.setTargetConsumer(this::delegateToMemberTarget);

            Platform.runLater(() -> _borderPanel.setLeft(node));

        }).start();


        //Enter listener for search table
        _searchField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                startSearch();
            }
        });

        //Opening un-closable tabs
        openMemberView(false);
        openTeamView(false);
    }

    @FXML
    private void startSearch() {
        String searchQuery = GUIHelper.readNullOrEmpty(_searchField.getText());

        if (searchQuery != null) {
            _searchResultViewController.showProgressAnimation();

            new Thread(() -> {

                try {

                    IMemberController memberController = CommunicationFacade.lookupForMemberController();
                    List<MemberDTO> rawSearchResults = memberController.searchForMembers(searchQuery);

                    if(rawSearchResults != null && !rawSearchResults.isEmpty()){

                        Platform.runLater(() -> _searchResultViewController.displayResults(rawSearchResults));

                    }else{
                        Platform.runLater(() -> {
                            GUIHelper.showAlert(
                                Alert.AlertType.INFORMATION,
                                NO_RESULTS_TITLE,
                                null,
                                NO_RESULTS_CONTEXT
                            );

                            _searchResultViewController.displayResults(null);
                        });
                    }

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurs while searching.", e);
                }

            }).start();
        }
    }

	private void openMemberView(boolean closable) {
        openNewTab(MEMBER_TAB_CAPTION, closable, null, MemberViewController.class);
	}

    private void openTeamView(boolean closable) {
        openNewTab(TEAM_TAB_CAPTION, closable, null, TeamViewController.class);
    }

    private void delegateToMemberTarget(MemberDTO dto) {
        Tab activeTab = _tabPanel.getSelectionModel().getSelectedItem();
        if (activeTab != null) {

            IJfxController controller = CONTROLLER_TO_TAB_MAP.get(activeTab);
            if (controller != null) {
                controller.displayDTO(dto);
            }

            //TODO reactivate when there will be more than 2 views;
            //else {
            //    //Default action if no tabs are selected or no tabs are available.
            //    openNewTab(MEMBER_TAB_CAPTION, false, dto, MemberViewController.class);
            //}
        }
    }

    private void openNewTab(
        String tabCaption,
        boolean closable,
        IDTO idto,
        Class<? extends IJfxController> controllerClass
    ) {

        new Thread(() -> {

            ViewLoader<? extends IJfxController> viewLoader = ViewLoader.loadView(controllerClass);
            Node node = viewLoader.loadNode();
            IJfxController controller = viewLoader.getController();

            //check if need to load a dto
            if (idto != null) controller.displayDTO(idto);

            Tab t = new Tab();
            t.setText(tabCaption);
            t.setContent(node);
            t.setClosable(closable);

            //registering bidirectional relation from tab to controller
            TAB_TO_CONTROLLER_MAP.put(controller, t);
            CONTROLLER_TO_TAB_MAP.put(t, controller);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });

        }).start();
    }
}