package at.sporty.team1.presentation.dialogs;

import at.sporty.team1.presentation.controllers.core.EditViewController;
import at.sporty.team1.presentation.util.GUIHelper;
import at.sporty.team1.presentation.util.SVGContainer;
import at.sporty.team1.presentation.util.ViewLoader;
import at.sporty.team1.shared.api.IDTO;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Created by sereGkaluv on 28-Nov-15.
 */
public class EditViewDialog<T extends IDTO, U extends EditViewController<T>> extends Dialog<T> {
    private static final String DIALOG_TITLE = "Edit View";
    private static final String BUTTON_SAVE_CAPTION = "Save";

    public EditViewDialog(T dto, Class<U> controllerClass) {
        prepareDialog(dto, controllerClass);
    }

    private void prepareDialog(T dto, Class<U> controllerClass) {
        //loading edit view mask
        ViewLoader<U> viewLoader = ViewLoader.loadView(controllerClass);
        Node content = viewLoader.loadNode();

        EditViewController<T> editViewController = viewLoader.getController();
        editViewController.loadDTO(dto);

        //Configuring dialog window
        setTitle(DIALOG_TITLE);
        setHeaderText(editViewController.getHeaderText());
        setGraphic(GUIHelper.loadSVGGraphic(SVGContainer.EDIT_ICON));

        getDialogPane().setContent(content);

        //preparing buttons for dialog
        ButtonType saveButtonType = new ButtonType(BUTTON_SAVE_CAPTION, ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        //disabling save button at the start
        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        //attaching listener from edit view (e.g. automatically disables save button when load dto is called)
        saveButton.disableProperty().bind(editViewController.IN_WORK_PROPERTY);

        //defining the result converter for dialog return type
        //setting listener for save button -> when button will be pressed
        //saveDTO() method in the child controller will be called
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return editViewController.saveDTO();
            }
            return null;
        });
    }
}
