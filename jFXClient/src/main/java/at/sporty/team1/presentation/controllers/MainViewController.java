package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainViewController extends JfxController {
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "MEMBER";
    private static final String COMPETITION_TAB_CAPTION = "NEW COMPETITION";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Tab, IJfxController> CONTROLLER_TO_TAB_MAP = new HashMap<>();

    @FXML private TextField _searchField;
    @FXML private TabPane _tabPanel;
    
    private SessionDTO _session;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        //Enter listener for search table
        _searchField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                startSearch();
            }
        });

        //Opening un-closable tabs
        openMemberView(false);
        openTeamView(false);
        openCompetitionView(false);
    }

    public void activateSession(SessionDTO session){
        _session = session;


    }

    @FXML
    private void startSearch() {
        IJfxController activeController = CONTROLLER_TO_TAB_MAP.get(getActiveTab());
        String searchString = GUIHelper.readNullOrEmpty(_searchField.getText());

        if (activeController instanceof RichViewController) {
            ((RichViewController) activeController).getSearchController().search(searchString);
        } else {
            LOGGER.warn(
                "Currently selected view controller \"{}\" does not support search option.",
                activeController.getClass().getCanonicalName()
            );
        }
    }

    private Tab getActiveTab() {
        return _tabPanel.getSelectionModel().getSelectedItem();
    }

	private void openMemberView(boolean closable) {
        openNewRichTab(
            MEMBER_TAB_CAPTION,
            closable,
            MemberDTO.class,
            MemberSearchViewController.class,
            MemberViewController.class
        );
	}

    private void openTeamView(boolean closable) {
        openNewRichTab(
            TEAM_TAB_CAPTION,
            closable,
            MemberDTO.class,
            MemberSearchViewController.class,
            TeamViewController.class
        );
    }

    private void openCompetitionView(boolean closable) {
        openNewRichTab(
            COMPETITION_TAB_CAPTION,
            closable,
            TournamentDTO.class,
            TournamentSearchViewController.class,
            CompetitionViewController.class
        );
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

            //registering relation from tab to controller
            CONTROLLER_TO_TAB_MAP.put(t, controller);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });
        }).start();
    }


    private void openNewRichTab(
        String tabCaption,
        boolean closable,
        Class<? extends IDTO> dtoClass,
        Class<? extends SearchViewController<? extends IDTO>> searchControllerClass,
        Class<? extends JfxController> consumerControllerClass
    ) {

        new Thread(() -> {

            ViewLoader<RichViewController> viewLoader = ViewLoader.loadView(RichViewController.class);
            Node node = viewLoader.loadNode();
            RichViewController controller = viewLoader.getController();
            controller.setViews(searchControllerClass, consumerControllerClass);

            Tab t = new Tab();
            t.setText(tabCaption);
            t.setContent(node);
            t.setClosable(closable);

            //registering bidirectional relation from tab to controller
            CONTROLLER_TO_TAB_MAP.put(t, controller);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });
        }).start();
    }
}