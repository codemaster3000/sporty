package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.INotificationControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.INotificationControllerRMI;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public class NotificationControllerRMIAdapter implements INotificationControllerUniversal {

    private final INotificationControllerRMI _notificationController;

    public NotificationControllerRMIAdapter(INotificationControllerRMI notificationController) {
        _notificationController = notificationController;
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
        try {
            return _notificationController.sendMessage(messageDTO, session);
        } catch (RemoteException e) {
            throw new RemoteCommunicationException(e);
        }
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteCommunicationException, NotAuthorisedException {
        try {
            return _notificationController.pullMessages(session);
        } catch (RemoteException e) {
            throw new RemoteCommunicationException(e);
        }
    }
}
