package at.sporty.team1.communication;

import at.sporty.team1.presentation.controllers.ITournamentController;
import at.sporty.team1.rmi.RemoteObjectRegistry;
import at.sporty.team1.rmi.api.IDepartmentController;
import at.sporty.team1.rmi.api.ILoginController;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.api.ITeamController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CommunicationFacade {
    private static final String DEFAULT_RMI = "rmi://localhost/%s";

    private CommunicationFacade() {
    }

    public static IMemberController lookupForMemberController()
    throws RemoteException, NotBoundException, MalformedURLException {
        return (IMemberController) Naming.lookup(
            String.format(DEFAULT_RMI, RemoteObjectRegistry.MEMBER_CONTROLLER.getNaming())
        );
    }

    public static ITeamController lookupForTeamController()
    throws RemoteException, NotBoundException, MalformedURLException {
        return (ITeamController) Naming.lookup(
            String.format(DEFAULT_RMI, RemoteObjectRegistry.TEAM_CONTROLLER.getNaming())
        );
    }

    public static IDepartmentController lookupForDepartmentController()
    throws RemoteException, NotBoundException, MalformedURLException {
        return (IDepartmentController) Naming.lookup(
            String.format(DEFAULT_RMI, RemoteObjectRegistry.DEPARTMENT_CONTROLLER.getNaming())
        );
    }

    public static ILoginController lookupForLoginController()
    throws  RemoteException, NotBoundException, MalformedURLException {
        return (ILoginController) Naming.lookup(
            String.format(DEFAULT_RMI, RemoteObjectRegistry.LOGIN_CONTROLLER.getNaming())
        );
    }

	public static ITournamentController lookupForTournamentController() {
		// TODO Auto-generated method stub
		return null;
	}
}
