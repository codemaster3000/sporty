package at.sporty.team1.communication.facades.ejb;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.ejb.adapters.*;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.ejb.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Properties;

public class CommunicationFacadeEJB implements ICommunicationFacade {
    private static final String CONTEXT_PROP_HOST = "org.omg.CORBA.ORBInitialHost";
    private static final String CONTEXT_PROP_PORT = "org.omg.CORBA.ORBInitialPort";
    private static final String EJB_CONNECTION_STRING = "java:global/%s/%s";
    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    private static final String TARGET_SERVER = "TARGET_SERVER";
    private static final String EJB_PORT = "EJB_PORT";

    private final Properties _properties;
    private InitialContext _context;

    public CommunicationFacadeEJB(Properties properties) {
        _properties = properties;
    }

    @Override
    public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        
    	try {

			IMemberControllerEJB iMemberControllerEJB = (IMemberControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.MEMBER_CONTROLLER, IMemberControllerEJB.class)
            );

			return new MemberControllerEJBAdapter(iMemberControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        
    	try {

			ITeamControllerEJB iTeamControllerEJB = (ITeamControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.TEAM_CONTROLLER, ITeamControllerEJB.class)
            );

			return new TeamControllerEJBAdapter(iTeamControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException {
    	
    	try {

			IDepartmentControllerEJB iDepartmentControllerEJB = (IDepartmentControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.DEPARTMENT_CONTROLLER, IDepartmentControllerEJB.class)
            );

			return new DepartmentControllerEJBAdapter(iDepartmentControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        
    	try {

			ILoginControllerEJB iLoginControllerEJB = (ILoginControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.LOGIN_CONTROLLER, ILoginControllerEJB.class)
            );

			return new LoginControllerEJBAdapter(iLoginControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ITournamentControllerUniversal lookupForTournamentController()
    throws RemoteCommunicationException {

        try {

        	ITournamentControllerEJB iTournamentControllerEJB = (ITournamentControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.TOURNAMENT_CONTROLLER, ITournamentControllerEJB.class)
            );

        	return new TournamentControllerEJBAdapter(iTournamentControllerEJB);
        	
        }catch(NamingException e){
        	throw new RemoteCommunicationException(e);
        }
    }

    @Override
    public INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException {
        try {

            INotificationControllerEJB controllerEJB = (INotificationControllerEJB) getContext().lookup(
                generateLookupName(RemoteObjectRegistry.NOTIFICATION_CONTROLLER, INotificationControllerEJB.class)
            );

            return new NotificationControllerEJBAdapter(controllerEJB);

        } catch (NamingException e) {
            throw new RemoteCommunicationException(e);
        }
    }

    /**
     * Generates Naming string for given EJB.
     *
     * @param remoteObject Universal naming that will be used to generate ejb binding name.
     * @param remoteInterface Remote interface of the lookup-ed EJB.
     * @return Naming string for given EJB.
     */
    private String generateLookupName(
        RemoteObjectRegistry remoteObject,
        Class<? extends IRemoteControllerEJB> remoteInterface
    ) {
        return String.format(
            EJB_CONNECTION_STRING,
            _properties.getProperty(APPLICATION_NAME),
            remoteObject.getNamingEJB() + "!" + remoteInterface.getName()
        );
    }

    /**
     * Prepares initial context.
     *
     * @return Prepared initial context.
     * @throws NamingException
     */
    private InitialContext getContext()
    throws NamingException {

        if (_context == null) {
            Hashtable<String, String> env = new Hashtable<>();

            String targetServer = _properties.getProperty(TARGET_SERVER);
            if (!isNullOrEmpty(targetServer)) env.put(CONTEXT_PROP_HOST, targetServer);

            String targetPort = _properties.getProperty(EJB_PORT);
            if (!isNullOrEmpty(targetPort)) env.put(CONTEXT_PROP_PORT, targetPort);

            _context = new InitialContext(env);
        }

        return _context;
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
