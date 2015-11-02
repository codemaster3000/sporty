package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class MainViewController extends JfxController {
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "Member";
    private static final Map<IJfxController, Tab> TAB_CONTROLLER_MAP = new HashMap<>();
    private static final Map<Class<? extends IDTO>, Consumer<IDTO>> TARGETS_MAP = new HashMap<>();

    private SearchViewController _searchViewController;
    
	@FXML private BorderPane _borderPanel;
	@FXML private TabPane _tabPanel;

    public MainViewController() {
        TARGETS_MAP.put(TeamDTO.class, e -> openTeamView((TeamDTO) e));
        TARGETS_MAP.put(MemberDTO.class, e -> openMemberView((MemberDTO) e));
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {

            ViewLoader<SearchViewController> viewLoader = ViewLoader.loadView(SearchViewController.class);
            Node node = viewLoader.loadNode();
            _searchViewController = viewLoader.getController();

            _searchViewController.setTargetDelgator(this::delegateToTarget);

            Platform.runLater(() -> _borderPanel.setLeft(node));

        }).start();
    }
	
	@FXML
	public void openNewMemberView() {
        openMemberView(null);
	}

    private void delegateToTarget(IDTO dto) {
        Consumer<IDTO> targetConsumer = TARGETS_MAP.get(dto.getClass());
        targetConsumer.accept(dto);
    }

    public void openTeamView(TeamDTO teamDTO) {
        new Thread(() -> {

            ViewLoader<TeamViewController> viewLoader = ViewLoader.loadView(TeamViewController.class);
            Node node = viewLoader.loadNode();
            TeamViewController controller = viewLoader.getController();

            //check if need to load an existing member
            if (teamDTO != null) controller.displayTeamData(teamDTO);

            openNewTab(TEAM_TAB_CAPTION, node, controller);

        }).start();
    }


    public void openMemberView(MemberDTO memberDTO) {
        new Thread(() -> {

            ViewLoader<MemberViewController> viewLoader = ViewLoader.loadView(MemberViewController.class);
            Node node = viewLoader.loadNode();
            MemberViewController controller = viewLoader.getController();

            //check if need to load an existing member
            if (memberDTO != null) controller.displayMemberData(memberDTO);

            openNewTab(MEMBER_TAB_CAPTION, node, controller);

        }).start();
    }

    private void openNewTab(String tabName, Node node, IJfxController controller) {
        new Thread(() -> {
            Tab t = new Tab();
            t.setText(tabName);
            t.setContent(node);
            t.setClosable(true);

            //assigning dispose function
            controller.setDisposeFunction(disposableController -> {
                Tab disposableTab = TAB_CONTROLLER_MAP.get(disposableController);
                if (disposableTab != null) {
                    _tabPanel.getTabs().remove(disposableTab);
                }

                //FIXME  check with debug tool amount of instances of controller in map
//                TAB_CONTROLLER_MAP.remove(disposableController);
            });

            //registering child controller
            TAB_CONTROLLER_MAP.put(controller, t);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });
        }).start();
    }
}