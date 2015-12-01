package at.sporty.team1.presentation.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.controllers.core.EditViewController;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class CompetitionEditViewController extends EditViewController<TournamentDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String VIEW_TEXT_HEADER = "TOURNAMENT EDITING VIEW";
    private static final String SUCCESSFUL_TOURNAMENT_SAVE = "Tournament was saved successfully.";
    private static final String SUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE = " Tournament Teams were saved successfully.";
    private static final String UNSUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE = "Error occurred while saving Teams to Tournament.";
    private static final Label NO_CONTENT_PLACEHOLDER = new Label("No Content");
    private static final String SUCCESSFUL_MATCHES_SAVE = "Matches were saved successfully.";
    private static final String CANNOT_LOAD_TEAMS = "Cannot load Teams.";


    private static TournamentDTO _activeCompetition;
    private ObservableList<MatchDTO> _tableMatchList;

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
            LOGGER.error("Error occurred while loading all known departments.", e);
        }

        if (departments != null) {
            _tournamentDepartmentComboBox.setItems(FXCollections.observableList(departments));
        }

        /**
         * Converter from TeamDTO to Team name (String)
         */
        StringConverter<DepartmentDTO> deptDTOConverter = GUIHelper.getDTOToStringConverter(DepartmentDTO::getSport);
        _tournamentDepartmentComboBox.setConverter(deptDTOConverter);

        //TODO: LeagueComboBox
        
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
        _team1Col.setOnEditCommit(cell -> cell.getRowValue().setTeam1(cell.getNewValue()));

        _team2Col.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));
        _team2Col.setOnEditCommit(cell -> cell.getRowValue().setTeam2(cell.getNewValue()));

        _timeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        _timeCol.setOnEditCommit(cell -> cell.getRowValue().setDate(cell.getNewValue()));

        _refereeCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _refereeCol.setCellValueFactory(new PropertyValueFactory<>("referee"));
        _refereeCol.setOnEditCommit(cell -> cell.getRowValue().setReferee(cell.getNewValue()));

        _courtCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _courtCol.setCellValueFactory(new PropertyValueFactory<>("court"));
        _courtCol.setOnEditCommit(cell -> cell.getRowValue().setLocation(cell.getNewValue()));

        _resultCol.setCellFactory(TextFieldTableCell.<MatchDTO>forTableColumn());
        _resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        _resultCol.setOnEditCommit(cell -> cell.getRowValue().setResult(cell.getNewValue()));

        ObservableList<MatchDTO> _tableMatchList = FXCollections.observableList(tempList);
        _matchTableView.setItems(FXCollections.observableList(_tableMatchList));
    }

    @Override
    public void loadDTO(TournamentDTO dto) {
        displayTournamentDTO(dto);
    }

    private void setVisibleOfMatchesView(boolean view) {
        _saveMatchesButton.setVisible(view);
        _removeSelectedMatch.setVisible(view);
        _labelMatches.setVisible(view);
        _matchTableView.setVisible(view);
    }

    private void setVisibleOfTournamentTeamView(boolean view) {

        if (view) {
        	_competitionTeamsListView.setPlaceholder(NO_CONTENT_PLACEHOLDER);

            /**
             * Fill TeamCombobox in Tournaments
             */
            try {

                IDepartmentController departmentController = CommunicationFacade.lookupForDepartmentController();
                DepartmentDTO dept = _tournamentDepartmentComboBox.getSelectionModel().getSelectedItem();
                List<TeamDTO> ownTournamentTeams = departmentController.loadDepartmentTeams(dept.getDepartmentId());

                ObservableList<TeamDTO> teamObservableList = FXCollections.observableList(ownTournamentTeams);
                _teamToCompetitionComboBox.setItems(teamObservableList);

            } catch (RemoteException | MalformedURLException | NotBoundException | UnknownEntityException e) {
            	LOGGER.error("Error occurred while searching all Teams by Department.", e);
            }

            /**
             * Converter from TeamDTO to Team name (String)
             */
            StringConverter<TeamDTO> departmentDTOStringConverter = GUIHelper.getDTOToStringConverter(TeamDTO::getTeamName);
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
    	//Competition
        _competitionDateTextField.clear();
        _competitionPlaceTextField.clear();
        _tournamentDepartmentComboBox.getSelectionModel().clearSelection();
        _tournamentLeagueComboBox.getSelectionModel().clearSelection();
        //Team
        _teamToCompetitionComboBox.getSelectionModel().clearSelection();
        _competitionExternalTeamTextField.clear();
        _competitionTeamsListView.getItems().clear();
        //Matches
        _matchTableView.getItems().clear();
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

                //if it is an already existing competition, changed competition data will be simply updated.
                _activeCompetition.setDate(date)
                        .setDepartment(dept)
                        .setLocation(location);

                ITournamentController imc = CommunicationFacade.lookupForTournamentController();
                Integer competitionId = imc.createOrSaveTournament(
                    _activeCompetition,
                    CommunicationFacade.getActiveSession()
                );

                _activeCompetition.setTournamentId(competitionId);

                GUIHelper.showSuccessAlert(SUCCESSFUL_TOURNAMENT_SAVE);
                setVisibleOfTournamentTeamView(true);

                LOGGER.info("Tournament \"{} {}\" was successfully saved.", dept.getSport(), date);

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurred while saving the tournament.", e);
            } catch (ValidationException e) {
                String context = String.format("Validation exception \"%s\" while saving tournament.", e.getCause());

                GUIHelper.showValidationAlert(context);
                LOGGER.error(context, e);
            } catch (NotAuthorisedException e) {
                LOGGER.error("Client save (Tournament) request was rejected. Not enough permissions.", e);
            }
        }
    }

    @FXML
    public void saveMatches(ActionEvent event) {

        //TODO: save Matches to _activeTournament
    	ITournamentController imc = null;
    	int tournamentId = _activeCompetition.getTournamentId();
    	
    	try {
			imc = CommunicationFacade.lookupForTournamentController();
			
			List<MatchDTO> matches = _matchTableView.getItems();
			
			for(MatchDTO match : matches){
				try {
					imc.createNewMatch(tournamentId, match, CommunicationFacade.getActiveSession());
				} catch (ValidationException e) {
                    String context = String.format("Validation exception \"%s\" while saving match.", e.getCause());

                    GUIHelper.showValidationAlert(context);
                    LOGGER.error(context, e);
				} catch (UnknownEntityException e) {
                    LOGGER.error("DTO was not saved in Data Storage before assigning Match to Tournament.", e);
                } catch (NotAuthorisedException e) {
                    LOGGER.error("Client save (Match) request was rejected. Not enough permissions.", e);
                }
            }
			LOGGER.info("Matches was successfully saved. Size:\"{}, TournamentId: {}\"", matches.size(), tournamentId);
			GUIHelper.showSuccessAlert(SUCCESSFUL_MATCHES_SAVE);
			
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			LOGGER.error("Error occurred while saving the matches.", e);
		}
    }

    @FXML
    public void removeSelectedMatch(ActionEvent event) {
        MatchDTO matchDTO = _matchTableView.getSelectionModel().getSelectedItem();
        _matchTableView.getItems().remove(matchDTO);
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
                    tournamentController.assignTeamToTournament(
                        team,
                        _activeCompetition.getTournamentId(),
                        CommunicationFacade.getActiveSession()
                    );
                    ++savedTeamsCounter;
                }

                GUIHelper.showSuccessAlert(savedTeamsCounter + SUCCESSFUL_TEAM_TO_TOURNAMENT_SAVE);
                setVisibleOfMatchesView(true);
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
            LOGGER.error("Client save (Teams to Tournament) request was rejected. Not enough permissions.", e);
        }
    }

    @FXML
    private void removeTeamFromTournament(ActionEvent event) {
        _competitionTeamsListView.getItems().remove(_competitionTeamsListView.getSelectionModel().getSelectedItem());
    }

    
    /**
     * Pre-loads data into all view fields.
     * @param tournamentDTO TournamentDTO that will be preloaded.
     */
    private void displayTournamentDTO(TournamentDTO tournamentDTO) {

        //clear form
        dispose();

        if (tournamentDTO != null) {
            _activeCompetition = tournamentDTO;

            //Competition
            _competitionDateTextField.setText(_activeCompetition.getDate());
            _competitionPlaceTextField.setText(_activeCompetition.getLocation());
            _tournamentDepartmentComboBox.getSelectionModel().select(_activeCompetition.getDepartment());

           //Teams
            try {
				List<String> teams = CommunicationFacade.lookupForTournamentController().searchAllTournamentTeams(_activeCompetition.getTournamentId());
				_competitionTeamsListView.getItems().addAll(teams);
			} catch (RemoteException | MalformedURLException | UnknownEntityException | NotBoundException e) {
				GUIHelper.showErrorAlert(CANNOT_LOAD_TEAMS);
	            LOGGER.error(CANNOT_LOAD_TEAMS + "[Communication problem]", e);
			}
            
            //Matches
            try {
				List<MatchDTO> matches = CommunicationFacade.lookupForTournamentController().searchAllTournamentMatches(_activeCompetition.getTournamentId());
				
			} catch (RemoteException | MalformedURLException | UnknownEntityException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
    }

	@Override
	public String getHeaderText() {
		return VIEW_TEXT_HEADER;
	}

	@Override
	public TournamentDTO saveDTO() {
		return _activeCompetition;
	}

    @FXML
    private void addNewMatchRowButton() {
        _matchTableView.getItems().add(new MatchDTO());
    }
}
