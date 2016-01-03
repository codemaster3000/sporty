package at.sporty.team1.application.auth;

import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.security.SecurityModule;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.KeyPair;
import java.util.concurrent.TimeUnit;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
public final class AuthManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final AuthManager SESSION_MANAGER_INSTANCE = new AuthManager();
    private static final PassiveExpiringMap<String, Integer> SESSION_REGISTRY = new PassiveExpiringMap<>(1, TimeUnit.HOURS);

    private KeyPair _serverKeyPair;
    private byte[] _encodedPublicServerKey;

    private AuthManager() {
        try {

            _serverKeyPair = SecurityModule.generateNewRSAKeyPair(SecurityModule.DEFAULT_KEY_SIZE);
            _encodedPublicServerKey = SecurityModule.getEncodedRSAPublicKey(_serverKeyPair);

        } catch (SecurityException e) {
            LOGGER.error("Error occurred while initializing AuthManager.", e);
            System.exit(1);
        }
    }

    public static AuthManager getInstance() {
        return SESSION_MANAGER_INSTANCE;
    }

    public final KeyPair getServerKeyPair() {
        return _serverKeyPair;
    }

    public final byte[] getEncodedPublicServerKey() {
        return _encodedPublicServerKey;
    }

    public final synchronized void registerNewSession(String sessionId, Integer memberId) {
        SESSION_REGISTRY.put(sessionId, memberId);
    }

    public final synchronized void restartTimeOut(String sessionId, Integer memberId) {
        //Remove value from registry.
        SESSION_REGISTRY.remove(sessionId);

        //Restarting timeout for given session.
        SESSION_REGISTRY.put(sessionId, memberId);
    }

    public final synchronized Integer getSession(String sessionId) {
        return SESSION_REGISTRY.get(sessionId);
    }

    public final synchronized void removeSession(String sessionId) {
        SESSION_REGISTRY.remove(sessionId);
    }
}
