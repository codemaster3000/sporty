package at.sporty.team1.presentation.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainViewController implements IJfxController {
	@FXML
	private TabPane _tabPanel;
	
	// commit test after new eclipse installation - Carola
	
	@FXML
	private void openNewMemberView()
	{
		
		//ViewLoader viewLoader = ViewLoader.loadView(NewMemberController.class);	
		//Node node = viewLoader.loadNode();
		//Tab t = new Tab(); // oder new Tab(node);
		// t.loadNode(node);
		//NewMemberController c = viewLoader.getController();
		
		//_tabPanel.getTabs().add(t);
		
	}
}