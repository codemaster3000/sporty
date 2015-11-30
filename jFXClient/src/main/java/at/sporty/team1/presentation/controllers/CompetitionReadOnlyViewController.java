package at.sporty.team1.presentation.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CompetitionReadOnlyViewController  extends ConsumerViewController<TournamentDTO> {

	private static final Logger LOGGER = LogManager.getLogger();
    private static final String NOT_AVAILABLE = "N/A";
    private static final SimpleBooleanProperty EDIT_VISIBILITY_PROPERTY = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty CREATE_VISIBILITY_PROPERTY = new SimpleBooleanProperty(false);
    private static final SimpleObjectProperty<TournamentDTO> ACTIVE_DTO = new SimpleObjectProperty<>();
	
   
    @FXML
    private ListView<String> _competitionTeamsListView; 
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
    private Label _labelTeams;
    @FXML
    private Label _labelMatches;
    @FXML
    private Label _leagueLabel;
   
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadDTO(TournamentDTO dto) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void onEditTournament(ActionEvent event){
		
	}
	
	@FXML
	public void onCreateTournament(ActionEvent event){
		
	}

}
