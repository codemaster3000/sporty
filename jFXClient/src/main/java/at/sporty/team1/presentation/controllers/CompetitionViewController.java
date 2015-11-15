package at.sporty.team1.presentation.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.dtos.TeamDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CompetitionViewController extends JfxController{

	@FXML private TextField _competitionDateTextField;
	@FXML private TextField _competitionPlaceTextField;
	@FXML private ListView<String> _competitionTeamsListView;
	@FXML private ComboBox<TeamDTO> _teamToCompetitionComboBox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//TODO:
		//load teams in listview
		
	}
	
	@FXML
	private void addTeamToCompetition(ActionEvent event){
		
		_competitionTeamsListView.getSelectionModel().getSelectedItems().add(_teamToCompetitionComboBox.getSelectionModel().getSelectedItem().getTeamName());
	}
	
	@FXML 
	private void clearForm(ActionEvent event){
		
		dispose();
	}

	@Override
    public void dispose() {

        _competitionDateTextField.clear();
        _competitionPlaceTextField.clear();
    }
}
