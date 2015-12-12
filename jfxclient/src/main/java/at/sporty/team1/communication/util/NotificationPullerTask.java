package at.sporty.team1.communication.util;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.sporty.team1.communication.facades.CommunicationFacade;
import at.sporty.team1.shared.api.rmi.INotificationControllerRMI;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import javafx.collections.ObservableList;

public class NotificationPullerTask implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();

    private final ObservableList<MessageDTO> _userMessages;
    private final SessionDTO _currentSession;

    public NotificationPullerTask(SessionDTO currentSession, ObservableList<MessageDTO> userMessages) {
        _userMessages = userMessages;
        _currentSession = currentSession;
    }

    @Override
	public void run() {
		try {

			INotificationControllerRMI notificationController = CommunicationFacade.lookupForNotificationControllerRMI();

			//Pull notifications from server (NotificationController)
			List<MessageDTO> messageList = notificationController.pullMessages(_currentSession);
			if (messageList != null && !messageList.isEmpty()) _userMessages.addAll(messageList);

		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			LOGGER.error("Error occurred while loading member Messages.", e);
		} catch (NotAuthorisedException e) {
			LOGGER.error("Message pull request was rejected. Not enough permissions.", e);
		}
	}
}
