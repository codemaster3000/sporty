package at.sporty.team1.executable;

import at.sporty.team1.application.controller.MemberController;
import at.sporty.team1.persistence.HibernateSessionUtil;
import at.sporty.team1.rmi.stubs.CommunicationStub;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

/**
 * Created by sereGkaluv on 23-Oct-15.
 * Sporty server starter class.
 */
public class Server {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DEFAULT_RMI = "rmi://localhost/%s";
    private static final int DEFAULT_PORT = 1099;

    /**
     * Binds servants to their string naming representation.
     *
     * @param obj Object to be bounded.
     */
    private static void bindName(CommunicationStub stub, Remote obj) {
        try {
            Naming.bind(String.format(DEFAULT_RMI, stub.getNaming()), obj);

            LOGGER.info("{} bounded to the registry.", stub.getNaming());
        } catch (Exception e) {
            LOGGER.error("{} was not bounded to the registry.", stub.getNaming(), e);
        }
    }

    /**
     * Method should be executed on the server start.
     *
     * @throws Exception
     */
    private static void start() throws Exception {
        //TODO: this is a temp solution
        LocateRegistry.createRegistry(DEFAULT_PORT);
        HibernateSessionUtil.getInstance().openSession();
    }

    /**
     * Method should be executed on the server stop.
     *
     * @throws Exception
     */
    private static void stop() throws Exception {
        HibernateSessionUtil.getInstance().close();
    }

    /**
     * Default main method. Starts "this" application.
     *
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {

            start();

            bindName(CommunicationStub.MEMBER_CONTROLLER, new MemberController());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
