package at.sporty.team1.communication.facades.ejb;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.ejb.adapters.*;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.ejb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class CommunicationFacadeEJB implements ICommunicationFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String EJB_TARGET = "EJB_TARGET";
    private final Properties _properties;
    private InitialContext _context;

    public CommunicationFacadeEJB(Properties properties) {

        _properties = properties;

        try {

            _context = new InitialContext();

        } catch (NamingException e) {
            LOGGER.error("An error occurs while initializing context.", e);
        }
    }

    @Override
    public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        
    	try {

			IMemberControllerEJB iMemberControllerEJB = (IMemberControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.MEMBER_CONTROLLER, IMemberControllerEJB.class)
            ));
			return new MemberControllerEJBAdapter(iMemberControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        
    	try {

			ITeamControllerEJB iTeamControllerEJB = (ITeamControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.TEAM_CONTROLLER, ITeamControllerEJB.class)
            ));
			return new TeamControllerEJBAdapter(iTeamControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException {
    	
    	try {

			IDepartmentControllerEJB iDepartmentControllerEJB = (IDepartmentControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.DEPARTMENT_CONTROLLER, IDepartmentControllerEJB.class)
            ));
			return new DepartmentControllerEJBAdapter(iDepartmentControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        
    	try {

			ILoginControllerEJB iLoginControllerEJB = (ILoginControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.LOGIN_CONTROLLER, ILoginControllerEJB.class)
            ));
			return new LoginControllerEJBAdapter(iLoginControllerEJB);
			
		} catch (NamingException e) {
			throw new RemoteCommunicationException(e);
		}
    }

    @Override
    public ITournamentControllerUniversal lookupForTournamentController()
    throws RemoteCommunicationException {

        try {

        	ITournamentControllerEJB iTournamentControllerEJB = (ITournamentControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.TOURNAMENT_CONTROLLER, ITournamentControllerEJB.class)
            ));
        	return new TournamentControllerEJBAdapter(iTournamentControllerEJB);
        	
        }catch(NamingException e){
        	throw new RemoteCommunicationException(e);
        }
    }

    @Override
    public INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException {
        try {

            INotificationControllerEJB controllerEJB = (INotificationControllerEJB) _context.lookup(String.format(
                _properties.getProperty(EJB_TARGET),
                getEJBNaming(RemoteObjectRegistry.NOTIFICATION_CONTROLLER, INotificationControllerEJB.class)
            ));
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
    private String getEJBNaming(RemoteObjectRegistry remoteObject, Class<? extends IRemoteControllerEJB> remoteInterface) {
        return remoteObject.getNamingEJB() + "!" + remoteInterface.getName();
    }
}
