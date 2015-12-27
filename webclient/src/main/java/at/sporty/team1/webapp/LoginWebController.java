package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ILoginControllerEJB;
import at.sporty.team1.shared.api.ejb.IMemberControllerEJB;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.security.SecurityModule;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ejb.EJB;
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
public class LoginWebController implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private ILoginControllerEJB _loginController;
    @EJB
    private IMemberControllerEJB _memberController;

    private String _user;
    private String _password;

    public LoginWebController() {
    }

    public void authorize() {

        try {

            SessionDTO session = SecurityModule.authorize(
                getUser(),
                getPassword(),
                getClientRSAKeyPair(),
                SecurityModule.getDecodedRSAPublicKey(_loginController.getServerPublicKey()),
                _loginController::authorize
            );

            getSessionMap().put(SessionConstants.ACTIVE_SESSION.getConstant(), session);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (RemoteCommunicationException e) {
            e.printStackTrace();
        }
    }

    public String logout() {
        getSessionMap().clear();

        //should be handled like this otherwise we will get an exception
        //because of reference to not existing object
        return "login";
    }

    /* getter and setter */
    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        _user = user;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    private synchronized KeyPair getClientRSAKeyPair()
    throws SecurityException {
        if (getSessionMap().containsKey(SessionConstants.CLIENT_KEY_PAIR.getConstant())) {
            getSessionMap().put(
                SessionConstants.CLIENT_KEY_PAIR.getConstant(),
                SecurityModule.generateNewRSAKeyPair(512)
            );
        }
        return (KeyPair) getSessionMap().get(SessionConstants.CLIENT_KEY_PAIR.getConstant());
    }

    private Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }
}
