package at.sporty.team1.communication.facades.ejb.adapters;

import at.sporty.team1.communication.facades.api.INotificationControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.INotificationControllerEJB;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

/**
 * Created by sereGkaluv on 12-Dec-15.
 */
public class NotificationControllerEJBAdapter implements INotificationControllerUniversal {

    private final INotificationControllerEJB _notificationController;

    public NotificationControllerEJBAdapter(INotificationControllerEJB notificationController) {
        _notificationController = notificationController;
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
        return _notificationController.sendMessage(messageDTO, session);
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteCommunicationException, NotAuthorisedException {
        return _notificationController.pullMessages(session);
    }
}
