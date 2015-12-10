package at.sporty.team1.application.controller.real.impl;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.application.controller.real.api.INotificationController;
import at.sporty.team1.application.jms.ConsumerJMS;
import at.sporty.team1.application.jms.ProducerJMS;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.enums.UserRole;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import java.util.List;

public class NotificationController implements INotificationController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    public NotificationController() {
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

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
    throws NotAuthorisedException {

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
