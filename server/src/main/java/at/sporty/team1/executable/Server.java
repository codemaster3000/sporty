package at.sporty.team1.executable;

import at.sporty.team1.application.controller.*;
import at.sporty.team1.rmi.exceptions.SecurityException;
import at.sporty.team1.persistence.util.HibernateSessionUtil;
import at.sporty.team1.rmi.RemoteObjectRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

            start();

            bindRemoteObjects();

            LOGGER.info("Server started successfully.");

            listenForCommands();

        } catch (Exception e) {
            LOGGER.error("Error occurred on server initialisation stage.", e);
        }
    }

    private static void bindRemoteObjects() throws RemoteException, SecurityException {
        bindName(RemoteObjectRegistry.LOGIN_CONTROLLER, new LoginController());
        bindName(RemoteObjectRegistry.MEMBER_CONTROLLER, new MemberController());
        bindName(RemoteObjectRegistry.TEAM_CONTROLLER, new TeamController());
        bindName(RemoteObjectRegistry.DEPARTMENT_CONTROLLER, new DepartmentController());
        bindName(RemoteObjectRegistry.TOURNAMENT_CONTROLLER, new TournamentController());
    }

    /**
     * Binds servants to their string naming representation.
     *
     * @param obj Object to be bounded.
     */
    private static void bindName(RemoteObjectRegistry stub, Remote obj) {
        try {
            Naming.bind(String.format(DEFAULT_RMI, stub.getNaming()), obj);

            LOGGER.info("{} bounded to the registry.", stub.getNaming());
        } catch (Exception e) {
            LOGGER.error("{} was not bounded to the registry.", stub.getNaming(), e);
        }
    }

    private static void listenForCommands() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                LOGGER.info(
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
        LOGGER.info("SHUTDOWN CLEANUP PROCESS STARTS");

        if (rmiRegistry != null) {
            try {
                UnicastRemoteObject.unexportObject(rmiRegistry, true);
            } catch (NoSuchObjectException e) {
                LOGGER.error("RMI Registry is not shared (UnicastRemoteObject can't find the Object).", e);
            }
        }
        LOGGER.info("RMI Registry successfully un-exported.");

        HibernateSessionUtil.getInstance().close();
        LOGGER.info("Hibernate session was successfully closed.");

        LOGGER.info("Server successfully stopped.");
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
