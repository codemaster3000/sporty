package at.sporty.team1.application.controller;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController extends UnicastRemoteObject implements IMemberController {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();

    public MemberController() throws RemoteException {
        super();
    }

    @Override
    public void createNewMember(String fName, String lName, String bday, String email, String phone, String address, String sport, String gender)
    throws RemoteException {
        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        if (inputSanitizer.check(fName, DataType.NAME) &&
            inputSanitizer.check(lName, DataType.NAME) &&
            inputSanitizer.check(bday, DataType.DAY_DATE) &&
            inputSanitizer.check(email, DataType.EMAIL) &&
            inputSanitizer.check(phone, DataType.PHONE_NUMBER) &&
            inputSanitizer.check(address, DataType.ADDRESS) &&
            inputSanitizer.check(sport, DataType.TEXT) &&
            inputSanitizer.check(gender, DataType.GENDER))
        {
            // all Input validated and OK

            IMember member = new Member();
            member.setFirstName(fName);
            member.setLastName(lName);
            member.setDateOfBirth(readDate(bday));
            member.setEmail(email);
            //FIXME: member.setPhoneNumber(phone); - is not present in DB.
            member.setAddress(address);
            member.setSport(sport);
            member.setGender(gender);

            try {
                /* pulling a GenericDAO and save the Member */
                PersistenceFacade.getNewGenericDAO(Member.class).saveOrUpdate((Member) member);

                LOGGER.info("New member \"{} {}\" was created.", fName, lName);

            } catch (PersistenceException e) {
                LOGGER.error("Error occurs while communicating with DB.", e);
            }

        } else {
            // There has been bad Input, throw the Exception
            LOGGER.error("Wrong Input creating Member: {}", inputSanitizer.getLastFailedValidation());

            //TODO  throws ValidationException;
            //ValidationException validationException = new ValidationException();
            //validationException.setReason(inputSanitizer.getLastFailedValidation());
            //
            //throw validationException;
        }
    }

    public void delete(String memberId) {
        //TODO Delete member
        //MemberDAO memberDAO = PersistenceFacade.getNewGenericDAO(Class<Member>);
        //Member member = .findById(memberId);
        //PersistenceFacade.
    }

    public void saveChanges(Member member) {
    }

    /**
     * A helping method
     *
     * @param s String to be parsed as a date
     * @return parsed date
     */
    private static Date readDate(String s) {
        return s != null ? Date.valueOf(s) : null;
    }
}
