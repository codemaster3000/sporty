package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class TournamentSearchViewController extends SearchViewController<TournamentDTO> {
    private static final String TOURNAMENT_DATE_COLUMN = "DATE";
    private static final String TOURNAMENT_PLACE_COLUMN = "PLACE";

    @FXML private VBox _searchBox;

    private final TableView<TournamentDTO> _resultTable;

    public TournamentSearchViewController() {
        _resultTable = getResultTableInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<TournamentDTO, String> dateColumn = new TableColumn<>(TOURNAMENT_DATE_COLUMN);
        TableColumn<TournamentDTO, String> placeColumn = new TableColumn<>(TOURNAMENT_PLACE_COLUMN);

        dateColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getDate()));
        placeColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getLocation()));

        dateColumn.setPrefWidth(110);
        placeColumn.setPrefWidth(200);

        _resultTable.getColumns().add(dateColumn);
        _resultTable.getColumns().add(placeColumn);

        _searchBox.getChildren().add(_resultTable);
    }

    @Override
    public void search(String searchString) {
        //TODO
    }

    @Override
    protected void handleReceivedResults(List<TournamentDTO> rawResults) {
        //TODO
        displayResults(rawResults, (t1, t2) -> {
            String s1 = t1.getDate() + t1.getLocation();
            String s2 = t2.getDate() + t2.getLocation();

            return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
        });
    }
}
