package at.sporty.team1.communication;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.rmi.api.INotificationController;
import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.enums.MessageType;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import at.sporty.team1.util.GUIHelper;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ClientNotificationPuller extends Thread {

	private static final Logger LOGGER = LogManager.getLogger();

	public ClientNotificationPuller() {
	}

	@Override
	public void run() {

		while (true) {

			try {

				// every 10 seconds
				sleep(10);

				INotificationController notificationController = CommunicationFacade.lookupForNotificationController();
				SessionDTO currentSession = CommunicationFacade.getActiveSession();

				// TODO pull notifications from server (NotificationController)
				List<MessageDTO> messageList = notificationController.pullMessages(currentSession);

				// TODO on new message, show alert.confirm or alert.info
				if (!messageList.isEmpty() && messageList != null) {

					while (!messageList.isEmpty()) {

						MessageDTO message = messageList.remove(0);

						if (message.getMessageType() != null) {

							switch (message.getMessageType()) {
							case PLAIN_TEXT: {
								GUIHelper.showExtendedInformationAlert("Subject: " + message.getMessageSubject(),
										"Content: " + message.getMessageContent());
								break;
							}

							case CONFIRMATION_REQUEST: {
								Optional<ButtonType> result = GUIHelper.showCustomAlert(AlertType.CONFIRMATION,
										"Confirmation", "Subject: " + message.getMessageSubject(),
										"Content: " + message.getMessageContent());

								MessageDTO returnMessage;

								if (result.isPresent() && result.get() == ButtonType.YES) {
									returnMessage = new MessageDTO().setRecipientId(message.getSenderId())
											.setSenderId(message.getRecipientId())
											.setMessageType(MessageType.PLAIN_TEXT)
											.setMessageSubject(message.getMessageSubject() + " confirmed.")
											.setMessageContent(
													"Request was confirmed by " + message.getRecipientId() + ".");
								} else {
									returnMessage = new MessageDTO().setRecipientId(message.getSenderId())
											.setSenderId(message.getRecipientId())
											.setMessageType(MessageType.PLAIN_TEXT)
											.setMessageSubject(message.getMessageSubject() + " rejected.")
											.setMessageContent(
													"Request was rejected by " + message.getRecipientId() + ".");
								}

								// TODO on accept / decline send DTO with answer
								// to server
								notificationController.sendMessage(returnMessage, currentSession);
								break;
							}

							}
						}
					}
				}

			} catch (RemoteException e) {
				// TODO look up messages in main app
				LOGGER.error("msg", e);
			} catch (NotAuthorisedException e) {
				// TODO Auto-generated catch block
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
