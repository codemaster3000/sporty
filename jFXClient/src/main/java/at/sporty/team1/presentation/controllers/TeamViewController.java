package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.JfxController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * represents the View of a Team (Mannschaft)
 */
public class TeamViewController extends JfxController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO Auto-generated method
    }


    // TODO memberDTO needed
    private ObservableList<MemberDTO> _teamMembers =
            FXCollections.observableArrayList(

            );

    /**
     * Constructor
     */
    public TeamViewController() {

    }
}
