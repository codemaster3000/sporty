package at.sporty.team1.presentation.dialogs;

import at.sporty.team1.util.GUIHelper;
import at.sporty.team1.util.SVGContainer;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Supplier;

public class ExtendedChoiceDialog<T extends List<U>, U> extends Dialog<U> {

    private static final Double DEFAULT_SPACING = 5.0;
    private static final Double DEFAULT_MIN_WIDTH = 225.0;

	public ExtendedChoiceDialog(U defaultChoice, T collection, StringConverter<U> collectionConverter) {
        prepareDialog(defaultChoice, collection, collectionConverter);
	}

    private void prepareDialog(U defaultChoice, T collection, StringConverter<U> collectionConverter) {
        setGraphic(GUIHelper.loadSVGGraphic(SVGContainer.CHOICE_ICON));

        //comboBox for collection
        ComboBox<U> comboBox = new ComboBox<>(FXCollections.observableList(collection));
        comboBox.getSelectionModel().select(defaultChoice);
        comboBox.setConverter(collectionConverter);
        comboBox.setMinWidth(DEFAULT_MIN_WIDTH);

        //work-around for contentTextProperty
        Label contentTextLabel = new Label();
        contentTextLabel.textProperty().bind(contentTextProperty());

        //node container
        HBox hBox = new HBox();
        hBox.setSpacing(DEFAULT_SPACING);
        hBox.getChildren().addAll(contentTextLabel, comboBox);

        //setting node container as a content for the dialog
        getDialogPane().setContent(hBox);

        //preparing buttons for dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //defining the result converter for dialog return type
        //setting listener for save button
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return comboBox.getSelectionModel().getSelectedItem();
            }
            return null;
        });
    }
}
