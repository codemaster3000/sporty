package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ILoginControllerEJB;
import at.sporty.team1.shared.api.ejb.IMemberControllerEJB;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.security.SecurityModule;
import at.sporty.team1.util.SessionConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.util.Map;

/**
 * Created by f00 on 27.12.15.
 */
@ManagedBean
@RequestScoped
public class AuthWebController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    @EJB
    private ILoginControllerEJB _loginController;
    @EJB
    private IMemberControllerEJB _memberController;

    private String _username;
    private String _password;
    private String _userInitials;
    private String _userFullName;

    public AuthWebController() {
    }

    public void authorize() {

        try {

            SessionDTO activeSession = SecurityModule.authorize(
                getUsername(),
                getPassword(),
                getClientRSAKeyPair(),
                SecurityModule.getDecodedRSAPublicKey(_loginController.getServerPublicKey()),
                _loginController::authorize
            );
            getSessionMap().put(SessionConstants.ACTIVE_SESSION.getConstant(), activeSession);

            MemberDTO activeUser = _memberController.findMemberById(activeSession.getUserId(), activeSession);
            getSessionMap().put(SessionConstants.ACTIVE_USER.getConstant(), activeUser);

        } catch (InvalidKeyException e) {

            LOGGER.error("Private key is not suitable.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

        } catch (BadPaddingException | IllegalBlockSizeException e) {

            LOGGER.error("Received data is corrupted.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

        } catch (SecurityException | NotAuthorisedException | UnknownEntityException e) {

            LOGGER.error("Error occurred while generating client fingerprint.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

        } catch (RemoteCommunicationException e) {

            LOGGER.error("Communication error occurred while attempting to authenticate.", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public String logout() {
        getSessionMap().clear();

        //should be handled like this otherwise we will get an exception
        //because of reference to not existing object
        return "auth";
    }

    public boolean getIsAuthorized() {
        return getSessionMap().containsKey(SessionConstants.ACTIVE_USER.getConstant());
    }

    /* getter and setter */
    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getUserInitials() {
        if (_userInitials == null) {
            MemberDTO activeUser = getActiveUser();

            if (activeUser != null) {
                String firstName = activeUser.getFirstName();
                String lastName = activeUser.getLastName();

                _userInitials = getInitial(firstName) + getInitial(lastName);
            }
        }
        return _userInitials;
    }

    public String getUserFullName() {
        if (_userFullName == null) {
            MemberDTO activeUser = getActiveUser();

            String firstName = activeUser.getFirstName();
            String lastName = activeUser.getLastName();

            _userFullName = String.format("%s %s", lastName, firstName);
        }
        return _userFullName;
    }

    private KeyPair getClientRSAKeyPair()
    throws SecurityException {
        if (getSessionMap().containsKey(SessionConstants.CLIENT_KEY_PAIR.getConstant())) {
            getSessionMap().put(
                SessionConstants.CLIENT_KEY_PAIR.getConstant(),
                SecurityModule.generateNewRSAKeyPair(SecurityModule.DEFAULT_KEY_SIZE)
            );
        }
        return (KeyPair) getSessionMap().get(SessionConstants.CLIENT_KEY_PAIR.getConstant());
    }

    private Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    private MemberDTO getActiveUser() {
        return (MemberDTO) getSessionMap().get(SessionConstants.ACTIVE_USER.getConstant());
    }

    private String getInitial(String s) {
        if (s != null && !s.isEmpty()) {
            return String.valueOf(s.charAt(0));
        }
        return "";
    }
}
