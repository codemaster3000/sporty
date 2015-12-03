package at.sporty.team1.communication;

import at.sporty.team1.rmi.RemoteObjectRegistry;
import at.sporty.team1.rmi.api.*;
import at.sporty.team1.rmi.dtos.AuthorisationDTO;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.SecurityException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.security.SecurityModule;
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

    private static final String DEFAULT_RMI = "rmi://5.35.247.12/%s";
    private static final Map<Class<? extends IRemoteController>, Remote> CONTROLLER_MAP = new HashMap<>();
    private static PublicKey _activeServerPublicKey;
    private static KeyPair _activeRSAKeyPair;
    private static CachedSession _extendedActiveSession;


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

    public static INotificationController lookupForNotificationController()
    throws  RemoteException, NotBoundException, MalformedURLException {
        if (!CONTROLLER_MAP.containsKey(INotificationController.class)) {
            CONTROLLER_MAP.put(INotificationController.class, Naming.lookup(
                String.format(DEFAULT_RMI, RemoteObjectRegistry.NOTIFICATION_CONTROLLER.getNaming())
            ));
        }

        return (INotificationController) CONTROLLER_MAP.get(INotificationController.class);
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
    
    // TODO register ClientNotificationPuller
}

