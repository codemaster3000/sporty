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
import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class CompetitionViewController extends JfxController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SUCCESSFUL_TOURNAMENT_SAVE = "Tournament was successfully saved.";

    private ObservableList<MatchDTO> matches;
    private ObservableList<String> _tournamentTeams;
    private static TournamentDTO _activeCompetition;

    @FXML private TextField _competitionDateTextField;
    @FXML private TextField _competitionPlaceTextField;
    @FXML private ListView<String> _competitionTeamsListView;
    @FXML private ComboBox<TeamDTO> _teamToCompetitionComboBox;
    @FXML private ComboBox<DepartmentDTO> _tournamentDepartmentCombobox;
    @FXML private ComboBox<DepartmentDTO> _tournamentLeagueCombobox;
    @FXML private TableView<MatchDTO> _matchTableView;
    @FXML private TableColumn<MatchDTO, String> _team1Col;
    @FXML private TableColumn<MatchDTO, String> _team2Col;
    @FXML private TableColumn<MatchDTO, String> _timeCol;
    @FXML private TableColumn<MatchDTO, String> _refereeCol;
    @FXML private TableColumn<MatchDTO, String> _courtCol;
    @FXML private TableColumn<MatchDTO, String> _resultCol;
    @FXML private TextField _competitionExternalTeamTextField;
    @FXML private Button _addTeamButton;
    @FXML private Label _labelTeams;
    @FXML private Label _labelMatches;
    @FXML private Button _buttonRemoveSelectedTeam;
    @FXML private Button _buttonSaveTeams;
    @FXML private Button _saveMatchesButton;
    @FXML private Button _removeSelectedMatch;
    @FXML private Button _openTournamentResults;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        List<DepartmentDTO> departments = null;
        LinkedList<MatchDTO> tempList = new LinkedList<>();

        setVisibleOfTournamentTeamView(false);
        setVisibleOfMatchesView(false);

        /**
         * TournamentView
         */
        try {
            departments = CommunicationFacade.lookupForDepartmentController().searchAllDepartments();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (departments != null) {
            _tournamentDepartmentCombobox.setItems(FXCollections.observableList(departments));
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
        _tournamentDepartmentCombobox.setConverter(departmentDTOStringConverter);

        //TODO: LeagueCombobox
        
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

        matches = FXCollections.observableList(tempList);
        _matchTableView.setItems(matches);

    }

    private void setVisibleOfMatchesView(boolean b) {

        _saveMatchesButton.setVisible(b);
        _removeSelectedMatch.setVisible(b);
        _labelMatches.setVisible(b);
        _matchTableView.setVisible(b);
    }

    private void setVisibleOfTournamentTeamView(boolean b) {
    	
    	 List<TeamDTO> ownTournamentTeams = null;
    	 List<String> teams = null;
    	 
    	 if(b){
    		 /**
	         * Fill TeamCombobox in Tournaments
	         */
	        try {
	            ownTournamentTeams = CommunicationFacade.lookupForTeamController().searchByDepartment(_tournamentDepartmentCombobox.getSelectionModel().getSelectedItem());
	            ObservableList<TeamDTO> teamObservableList = FXCollections.observableList(ownTournamentTeams);
	            _teamToCompetitionComboBox.setItems(teamObservableList);
	        } catch (RemoteException | MalformedURLException | NotBoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        //load teams in listview
	        teams = CommunicationFacade.lookupForTournamentController().getAllTournamentTeams();
	        if (teams != null) {
	            _tournamentTeams = FXCollections.observableList(teams);
	            _competitionTeamsListView.setItems(_tournamentTeams);
	        }
    	 }
    	
        _competitionTeamsListView.setVisible(b);
        _teamToCompetitionComboBox.setVisible(b);
        _competitionExternalTeamTextField.setVisible(b);
        _addTeamButton.setVisible(b);
        _labelTeams.setVisible(b);
        _buttonRemoveSelectedTeam.setVisible(b);
        _buttonSaveTeams.setVisible(b);
        
        
    }

    @FXML
    private void clearForm(ActionEvent event) {
        dispose();
    }

    @Override
    public void dispose() {

        _competitionDateTextField.clear();
        _competitionPlaceTextField.clear();
        _tournamentDepartmentCombobox.getSelectionModel().clearSelection();
        _tournamentLeagueCombobox.getSelectionModel().clearSelection();
    }

    @FXML
    private void saveCompetition(ActionEvent event) {

        String date = _competitionDateTextField.getText();
        DepartmentDTO dept = _tournamentDepartmentCombobox.getSelectionModel().getSelectedItem();
        //TODO: String league = _tournamentLeagueCombobox.getSelectionModel().getSelectedItem().toString();
        String location = _competitionPlaceTextField.getText();

		if((date != null) && (dept != null) && (location != null)){
			
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
                imc.createOrSaveTournament(_activeCompetition);

                GUIHelper.showSuccessAlert(SUCCESSFUL_TOURNAMENT_SAVE);

                //Logging and cleaning the tab
                LOGGER.info("Tournament \"{} {}\" was successfully saved.", dept, date);
                dispose();

            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                LOGGER.error("Error occurs while saving the tournament.", e);
            } catch (ValidationException e) {
            	String context = String.format("Validation exception %s while saving tournament.", e.getCause());
            	
            	GUIHelper.showValidationAlert(context);
				LOGGER.error(context, e);
			}
			
			setVisibleOfTournamentTeamView(true);
		}
        setVisibleOfTournamentTeamView(true);

    }

    @FXML
    public void saveMatches(ActionEvent event) {

        //TODO: save Matches to _activeTournament

    }

    @FXML
    public void removeSelectedMatch(ActionEvent event) {

        //TODO:

    }

    @FXML
    public void addTeamToTeamList(ActionEvent event) {

        //TODO: Team from textfield or Combobox

    }

    @FXML
    public void saveTeamsToTournament(ActionEvent event) {

        //TODO: save Teams to _activeTournament

    }

    @FXML
    public void removeTeamFromTournament(ActionEvent event) {

        //TODO: save Matches to _activeTournament

    }

}
