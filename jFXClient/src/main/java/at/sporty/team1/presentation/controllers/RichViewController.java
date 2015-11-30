package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.rmi.api.IDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 21-Nov-15.
 */
public class RichViewController extends JfxController {

    @FXML private BorderPane _borderPanel;

    private Class<? extends SearchViewController<? extends IDTO>> _searchControllerClass;
    private Class<? extends ConsumerViewController<? extends IDTO>> _consumerControllerClass;
    private SearchViewController<? extends IDTO> _searchController;
    private ConsumerViewController<? extends IDTO> _consumerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public <T extends IDTO, U extends SearchViewController<T>, V extends ConsumerViewController<T>>
    void setViews(Class<U> searchControllerClass, Class<V> consumerControllerClass) {
        _searchControllerClass = searchControllerClass;
        _consumerControllerClass = consumerControllerClass;

        new Thread(() -> {

            ViewLoader<V> consumerViewLoader = ViewLoader.loadView(consumerControllerClass);
            Node consumerView = consumerViewLoader.loadNode();
            V consumerController = consumerViewLoader.getController();
            _consumerController = consumerController;

            ViewLoader<U> searchViewLoader = ViewLoader.loadView(searchControllerClass);
            Node searchView = searchViewLoader.loadNode();
            U searchController = searchViewLoader.getController();
            _searchController = searchController;

            searchController.setTargetConsumer(consumerController::loadDTO);

            Platform.runLater(() -> {
                _borderPanel.setLeft(searchView);
                _borderPanel.setCenter(consumerView);
            });

        }).start();
    }

    public SearchViewController<? extends IDTO> getSearchController() {
        return _searchController;
    }

    public JfxController getConsumerController() {
        return _consumerController;
    }
}
