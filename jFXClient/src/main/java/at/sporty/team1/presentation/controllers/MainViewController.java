package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainViewController extends JfxController {
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "Member";
    private static final Map<IJfxController, Tab> TAB_TO_CONTROLLER_MAP = new HashMap<>();
    private static final Map<Tab, IJfxController> CONTROLLER_TO_TAB_MAP = new HashMap<>();

    private SearchViewController _searchViewController;
    
	@FXML private BorderPane _borderPanel;
	@FXML private TabPane _tabPanel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {

            ViewLoader<SearchViewController> viewLoader = ViewLoader.loadView(SearchViewController.class);
            Node node = viewLoader.loadNode();
            _searchViewController = viewLoader.getController();

            _searchViewController.setTargetDelegator(this::delegateToMemberTarget);

            Platform.runLater(() -> _borderPanel.setLeft(node));

        }).start();

        openNewMemberView(false);
        openNewTeamView(false);
    }

	private void openNewMemberView(boolean closable) {
        openView(MEMBER_TAB_CAPTION, closable, null, MemberViewController.class);
	}

    private void openNewTeamView(boolean closable) {
        openView(TEAM_TAB_CAPTION, closable, null, TeamViewController.class);
    }

    private void delegateToMemberTarget(MemberDTO dto) {
        Tab activeTab = _tabPanel.getSelectionModel().getSelectedItem();
        if (activeTab != null) {

            IJfxController controller = CONTROLLER_TO_TAB_MAP.get(activeTab);
            if (controller != null) {
                controller.displayDTO(dto);
            }

            //TODO reactivate when there will be more than 2 views;
//            else {
//                //Default action if no tabs are selected or no tabs are available.
//                openView(MEMBER_TAB_CAPTION, false, dto, MemberViewController.class);
//            }
        }
    }

    public void openView(String viewCaption, boolean closable,  IDTO idto, Class<? extends IJfxController> controllerClass) {
        new Thread(() -> {

            ViewLoader<? extends IJfxController> viewLoader = ViewLoader.loadView(controllerClass);
            Node node = viewLoader.loadNode();
            IJfxController controller = viewLoader.getController();

            //check if need to load a dto
            if (idto != null) controller.displayDTO(idto);

            openNewTab(viewCaption, closable,  node, controller);

        }).start();
    }

    private void openNewTab(String tabName, boolean closable, Node node, IJfxController controller) {
        new Thread(() -> {
            Tab t = new Tab();
            t.setText(tabName);
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