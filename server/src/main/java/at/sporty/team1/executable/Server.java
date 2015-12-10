package at.sporty.team1.executable;

import at.sporty.team1.application.controller.rmi.impl.*;
import at.sporty.team1.persistence.util.HibernateSessionUtil;
import at.sporty.team1.shared.enums.RemoteObjectRegistry;
import at.sporty.team1.shared.exceptions.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by sereGkaluv on 23-Oct-15.
 * Sporty server starter class.
 */
public class Server {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Marker SERVER_LIFECYCLE_MARKER = MarkerManager.getMarker("SERVER_LIFECYCLE");
    private static final String DEFAULT_RMI = "rmi://localhost/%s";
    private static final int DEFAULT_PORT = 1099;

    private static Registry rmiRegistry;

    /**
     * Default main method. Starts "this" application.
     *
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {

        try {

            LOGGER.info(SERVER_LIFECYCLE_MARKER, "STARTING SPORTY SERVER");

            start();

            bindRemoteObjects();

            LOGGER.info(SERVER_LIFECYCLE_MARKER, "Server started successfully.");

            listenForCommands();

        } catch (Exception e) {
            LOGGER.error(SERVER_LIFECYCLE_MARKER, "Error occurred on server initialisation stage.", e);
        }
    }

    private static void bindRemoteObjects() throws RemoteException, SecurityException {
        bindName(RemoteObjectRegistry.LOGIN_CONTROLLER, new LoginControllerRMIAdapter());
        bindName(RemoteObjectRegistry.NOTIFICATION_CONTROLLER, new NotificationControllerRMIAdapter());
        bindName(RemoteObjectRegistry.MEMBER_CONTROLLER, new MemberControllerRMIAdapter());
        bindName(RemoteObjectRegistry.TEAM_CONTROLLER, new TeamControllerRMIAdapter());
        bindName(RemoteObjectRegistry.DEPARTMENT_CONTROLLER, new DepartmentControllerRMIAdapter());
        bindName(RemoteObjectRegistry.TOURNAMENT_CONTROLLER, new TournamentControllerRMIAdapter());
    }

    /**
     * Binds servants to their string naming representation.
     *
     * @param obj Object to be bounded.
     */
    private static void bindName(RemoteObjectRegistry stub, Remote obj) {
        try {
            Naming.bind(String.format(DEFAULT_RMI, stub.getNaming()), obj);

            LOGGER.info(SERVER_LIFECYCLE_MARKER, "{} bounded to the registry.", stub.getNaming());
        } catch (Exception e) {
            LOGGER.error(SERVER_LIFECYCLE_MARKER, "{} was not bounded to the registry.", stub.getNaming(), e);
        }
    }

    private static void listenForCommands() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {

                LOGGER.info(
                    SERVER_LIFECYCLE_MARKER,
                    "Available Commands: {}",
                    ServerCommands.SHUTDOWN.getCommand()
                );

                String command = br.readLine();

                if (command != null) {
                    switch (ServerCommands.valueOf(command.toUpperCase())) {
                        case SHUTDOWN: System.exit(0);

                        default: {
                            LOGGER.error("Received command \"{}\" is not supported.", command);
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("Problem occurred while handling command.", e);
        }
    }

    /**
     * Method should be executed on the server start.
     *
     * @throws Exception
     */
    private static void start() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(Server::shutdown, "Shutdown-thread"));

        HibernateSessionUtil.getInstance().openSession();
        rmiRegistry = LocateRegistry.createRegistry(DEFAULT_PORT);
    }

    /**
     * Method should be executed on the server shutdown.
     */
    private static void shutdown() {
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "SHUTDOWN CLEANUP PROCESS STARTS");

        if (rmiRegistry != null) {
            try {
                UnicastRemoteObject.unexportObject(rmiRegistry, true);
            } catch (NoSuchObjectException e) {
                LOGGER.error("RMI Registry is not shared (UnicastRemoteObject can't find the Object).", e);
            }
        }
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "RMI Registry successfully un-exported.");

        HibernateSessionUtil.getInstance().close();
        LOGGER.info(SERVER_LIFECYCLE_MARKER, "Hibernate session was successfully closed.");

        LOGGER.info(SERVER_LIFECYCLE_MARKER, "Server successfully stopped.");
    }

    private enum  ServerCommands {
        SHUTDOWN("shutdown");

        private final String _command;

        ServerCommands(String command) {
            _command = command;
        }

        public String getCommand() {
            return _command;
        }
    }
}
