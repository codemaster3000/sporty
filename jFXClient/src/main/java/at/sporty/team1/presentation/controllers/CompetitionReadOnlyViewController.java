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
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.DTOPair;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
    private Label _tournamentDepartmentLabel;
    @FXML private Label _competitionDateTextField;
    @FXML private Label _competitionPlaceTextField;
    @FXML
    private Label _leagueLabel;
    @FXML private Button _editTournamentButton;
    @FXML private Button _createTournamentButton;
   
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		_createTournamentButton.visibleProperty().bind(CREATE_VISIBILITY_PROPERTY);
		_editTournamentButton.visibleProperty().bind(EDIT_VISIBILITY_PROPERTY.and(ACTIVE_DTO.isNotNull()));
		
		if (CommunicationFacade.getExtendedActiveSession() != null) {
            String role = CommunicationFacade.getExtendedActiveSession().getUser().getRole();

            //enabling gui options for specific roles
            switch (role) {
                case "departmentHead": {
                    EDIT_VISIBILITY_PROPERTY.set(true);
                    CREATE_VISIBILITY_PROPERTY.set(true);
                    break;
                }

                case "admin": {
                    EDIT_VISIBILITY_PROPERTY.set(true);
                    CREATE_VISIBILITY_PROPERTY.set(true);
                    break;
                }
            }
        }
	}

	@Override
	public void loadDTO(TournamentDTO dto) {
		ACTIVE_DTO.set(dto);
        displayTournamentDTO(dto);
		
	}
	
	 /**
     * Pre-loads data into all view fields.
     * @param tournamentDTO TournamentDTO that will be preloaded.
     */
    private void displayTournamentDTO(TournamentDTO tournamentDTO) {      

        if (tournamentDTO != null) {

            new Thread(() -> {

                try {

                    ITournamentController tournamentController = CommunicationFacade.lookupForTournamentController();

                    Platform.runLater(() -> {

                        //clearing old values
                        dispose();

                        _tournamentDepartmentLabel.setText(tournamentDTO.getDepartment().getSport());
                        _competitionDateTextField.setText(tournamentDTO.getDate());
                        _competitionPlaceTextField.setText(tournamentDTO.getLocation());
                        
                        //ListView with teams
                        List<String> temp = null;
						try {
							temp = tournamentController.searchAllTournamentTeams(tournamentDTO.getTournamentId());
						} catch (Exception e) {							
							LOGGER.error(e);
						}
						
                        if(temp != null && !temp.isEmpty()){
                        	ObservableList<String> teams = FXCollections.observableList(temp);
	                        _competitionTeamsListView.getItems().addAll(teams);
                        }
                    });
                    
                    //Matches

					List<MatchDTO> matches = tournamentController.searchAllTournamentMatches(tournamentDTO.getTournamentId());
					
					if(matches != null && !matches.isEmpty()){
						Platform.runLater(() ->
							_matchTableView.getItems().addAll(FXCollections.observableList(matches))
						);
					}

                    
                } catch (RemoteException | MalformedURLException | NotBoundException e) {

                    LOGGER.error("Error occurred while loading Tournament data.", e);
                    Platform.runLater(() ->
                        GUIHelper.showErrorAlert("Error occurred while loading Tournament data.")
                    );
                } catch (UnknownEntityException e) {

                    LOGGER.error("Error occurred while loading Matches.", e);
                }
            }).start();
        }
    }
	
	@FXML
	public void onEditTournament(ActionEvent event){
		
	}
	
	@FXML
	public void onCreateTournament(ActionEvent event){
		
	}

}
