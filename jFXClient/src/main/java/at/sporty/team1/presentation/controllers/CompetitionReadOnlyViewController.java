package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.CommunicationFacade;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.presentation.dialogs.EditViewDialog;
import at.sporty.team1.presentation.dialogs.ExtendedChoiceDialog;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CompetitionReadOnlyViewController extends ConsumerViewController<TournamentDTO> {

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
    @FXML
    private Label _competitionDateTextField;
    @FXML
    private Label _competitionPlaceTextField;
    @FXML
    private Label _leagueLabel;
    @FXML
    private Button _editTournamentButton;
    @FXML
    private Button _createTournamentButton;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        _createTournamentButton.visibleProperty().bind(
            CommunicationFacade.SESSION_AVAILABLE_PROPERTY.and(
            CREATE_VISIBILITY_PROPERTY)
        );

        _editTournamentButton.visibleProperty().bind(
            CommunicationFacade.SESSION_AVAILABLE_PROPERTY.and(
            EDIT_VISIBILITY_PROPERTY.and(
            ACTIVE_DTO.isNotNull()))
        );

        CommunicationFacade.SESSION_AVAILABLE_PROPERTY.addListener((p, newValue, oldValue) -> {
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
        });

        _matchTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        _team1Col.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getTeam1() != null) return new SimpleStringProperty(matchDTO.getTeam1());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _team2Col.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getTeam2() != null) return new SimpleStringProperty(matchDTO.getTeam2());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _timeCol.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getDate() != null) return new SimpleStringProperty(matchDTO.getDate());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _refereeCol.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getReferee() != null) return new SimpleStringProperty(matchDTO.getReferee());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _courtCol.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getLocation() != null) return new SimpleStringProperty(matchDTO.getLocation());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });

        _resultCol.setCellValueFactory(dto -> {
            MatchDTO matchDTO = dto.getValue();

            if (matchDTO != null && matchDTO.getResult() != null) return new SimpleStringProperty(matchDTO.getResult());
            return new SimpleStringProperty(NOT_AVAILABLE);
        });
    }

    @Override
    public void loadDTO(TournamentDTO dto) {
        ACTIVE_DTO.set(dto);
        displayTournamentDTO(dto);
    }

    /**
     * Pre-loads data into all view fields.
     *
     * @param tournamentDTO TournamentDTO that will be preloaded.
     */
    private void displayTournamentDTO(TournamentDTO tournamentDTO) {

        if (tournamentDTO != null) {

            new Thread(() -> {

                try {

                    ITournamentController tournamentController = CommunicationFacade.lookupForTournamentController();

                    //Teams
                    List<String> teams = tournamentController.searchAllTournamentTeams(tournamentDTO.getTournamentId());

                    //Matches
                    List<MatchDTO> matches = tournamentController.searchAllTournamentMatches(tournamentDTO.getTournamentId());

                    Platform.runLater(() -> {
                        //clear old values
                        dispose();

                        _tournamentDepartmentLabel.setText(tournamentDTO.getDepartment().getSport());
                        _competitionDateTextField.setText(tournamentDTO.getDate());
                        _competitionPlaceTextField.setText(tournamentDTO.getLocation());
                   
                        if (teams != null && !teams.isEmpty()) {
                            _competitionTeamsListView.getItems().addAll(FXCollections.observableList(teams));
                        }

                        if (matches != null && !matches.isEmpty()) {
                            _matchTableView.setItems(FXCollections.observableList(matches));
                        }
                    });
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
    private void onEditTournament(ActionEvent event) {
		
		Optional<TournamentDTO> result = new EditViewDialog<>(ACTIVE_DTO.get(), CompetitionEditViewController.class).showAndWait();
        if (result.isPresent()) {
            loadDTO(result.get());
        }
		
    }

    @FXML
    private void onCreateTournament(ActionEvent event) {
    	
    	try {
			List<DepartmentDTO> departments = CommunicationFacade.lookupForDepartmentController().searchAllDepartments();
			
			if(departments != null && !departments.isEmpty()){
				
				ExtendedChoiceDialog<DepartmentDTO> dialog = new ExtendedChoiceDialog<>(departments.get(0), departments);
				dialog.setTitle("Choose Department!");
				dialog.setHeaderText("Please choose Department!!");
				dialog.setContentText("Departments: " );
				
				Optional<DepartmentDTO> departmentContainer = dialog.showAndWait();
				if (departmentContainer.isPresent()) {
					
					TournamentDTO tournament = new TournamentDTO();
					tournament.setDepartment(departmentContainer.get());
					
			        Optional<TournamentDTO> result = new EditViewDialog<>(tournament, CompetitionEditViewController.class).showAndWait();
			        if (result.isPresent()) {
			            loadDTO(result.get());
			        }
				}
			}
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        
    }

    @Override
    public void dispose() {
        _tournamentDepartmentLabel.setText(NOT_AVAILABLE);
        _competitionDateTextField.setText(NOT_AVAILABLE);
        _competitionPlaceTextField.setText(NOT_AVAILABLE);

        _competitionTeamsListView.getItems().clear();
        _matchTableView.getItems().clear();
    }
}
