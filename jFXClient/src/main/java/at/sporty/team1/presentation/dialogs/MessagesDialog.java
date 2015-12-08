package at.sporty.team1.presentation.dialogs;

import at.sporty.team1.presentation.ViewLoader;
import at.sporty.team1.presentation.controllers.LoginMaskViewController;
import at.sporty.team1.presentation.controllers.MessagesMaskViewController;
import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.util.GUIHelper;
import at.sporty.team1.util.SVGContainer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Created by sereGkaluv on 08-Dec-15.
 */
public class MessagesDialog extends Dialog {
    private static final String DIALOG_TITLE = "Sporty Messages";
    private static final String DIALOG_HEADER = "USER MESSAGES VIEW";

    public MessagesDialog(ObservableList<MessageDTO> messageList) {
        prepareDialog(messageList);
    }

    private void prepareDialog(ObservableList<MessageDTO> messageList) {
        setTitle(DIALOG_TITLE);
        setHeaderText(DIALOG_HEADER);
        setGraphic(GUIHelper.loadSVGGraphic(SVGContainer.MESSAGE_ICON));

        //loading login mask
        ViewLoader<MessagesMaskViewController> viewLoader = ViewLoader.loadView(MessagesMaskViewController.class);
        Node content = viewLoader.loadNode();
        getDialogPane().setContent(content);

        viewLoader.getController().setMessageList(messageList);

        //preparing buttons for dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    }
}
