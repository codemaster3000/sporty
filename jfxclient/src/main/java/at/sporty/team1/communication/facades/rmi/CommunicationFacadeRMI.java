package at.sporty.team1.communication.facades.rmi;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.rmi.adapters.*;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.rmi.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

public class CommunicationFacadeRMI implements ICommunicationFacade {
    private static final int DEFAULT_TARGET_PORT = 1099;
    private static final String DEFAULT_TARGET_SERVER = "localhost";
    private static final String RMI_CONNECTION_STRING = "rmi://%s/%s";
    private static final String TARGET_SERVER = "TARGET_SERVER";
    private static final String RMI_PORT = "RMI_PORT";

    private final Properties _properties;
    private String _providerURL;

    public CommunicationFacadeRMI(Properties properties) {
        _properties = properties;
	}
	
	public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        try {

            IMemberControllerRMI controllerRMI = (IMemberControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.MEMBER_CONTROLLER)
            );

            return new MemberControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        try {

            ITeamControllerRMI controllerRMI = (ITeamControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.TEAM_CONTROLLER)
            );

            return new TeamControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	 
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException{
        try {

            IDepartmentControllerRMI controllerRMI = (IDepartmentControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.DEPARTMENT_CONTROLLER)
            );

            return new DepartmentControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }
	 
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        try {

            ILoginControllerRMI controllerRMI = (ILoginControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.LOGIN_CONTROLLER)
            );

            return new LoginControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }

	public ITournamentControllerUniversal lookupForTournamentController()
	throws RemoteCommunicationException {
        try {

            ITournamentControllerRMI controllerRMI = (ITournamentControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.TOURNAMENT_CONTROLLER)
            );

            return new TournamentControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
	}
	
	public INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException {
        try {

            INotificationControllerRMI controllerRMI = (INotificationControllerRMI) Naming.lookup(
                generateLookupName(RemoteObjectRegistry.NOTIFICATION_CONTROLLER)
            );

            return new NotificationControllerRMIAdapter(controllerRMI);

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            throw new RemoteCommunicationException(e);
        }
    }

    /**
     * Generates Naming string for given RMI.
     *
     * @param remoteObject Universal naming that will be used to generate rmi binding name.
     * @return Naming string for given RMI.
     */
    private String generateLookupName(RemoteObjectRegistry remoteObject) {

        if (_providerURL == null) {
            StringBuilder urlBuilder = new StringBuilder();

            String targetServer = _properties.getProperty(TARGET_SERVER);
            if (!isNullOrEmpty(targetServer)) urlBuilder.append(targetServer);
            else urlBuilder.append(DEFAULT_TARGET_SERVER);

            String targetPort = _properties.getProperty(RMI_PORT);
            if (!isNullOrEmpty(targetPort)) urlBuilder.append(':').append(targetPort);
            else urlBuilder.append(':').append(DEFAULT_TARGET_PORT);

            _providerURL = urlBuilder.toString();
        }

        return String.format(
            RMI_CONNECTION_STRING,
            _providerURL,
            remoteObject.getNamingRMI()
        );
    }

    /**
     * Utility method, checks if a given string is null or empty.
     *
     * @param value value to be checked.
     * @return true if is null and / or is empty, false if it is nor null and nor empty.
     */
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
