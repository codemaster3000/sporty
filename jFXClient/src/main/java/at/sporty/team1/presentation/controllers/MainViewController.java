package at.sporty.team1.presentation.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import at.sporty.team1.presentation.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainViewController implements IJfxController {
	
	@FXML
	private TabPane _tabPanel;
	
	// commit test after new eclipse installation - Carola
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void openNewMemberView()
	{
		ViewLoader<NewMemberController> viewLoader = ViewLoader.loadView(NewMemberController.class);	
		Node node = viewLoader.loadNode();
		Tab t = new Tab(); 
		t.setText("New Member");
		t.setContent(node);
		NewMemberController c = viewLoader.getController();
		
		_tabPanel.getTabs().add(t);
		
	}	
}