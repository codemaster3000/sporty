package at.sporty.team1.shared.api.rmi;

import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public interface INotificationControllerRMI extends IRemoteControllerRMI {

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
