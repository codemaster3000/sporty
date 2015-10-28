package at.sporty.team1.application.controller;

import at.sporty.team1.application.dtos.MemberDTO;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.daos.MemberDAO;

import javax.persistence.PersistenceException;
import java.sql.Date;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController {

    public static MemberDTO getNewMemberDTO() {
        return new MemberDTO();
    }

    public static void create(MemberDTO memberDTO) {
        //TODO conditions validation

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
            PersistenceFacade.getNewGenericDAO(Member.class).saveOrUpdate((Member) member);

            //TODO replace with logger.
            System.out.println("NEW MEMBER WAS CREATED.");
        } catch (PersistenceException e) {
            //TODO: LOGGER.error(e);
        }
    }

    public static void delete(String memberId) {

    }

    public static void saveChanges(Member member) {

    }

    private static Date readDate(String s) {
        return s != null ? Date.valueOf(s) : null;
    }
}
