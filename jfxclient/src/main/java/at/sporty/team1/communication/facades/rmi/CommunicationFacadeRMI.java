package at.sporty.team1.communication.facades.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.rmi.*;

public class CommunicationFacadeRMI {

	private static final Map<Class<? extends IRemoteControllerRMI>, Remote> CONTROLLER_MAP = new HashMap<>();
	private static final String DEFAULT_RMI = "rmi://localhost/%s";
	
	private CommunicationFacadeRMI(){}
	
	public static IMemberControllerRMI lookupForMemberControllerRMI()
    throws  NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IMemberControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(IMemberControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.MEMBER_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (IMemberControllerRMI) CONTROLLER_MAP.get(IMemberControllerRMI.class);
    }
	
	 public static ITeamControllerRMI lookupForTeamControllerRMI()
    throws NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITeamControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(ITeamControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.TEAM_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (ITeamControllerRMI) CONTROLLER_MAP.get(ITeamControllerRMI.class);
    }
	 
	 public static IDepartmentControllerRMI lookupForDepartmentControllerRMI()
    throws NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IDepartmentControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(IDepartmentControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.DEPARTMENT_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (IDepartmentControllerRMI) CONTROLLER_MAP.get(IDepartmentControllerRMI.class);
    }
	 
	 public static ILoginControllerRMI lookupForLoginControllerRMI()
    throws  NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ILoginControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(ILoginControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (ILoginControllerRMI) CONTROLLER_MAP.get(ILoginControllerRMI.class);
    }

	public static ITournamentControllerRMI lookupForTournamentControllerRMI()
	throws NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITournamentControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(ITournamentControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.TOURNAMENT_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (ITournamentControllerRMI) CONTROLLER_MAP.get(ITournamentControllerRMI.class);
	}
	
	public static INotificationControllerRMI lookupForNotificationControllerRMI()
    throws NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(INotificationControllerRMI.class)) {
            try {
				CONTROLLER_MAP.put(INotificationControllerRMI.class, Naming.lookup(
				    String.format(DEFAULT_RMI, RemoteObjectRegistry.NOTIFICATION_CONTROLLER.getNamingRMI())
				));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return (INotificationControllerRMI) CONTROLLER_MAP.get(INotificationControllerRMI.class);
    }
	
	
	
}
