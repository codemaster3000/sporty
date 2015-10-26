package at.sporty.team1;

import java.rmi.Naming;

/**
 * Created by sereGkaluv on 23-Oct-15.
 */
public class Server {
    private static final String DEFAULT_RMI = "rmi://localhost/";


    private static void bind(IServant servant) {
        try {
            String servantName = servant.getClass().getName();
            Naming.bind(DEFAULT_RMI + servantName, servant);

            System.out.println(servantName + " bounded in registry.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //bind();
    }
}
