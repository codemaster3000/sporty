package at.sporty.team1.application.controller;

import at.sporty.team1.application.dtos.MemberDTO;
import at.sporty.team1.application.exceptions.ValidationException;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.misc.InputSanitizer;
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
        if (InputSanitizer.check(memberDTO.getFirstName(), InputSanitizer.TYPE.name) &&
                InputSanitizer.check(memberDTO.getLastName(), InputSanitizer.TYPE.name) &&
                InputSanitizer.check(memberDTO.getGender(), InputSanitizer.TYPE.gender) &&
                InputSanitizer.check(memberDTO.getDateOfBirth(), InputSanitizer.TYPE.daydate) &&
                InputSanitizer.check(memberDTO.getEmail(), InputSanitizer.TYPE.email) &&
                InputSanitizer.check(memberDTO.getAddress(), InputSanitizer.TYPE.address) &&
                InputSanitizer.check(memberDTO.getDepartment(), InputSanitizer.TYPE.text) &&
                InputSanitizer.check(memberDTO.getRole(), InputSanitizer.TYPE.text) &&
                InputSanitizer.check(memberDTO.getSport(), InputSanitizer.TYPE.text) &&
                InputSanitizer.check(memberDTO.getUsername(), InputSanitizer.TYPE.username) &&
                InputSanitizer.check(memberDTO.getPassword(), InputSanitizer.TYPE.password))
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
            LOGGER.error("Wrong Input creating Member: {}", InputSanitizer.lastFailedValidation);

            ValidationException validationException = new ValidationException();
            validationException.setReason(InputSanitizer.lastFailedValidation);

            throw validationException;
        }
    }

    public static void delete(String memberId) { //TODO
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
