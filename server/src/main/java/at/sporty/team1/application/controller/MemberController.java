package at.sporty.team1.application.controller;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.readonly.IRMember;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.persistence.daos.MemberDAO;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController extends UnicastRemoteObject implements IMemberController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();

    public MemberController() throws RemoteException {
        super();
    }

    @Override
    public void createOrSaveMember(MemberDTO memberDTO)
    throws RemoteException, ValidationException {

        if (memberDTO == null) return;

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();

        if (inputSanitizer.check(memberDTO.getFirstName(), DataType.NAME) &&
            inputSanitizer.check(memberDTO.getLastName(), DataType.NAME) &&
            inputSanitizer.check(memberDTO.getDateOfBirth(), DataType.SQL_DATE) &&
            inputSanitizer.check(memberDTO.getEmail(), DataType.EMAIL) &&
            inputSanitizer.check(memberDTO.getAddress(), DataType.ADDRESS) &&
            inputSanitizer.check(memberDTO.getGender(), DataType.GENDER))
        {
        	
	    	 try {
	             /* pulling a MemberDAO and save the Member */
	             PersistenceFacade.getNewMemberDAO().saveOrUpdate(
	                 convertDTOToMember(memberDTO)
	             );
	
	             LOGGER.info("New member \"{} {}\" was created.", memberDTO.getFirstName(), memberDTO.getLastName());
	
	         } catch (PersistenceException e) {
	             LOGGER.error("Error occurs while communicating with DB.", e);
	         }
        	
     
        } else {
            // There has been bad Input, throw the Exception
            LOGGER.error("Wrong Input creating Member: {}", inputSanitizer.getLastFailedValidation());

            ValidationException validationException = new ValidationException();
            validationException.setReason(inputSanitizer.getLastFailedValidation());
            
            throw validationException;
        }
        
       
    }

    @Override
    public MemberDTO loadMemberById(int memberId)
    throws RemoteException {
        return convertMemberToDTO(PersistenceFacade.getNewMemberDAO().findById(memberId));
    }

    /**
     * Search for members by String (name, birthdate, department, teamname)
     *
     * @param searchQuery
     *
     * @return null or List<IMember>
     * @throws RemoteException
     */
    @Override
    public List<MemberDTO> searchForMembers(String searchQuery)
    throws RemoteException {
        List<? extends IMember> rawSearchResultsList = PersistenceFacade.getNewMemberDAO().findByString(searchQuery);

        //Converting results to MemberDTO
        return rawSearchResultsList.stream()
                .map(MemberController::convertMemberToDTO)
                .collect(Collectors.toList());
    }

    public void delete(String memberId) throws SQLException {
        //TODO test

        MemberDAO memberDAO = PersistenceFacade.getNewMemberDAO();
        Member member = memberDAO.findById(memberId);
        memberDAO.delete(member);
    }

    /**
     * A helping method, converts all Member objects to MemberDTO.
     *
     * @param member Member to be converted to a MemberDTO
     * @return MemberDTO representation of the given Member.
     */
    private static MemberDTO convertMemberToDTO (IRMember member){
        if (member != null) {
            return new MemberDTO()
                .setMemberId(member.getMemberId())
                .setFirstName(member.getFirstName())
                .setLastName(member.getLastName())
                .setGender(member.getGender())
                .setDateOfBirth(convertDateToString(member.getDateOfBirth()))
                .setEmail(member.getEmail())
                .setAddress(member.getAddress())
                .setDepartment(member.getDepartment())
                .setTeamId(member.getTeamId())
                .setSquad(member.getSquad())
                .setRole(member.getRole())
                .setUsername(member.getUsername());
        }
        return null;
    }

    /**
     * A helping method, converts all MemberDTO to Member objects.
     *
     * @param memberDTO MemberDTO to be converted to a Member
     * @return Member representation of the given MemberDTO.
     */
    private static Member convertDTOToMember (MemberDTO memberDTO){
        if (memberDTO != null) {
            Member member = new Member();

            member.setMemberId(memberDTO.getMemberId());
            member.setFirstName(memberDTO.getFirstName());
            member.setLastName(memberDTO.getLastName());
            member.setGender(memberDTO.getGender());
            member.setDateOfBirth(parseDate(memberDTO.getDateOfBirth()));
            member.setEmail(memberDTO.getEmail());
            member.setAddress(memberDTO.getAddress());
            member.setDepartment(memberDTO.getDepartment());
            member.setTeamId(memberDTO.getTeamId());
            member.setSquad(memberDTO.getSquad());
            member.setRole(memberDTO.getRole());
            member.setUsername(memberDTO.getUsername());

            return member;
        }
        return null;
    }

    /**
     * A helping method.
     *
     * @param s String to be parsed as a date
     * @return parsed date
     */
    private static Date parseDate(String s) {
        return s != null ? Date.valueOf(s) : null;
    }

    /**
     * A helping method.
     *
     * @param d Date to be converted to String
     * @return converted date
     */
    private static String convertDateToString(Date d) {
        return d != null ? d.toString() : null;
    }
}
