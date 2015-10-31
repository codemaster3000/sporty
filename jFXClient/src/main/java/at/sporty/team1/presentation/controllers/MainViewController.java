package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
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
    private static final String NEW_MEMBER_TAB_CAPTION = "New Member";
    private final Map<IJfxController, Tab> _tabControllerMap = new HashMap<>();

	@FXML private BorderPane _borderPanel;
	@FXML private TabPane _tabPanel;

    private SearchViewController _searchViewController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {

            ViewLoader<SearchViewController> viewLoader = ViewLoader.loadView(SearchViewController.class);
            Node node = viewLoader.loadNode();
            _searchViewController = viewLoader.getController();

            Platform.runLater(() -> _borderPanel.setLeft(node));

        }).start();
    }
	
	@FXML
	private void openNewMemberView() {
        new Thread(() -> {

            ViewLoader<MemberViewController> viewLoader = ViewLoader.loadView(MemberViewController.class);
            Node node = viewLoader.loadNode();

            Tab t = new Tab();
            t.setText(NEW_MEMBER_TAB_CAPTION);
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
	}	
}