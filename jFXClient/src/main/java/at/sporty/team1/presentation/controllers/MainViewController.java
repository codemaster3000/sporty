package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.rmi.api.IDTO;
import at.sporty.team1.util.CachedSession;
import at.sporty.team1.util.GUIHelper;
import at.sporty.team1.util.SVGContainer;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewController extends JfxController {
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "MEMBER";
    private static final String COMPETITION_TAB_CAPTION = "NEW COMPETITION";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<Tab, IJfxController> CONTROLLER_TO_TAB_MAP = new HashMap<>();

    @FXML private TextField _searchField;
    @FXML private TabPane _tabPanel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        _tabPanel.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        //Enter listener for search table
        _searchField.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                startSearch();
            }
        });

        //Opening un-closable tabs

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(loadMemberViewTask(false));
        executor.execute(loadTeamViewTask(false));
        executor.execute(loadCompetitionViewTask(false));
        executor.execute(() -> Platform.runLater(() -> _tabPanel.getSelectionModel().selectFirst()));
    }

    public void activateSession(CachedSession session){
        //TODO move login functionality
    }

    @FXML
    private void startSearch() {
        IJfxController activeController = CONTROLLER_TO_TAB_MAP.get(getActiveTab());
        String searchString = GUIHelper.readNullOrEmpty(_searchField.getText());

        if (activeController instanceof RichViewController) {
            ((RichViewController) activeController).getSearchController().search(searchString);
        } else if (activeController != null) {
            LOGGER.warn(
                "Currently selected view controller \"{}\" does not support search option.",
                activeController.getClass().getCanonicalName()
            );
        }
    }

    private Tab getActiveTab() {
        return _tabPanel.getSelectionModel().getSelectedItem();
    }

	private Thread loadMemberViewTask(boolean closable) {
        return newOpenNewRichTabTask(
            MEMBER_TAB_CAPTION,
            SVGContainer.MEMBER_ICON,
            closable,
            MemberSearchViewController.class,
            MemberReadOnlyViewController.class
        );
	}

    private Thread loadTeamViewTask(boolean closable) {
        return newOpenNewRichTabTask(
            TEAM_TAB_CAPTION,
            SVGContainer.TEAM_ICON,
            closable,
            MemberSearchViewController.class,
            TeamViewController.class
        );
    }

    private Thread loadCompetitionViewTask(boolean closable) {
        return newOpenNewRichTabTask(
            COMPETITION_TAB_CAPTION,
            SVGContainer.TOURNAMENT_ICON,
            closable,
            TournamentSearchViewController.class,
            CompetitionViewController.class
        );
    }

    private Thread newOpenNewSimpleTabTask(
        String tabCaption,
        boolean closable,
        Class<? extends IJfxController> controllerClass
    ) {

        return new Thread(() -> {

            ViewLoader<? extends IJfxController> viewLoader = ViewLoader.loadView(controllerClass);
            Node node = viewLoader.loadNode();
            IJfxController controller = viewLoader.getController();

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
        });
    }

    private <T extends IDTO, U extends SearchViewController<T>, V extends ConsumerViewController<T>> Thread newOpenNewRichTabTask(
        String tabCaption,
        SVGContainer icon,
        boolean closable,
        Class<U> searchControllerClass,
        Class<V> consumerControllerClass
    ) {

        return new Thread(() -> {

            ViewLoader<RichViewController> viewLoader = ViewLoader.loadView(RichViewController.class);
            Node node = viewLoader.loadNode();
            RichViewController controller = viewLoader.getController();
            controller.setViews(searchControllerClass, consumerControllerClass);

            Tab t = new Tab();
            t.setText(tabCaption);
            t.setGraphic(GUIHelper.loadSVGGraphic(icon));
            t.setContent(node);
            t.setClosable(closable);

            //registering bidirectional relation from tab to controller
            CONTROLLER_TO_TAB_MAP.put(t, controller);

            Platform.runLater(() -> {
                _tabPanel.getTabs().add(t);
                _tabPanel.getSelectionModel().select(t);
            });
        });
    }
}