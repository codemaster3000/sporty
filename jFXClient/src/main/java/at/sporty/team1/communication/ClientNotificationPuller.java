package at.sporty.team1.communication;

import at.sporty.team1.rmi.api.INotificationController;
import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.enums.MessageType;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class ClientNotificationPuller extends Thread {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MESSAGE_SUBJECT = "Subject: ";
	public static final String MESSAGE_CONTENT = "Content: ";

	public ClientNotificationPuller() {
	}

	@Override
	public void run() {
		try {

			INotificationController notificationController = CommunicationFacade.lookupForNotificationController();
			SessionDTO currentSession = CommunicationFacade.getActiveSession();

			//Pull notifications from server (NotificationController)
			List<MessageDTO> messageList = notificationController.pullMessages(currentSession);

			//On new message, show alert.confirm or alert.info
			if (messageList != null && !messageList.isEmpty()) {

				while (!messageList.isEmpty()) {

					MessageDTO message = messageList.remove(0);

					if (message.getMessageType() != null) {

						switch (message.getMessageType()) {

							case PLAIN_TEXT: {
								GUIHelper.showExtendedInformationAlert(
									MESSAGE_SUBJECT + message.getMessageSubject(),
									MESSAGE_CONTENT + message.getMessageContent()
								);

								break;
							}

							case CONFIRMATION_REQUEST: {
								Optional<ButtonType> result = GUIHelper.showCustomAlert(
									AlertType.CONFIRMATION,
									"Confirmation",
									MESSAGE_SUBJECT + message.getMessageSubject(),
									MESSAGE_CONTENT + message.getMessageContent()
								);

								MessageDTO returnMessage;

								if (result.isPresent() && result.get() == ButtonType.YES) {
									returnMessage = new MessageDTO()
										.setRecipientId(message.getSenderId())
										.setSenderId(message.getRecipientId())
										.setMessageType(MessageType.PLAIN_TEXT)
										.setMessageSubject(message.getMessageSubject() + " confirmed.")
										.setMessageContent("Request was confirmed by " + message.getRecipientId() + ".");

								} else {

									returnMessage = new MessageDTO().setRecipientId(message.getSenderId())
										.setSenderId(message.getRecipientId())
										.setMessageType(MessageType.PLAIN_TEXT)
										.setMessageSubject(message.getMessageSubject() + " rejected.")
										.setMessageContent("Request was rejected by " + message.getRecipientId() + ".");
								}

								//On accept / decline send DTO with answer to server
								notificationController.sendMessage(returnMessage, currentSession);

								break;
							}
						}
					}
				}
			}

		} catch (RemoteException | MalformedURLException | NotBoundException e) {

			String context = "Error occurred while loading member Messages.";

			LOGGER.error(context, e);
			Platform.runLater(() -> GUIHelper.showErrorAlert(context));
		} catch (NotAuthorisedException e) {

			String context = "Message pull request was rejected. Not enough permissions.";

			LOGGER.error(context, e);
			Platform.runLater(() -> GUIHelper.showErrorAlert(context));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
}
