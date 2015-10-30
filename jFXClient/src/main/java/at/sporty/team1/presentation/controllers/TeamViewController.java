package at.sporty.team1.presentation.controllers;

import at.sporty.team1.rmi.dtos.MemberDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * represents the View of a Team (Mannschaft)
 */
public class TeamViewController {


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
