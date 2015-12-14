package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.facades.api.IMemberControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.presentation.controllers.core.SearchViewController;
import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class MemberSearchViewController extends SearchViewController<MemberDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final CommunicationFacade COMMUNICATION_FACADE = CommunicationFacade.getInstance();
    private static final String MEMBER_LAST_NAME_COLUMN = "LAST NAME";
    private static final String MEMBER_FIRST_NAME_COLUMN = "FIRST NAME";
    private static final String MEMBER_FEE_PAID_COLUMN = "FEE PAID";
    private static final String FEE_PAID_SYMBOL = "✓";
    private static final String FEE_NOT_PAID_SYMBOL = "✗";

    @FXML private VBox _searchBox;
    @FXML private ComboBox<SearchType> _searchType;
    @FXML private RadioButton _allRadioButton;
    @FXML private RadioButton _paidRadioButton;
    @FXML private RadioButton _notPaidRadioButton;

    private final TableView<MemberDTO> _resultTable;

    public MemberSearchViewController() {
        _resultTable = getResultTableInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* Setting toggle group for radio buttons */
        ToggleGroup group = new ToggleGroup();
        _allRadioButton.setToggleGroup(group);
        _paidRadioButton.setToggleGroup(group);
        _notPaidRadioButton.setToggleGroup(group);

        /* Adding search types to search type ComboBox */
        _searchType.setItems(FXCollections.observableList(Arrays.asList(SearchType.values())));
        _searchType.getSelectionModel().select(SearchType.MEMBER_NAME);

        /* Adding result table to _searchBox*/
        TableColumn<MemberDTO, String> lastNameColumn = new TableColumn<>(MEMBER_LAST_NAME_COLUMN);
        TableColumn<MemberDTO, String> firstNameColumn = new TableColumn<>(MEMBER_FIRST_NAME_COLUMN);
        TableColumn<MemberDTO, String> feePaidColumn = new TableColumn<>(MEMBER_FEE_PAID_COLUMN);

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

        _searchBox.getChildren().add(_resultTable);
    }

    @Override
    public void search(String searchString) {
        showProgressAnimation();

        if (searchString != null) {

            new Thread(() -> {
                try {

                    IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();

                    //Performing search depending on selected search type
                    switch (_searchType.getValue()) {
                        case MEMBER_NAME: {
                            List<MemberDTO> memberList = memberController.searchMembersByNameString(
                                searchString,
                                readIsFeePaidState(
                                    _paidRadioButton.isSelected(),
                                    _notPaidRadioButton.isSelected()
                                ),
                                COMMUNICATION_FACADE.getActiveSession()
                            );

                            handleReceivedResults(memberList);
                            break;
                        }

                        case DATE_OF_BIRTH: {
                            List<MemberDTO> memberList = memberController.searchMembersByDateOfBirth(
                                searchString,
                                readIsFeePaidState(
                                    _paidRadioButton.isSelected(),
                                    _notPaidRadioButton.isSelected()
                                ),
                                COMMUNICATION_FACADE.getActiveSession()
                            );

                            handleReceivedResults(memberList);
                            break;
                        }

                        case COMMON_TEAM_NAME: {
                            List<MemberDTO> memberList = memberController.searchMembersByCommonTeamName(
                                searchString,
                                readIsFeePaidState(
                                    _paidRadioButton.isSelected(),
                                    _notPaidRadioButton.isSelected()
                                ),
                                COMMUNICATION_FACADE.getActiveSession()
                            );

                            handleReceivedResults(memberList);
                            break;
                        }

                        case TOURNAMENT_TEAM_NAME: {
                            List<MemberDTO> memberList = memberController.searchMembersByTournamentTeamName(
                                searchString,
                                readIsFeePaidState(
                                    _paidRadioButton.isSelected(),
                                    _notPaidRadioButton.isSelected()
                                ),
                                COMMUNICATION_FACADE.getActiveSession()
                            );

                            handleReceivedResults(memberList);
                            break;
                        }
                    }

                } catch (RemoteCommunicationException e) {
                    LOGGER.error("Error occurred while searching for members.", e);
                    displayNoResults();
                } catch (ValidationException e) {
                    LOGGER.error("Error occurred while searching for members.", e);
                    Platform.runLater(() -> GUIHelper.showValidationAlert(NOT_VALID_SEARCH_INPUT));
                    displayNoResults();
                } catch (NotAuthorisedException e) {
                    LOGGER.error("Client search (Member) request was rejected. Not enough permissions.", e);
                    displayNoResults();
                }
            }).start();

        } else {

            new Thread(() -> {
                try {

                    IMemberControllerUniversal memberController = COMMUNICATION_FACADE.lookupForMemberController();
                    List<MemberDTO> memberList = memberController.searchAllMembers(
                        readIsFeePaidState(
                            _paidRadioButton.isSelected(),
                            _notPaidRadioButton.isSelected()
                        ),
                        COMMUNICATION_FACADE.getActiveSession()
                    );

                    handleReceivedResults(memberList);

                } catch (RemoteCommunicationException e) {
                    LOGGER.error("Error occurred while searching for members.", e);
                    displayNoResults();
                } catch (NotAuthorisedException e) {
                    LOGGER.error("Client search (Member) request was rejected. Not enough permissions.", e);
                    displayNoResults();
                }
            }).start();
        }
    }

    @Override
    protected void handleReceivedResults(List<MemberDTO> rawResults) {
        if (rawResults != null && !rawResults.isEmpty()) {

            Platform.runLater(() -> displayResults(
                rawResults,
                (m1, m2) -> {
                    String s1 = m1.getLastName() + m1.getFirstName();
                    String s2 = m2.getLastName() + m2.getFirstName();

                    return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
                }
            ));

        } else {
            displayNoResults();
        }
    }

    private Boolean readIsFeePaidState(boolean isPaidState, boolean isNotPaidState) {
        if (isPaidState == isNotPaidState) {
            return null;
        }
        return isPaidState;
    }

    private enum SearchType {
        MEMBER_NAME("member name"),
        DATE_OF_BIRTH("date of birth"),
        COMMON_TEAM_NAME("common team name"),
        TOURNAMENT_TEAM_NAME("tournament team name");

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
