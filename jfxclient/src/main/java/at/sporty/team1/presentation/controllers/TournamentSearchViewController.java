package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class TournamentSearchViewController extends SearchViewController<TournamentDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TOURNAMENT_SPORT_COLUMN = "SPORT";
    private static final String TOURNAMENT_DATE_COLUMN = "EVENT DATE";
    private static final String TOURNAMENT_PLACE_COLUMN = "LOCATION";

    @FXML private VBox _searchBox;
    @FXML private ComboBox<SearchType> _searchType;

    private final TableView<TournamentDTO> _resultTable;

    public TournamentSearchViewController() {
        _resultTable = getResultTableInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* Adding search types to search type ComboBox */
        _searchType.setItems(FXCollections.observableList(Arrays.asList(SearchType.values())));
        _searchType.getSelectionModel().select(SearchType.DEPARTMENT);

        /* Adding result table to _searchBox*/
        TableColumn<TournamentDTO, String> sportColumn = new TableColumn<>(TOURNAMENT_SPORT_COLUMN);
        TableColumn<TournamentDTO, String> dateColumn = new TableColumn<>(TOURNAMENT_DATE_COLUMN);
        TableColumn<TournamentDTO, String> placeColumn = new TableColumn<>(TOURNAMENT_PLACE_COLUMN);

        sportColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getDepartment().getSport()));
        dateColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getDate()));
        placeColumn.setCellValueFactory(dto -> new SimpleStringProperty(dto.getValue().getLocation()));

        sportColumn.setPrefWidth(110);
        dateColumn.setPrefWidth(90);
        placeColumn.setPrefWidth(110);

        _resultTable.getColumns().add(sportColumn);
        _resultTable.getColumns().add(dateColumn);
        _resultTable.getColumns().add(placeColumn);

        _searchBox.getChildren().add(_resultTable);
    }

    @Override
    public void search(String searchString) {
        showProgressAnimation();

        if (searchString != null) {

            new Thread(() -> {
                try {

                    ITournamentController tournamentController = CommunicationFacade.lookupForTournamentController();

                    //Performing search depending on selected search type
                    switch (_searchType.getValue()) {
                        case DEPARTMENT: {
                            List<TournamentDTO> tournamentList = tournamentController.searchTournamentsBySport(
                                searchString
                            );

                            handleReceivedResults(tournamentList);
                            break;
                        }

                        case EVENT_DATE: {
                            List<TournamentDTO> tournamentList = tournamentController.searchTournamentsByDate(
                                searchString
                            );

                            handleReceivedResults(tournamentList);
                            break;
                        }

                        case LOCATION: {
                            List<TournamentDTO> tournamentList = tournamentController.searchTournamentsByLocation(
                                searchString
                            );

                            handleReceivedResults(tournamentList);
                            break;
                        }
                    }

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurred while searching.", e);
                    displayNoResults();
                } catch (ValidationException e) {
                    LOGGER.error("Error occurred while searching.", e);
                    Platform.runLater(() -> GUIHelper.showValidationAlert(NOT_VALID_SEARCH_INPUT));
                    displayNoResults();
                }
            }).start();

        } else {

            new Thread(() -> {
                try {

                    ITournamentController tournamentController = CommunicationFacade.lookupForTournamentController();
                    handleReceivedResults(tournamentController.searchAllTournaments());

                } catch (RemoteException | MalformedURLException | NotBoundException e) {
                    LOGGER.error("Error occurred while searching.", e);
                    displayNoResults();
                }
            }).start();
        }
    }

    @Override
    protected void handleReceivedResults(List<TournamentDTO> rawResults) {
        if (rawResults != null && !rawResults.isEmpty()) {

            Platform.runLater(() -> displayResults(
                rawResults,
                (t1, t2) -> {
                    String s1 = t1.getDate() + t1.getLocation();
                    String s2 = t2.getDate() + t2.getLocation();

                    return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
                }
            ));

        } else {
            displayNoResults();
        }
    }

    private enum SearchType {
        DEPARTMENT("sport"),
        EVENT_DATE("date of the event"),
        LOCATION("location");

        private final String _stringValue;

        SearchType(String stringValue) {
            _stringValue = stringValue;
        }

        @Override
        public String toString() {
            return _stringValue;
        }
    }
}
