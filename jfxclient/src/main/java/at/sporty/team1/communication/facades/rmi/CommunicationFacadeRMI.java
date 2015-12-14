package at.sporty.team1.communication.facades.rmi;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.rmi.adapters.*;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.rmi.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public class CommunicationFacadeRMI implements ICommunicationFacade {
    private static final String RMI_TARGET = "RMI_TARGET";
    private final Properties _properties;

    public CommunicationFacadeRMI(Properties properties) {
        _properties = properties;
	}
	
	public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        try {

            IMemberControllerRMI controllerRMI = (IMemberControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.MEMBER_CONTROLLER.getNamingRMI()
            ));

            return new MemberControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        try {

            ITeamControllerRMI controllerRMI = (ITeamControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.TEAM_CONTROLLER.getNamingRMI()
            ));

            return new TeamControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	 
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException{
        try {

            IDepartmentControllerRMI controllerRMI = (IDepartmentControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.DEPARTMENT_CONTROLLER.getNamingRMI()
            ));

            return new DepartmentControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	 
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        try {

            ILoginControllerRMI controllerRMI = (ILoginControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingRMI()
            ));

            return new LoginControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }

	public ITournamentControllerUniversal lookupForTournamentController()
	throws RemoteCommunicationException {
        try {

            ITournamentControllerRMI controllerRMI = (ITournamentControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.TOURNAMENT_CONTROLLER.getNamingRMI()
            ));

            return new TournamentControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
	}
	
	public INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException {
        try {

            INotificationControllerRMI controllerRMI = (INotificationControllerRMI) Naming.lookup(String.format(
                _properties.getProperty(RMI_TARGET),
                RemoteObjectRegistry.NOTIFICATION_CONTROLLER.getNamingRMI())
            );

            return new NotificationControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
}
