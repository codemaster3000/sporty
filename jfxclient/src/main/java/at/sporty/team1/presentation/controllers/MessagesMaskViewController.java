package at.sporty.team1.presentation.controllers;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.presentation.controllers.core.ConsumerViewController;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.enums.MessageType;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 08-Dec-15.
 */
public class MessagesMaskViewController extends ConsumerViewController<MessageDTO> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final CommunicationFacade COMMUNICATION_FACADE = CommunicationFacade.getInstance();
    private static final String NOT_AVAILABLE = "N/A";
    private static final SimpleObjectProperty<MessageType> MESSAGE_TYPE_PROPERTY = new SimpleObjectProperty<>(null);

    @FXML private ListView<MessageDTO> _messagesListView;
    @FXML private Label _subjectLabel;
    @FXML private Label _fromLabel;
    @FXML private Label _contentLabel;
    @FXML private Button _confirmReadButton;
    @FXML private Button _acceptRequestButton;
    @FXML private Button _declineRequestButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _messagesListView.setCellFactory(p -> new ListCell<MessageDTO>() {
            @Override
            protected void updateItem(MessageDTO dto, boolean empty) {
                super.updateItem(dto, empty);
                if (dto != null) {

                    StringBuilder sb = new StringBuilder();
                    sb.append(dto.getSenderId());

                    if (dto.getMessageSubject() != null) {
                        sb.append(" - ");
                        sb.append(dto.getMessageSubject());
                    }

                    setText(sb.toString());

                } else {
                    setText("");
                }
            }
        });

        _messagesListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> loadDTO(newValue)
        );

        _confirmReadButton.visibleProperty().bind(
            MESSAGE_TYPE_PROPERTY.isNotNull().and(
            MESSAGE_TYPE_PROPERTY.isEqualTo(MessageType.PLAIN_TEXT))
        );
        _confirmReadButton.managedProperty().bind(_confirmReadButton.visibleProperty());

        _acceptRequestButton.visibleProperty().bind(
            MESSAGE_TYPE_PROPERTY.isNotNull().and(
            MESSAGE_TYPE_PROPERTY.isEqualTo(MessageType.CONFIRMATION_REQUEST))
        );
        _acceptRequestButton.managedProperty().bind(_acceptRequestButton.visibleProperty());

        _declineRequestButton.visibleProperty().bind(
            MESSAGE_TYPE_PROPERTY.isNotNull().and(
            MESSAGE_TYPE_PROPERTY.isEqualTo(MessageType.CONFIRMATION_REQUEST))
        );
        _declineRequestButton.managedProperty().bind(_declineRequestButton.visibleProperty());
    }

    @Override
    public void loadDTO(MessageDTO messageDTO) {
        if (messageDTO != null) {
            _fromLabel.setText(Integer.toString(messageDTO.getSenderId()));
            _subjectLabel.setText(readNullOrNotAvailable(messageDTO.getMessageSubject()));
            _contentLabel.setText(readNullOrNotAvailable(messageDTO.getMessageContent()));

            MESSAGE_TYPE_PROPERTY.set(messageDTO.getMessageType());
        }
    }

    @Override
    public void dispose() {
        _fromLabel.setText(NOT_AVAILABLE);
        _subjectLabel.setText(NOT_AVAILABLE);
        _contentLabel.setText(NOT_AVAILABLE);

        MESSAGE_TYPE_PROPERTY.set(null);
    }

    public void setMessageList(ObservableList<MessageDTO> messageList) {
        _messagesListView.setItems(messageList);
    }

    @FXML
    private void onConfirmRead(ActionEvent actionEvent) {
        MessageDTO readMessage = _messagesListView.getSelectionModel().getSelectedItem();
        _messagesListView.getItems().remove(readMessage);

        dispose();
    }

    @FXML
    private void onAcceptRequest(ActionEvent actionEvent) {
        try {

            MessageDTO readMessage = _messagesListView.getSelectionModel().getSelectedItem();

            sendRequestAnswerMessage(readMessage, AnswerType.ACCEPT);
            _messagesListView.getItems().remove(readMessage);

            dispose();

        } catch (RemoteCommunicationException e) {
            LOGGER.error("Error occurred while sending a confirmation Message.", e);
        } catch (NotAuthorisedException e) {
            LOGGER.error("Message send request was rejected. Not enough permissions.", e);
        } catch (ValidationException e) {
            LOGGER.error("Message format is not valid. Message was not send.", e);
        }
    }

    @FXML
    private void onDeclineRequest(ActionEvent actionEvent) {
        try {

            MessageDTO readMessage = _messagesListView.getSelectionModel().getSelectedItem();

            sendRequestAnswerMessage(readMessage, AnswerType.DECLINE);
            _messagesListView.getItems().remove(readMessage);

            dispose();

        } catch (RemoteCommunicationException e) {
            LOGGER.error("Error occurred while sending a decline Message.", e);
        } catch (NotAuthorisedException e) {
            LOGGER.error("Message send request was rejected. Not enough permissions.", e);
        } catch (ValidationException e) {
            LOGGER.error("Message format is not valid. Message was not send.", e);
        }
    }

    private void sendRequestAnswerMessage(MessageDTO sourceMessage, AnswerType answerType)
    throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
    	
        String answerSubject = String.format(sourceMessage.getMessageSubject() + " %s.", answerType);
        String answerContent = String.format("Request was %s by %s.", answerType, sourceMessage.getRecipientId());

        MessageDTO returnMessage = new MessageDTO()
            .setRecipientId(sourceMessage.getSenderId())
            .setSenderId(sourceMessage.getRecipientId())
            .setMessageType(MessageType.PLAIN_TEXT)
            .setMessageSubject(answerSubject)
            .setMessageContent(answerContent);

        COMMUNICATION_FACADE.lookupForNotificationController().sendMessage(
            returnMessage,
            COMMUNICATION_FACADE.getActiveSession()
        );
    }

    public static String readNullOrNotAvailable(String s) {
        return (s == null || s.isEmpty()) ? NOT_AVAILABLE : s;
    }

    private enum AnswerType{
        ACCEPT("accepted"),
        DECLINE("declined");

        private final String _stringForm;

        AnswerType(String stringForm) {
            _stringForm = stringForm;
        }

        @Override
        public String toString() {
            return _stringForm;
        }
    }
}
