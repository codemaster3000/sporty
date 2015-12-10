package at.sporty.team1.application.controller.rmi.impl;

import at.sporty.team1.application.controller.real.api.INotificationController;
import at.sporty.team1.application.controller.real.impl.NotificationController;
import at.sporty.team1.shared.api.rmi.INotificationControllerRMI;
import at.sporty.team1.shared.dtos.MessageDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
public class NotificationControllerRMIAdapter extends UnicastRemoteObject implements INotificationControllerRMI {
    private static final long serialVersionUID = 1L;
    private final INotificationController _controller;

    public NotificationControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new NotificationController();
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.sendMessage(messageDTO, session);
    }

    @Override
    public List<MessageDTO> pullMessages(SessionDTO session)
    throws RemoteException, NotAuthorisedException {

        return _controller.pullMessages(session);
    }
}
