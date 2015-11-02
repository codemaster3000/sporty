package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
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
    private static final String MEMBER_TAB_CAPTION = "Member";
    private final Map<IJfxController, Tab> _tabControllerMap = new HashMap<>();
    
    private SearchViewController _searchViewController;
    
	@FXML private BorderPane _borderPanel;
	@FXML private TabPane _tabPanel;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {

            ViewLoader<SearchViewController> viewLoader = ViewLoader.loadView(SearchViewController.class);
            Node node = viewLoader.loadNode();
            _searchViewController = viewLoader.getController();
            
            _searchViewController.setTargetConsumer(this::openMemberView);

            Platform.runLater(() -> _borderPanel.setLeft(node));

        }).start();
    }
	
	@FXML
	public void openNewMemberView() {
        openMemberView(null);
	}	
	
	public void openMemberView(MemberDTO memberDTO) {
        new Thread(() -> {

            ViewLoader<MemberViewController> viewLoader = ViewLoader.loadView(MemberViewController.class);
            Node node = viewLoader.loadNode();

            Tab t = new Tab();
            t.setText(MEMBER_TAB_CAPTION);
            t.setContent(node);
            t.setClosable(true);

            MemberViewController controller = viewLoader.getController();

            //check if need to load an existing member
            if (memberDTO != null) controller.displayMemberData(memberDTO);

            //assigning dispose function
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