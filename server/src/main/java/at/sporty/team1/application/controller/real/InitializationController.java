package at.sporty.team1.application.controller.real;

import at.sporty.team1.application.controller.real.api.IController;
import at.sporty.team1.application.controller.rmi.*;
import at.sporty.team1.application.controller.util.RemoteObject;
import at.sporty.team1.persistence.util.HibernateSessionUtil;
import at.sporty.team1.shared.exceptions.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sereGkaluv on 14-Dec-15.
 */
public class InitializationController implements IController {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker SERVER_LIFECYCLE_MARKER = MarkerManager.getMarker("SERVER_LIFECYCLE");
    private static final String DEFAULT_RMI = "rmi://localhost/%s";
    private static final int DEFAULT_PORT = 1099;

    private static Registry _rmiRegistry;

    /**
     * Default initialisation method. Performs RMI Binding.
     */
    public static InitializationController initialize() {

        try {

            LOGGER.info(SERVER_LIFECYCLE_MARKER, "STARTING SPORTY SERVER");

            InitializationController initController = new InitializationController();

            initController.start();
            initController.bindRemoteObjects();

            LOGGER.info(SERVER_LIFECYCLE_MARKER, "Server started successfully.");
            return initController;

        } catch (Exception e) {
            LOGGER.error(SERVER_LIFECYCLE_MARKER, "Error occurred on server initialisation stage.", e);
            return null;
        }
    }

    private void bindRemoteObjects() throws RemoteException, SecurityException {
        bindRemoteObject(LoginControllerRMIAdapter.class);
        bindRemoteObject(NotificationControllerRMIAdapter.class);
        bindRemoteObject(MemberControllerRMIAdapter.class);
        bindRemoteObject(TeamControllerRMIAdapter.class);
        bindRemoteObject(DepartmentControllerRMIAdapter.class);
        bindRemoteObject(TournamentControllerRMIAdapter.class);
    }

    /**
     * Binds RMI servants to their string naming representation.
     *
     * @param clazz Class of the remote object to be bounded.
     */
    private void bindRemoteObject(Class<? extends Remote> clazz) {
        try {

            if (clazz.isAnnotationPresent(RemoteObject.class)) {

                RemoteObject annotation = clazz.getAnnotation(RemoteObject.class);
                String objectNaming = annotation.name() != null ? annotation.name() : clazz.getSimpleName();

                Naming.bind(String.format(DEFAULT_RMI, objectNaming), clazz.newInstance());

                LOGGER.info(SERVER_LIFECYCLE_MARKER, "{} bounded to the registry.", objectNaming);

            } else {
                throw new Exception("Remote Object \"" + clazz.getSimpleName() + "\" is not annotated.");
            }

        } catch (Exception e) {
            LOGGER.error(
                SERVER_LIFECYCLE_MARKER,
                "{} was not bounded to the registry.",
                clazz.getCanonicalName(),
                e
            );
        }
    }

    /**
     * Method should be executed on the server start.
     *
     * @throws Exception
     */
    private void start() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Shutdown-thread"));

        HibernateSessionUtil.getInstance().openSession();
        _rmiRegistry = LocateRegistry.createRegistry(DEFAULT_PORT);
    }

    /**
     * Method should be executed on the server shutdown.
     */
    public void shutdown() {
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "SHUTDOWN CLEANUP PROCESS STARTS");

        if (_rmiRegistry != null) {
            try {
                UnicastRemoteObject.unexportObject(_rmiRegistry, true);
            } catch (NoSuchObjectException e) {
                LOGGER.error("RMI Registry is not shared (UnicastRemoteObject can't find the Object).", e);
            }
        }
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "RMI Registry successfully un-exported.");

        HibernateSessionUtil.getInstance().close();
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "Hibernate session was successfully closed.");

        LOGGER.info(SERVER_LIFECYCLE_MARKER, "Server successfully stopped.");
    }
}
