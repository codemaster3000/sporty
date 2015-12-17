package at.sporty.team1.communication.facades;

import at.sporty.team1.communication.facades.api.*;
import at.sporty.team1.communication.facades.ejb.CommunicationFacadeEJB;
import at.sporty.team1.communication.facades.rmi.CommunicationFacadeRMI;
import at.sporty.team1.communication.util.CachedSession;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.presentation.util.CommunicationType;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.security.SecurityModule;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Properties;

public class CommunicationFacade implements ICommunicationFacade {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String COMMUNICATION_TYPE = "COMMUNICATION_TYPE";
    private static final String PROPERTY_FILE = "client.properties";
    private static final Properties PROPERTIES = new Properties();
    private static final SimpleBooleanProperty SESSION_AVAILABLE_PROPERTY = new SimpleBooleanProperty(false);
    private static CommunicationFacade _instance;

    private PublicKey _activeServerPublicKey;
    private KeyPair _activeRSAKeyPair;
    private CachedSession _extendedActiveSession;

    private final ICommunicationFacade _subjectCommunicationFacade;

    /**
     * Communication facade constructor receives CommunicationType (RMI or EJB)
     * as parameter and instantiates a subject depending on received parameter.
     *
     * @param communicationType CommunicationType - ENUM, defines which subject will be used.
     */
    private CommunicationFacade(CommunicationType communicationType, Properties properties) {
        switch (communicationType) {
            case RMI: {
                _subjectCommunicationFacade = new CommunicationFacadeRMI(properties);

                LOGGER.info("Starting client in RMI mode.");
                break;
            }

            case EJB: {
                _subjectCommunicationFacade = new CommunicationFacadeEJB(properties);

                LOGGER.info("Starting client in EJB mode.");
                break;
            }

            default: {
                _subjectCommunicationFacade = new CommunicationFacadeEJB(properties);

                LOGGER.info("Starting client in Default(RMI) mode.");
                break;
            }
        }
    }

    public static CommunicationFacade getInstance() {
        if (_instance == null) {

            synchronized (CommunicationFacade.class) {

                if (_instance == null) {
                    try {

                        PROPERTIES.load(getPropertySource(PROPERTY_FILE));

                        CommunicationType communicationType = CommunicationType.valueOf(
                            PROPERTIES.getProperty(COMMUNICATION_TYPE)
                        );

                        _instance = new CommunicationFacade(communicationType, PROPERTIES);

                    } catch (FileNotFoundException e) {
                        LOGGER.error(
                            "Could not find config files. Both external and from classpath {}. Execution is terminated.",
                            PROPERTY_FILE,
                            e
                        );

                        System.exit(1);

                    } catch (IOException e) {
                        LOGGER.error(
                            "An error occurs while loading client properties from {}. Execution is terminated.",
                            PROPERTY_FILE,
                            e
                        );

                        System.exit(1);
                    } catch (IllegalArgumentException e) {
                        LOGGER.error(
                            "Unknown communication type received. Check your property file.",
                            PROPERTY_FILE,
                            e
                        );

                        System.exit(1);
                    }
                }
            }
        }

        return _instance;
    }

    @Override
    public IMemberControllerUniversal lookupForMemberController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForMemberController();
    }

    @Override
    public ITeamControllerUniversal lookupForTeamController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForTeamController();
    }

    @Override
    public IDepartmentControllerUniversal lookupForDepartmentController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForDepartmentController();
    }

    @Override
    public ILoginControllerUniversal lookupForLoginController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForLoginController();
    }

    @Override
    public ITournamentControllerUniversal lookupForTournamentController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForTournamentController();
    }

    @Override
    public INotificationControllerUniversal lookupForNotificationController()
    throws RemoteCommunicationException {
        return _subjectCommunicationFacade.lookupForNotificationController();
    }

    public synchronized boolean authorize(String username, String password)
    throws RemoteCommunicationException, SecurityException, InvalidKeyException, BadPaddingException,
    IllegalBlockSizeException, UnknownEntityException, NotAuthorisedException {
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
            .setClientPublicKey(SecurityModule.getEncodedRSAPublicKey(getClientRSAKeyPair()));

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

    public synchronized void logout() {
        SESSION_AVAILABLE_PROPERTY.set(false);
        _extendedActiveSession = null;
        _activeServerPublicKey = null;
    }

    public synchronized PublicKey getServerPublicKey()
    throws RemoteCommunicationException, SecurityException {
        if (_activeServerPublicKey == null) {
            _activeServerPublicKey = SecurityModule.getDecodedRSAPublicKey(
                lookupForLoginController().getServerPublicKey()
            );
        }
        return _activeServerPublicKey;
    }

    public synchronized KeyPair getClientRSAKeyPair()
    throws SecurityException {
        if (_activeRSAKeyPair == null) {
            _activeRSAKeyPair = SecurityModule.generateNewRSAKeyPair(512);
        }
        return _activeRSAKeyPair;
    }

    public synchronized SessionDTO getActiveSession() {
        return _extendedActiveSession != null ? _extendedActiveSession.getSessionDTO() : null;
    }

    public synchronized CachedSession getExtendedActiveSession() {
        return _extendedActiveSession;
    }

    public SimpleBooleanProperty getSessionAvailableProperty() {
        return SESSION_AVAILABLE_PROPERTY;
    }

    private static FileInputStream getPropertySource(String propertyFile)
    throws FileNotFoundException {
        try {
            //looking up for external config
            return getExternalPropertySource(propertyFile);
        } catch (FileNotFoundException e) {
            //looking up for integrated config
            return getClassPathPropertySource(propertyFile);
        }
    }

    private static FileInputStream getExternalPropertySource(String propertyFile)
    throws FileNotFoundException {
        return new FileInputStream(propertyFile);
    }

    private static FileInputStream getClassPathPropertySource(String propertyFile)
    throws FileNotFoundException {
        ClassLoader classLoader = CommunicationFacade.class.getClassLoader();

        URL propertyURL = classLoader.getResource(propertyFile);
        if (propertyURL != null) {
            return new FileInputStream(propertyURL.getFile());
        } else {
            throw new FileNotFoundException(propertyFile + " was not found.");
        }
    }
}

