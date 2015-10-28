package at.sporty.team1.executable;

import at.sporty.team1.application.controller.MemberController;
import at.sporty.team1.application.dtos.MemberDTO;
import at.sporty.team1.misc.IServant;
import at.sporty.team1.persistence.HibernateSessionUtil;

import java.rmi.Naming;

/**
 * Created by sereGkaluv on 23-Oct-15.
 * Sporty server starter class.
 */
public class Server {
    private static final String DEFAULT_RMI = "rmi://localhost/%s";

    /**
     * Binds servants to their string naming representation.
     *
     * @param servant servant to be bind.
     */
    private static void bindName(IServant servant) {
        try {
            String servantName = servant.getClass().getName();
            Naming.bind(String.format(DEFAULT_RMI, servantName), servant);

            System.out.println(servantName + " bounded in registry.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method should be executed on the server start.
     *
     * @throws Exception
     */
    private static void start() throws Exception {
        //bindName();
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


            //Following code is just for testing. It should be removed after testing phase.
            MemberDTO memberDTO = MemberController.getNewMemberDTO()
                    .setFirstName("Test Member #1 FIRST Name")
                    .setLastName("Test Member #1 LAST Name");

            MemberController.create(memberDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
