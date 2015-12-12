package at.sporty.team1.communication.facades.ejb;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.ejb.adapters.NotificationControllerEJBAdapter;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.ejb.INotificationControllerEJB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommunicationFacadeEJB implements ICommunicationFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    //FIXME (property file does not exist - I need to check how this should be implemented)
    private static final String PROPERTY_FILE = "ejb.properties";
    private static final Properties PROPERTIES = new Properties();

    private InitialContext _context;

    public CommunicationFacadeEJB(){
        try {

            PROPERTIES.load(new FileInputStream(PROPERTY_FILE));
            _context = new InitialContext(PROPERTIES);

        } catch (IOException e) {
            LOGGER.error("An error occurs while loading EJB properties from {}.", PROPERTY_FILE, e);
        } catch (NamingException e) {
            LOGGER.error("An error occurs while initializing context.", e);
        }
    }

    @Override
    public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        return null;
    }

    @Override
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        return null;
    }

    @Override
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException {
        return null;
    }

    @Override
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        return null;
    }

    @Override
    public ITournamentControllerUniversal lookupForTournamentController()
    throws RemoteCommunicationException {
        return null;
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
