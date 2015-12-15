package at.sporty.team1.communication.util;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.facades.api.INotificationControllerUniversal;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class NotificationPullerTask implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final CommunicationFacade COMMUNICATION_FACADE = CommunicationFacade.getInstance();

    private final Consumer<Collection<MessageDTO>> _messageConsumer;
    private final SessionDTO _currentSession;

    public NotificationPullerTask(SessionDTO currentSession, Consumer<Collection<MessageDTO>> messageConsumer) {
        _messageConsumer = messageConsumer;
        _currentSession = currentSession;
    }

    @Override
	public void run() {
		try {

			INotificationControllerUniversal notificationController = COMMUNICATION_FACADE.lookupForNotificationController();

			//Pull notifications from server (NotificationController)
			List<MessageDTO> messageList = notificationController.pullMessages(_currentSession);
			if (messageList != null && !messageList.isEmpty()) _messageConsumer.accept(messageList);

		} catch (RemoteCommunicationException e) {
			LOGGER.error("Error occurs while pulling messages from notification puller thread.", e);
		} catch (NotAuthorisedException e) {
			LOGGER.error("Message pull request was rejected. Not enough permissions.", e);
		}
	}
}
