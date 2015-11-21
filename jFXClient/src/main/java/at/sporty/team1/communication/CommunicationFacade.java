package at.sporty.team1.communication;

import at.sporty.team1.rmi.RemoteObjectRegistry;
import at.sporty.team1.rmi.api.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class CommunicationFacade {
    private static final String DEFAULT_RMI = "rmi://localhost/%s";
    private static final Map<Class<? extends IRemoteController>, Remote> CONTROLLER_MAP = new HashMap<>();

    private CommunicationFacade() {
    }

    public static IMemberController lookupForMemberController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IMemberController.class)) {
            CONTROLLER_MAP.put(IMemberController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.MEMBER_CONTROLLER.getNaming())
            ));
        }

        return (IMemberController) CONTROLLER_MAP.get(IMemberController.class);
    }

    public static ITeamController lookupForTeamController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITeamController.class)) {
            CONTROLLER_MAP.put(ITeamController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.TEAM_CONTROLLER.getNaming())
            ));
        }

        return (ITeamController) CONTROLLER_MAP.get(ITeamController.class);
    }

    public static IDepartmentController lookupForDepartmentController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IDepartmentController.class)) {
            CONTROLLER_MAP.put(IDepartmentController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.DEPARTMENT_CONTROLLER.getNaming())
            ));
        }

        return (IDepartmentController) CONTROLLER_MAP.get(IDepartmentController.class);
    }

    public static ILoginController lookupForLoginController()
    throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ILoginController.class)) {
            CONTROLLER_MAP.put(ILoginController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.LOGIN_CONTROLLER.getNaming())
            ));
        }

        return (ILoginController) CONTROLLER_MAP.get(ILoginController.class);
    }

	public static ITournamentController lookupForTournamentController() 
	throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITournamentController.class)) {
            CONTROLLER_MAP.put(ITournamentController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.TOURNAMENT_CONTROLLER.getNaming())
            ));
        }

        return (ITournamentController) CONTROLLER_MAP.get(ITournamentController.class);
	}
}

