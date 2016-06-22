package at.sporty.team1.communication.facades.ejb;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.ejb.adapters.*;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.ejb.*;

import javax.naming.*;
import java.util.Properties;

public class CommunicationFacadeEJB implements ICommunicationFacade {
    private static final String PROVIDER_URL_FORMAT = "http-remoting://%s:%s";
    private static final String EJB_NAMING_FORMAT = "/%s/%s";
    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    private static final String TARGET_SERVER = "TARGET_SERVER";
    private static final String SECURITY_PRINCIPAL = "SECURITY_PRINCIPAL";
    private static final String SECURITY_CREDENTIALS = "SECURITY_CREDENTIALS";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String EJB_CONTEXT = "jboss.naming.client.ejb.context";
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
            EJB_NAMING_FORMAT,
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
            Properties env = new Properties();

            // Server configs
            String targetServer = _properties.getProperty(TARGET_SERVER);
            String targetPort = _properties.getProperty(EJB_PORT);

            // AS Credentials (Compatible with JBoss and WildFly)
            String principalServer = _properties.getProperty(SECURITY_PRINCIPAL);
            String securityServer = _properties.getProperty(SECURITY_CREDENTIALS);

            if (!isNullOrEmpty(targetServer) && !isNullOrEmpty(targetPort)) {

                String providerUrl = String.format(PROVIDER_URL_FORMAT, targetServer, targetPort);

                env.put(Context.PROVIDER_URL, providerUrl);
                env.put(Context.SECURITY_PRINCIPAL, principalServer);
                env.put(Context.SECURITY_CREDENTIALS, securityServer);
                env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
                env.put(EJB_CONTEXT, true);

                _context = new InitialContext(env);
            }
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
