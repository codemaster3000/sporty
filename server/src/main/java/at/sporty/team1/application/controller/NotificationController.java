package at.sporty.team1.application.controller;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.application.jms.ConsumerJMS;
import at.sporty.team1.application.jms.ProducerJMS;
import at.sporty.team1.rmi.api.INotificationController;
import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import javax.jms.JMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NotificationController extends UnicastRemoteObject implements INotificationController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    public NotificationController() throws RemoteException {
        super();
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        //1 STEP
        if (messageDTO == null) throw new NotAuthorisedException();

        //2 STEP
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isTrainerOfMember(messageDTO.getRecipientId()),
                BasicAccessPolicies.isDepartmentHeadOfMember(messageDTO.getRecipientId())
            )
        )) throw new NotAuthorisedException();

        try {

            ProducerJMS.sendMessage(messageDTO);
            return true;

        } catch (JMSException e) {

            LOGGER.error(
                "Error occurs while sending message from Member #{} to Member #{}.",
                messageDTO.getSenderId(),
                messageDTO.getRecipientId(),
                e
            );

            return false;
        }
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isSelf(session.getUserId())
        )) throw new NotAuthorisedException();

        try {

            return ConsumerJMS.pullMessages(session.getUserId());

        } catch (JMSException e) {

            LOGGER.error(
                "Error occurs while pulling messages for Member #{}.",
                session.getUserId(),
                e
            );

            return null;
        }
    }
}
