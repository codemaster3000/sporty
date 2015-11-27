package at.sporty.team1.application.controller;

import at.sporty.team1.rmi.api.INotificationController;
import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by CarolaFHV on 27-Nov-15.
 */
public class NotificationController extends UnicastRemoteObject implements INotificationController {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();


    public NotificationController() throws RemoteException {
        super();
    }

    @Override
    public void sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {
        //TODO unimplemented method
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteException, NotAuthorisedException {
        //TODO unimplemented method
        return null;
    }

    @Override
    public void confirmMessagePoll(MessageDTO messageDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {
        //TODO unimplemented method
    }
}
