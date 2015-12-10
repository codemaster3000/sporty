package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.EditViewController;
import at.sporty.team1.shared.api.rmi.IDepartmentControllerRMI;
import at.sporty.team1.shared.api.rmi.ITournamentControllerRMI;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class CompetitionEditViewController extends EditViewController<TournamentDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String VIEW_TEXT_HEADER = "TOURNAMENT EDITING VIEW";
    private static final String SUCCESSFUL_TOURNAMENT_SAVE = "Tournament was saved successfully.";
    private static final String UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE = "Error occurred while saving Teams to Tournament.";
    private static final String UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE = "Error occurred while saving Matches to Tournament.";
    private static final String COLUMN_PROP_TEAM_1 = "team1";
    private static final String COLUMN_PROP_TEAM_2 = "team2";
    private static final String COLUMN_PROP_TIME = "date";
    private static final String COLUMN_PROP_REFEREE = "referee";
    private static final String COLUMN_PROP_COURT = "location";
    private static final String COLUMN_PROP_RESULT = "result";
    private static final Label NO_CONTENT_PLACEHOLDER = new Label("No Content");

    @FXML
    private TextField _competitionDateTextField;
    @FXML
    private TextField _competitionPlaceTextField;
    @FXML
    private ListView<String> _competitionTeamsListView;
    @FXML
    private ComboBox<TeamDTO> _teamToCompetitionComboBox;
    @FXML
    private ComboBox<DepartmentDTO> _tournamentLeagueComboBox;
    @FXML
    private TableView<MatchDTO> _matchTableView;
    @FXML
    private TableColumn<MatchDTO, String> _team1Col;
    @FXML
    private TableColumn<MatchDTO, String> _team2Col;
    @FXML
    private TableColumn<MatchDTO, String> _timeCol;
    @FXML
    private TableColumn<MatchDTO, String> _refereeCol;
    @FXML
    private TableColumn<MatchDTO, String> _courtCol;
    @FXML
    private TableColumn<MatchDTO, String> _resultCol;
    @FXML
    private TextField _competitionExternalTeamTextField;
    @FXML
    private Label _competitionDepartmentLabel;
    @FXML
    private Label _labelTeams;
    @FXML
    private Label _labelMatches;
    @FXML
    private Label _leagueLabel;
    @FXML
    private Button _addTeamButton;
    @FXML
    private Button _buttonRemoveSelectedTeam;
    @FXML
    private Button _removeSelectedMatch;

    private static TournamentDTO _activeTournamentDTO;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Preparing (Tournament) Team View
        _competitionTeamsListView.setPlaceholder(NO_CONTENT_PLACEHOLDER);
        _matchTableView.setPlaceholder(NO_CONTENT_PLACEHOLDER);

        _matchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Adding Delete Functionality to Teams View
        _competitionTeamsListView.setCellFactory(t -> new ListCell<String>() {
            @Override
            protected void updateItem(String teamName, boolean bln) {
                super.updateItem(teamName, bln);

                if (teamName != null) {
                    setText(teamName);

                    MenuItem deleteItem = new MenuItem();
                    deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", teamName));
                    deleteItem.setOnAction(event -> _competitionTeamsListView.getItems().removeAll(getItem()));

                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().add(deleteItem);

                    emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            setContextMenu(null);
                        } else {
                            setContextMenu(contextMenu);
                        }
                    });
                } else {
                    //remove caption from list
                    setText("");
                }
            }
        });

        //Converter from TeamDTO to Team name (String)
        StringConverter<TeamDTO> teamDTOStringConverter = GUIHelper.getDTOToStringConverter(TeamDTO::getTeamName);
        _teamToCompetitionComboBox.setConverter(teamDTOStringConverter);

        //Cell factories for Match View
        _team1Col.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _team1Col.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_TEAM_1));
        _team1Col.setOnEditCommit(cell -> cell.getRowValue().setTeam1(cell.getNewValue()));

        _team2Col.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _team2Col.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_TEAM_2));
        _team2Col.setOnEditCommit(cell -> cell.getRowValue().setTeam2(cell.getNewValue()));

        _timeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _timeCol.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_TIME));
        _timeCol.setOnEditCommit(cell -> cell.getRowValue().setDate(cell.getNewValue()));

        _refereeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _refereeCol.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_REFEREE));
        _refereeCol.setOnEditCommit(cell -> cell.getRowValue().setReferee(cell.getNewValue()));

        _courtCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _courtCol.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_COURT));
        _courtCol.setOnEditCommit(cell -> cell.getRowValue().setLocation(cell.getNewValue()));

        _resultCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _resultCol.setCellValueFactory(new PropertyValueFactory<>(COLUMN_PROP_RESULT));
        _resultCol.setOnEditCommit(cell -> cell.getRowValue().setResult(cell.getNewValue()));

        //League is not implemented yet
        //TODO: LeagueComboBox
        _tournamentLeagueComboBox.setVisible(false);
        _leagueLabel.setVisible(false);
    }

    @Override
    public String getHeaderText() {
        return VIEW_TEXT_HEADER;
    }

    @Override
    public void loadDTO(TournamentDTO dto) {
        IN_WORK_PROPERTY.set(true);

        _activeTournamentDTO = dto;
        displayTournamentDTO(dto);
    }

    /**
     * Pre-loads data into all view fields.
     * @param tournamentDTO TournamentDTO that will be preloaded.
     */
    private void displayTournamentDTO(TournamentDTO tournamentDTO) {

        if (tournamentDTO != null && tournamentDTO.getDepartment() != null) {

            new Thread(() -> {

                try {
                    //Teams (Fill TeamComboBox in Tournaments)
                    DepartmentDTO dept = _activeTournamentDTO.getDepartment();

                    IDepartmentControllerRMI departmentController = CommunicationFacade.lookupForDepartmentController();
                    List<TeamDTO> ownTournamentTeams = departmentController.loadDepartmentTeams(dept.getDepartmentId());

                    if (tournamentDTO.getTournamentId() != null) {
                        //Loading Tournament Data
                        ITournamentControllerRMI tournamentController = CommunicationFacade.lookupForTournamentController();

                        //Loading tournament Teams
                        List<String> teams = tournamentController.searchAllTournamentTeams(
                                _activeTournamentDTO.getTournamentId()
                        );

                        //Loading tournament Matches
                        List<MatchDTO> matches = tournamentController.searchAllTournamentMatches(
                                _activeTournamentDTO.getTournamentId()
                        );

                        Platform.runLater(() -> {
                            //Tournament data
                            _competitionDepartmentLabel.setText(_activeTournamentDTO.getDepartment().getSport());
                            _competitionDateTextField.setText(_activeTournamentDTO.getDate());
                            _competitionPlaceTextField.setText(_activeTournamentDTO.getLocation());

                            //Setting department teams
                            if (ownTournamentTeams != null) {
                                _teamToCompetitionComboBox.setItems(FXCollections.observableList(ownTournamentTeams));
                            }

                            //Setting tournament teams
                            if (teams != null) {
                                _competitionTeamsListView.getItems().addAll(teams);
                            } else {
                                _competitionTeamsListView.setItems(FXCollections.observableList(new LinkedList<>()));
                            }

                            //Setting tournament matches
                            if (matches != null) {
                                _matchTableView.setItems(FXCollections.observableList(matches));
                            } else {
                                _matchTableView.setItems(FXCollections.observableList(new LinkedList<>()));
                            }

                            IN_WORK_PROPERTY.set(false);
                        });

                    } else {

                        Platform.runLater(() -> {
                            //Competition
                            _competitionDepartmentLabel.setText(_activeTournamentDTO.getDepartment().getSport());
                            _competitionDateTextField.setText(_activeTournamentDTO.getDate());
                            _competitionPlaceTextField.setText(_activeTournamentDTO.getLocation());

                            //Setting department teams
                            if (ownTournamentTeams != null) {
                                _teamToCompetitionComboBox.setItems(FXCollections.observableList(ownTournamentTeams));
                            }

                            _competitionTeamsListView.setItems(FXCollections.observableList(new LinkedList<>()));
                            _matchTableView.setItems(FXCollections.observableList(new LinkedList<>()));

                            IN_WORK_PROPERTY.set(false);
                        });
                    }
                } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {
                    LOGGER.error("Error occurred while searching all Teams by Department.", e);
                }
            }).start();
        }
    }

    @Override
    public TournamentDTO saveDTO() {

        if (_activeTournamentDTO != null && _activeTournamentDTO.getDepartment() != null && !IN_WORK_PROPERTY.get()) {

            String date = _competitionDateTextField.getText();
            String location = _competitionPlaceTextField.getText();
            //TODO: String league = _tournamentLeagueComboBox.getSelectionModel().getSelectedItem().toString();

            try {

                //updating changed competition data.
                _activeTournamentDTO.setDate(date).setLocation(location);

                ITournamentControllerRMI imc = CommunicationFacade.lookupForTournamentController();
                Integer competitionId = imc.createOrSaveTournament(
                    _activeTournamentDTO,
                    CommunicationFacade.getActiveSession()
                );

                _activeTournamentDTO.setTournamentId(competitionId);

                saveOrUpdateTournamentTeams(competitionId);
                saveOrUpdateTournamentMatches(competitionId);

                //FIXME we will get success message even if teams and departments will be not assigned
                GUIHelper.showSuccessAlert(SUCCESSFUL_TOURNAMENT_SAVE);

                LOGGER.info(
                    "Tournament \"{} {}\" was successfully saved.",
                    _activeTournamentDTO.getDepartment().getSport(),
                    date
                );

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurred while saving the tournament.", e);
            } catch (ValidationException e) {
                String context = String.format("Validation exception \"%s\" while saving tournament.", e.getCause());

                GUIHelper.showValidationAlert(context);
                LOGGER.error(context, e);
            } catch (NotAuthorisedException e) {
                String context = "Client save (Tournament) request was rejected. Not enough permissions.";

                LOGGER.error(context, e);
                GUIHelper.showErrorAlert(context);
            }
        }

        return null;
    }

    private void saveOrUpdateTournamentTeams(Integer tournamentId) {

        if (tournamentId == null) {
            GUIHelper.showErrorAlert(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
            return;
        }

        try {

            ITournamentControllerRMI tournamentController = CommunicationFacade.lookupForTournamentController();

            for (String team : _competitionTeamsListView.getItems()) {
                tournamentController.assignTeamToTournament(
                    team,
                    tournamentId,
                    CommunicationFacade.getActiveSession()
                );
            }

        } catch (RemoteException | MalformedURLException | NotBoundException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
            LOGGER.error(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE + "[Communication problem]", e);

        } catch (UnknownEntityException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
            LOGGER.error(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE + "[Unknown entity in request]", e);

        } catch (ValidationException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
            LOGGER.error(
                UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE + "[Validation problem \"{}\"]",
                e.getReason(),
                e
            );

        } catch (NotAuthorisedException e) {
            String context = "Client save (Teams to Tournament) request was rejected. Not enough permissions.";

            LOGGER.error(context, e);
            GUIHelper.showErrorAlert(context);
        }
    }

    @FXML
    private void addTeamToTeamList(ActionEvent event) {

        //adding team from ComboBox
        if (_teamToCompetitionComboBox.getSelectionModel().getSelectedItem() != null) {
            _competitionTeamsListView.getItems().add(
                _teamToCompetitionComboBox.getSelectionModel().getSelectedItem().getTeamName()
            );

            _teamToCompetitionComboBox.getSelectionModel().clearSelection();
        }

        //adding team from TextField
        String teamFromTextField = GUIHelper.readNullOrEmpty(_competitionExternalTeamTextField.getText());
        if (teamFromTextField != null && !teamFromTextField.trim().isEmpty()) {

            _competitionTeamsListView.getItems().add(_competitionExternalTeamTextField.getText());
            _competitionExternalTeamTextField.clear();
        }
    }

    @FXML
    private void removeTeamFromTournament(ActionEvent event) {
        _competitionTeamsListView.getItems().remove(
            _competitionTeamsListView.getSelectionModel().getSelectedItem()
        );
    }

    private void saveOrUpdateTournamentMatches(Integer tournamentId) {

        if (tournamentId == null) {
            GUIHelper.showErrorAlert(UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
            return;
        }

    	try {

            ITournamentControllerRMI tournamentController = CommunicationFacade.lookupForTournamentController();

			for(MatchDTO match : _matchTableView.getItems()){
                tournamentController.createNewMatch(
                    tournamentId,
                    match,
                    CommunicationFacade.getActiveSession()
                );
            }

		} catch (RemoteException | MalformedURLException | NotBoundException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE);
            LOGGER.error(UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE + "[Communication problem]", e);

        } catch (UnknownEntityException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE);
            LOGGER.error(UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE + "[Unknown entity in request]", e);

        } catch (ValidationException e) {

            GUIHelper.showErrorAlert(UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE);
            LOGGER.error(
                UNSUCCESSFUL_MATCH_TO_TOURNAMENT_SAVE + "[Validation problem \"{}\"]",
                e.getReason(),
                e
            );

        } catch (NotAuthorisedException e) {
            String context = "Client save (Match) request was rejected. Not enough permissions.";

            LOGGER.error(context, e);
            GUIHelper.showErrorAlert(context);
        }
    }

    @FXML
    private void addNewMatchRowButton(ActionEvent event) {
        _matchTableView.getItems().add(new MatchDTO());

        if (!_matchTableView.getItems().isEmpty()) {
            _matchTableView.getSelectionModel().selectLast();
            _matchTableView.requestFocus();
        }
    }

    @FXML
    private void removeSelectedMatch(ActionEvent event) {
        MatchDTO matchDTO = _matchTableView.getSelectionModel().getSelectedItem();
        _matchTableView.getItems().remove(matchDTO);
    }
}
