package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class SearchResultViewController extends JfxController {
    private static final String PROGRESS_CSS_CLASS = "progress";
    private static final String LAST_NAME_COLUMN = "LAST NAME";
    private static final String FIRST_NAME_COLUMN = "FIRST NAME";
    private static final String FEE_PAID_NAME_COLUMN = "FEE PAID";
    private static final String FEE_PAID_SYMBOL = "✓";
    private static final String FEE_NOT_PAID_SYMBOL = "✗";
    private static final Label SEARCH_IN_PROGRESS_PLACEHOLDER = new Label("Searching...");
    private static final Label NO_CONTENT_PLACEHOLDER = new Label("No Content");

    @FXML private TableView<MemberDTO> _resultTable;

    private Consumer<MemberDTO> _selectionConsumer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _resultTable.setPlaceholder(NO_CONTENT_PLACEHOLDER);

        TableColumn<MemberDTO, String> lastNameColumn = new TableColumn<>(LAST_NAME_COLUMN);
        TableColumn<MemberDTO, String> firstNameColumn = new TableColumn<>(FIRST_NAME_COLUMN);
        TableColumn<MemberDTO, String> feePaidColumn = new TableColumn<>(FEE_PAID_NAME_COLUMN);

        lastNameColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getLastName()));
        firstNameColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getFirstName()));
        feePaidColumn.setCellValueFactory(dto -> {
            Boolean isPayed = dto.getValue().getIsFeePaid();

            if (isPayed != null && isPayed) return new SimpleStringProperty(FEE_PAID_SYMBOL);
            return new SimpleStringProperty(FEE_NOT_PAID_SYMBOL);
        });

        lastNameColumn.setPrefWidth(110);
        firstNameColumn.setPrefWidth(110);
        feePaidColumn.setPrefWidth(90);

        _resultTable.getColumns().add(lastNameColumn);
        _resultTable.getColumns().add(firstNameColumn);
        _resultTable.getColumns().add(feePaidColumn);

        //Mouse listener for search table
        _resultTable.setOnMouseClicked(e -> {
            if (isDoubleClick(e, MouseButton.PRIMARY)) {

                MemberDTO selectedMember = _resultTable.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    _selectionConsumer.accept(selectedMember);
                }
            }
        });

        //Arrow listener for search table
        _resultTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.RIGHT) {

                MemberDTO selectedMember = _resultTable.getSelectionModel().getSelectedItem();
                if (selectedMember != null) {
                    _selectionConsumer.accept(selectedMember);
                }
            }
        });
    }

    public void setTargetConsumer(Consumer<MemberDTO> selectionConsumer) {
        _selectionConsumer = selectionConsumer;
    }

    public void showProgressAnimation() {
        if (_resultTable.getItems() != null) _resultTable.getItems().clear();

        _resultTable.getStyleClass().add(PROGRESS_CSS_CLASS);
        _resultTable.setPlaceholder(SEARCH_IN_PROGRESS_PLACEHOLDER);
    }

    public void displayResults(List<MemberDTO> results) {
        if (_resultTable.getItems() != null) _resultTable.getItems().clear();

        _resultTable.getStyleClass().remove(PROGRESS_CSS_CLASS);

        if (results != null && !results.isEmpty()) {

            //Sorting the results
            ObservableList<MemberDTO> list = FXCollections.observableList(results);
            list.sort((o1, o2) -> {
                String s1 = o1.getLastName() + o1.getFirstName();
                String s2 = o2.getLastName() + o2.getFirstName();

                return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
            });

            //setting values to table
            _resultTable.setItems(list);

            //Setting focus to first element
            _resultTable.requestFocus();
            _resultTable.getSelectionModel().select(0);
            _resultTable.getFocusModel().focus(0);
        } else {
            _resultTable.setPlaceholder(NO_CONTENT_PLACEHOLDER);
        }
    }

    private boolean isDoubleClick(MouseEvent event, MouseButton mouseButton) {
        return event.getButton().equals(mouseButton) && event.getClickCount() == 2;
    }

}
