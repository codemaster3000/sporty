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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class CommunicationFacadeEJB implements ICommunicationFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    //FIXME (property file does not exist - I need to check how this should be implemented)
    private static final String PROPERTY_FILE = "ejb.properties";
    private static final Properties PROPERTIES = new Properties();

    private InitialContext _context;

    public CommunicationFacadeEJB() {
        try {

            URL propertyURL = getClass().getClassLoader().getResource(PROPERTY_FILE);
            if (propertyURL != null) {

                PROPERTIES.load(new FileInputStream(propertyURL.getFile()));
                _context = new InitialContext(PROPERTIES);

            } else {
                throw new FileNotFoundException(PROPERTY_FILE + " was not found");
            }

        } catch (IOException e) {
            LOGGER.error("An error occurs while loading EJB properties from {}.", PROPERTY_FILE, e);
        } catch (NamingException e) {
            LOGGER.error("An error occurs while initializing context.", e);
        }
    }

    @Override
    public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        
    	try {
			IMemberControllerEJB iMemberControllerEJB = (IMemberControllerEJB) _context.lookup(
				RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingEJB()	
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
			ITeamControllerEJB iTeamControllerEJB = (ITeamControllerEJB) _context.lookup(
				RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingEJB()	
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
			IDepartmentControllerEJB iDepartmentControllerEJB = (IDepartmentControllerEJB) _context.lookup(
				RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingEJB()	
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
			ILoginControllerEJB iLoginControllerEJB = (ILoginControllerEJB) _context.lookup(
				RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingEJB()	
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
        	ITournamentControllerEJB iTournamentControllerEJB = (ITournamentControllerEJB) _context.lookup(
        		RemoteObjectRegistry.TOURNAMENT_CONTROLLER.getNamingEJB()	
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

            INotificationControllerEJB controllerEJB = (INotificationControllerEJB) _context.lookup(
                RemoteObjectRegistry.NOTIFICATION_CONTROLLER.getNamingEJB()
            );

            return new NotificationControllerEJBAdapter(controllerEJB);

        } catch (NamingException e) {
            throw new RemoteCommunicationException(e);
        }
    }
}
