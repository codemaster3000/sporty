package at.sporty.team1.presentation.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.api.ITeamController;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class CompetitionViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUCCESSFUL_TOURNAMENT_SAVE = "Tournament was saved successfully.";
    private static final String SUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE = " Tournament Teams were saved successfully.";
    private static final String UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE = "Error occurred while saving Teams to Tournament.";
    private static final Label NO_CONTENT_PLACEHOLDER = new Label("No Content");

    private TournamentDTO _activeCompetition;

    @FXML
    private TextField _competitionDateTextField;
    @FXML
    private TextField _competitionPlaceTextField;
    @FXML
    private ListView<String> _competitionTeamsListView;
    @FXML
    private ComboBox<TeamDTO> _teamToCompetitionComboBox;
    @FXML
    private ComboBox<DepartmentDTO> _tournamentDepartmentComboBox;
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
    private Button _addTeamButton;
    @FXML
    private Label _labelTeams;
    @FXML
    private Label _labelMatches;
    @FXML
    private Label _leagueLabel;
    @FXML
    private Button _buttonRemoveSelectedTeam;
    @FXML
    private Button _buttonSaveTeams;
    @FXML
    private Button _saveMatchesButton;
    @FXML
    private Button _removeSelectedMatch;
    @FXML
    private Button _openTournamentResults;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        List<DepartmentDTO> departments = null;
        LinkedList<MatchDTO> tempList = new LinkedList<>();

        setVisibleOfTournamentTeamView(false);
        setVisibleOfMatchesView(false);

        /**
         * League is not implemented yet
         */
        _tournamentLeagueComboBox.setVisible(false);
        _leagueLabel.setVisible(false);

        /**
         * TournamentView
         */
        try {
            departments = CommunicationFacade.lookupForDepartmentController().searchAllDepartments();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
           
            e.printStackTrace();
        }

        if (departments != null) {
            _tournamentDepartmentComboBox.setItems(FXCollections.observableList(departments));
        }

        /**
         * Converter from TeamDTO to Team name (String)
         */
        StringConverter<DepartmentDTO> departmentDTOStringConverter = new StringConverter<DepartmentDTO>() {
            @Override
            public String toString(DepartmentDTO departmentDTO) {
                if (departmentDTO != null) {
                    return departmentDTO.getSport();
                }
                return null;
            }

            @Override
            public DepartmentDTO fromString(String string) {
                return null;
            }
        };
        _tournamentDepartmentComboBox.setConverter(departmentDTOStringConverter);

        //TODO: LeagueCombobox
        
        /**
         * addTeamsView
         */
        _competitionTeamsListView.setCellFactory(m -> new ListCell<String>() {
            @Override
            protected void updateItem(String teamname, boolean bln) {
                super.updateItem(teamname, bln);

                if (teamname != null) {
                    setText(teamname);

                    MenuItem deleteItem = new MenuItem();
                    deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", teamname));
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

        /**
         * Matchesview in Tournament
         */
        _team1Col.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));

        _team2Col.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));

        _timeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        _refereeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _refereeCol.setCellValueFactory(new PropertyValueFactory<>("referee"));

        _courtCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _courtCol.setCellValueFactory(new PropertyValueFactory<>("court"));

        _resultCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        for (int i = 0; i < 20; ++i) {
            tempList.add(new MatchDTO());
        }

        _matchTableView.setItems(FXCollections.observableList(tempList));
    }

    private void setVisibleOfMatchesView(boolean view) {

        _saveMatchesButton.setVisible(view);
        _removeSelectedMatch.setVisible(view);
        _labelMatches.setVisible(view);
        _matchTableView.setVisible(view);
        _openTournamentResults.setVisible(view);
    }

    private void setVisibleOfTournamentTeamView(boolean view) {

        if (view) {
        	_competitionTeamsListView.setPlaceholder(NO_CONTENT_PLACEHOLDER);

            /**
             * Fill TeamCombobox in Tournaments
             */
            try {

                IDepartmentController departmentController = CommunicationFacade.lookupForDepartmentController();

                List<TeamDTO> ownTournamentTeams = departmentController.loadDepartmentTeams(
                    _tournamentDepartmentComboBox.getSelectionModel().getSelectedItem().getDepartmentId()
                );

                ObservableList<TeamDTO> teamObservableList = FXCollections.observableList(ownTournamentTeams);
                _teamToCompetitionComboBox.setItems(teamObservableList);

            } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {
            	LOGGER.error("Error occurred while searching all Teams by Department.", e);
            }
            
            /**
             * Converter from TeamDTO to Team name (String)
             */
            StringConverter<TeamDTO> departmentDTOStringConverter = new StringConverter<TeamDTO>() {
                @Override
                public String toString(TeamDTO teamDTO) {
                    if (teamDTO != null) {
                        return teamDTO.getTeamName();
                    }
                    return null;
                }

                @Override
                public TeamDTO fromString(String string) {
                    return null;
                }
            };
            _teamToCompetitionComboBox.setConverter(departmentDTOStringConverter);
        }

        _competitionTeamsListView.setVisible(view);
        _teamToCompetitionComboBox.setVisible(view);
        _competitionExternalTeamTextField.setVisible(view);
        _addTeamButton.setVisible(view);
        _labelTeams.setVisible(view);
        _buttonRemoveSelectedTeam.setVisible(view);
        _buttonSaveTeams.setVisible(view);
    }

    @FXML
    private void clearForm(ActionEvent event) {
        dispose();
    }

    @Override
    public void dispose() {

        _competitionDateTextField.clear();
        _competitionPlaceTextField.clear();
        _tournamentDepartmentComboBox.getSelectionModel().clearSelection();
        _tournamentLeagueComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void saveCompetition(ActionEvent event) {

        String date = _competitionDateTextField.getText();
        DepartmentDTO dept = _tournamentDepartmentComboBox.getSelectionModel().getSelectedItem();
        //TODO: String league = _tournamentLeagueComboBox.getSelectionModel().getSelectedItem().toString();
        String location = _competitionPlaceTextField.getText();

        if ((date != null) && (dept != null) && (location != null)) {

            try {

                //check if we are creating a new or editing an existing Competition
                if (_activeCompetition == null) {
                    //this is a new Competition
                    _activeCompetition = new TournamentDTO();
                }

                //if it is an already existing member, changed member data will be simply updated.
                _activeCompetition.setDate(date)
                        .setDepartment(dept)
                        .setLocation(location);

                ITournamentController imc = CommunicationFacade.lookupForTournamentController();
                Integer competitionId = imc.createOrSaveTournament(_activeCompetition);
                _activeCompetition.setId(competitionId); //FIXME (Sergii) avoid DTO better own property

                GUIHelper.showSuccessAlert(SUCCESSFUL_TOURNAMENT_SAVE);
                setVisibleOfTournamentTeamView(true);

                //Logging and cleaning the tab
                LOGGER.info("Tournament \"{} {}\" was successfully saved.", dept.getSport(), date);
                dispose();

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurred while saving the tournament.", e);
            } catch (ValidationException e) {
                String context = String.format("Validation exception \"%s\" while saving tournament.", e.getCause());

                GUIHelper.showValidationAlert(context);
                LOGGER.error(context, e);
            }
        }
    }

    @FXML
    public void saveMatches(ActionEvent event) {

        //TODO: save Matches to _activeTournament
    }

    @FXML
    public void removeSelectedMatch(ActionEvent event) {

    	_matchTableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void addTeamToTeamList(ActionEvent event) {

    	String teamFromTextField = GUIHelper.readNullOrEmpty(_competitionExternalTeamTextField.getText());
    	
    	
        if (teamFromTextField != null) {
        	_competitionTeamsListView.getItems().add(_competitionExternalTeamTextField.getText());
            
        }else if (_teamToCompetitionComboBox.getSelectionModel().getSelectedItem() != null) {
        	_competitionTeamsListView.getItems().add(_teamToCompetitionComboBox.getSelectionModel().getSelectedItem().getTeamName());
            
        }
        _competitionExternalTeamTextField.clear();
        _teamToCompetitionComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void saveTeamsToTournament(ActionEvent event) {

        List<String> teams = _competitionTeamsListView.getItems();


        try {
            ITournamentController tournamentController = CommunicationFacade.lookupForTournamentController();

            if ((_activeCompetition != null) && (teams != null) && (!teams.isEmpty())) {

                int savedTeamsCounter = 0;

                for (String team : teams) {
                    tournamentController.assignTeamToTournament(team, _activeCompetition.getId());
                    ++savedTeamsCounter;
                }

                GUIHelper.showSuccessAlert(savedTeamsCounter + SUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
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

        }
    }

    @FXML
    public void removeTeamFromTournament(ActionEvent event) {

        _competitionTeamsListView.getItems().remove(_competitionTeamsListView.getSelectionModel().getSelectedItem());
    }
}
