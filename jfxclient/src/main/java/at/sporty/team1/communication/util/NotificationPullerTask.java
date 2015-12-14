package at.sporty.team1.communication.util;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.communication.facades.api.INotificationControllerUniversal;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NotificationPullerTask implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final CommunicationFacade COMMUNICATION_FACADE = CommunicationFacade.getInstance();

    private final ObservableList<MessageDTO> _userMessages;
    private final SessionDTO _currentSession;

    public NotificationPullerTask(SessionDTO currentSession, ObservableList<MessageDTO> userMessages) {
        _userMessages = userMessages;
        _currentSession = currentSession;
    }

    @Override
	public void run() {
		try {

			INotificationControllerUniversal notificationController = COMMUNICATION_FACADE.lookupForNotificationController();

			//Pull notifications from server (NotificationController)
			List<MessageDTO> messageList = notificationController.pullMessages(_currentSession);
			if (messageList != null && !messageList.isEmpty()) _userMessages.addAll(messageList);

		} catch (NotAuthorisedException | RemoteCommunicationException e) {
			LOGGER.error("Message pull request was rejected. Not enough permissions.", e);
		}
	}
}
