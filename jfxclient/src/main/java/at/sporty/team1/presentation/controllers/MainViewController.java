package at.sporty.team1.presentation.controllers;

import java.net.URL;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.util.NotificationPullerTask;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.controllers.core.IJfxController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.presentation.dialogs.MessagesDialog;
import at.sporty.team1.presentation.util.DaemonThreadFactory;
import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.presentation.util.SVGContainer;
import at.sporty.team1.presentation.util.ViewLoader;
import at.sporty.team1.shared.api.IDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class MainViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TEAM_TAB_CAPTION = "TEAM";
    private static final String MEMBER_TAB_CAPTION = "MEMBER";
    private static final String COMPETITION_TAB_CAPTION = "COMPETITION";
    private static final String MESSAGE_BOX_IS_EMPTY = "Message Box is empty";
    private static final int MESSAGE_PULL_INIT_DELAY = 0;
    private static final int MESSAGE_PULL_PERIOD = 30;

    private static final SimpleBooleanProperty LOGIN_BUTTON_VISIBILITY_PROPERTY = new SimpleBooleanProperty(true);
    private static final Map<Tab, IJfxController> CONTROLLER_TO_TAB_MAP = new HashMap<>();
    private static final ScheduledExecutorService MESSAGE_PULL_SCHEDULER = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
    private static final ObservableList<MessageDTO> USER_MESSAGES = FXCollections.emptyObservableList();
    private static final ContextMenu MESSAGES_MENU = new ContextMenu();

    @FXML private TextField _searchField;
    @FXML private GridPane _userBlock;
    @FXML private Label _userLabel;
    @FXML private Label _userRoleLabel;
    @FXML private Label _userMessagesLabel;
    @FXML private Button _loginButton;
    @FXML private Button _logoutButton;
    @FXML private TabPane _tabPanel;


    @Override
	public void initialize(URL location, ResourceBundle resources) {
        _tabPanel.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        //login button
        _loginButton.visibleProperty().bind(LOGIN_BUTTON_VISIBILITY_PROPERTY);
        _loginButton.managedProperty().bind(_loginButton.visibleProperty());

        //user block
        _userBlock.visibleProperty().bind(_loginButton.visibleProperty().not());
        _userBlock.managedProperty().bind(_userBlock.visibleProperty());

        //user messages
        _userMessagesLabel.setGraphic(GUIHelper.loadSVGGraphic(SVGContainer.MESSAGE_ICON));
        _userMessagesLabel.textProperty().bind(Bindings.size(USER_MESSAGES).asString());
        _userMessagesLabel.setContextMenu(MESSAGES_MENU);
        _userMessagesLabel.setOnMouseClicked(e -> {
            if (!USER_MESSAGES.isEmpty()) {
                new MessagesDialog(USER_MESSAGES).showAndWait();
            } else {
                GUIHelper.showInformationAlert(MESSAGE_BOX_IS_EMPTY);
            }
        });

        //logout button
        _logoutButton.visibleProperty().bind(_loginButton.visibleProperty().not());
        _logoutButton.managedProperty().bind(_logoutButton.visibleProperty());

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

    @FXML
    private void onLogin() {
        //In case of successful onLogin hide onLogin button
        if (performLogin()) {
            LOGIN_BUTTON_VISIBILITY_PROPERTY.set(false);
            _userLabel.setText(getUserName());
            _userRoleLabel.setText(getUserRole());

            try {
                MESSAGE_PULL_SCHEDULER.scheduleAtFixedRate(
                    new NotificationPullerTask(
                        CommunicationFacade.getInstance().getActiveSession(),
                        USER_MESSAGES
                    ),
                    MESSAGE_PULL_INIT_DELAY,
                    MESSAGE_PULL_PERIOD,
                    TimeUnit.SECONDS
                );
            } catch (RejectedExecutionException e) {
                LOGGER.error("Message-Pull scheduler was rejected to continue it's work.", e);
            }
        }
    }

    @FXML
    private void onLogout() {
        LOGIN_BUTTON_VISIBILITY_PROPERTY.set(true);

        MESSAGE_PULL_SCHEDULER.shutdownNow();
        USER_MESSAGES.clear();

        CommunicationFacade.getInstance().logout();
    }

    private boolean performLogin() {
        try {

            Optional<Pair<String, String>> result = GUIHelper.showLoginDialog();
            if (result.isPresent()) {
                Pair<String, String> loginData = result.get();

                try {
                    //Authorising
                    boolean isSuccessful = CommunicationFacade.getInstance().authorize(
                        loginData.getKey(),
                        loginData.getValue()
                    );

                    if (isSuccessful) {

                        GUIHelper.showSuccessAlert("Login was successful. :)");
                        return true;
                    } else {

                        GUIHelper.showErrorAlert("Invalid Username or Password.");
                        return performLogin();
                    }

                } catch (InvalidKeyException e) {
                    LOGGER.error("Private key is not suitable.", e);
                } catch (BadPaddingException | IllegalBlockSizeException e) {
                    LOGGER.error("Received data is corrupted.", e);
                } catch (SecurityException | NotAuthorisedException | UnknownEntityException e) {
                    LOGGER.error("Error occurs while generating client fingerprint.", e);
                }
            }

        } catch (RemoteCommunicationException e) {
            LOGGER.error("Unsuccessful onLogin detected.", e);
        }

        return false;
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
            CompetitionReadOnlyViewController.class
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

    private String getUserName() {
        MemberDTO user = CommunicationFacade.getInstance().getExtendedActiveSession().getUser();

        StringBuilder sb = new StringBuilder();
        if (user.getLastName() != null) sb.append(user.getLastName());
        if (user.getFirstName() != null) sb.append(" ").append(user.getFirstName());

        return sb.toString().toUpperCase();
    }

    private String getUserRole() {
        MemberDTO user = CommunicationFacade.getInstance().getExtendedActiveSession().getUser();
        return user.getRole() != null ? user.getRole() : null;
    }
}