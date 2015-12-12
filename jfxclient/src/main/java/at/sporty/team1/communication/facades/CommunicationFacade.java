package at.sporty.team1.communication.facades;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import at.sporty.team1.communication.facades.rmi.CommunicationFacadeRMI;
import at.sporty.team1.communication.util.CachedSession;
import at.sporty.team1.presentation.util.CommunicationType;
import at.sporty.team1.shared.api.ejb.INotificationControllerEJB;
import at.sporty.team1.shared.api.rmi.INotificationControllerRMI;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.security.SecurityModule;
import javafx.beans.property.SimpleBooleanProperty;

public class CommunicationFacade {
    public static final SimpleBooleanProperty SESSION_AVAILABLE_PROPERTY = new SimpleBooleanProperty(false);
    
    private static PublicKey _activeServerPublicKey;
    private static KeyPair _activeRSAKeyPair;
    private static CachedSession _extendedActiveSession;
    private static CommunicationType _communicationType;

    private CommunicationFacade() {
    }

    /**
     * sets the type of the Communication RMI or EJB
     * @param type (CommunicationType - ENUM)
     */
    public static void setCommunicationType(CommunicationType type){
    	_communicationType = type;
    }
    
    public static PublicKey getServerPublicKey()
    throws NotBoundException, MalformedURLException, SecurityException {
        if (_activeServerPublicKey == null) {
        	
        	if(_communicationType == CommunicationType.RMI){
	            _activeServerPublicKey = SecurityModule.getDecodedRSAPublicKey(
	                CommunicationFacadeRMI.lookupForLoginControllerRMI().getServerPublicKey()
	            );
        	}else if(_communicationType == CommunicationType.EJB){
        		//TODO:
        	}else{
        		//TODO: exception
        	}
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
    throws NotBoundException, MalformedURLException, SecurityException, InvalidKeyException,
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
        SessionDTO session = CommunicationFacadeRMI.lookupForLoginControllerRMI().authorize(authorisationDTO);
        if (session != null) {
            //Decrypting client fingerprint for client side
            cipher.init(Cipher.DECRYPT_MODE, getClientRSAKeyPair().getPrivate());
            byte[] decodedFingerprint = cipher.doFinal(session.getClientFingerprint());

            //Encrypting client fingerprint for server side
            cipher.init(Cipher.ENCRYPT_MODE, getServerPublicKey());
            byte[] clientFingerprint = cipher.doFinal(decodedFingerprint);

            session.setClientFingerprint(clientFingerprint);

            MemberDTO user = CommunicationFacadeRMI.lookupForMemberControllerRMI().findMemberById(session.getUserId(), session);
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

	public static INotificationControllerRMI lookupForNotificationControllerRMI() 
	throws MalformedURLException, NotBoundException{
		
		return CommunicationFacadeRMI.lookupForNotificationControllerRMI();		
	}
	
	public static INotificationControllerEJB lookupForNotificationControllerEJB() 
	throws MalformedURLException, NotBoundException{
		
		return CommunicationFacadeEJB.lookupForNotificationControllerEJB();		
	}
}

