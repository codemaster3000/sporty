package at.sporty.team1.presentation.controllers.core;

import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.shared.api.IDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public abstract class SearchViewController<T extends IDTO> extends JfxController {
    private static final String NO_RESULTS_CONTEXT = "No results were found.";
    private static final String PROGRESS_CSS_CLASS = "progress";
    protected static final String NOT_VALID_SEARCH_INPUT = "Not valid search input";

    private final Label NO_CONTENT_PLACEHOLDER = new Label("No Content");
    private final Label SEARCH_IN_PROGRESS_PLACEHOLDER = new Label("Searching...");

    private TableView<T> _resultTable;

    private Consumer<T> _selectionConsumer;

    public abstract void search(String searchString);

    public void setTargetConsumer(Consumer<T> selectionConsumer) {
        _selectionConsumer = selectionConsumer;
    }

    public void displayNoResults() {
        Platform.runLater(() -> {
            if (_resultTable.getItems() != null) _resultTable.getItems().clear();

            hideProgressAnimation();
            GUIHelper.showInformationAlert(NO_RESULTS_CONTEXT);
        });
    }

    protected void showProgressAnimation() {
        Platform.runLater(() -> {
            if (_resultTable.getItems() != null) _resultTable.getItems().clear();

            _resultTable.getStyleClass().add(PROGRESS_CSS_CLASS);
            _resultTable.setPlaceholder(SEARCH_IN_PROGRESS_PLACEHOLDER);
        });
    }

    protected void hideProgressAnimation() {
        Platform.runLater(() -> {
            if (_resultTable.getItems() != null) _resultTable.getItems().clear();

            _resultTable.getStyleClass().remove(PROGRESS_CSS_CLASS);
            _resultTable.setPlaceholder(NO_CONTENT_PLACEHOLDER);
        });
    }

    protected abstract void handleReceivedResults(List<T> rawResults);

    protected void displayResults(List<T> rawResults) {
        displayResults(rawResults, null);
    }

    protected void displayResults(List<T> rawResults, Comparator<T> comparator) {
        if (rawResults != null && !rawResults.isEmpty()) {

            //converting to observable
            ObservableList<T> precookedResults = FXCollections.observableList(rawResults);

            //sorting only if comparator is specified
            if (comparator != null) precookedResults.sort(comparator);

            hideProgressAnimation();

            Platform.runLater(() -> {
                //setting values to table
                _resultTable.setItems(precookedResults);

                //Setting focus to first element
                _resultTable.requestFocus();
                _resultTable.getSelectionModel().select(0);
                _resultTable.getFocusModel().focus(0);
            });

        } else {
            displayNoResults();
        }
    }

    protected TableView<T> getResultTableInstance() {

        //Return always same instance. (Protection against multiple method calls)
        if (_resultTable != null) return _resultTable;

        _resultTable = new TableView<>();
        _resultTable.setPlaceholder(NO_CONTENT_PLACEHOLDER);

        _resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Mouse listener for search table
        _resultTable.setOnMouseClicked(e -> {
            if (isDoubleClick(e, MouseButton.PRIMARY)) {

                T selectedDTO = _resultTable.getSelectionModel().getSelectedItem();
                if (selectedDTO != null) {
                    _selectionConsumer.accept(selectedDTO);
                }
            }
        });

        //Arrow listener for search table
        _resultTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.KP_RIGHT) {

                T selectedDTO = _resultTable.getSelectionModel().getSelectedItem();
                if (selectedDTO != null) {
                    _selectionConsumer.accept(selectedDTO);
                }
            }
        });

        VBox.setVgrow(_resultTable, Priority.ALWAYS);

        return _resultTable;
    }

    private boolean isDoubleClick(MouseEvent event, MouseButton mouseButton) {
        return event.getButton().equals(mouseButton) && event.getClickCount() == 2;
    }
}
