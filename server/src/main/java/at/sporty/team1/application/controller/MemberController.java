package at.sporty.team1.application.controller;

import at.sporty.team1.application.builder.MemberBuilder;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.daos.MemberDAO;

import javax.persistence.PersistenceException;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController {
    private MemberDAO _memberDAO;
    private Member _member;

    public void create() {
//        //TODO conditions validation
//
//        IMember member = new Member();
//        member.setFirstName(_firstName);
//        member.setLastName(_lastName);
//        member.setGender(_gender);
//        member.setDateOfBirth(_dateOfBirth);
//        member.setEmail(_email);
//        member.setAddress(_address);
//        member.setDepartment(_department);
//        member.setRole(_role);
//        member.setSport(_sport);
//        member.setUsername(_username);
//        member.setPassword(_password);
//
//        try {
//            PersistenceFacade.getNewGenericDAO(Member.class).saveOrUpdate((Member) member);
//
//            //TODO replace with logger.
//            System.out.println("NEW MEMBER WAS CREATED.");
//        } catch (PersistenceException e) {
//            //TODO: LOGGER.error(e);
//        }
    }

    public static MemberBuilder createNewMember() {
        return new MemberBuilder();
    }

    public void delete(String memberId) {

    }

    public void saveChanges(Member member) {

    }


}
