/*
package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ILoginControllerEJB;
import at.sporty.team1.shared.dtos.AuthorisationDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.exceptions.SecurityException;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.security.CryptoPrimitive;

*/
/**
 * Created by f00 on 27.12.15.
 *//*

@ManagedBean
@RequestScoped
public class LoginWebController implements Serializable {
    private static final long serialVersionUID = 1L;

    public String user;
    public String pw;

    @EJB
    private ILoginControllerEJB _iLoginControllerEJB;

    public LoginWebController() {
    }

    */
/*
    Getter + Setter for Login / Password.
Authorize method. (Delegates request to the LoginControllerEJBAdapter. On suceesfull
login stores OUR session object in Java "SessionStorage". Not in the web browser's
session storage!!)
     *//*


    */
/**
     * Authorize delegation method
     * @param username
     * @param pw
     *//*

    public void authorize(String username, String pw) {
        //TODO encrypt with pubkey

        AuthorisationDTO authorisationDTO = new AuthorisationDTO();
        authorisationDTO.setEncryptedUserLogin(username);
        authorisationDTO.setEncryptedUserPassword(pw);

        try {

           SessionDTO sessionDTO = _iLoginControllerEJB.authorize(authorisationDTO);
            //TODO Store sessionDTO

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    */
/* getter and setter *//*

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
*/
