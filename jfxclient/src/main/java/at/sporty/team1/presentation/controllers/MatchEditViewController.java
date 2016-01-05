package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.EditViewController;
import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.shared.dtos.MatchDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 05-01-2016.
 */
public class MatchEditViewController extends EditViewController<MatchDTO> {
    private static final String VIEW_TEXT_HEADER = "MATCH EDITING VIEW";
    private static final String SUCCESSFUL_MATCH_SAVE = "Match was successfully saved (parent tournament should be saved as well if you want to publish your changes).";

    @FXML
    private ComboBox<String> _team1ComboBox;
    @FXML
    private ComboBox<String> _team2ComboBox;
    @FXML
    private TextField _dateTextField;
    @FXML
    private TextField _refereeTextField;
    @FXML
    private TextField _courtTextField;
    @FXML
    private TextField _resultTeam1TextField;
    @FXML
    private TextField _resultTeam2TextField;
    @FXML
    private CheckBox _isFinalResultsCheckBox;

    private static MatchDTO _activeMatchDTO;
    private static List<String> _teamList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public String getHeaderText() {
        return VIEW_TEXT_HEADER;
    }

    @Override
    public MatchDTO saveDTO() {
        if (_activeMatchDTO != null) {

            _activeMatchDTO.setTeam1(GUIHelper.readNullOrEmpty(
                _team1ComboBox.getSelectionModel().getSelectedItem()
            ));

            _activeMatchDTO.setTeam2(GUIHelper.readNullOrEmpty(
                _team2ComboBox.getSelectionModel().getSelectedItem()
            ));

            _activeMatchDTO.setDate(GUIHelper.readNullOrEmpty(_dateTextField.getText()));
            _activeMatchDTO.setReferee(GUIHelper.readNullOrEmpty(_refereeTextField.getText()));
            _activeMatchDTO.setLocation(GUIHelper.readNullOrEmpty(_courtTextField.getText()));
            _activeMatchDTO.setResultTeam1(GUIHelper.readNullOrEmpty(_resultTeam1TextField.getText()));
            _activeMatchDTO.setResultTeam2(GUIHelper.readNullOrEmpty(_resultTeam2TextField.getText()));
            _activeMatchDTO.setIsFinalResults(_isFinalResultsCheckBox.isSelected());
        }

        GUIHelper.showSuccessAlert(SUCCESSFUL_MATCH_SAVE);
        return _activeMatchDTO;
    }

    @Override
    public void loadDTO(MatchDTO dto) {
        _activeMatchDTO = dto;
        displayMatchDTO(dto);
    }

    public void loadTeams(List<String> teamList) {
        _teamList = teamList;

        Platform.runLater(() -> {
            _team1ComboBox.setItems(FXCollections.observableList(_teamList));
            _team2ComboBox.setItems(FXCollections.observableList(_teamList));
        });
    }

    private void displayMatchDTO(MatchDTO matchDTO) {
        IN_WORK_PROPERTY.setValue(true);

        if (matchDTO != null) {

            loadTeams(_teamList);

            _team1ComboBox.getSelectionModel().select(matchDTO.getTeam1());
            _team2ComboBox.getSelectionModel().select(matchDTO.getTeam2());
            _dateTextField.setText(matchDTO.getDate());
            _refereeTextField.setText(matchDTO.getReferee());
            _courtTextField.setText(matchDTO.getLocation());
            _resultTeam1TextField.setText(matchDTO.getResultTeam1());
            _resultTeam2TextField.setText(matchDTO.getResultTeam2());
            _isFinalResultsCheckBox.setSelected(matchDTO.getIsFinalResults() != null && matchDTO.getIsFinalResults());

        } else dispose();

        IN_WORK_PROPERTY.setValue(false);
    }

    @Override
    public void dispose() {
        _dateTextField.clear();
        _refereeTextField.clear();
        _courtTextField.clear();
        _resultTeam1TextField.clear();
        _resultTeam2TextField.clear();
        _isFinalResultsCheckBox.setSelected(false);

        _team1ComboBox.getSelectionModel().select(null);
        _team1ComboBox.setValue(null);

        _team2ComboBox.getSelectionModel().select(null);
        _team2ComboBox.setValue(null);
    }
}
