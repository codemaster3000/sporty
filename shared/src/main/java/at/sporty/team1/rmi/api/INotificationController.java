package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.MessageDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public interface INotificationController extends IRemoteController {

    /**
     * Sends a message to the predefined queue.
     *
     * @param messageDTO Message container, contains message type, text and receiver.
     * @param session Session object.
     * @return status of the execution true on success, false on failure
     * @throws RemoteException
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;

    /**
     * Loads all messages from the queue assigned to the given member (from active session).
     *
     * @param session Session object.
     * @return List<MessageDTO> pulls all messages from the queue assigned to the given member (from active session).
     * @throws RemoteException
     * @throws NotAuthorisedException
     */
    List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteException, NotAuthorisedException;
}
