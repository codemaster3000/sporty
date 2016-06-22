package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.NotificationController;
import at.sporty.team1.application.controller.real.api.INotificationController;
import at.sporty.team1.shared.api.ejb.INotificationControllerEJB;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.jboss.ejb3.annotation.Clustered;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by f00 on 13.12.15.
 */
@Clustered
@Stateless(name = "NOTIFICATION_CONTROLLER_EJB")
public class NotificationControllerEJBAdapter implements INotificationControllerEJB {
    private static final long serialVersionUID = 1L;
    private transient final INotificationController _controller;

    public NotificationControllerEJBAdapter() {
        _controller = new NotificationController();
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws NotAuthorisedException {

        return _controller.pullMessages(session);
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

        return _controller.sendMessage(messageDTO,session);
    }
}
