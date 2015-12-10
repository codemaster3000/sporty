package at.sporty.team1.communication.facades;

import at.sporty.team1.communication.util.RemoteObjectRegistry;
import at.sporty.team1.shared.api.rmi.*;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.security.SecurityModule;
import at.sporty.team1.util.CachedSession;
import javafx.beans.property.SimpleBooleanProperty;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class CommunicationFacade {
    public static final SimpleBooleanProperty SESSION_AVAILABLE_PROPERTY = new SimpleBooleanProperty(false);

    private static final String DEFAULT_RMI = "rmi://localhost/%s";
    private static final Map<Class<? extends IRemoteControllerRMI>, Remote> CONTROLLER_MAP = new HashMap<>();
    private static PublicKey _activeServerPublicKey;
    private static KeyPair _activeRSAKeyPair;
    private static CachedSession _extendedActiveSession;


    private CommunicationFacade() {
    }

    public static IMemberControllerRMI lookupForMemberController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IMemberControllerRMI.class)) {
            CONTROLLER_MAP.put(IMemberControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.MEMBER_CONTROLLER.getNamingRMI())
            ));
        }

        return (IMemberControllerRMI) CONTROLLER_MAP.get(IMemberControllerRMI.class);
    }

    public static ITeamControllerRMI lookupForTeamController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITeamControllerRMI.class)) {
            CONTROLLER_MAP.put(ITeamControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.TEAM_CONTROLLER.getNamingRMI())
            ));
        }

        return (ITeamControllerRMI) CONTROLLER_MAP.get(ITeamControllerRMI.class);
    }

    public static IDepartmentControllerRMI lookupForDepartmentController()
    throws RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(IDepartmentControllerRMI.class)) {
            CONTROLLER_MAP.put(IDepartmentControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.DEPARTMENT_CONTROLLER.getNamingRMI())
            ));
        }

        return (IDepartmentControllerRMI) CONTROLLER_MAP.get(IDepartmentControllerRMI.class);
    }

    public static ILoginControllerRMI lookupForLoginController()
    throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ILoginControllerRMI.class)) {
            CONTROLLER_MAP.put(ILoginControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.LOGIN_CONTROLLER.getNamingRMI())
            ));
        }

        return (ILoginControllerRMI) CONTROLLER_MAP.get(ILoginControllerRMI.class);
    }

	public static ITournamentControllerRMI lookupForTournamentController()
	throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(ITournamentControllerRMI.class)) {
            CONTROLLER_MAP.put(ITournamentControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.TOURNAMENT_CONTROLLER.getNamingRMI())
            ));
        }

        return (ITournamentControllerRMI) CONTROLLER_MAP.get(ITournamentControllerRMI.class);
	}

    public static INotificationControllerRMI lookupForNotificationController()
    throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(INotificationControllerRMI.class)) {
            CONTROLLER_MAP.put(INotificationControllerRMI.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.NOTIFICATION_CONTROLLER.getNamingRMI())
            ));
        }

        return (INotificationControllerRMI) CONTROLLER_MAP.get(INotificationControllerRMI.class);
    }

    public static PublicKey getServerPublicKey()
    throws RemoteException, NotBoundException, MalformedURLException, SecurityException {
        if (_activeServerPublicKey == null) {
            _activeServerPublicKey = SecurityModule.getDecodedRSAPublicKey(
                lookupForLoginController().getServerPublicKey()
            );
        }
        return _activeServerPublicKey;
    }

    public static KeyPair getClientRSAKeyPair()
    throws SecurityException {
        if (_activeRSAKeyPair == null) {
            _activeRSAKeyPair = SecurityModule.generateNewRSAKeyPair(512);
        }
        return _activeRSAKeyPair;
    }

    public static SessionDTO getActiveSession() {
        return _extendedActiveSession != null ? _extendedActiveSession.getSessionDTO() : null;
    }

    public static CachedSession getExtendedActiveSession() {
        return _extendedActiveSession;
    }

    public static boolean authorize(String username, String password)
    throws RemoteException, NotBoundException, MalformedURLException, SecurityException, InvalidKeyException,
    BadPaddingException, IllegalBlockSizeException, UnknownEntityException, NotAuthorisedException {
        //disposing old session
        _extendedActiveSession = null;

        //Reading username and password
        byte[] rawUsername = username.getBytes();
        byte[] rawPassword = password.getBytes();

        //Preparing for data encryption
        Cipher cipher = SecurityModule.getNewRSACipher();
        cipher.init(Cipher.ENCRYPT_MODE, getServerPublicKey());

        AuthorisationDTO authorisationDTO = new AuthorisationDTO()
            .setEncryptedUserLogin(cipher.doFinal(rawUsername))
            .setEncryptedUserPassword(cipher.doFinal(rawPassword))
            .setClientPublicKey(SecurityModule.getEncodedRSAPublicKey(
                CommunicationFacade.getClientRSAKeyPair()
            ));

        //Getting authorisation result
        SessionDTO session = lookupForLoginController().authorize(authorisationDTO);
        if (session != null) {
            //Decrypting client fingerprint for client side
            cipher.init(Cipher.DECRYPT_MODE, getClientRSAKeyPair().getPrivate());
            byte[] decodedFingerprint = cipher.doFinal(session.getClientFingerprint());

            //Encrypting client fingerprint for server side
            cipher.init(Cipher.ENCRYPT_MODE, getServerPublicKey());
            byte[] clientFingerprint = cipher.doFinal(decodedFingerprint);

            session.setClientFingerprint(clientFingerprint);

            MemberDTO user = lookupForMemberController().findMemberById(session.getUserId(), session);
            _extendedActiveSession = new CachedSession(user, session);

            SESSION_AVAILABLE_PROPERTY.set(true);
            return true;
        } else {

            logout();
            return false;
        }
    }

    public static void logout() {
        SESSION_AVAILABLE_PROPERTY.set(false);
        _extendedActiveSession = null;
    }
}

