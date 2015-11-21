package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.ViewLoader;
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
    private Class<? extends JfxController> _consumerControllerClass;
    private SearchViewController<? extends IDTO> _searchController;
    private JfxController _consumerController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setViews(
        Class<? extends SearchViewController<? extends IDTO>> searchControllerClass,
        Class<? extends JfxController> consumerControllerClass
    ) {

        _searchControllerClass = searchControllerClass;
        _consumerControllerClass = consumerControllerClass;

        new Thread(() -> {

            ViewLoader<? extends JfxController> consumerViewLoader = ViewLoader.loadView(_consumerControllerClass);
            Node consumerView = consumerViewLoader.loadNode();
            _consumerController = consumerViewLoader.getController();

            ViewLoader<? extends SearchViewController<? extends IDTO>> searchViewLoader = ViewLoader.loadView(_searchControllerClass);
            Node searchView = searchViewLoader.loadNode();
            _searchController = searchViewLoader.getController();
            _searchController.setTargetConsumer(_consumerController::displayDTO);

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
