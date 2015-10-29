package at.sporty.team1.application.controller;

import at.sporty.team1.application.dtos.MemberDTO;
import at.sporty.team1.application.exceptions.ValidationException;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.persistence.PersistenceFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.sql.Date;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController {
    private static final Logger LOGGER = LogManager.getLogger();

    public static MemberDTO getNewMemberDTO() {
        return new MemberDTO();
    }

    public static void create(MemberDTO memberDTO) throws ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        if (inputSanitizer.check(memberDTO.getFirstName(), DataType.NAME) &&
                inputSanitizer.check(memberDTO.getLastName(), DataType.NAME) &&
                inputSanitizer.check(memberDTO.getGender(), DataType.GENDER) &&
                inputSanitizer.check(memberDTO.getDateOfBirth(), DataType.DAY_DATE) &&
                inputSanitizer.check(memberDTO.getEmail(), DataType.EMAIL) &&
                inputSanitizer.check(memberDTO.getAddress(), DataType.ADDRESS) &&
                inputSanitizer.check(memberDTO.getDepartment(), DataType.TEXT) &&
                inputSanitizer.check(memberDTO.getRole(), DataType.TEXT) &&
                inputSanitizer.check(memberDTO.getSport(), DataType.TEXT) &&
                inputSanitizer.check(memberDTO.getUsername(), DataType.USERNAME) &&
                inputSanitizer.check(memberDTO.getPassword(), DataType.PASSWORD))
        {
            // all Input validated and OK

            IMember member = new Member();
            member.setFirstName(memberDTO.getFirstName());
            member.setLastName(memberDTO.getLastName());
            member.setGender(memberDTO.getGender());
            member.setDateOfBirth(readDate(memberDTO.getDateOfBirth()));
            member.setEmail(memberDTO.getEmail());
            member.setAddress(memberDTO.getAddress());
            member.setDepartment(memberDTO.getDepartment());
            member.setRole(memberDTO.getRole());
            member.setSport(memberDTO.getSport());
            member.setUsername(memberDTO.getUsername());
            member.setPassword(memberDTO.getPassword());

            try {
                /* get a MemberDAO and save the Member */
                PersistenceFacade.getNewGenericDAO(Member.class).saveOrUpdate((Member) member);
                LOGGER.info("NEW MEMBER WAS CREATED");
            } catch (PersistenceException e) {
                LOGGER.error(e);
            }

        } else {
            // There has been bad Input, throw the Exception
            LOGGER.error("Wrong Input creating Member: {}", inputSanitizer.getLastFailedValidation());

            ValidationException validationException = new ValidationException();
            validationException.setReason(inputSanitizer.getLastFailedValidation());

            throw validationException;
        }
    }

    public static void delete(String memberId) {
        //TODO Delete member
//        MemberDAO memberDAO = PersistenceFacade.getNewGenericDAO(Class<Member>);
//       Member member = .findById(memberId);
//        PersistenceFacade.
    }

    public static void saveChanges(Member member) {


    }

    /**
     * A helping function
     * @param s
     * @return
     */
    private static Date readDate(String s) {
        return s != null ? Date.valueOf(s) : null;
    }
}
