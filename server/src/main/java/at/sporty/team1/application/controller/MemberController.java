package at.sporty.team1.application.controller;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.readonly.IRMember;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by f00 on 28.10.15.
 */
public class MemberController extends UnicastRemoteObject implements IMemberController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public MemberController() throws RemoteException {
        super();
    }

    @Override
    public void createOrSaveMember(MemberDTO dto)
    throws RemoteException, ValidationException {

        if (dto == null) return;

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (
            !inputSanitizer.isValid(dto.getFirstName(), DataType.NAME) ||
            !inputSanitizer.isValid(dto.getLastName(), DataType.NAME) ||
            !inputSanitizer.isValid(dto.getDateOfBirth(), DataType.SQL_DATE) ||
            !inputSanitizer.isValid(dto.getEmail(), DataType.EMAIL) ||
            !inputSanitizer.isValid(dto.getAddress(), DataType.ADDRESS) ||
            !inputSanitizer.isValid(dto.getGender(), DataType.GENDER)
        ) {
            // There has been bad input, throw the Exception
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            /* pulling a MemberDAO and save the Member */
            PersistenceFacade.getNewMemberDAO().saveOrUpdate(
                MAPPER.map(dto, Member.class)
            );

            LOGGER.info("Member \"{} {}\" was successfully saved.", dto.getFirstName(), dto.getLastName());

        } catch (PersistenceException e) {
            LOGGER.error("Error occurs while communicating with DB.", e);
        }
    }
    
    @Override
    public List<MemberDTO> searchAllMembers()
    throws RemoteException {

        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findAll();

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"all Members\".", e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByNameString(String searchString, boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException, ValidationException {

    	List<? extends IMember> rawResults = new LinkedList<>();
    	List<? extends IMember> rawResultsByFee = null;
    	
        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(searchString, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResultsByName = PersistenceFacade.getNewMemberDAO().findByNameString(searchString);

//            if(notPaidCheckbox){
//            	rawResultsByFee = PersistenceFacade.getNewMemberDAO().findByNotPayedFee();
//            }else if(paidCheckbox){
//            	rawResultsByFee = PersistenceFacade.getNewMemberDAO().findByPayedFee();
//            }
//
//            //Compare the two lists
//            if(rawResultsByFee != null){
//
//
//
//	            	for(int i = 0; i < rawResultsByFee.size(); i++){
//	            		if(rawResultsByName.contains(rawResultsByFee.get(i))){
//	                		rawResults.add(rawResultsByFee.get(i));
//	                	}
//	            	}
//
//
//            }else{
//            	rawResults.addAll(rawResultsByName);
//            }

            //creating predicate basic predicate for filtering (Object != null)
            Predicate<IRMember> nonNullPredicate = Objects::nonNull;


            //filtering all rawResultsByName for isFeePaid
            if (paidCheckbox && !notPaidCheckbox) {

                //filter for members who paid their Fee
                Predicate<IRMember> paidPredicate = nonNullPredicate.and(
                    member -> member.getIsFeePaid().equals(paidCheckbox)
                );

                rawResults = rawResultsByName.stream()
                        .filter(paidPredicate).collect(Collectors.toList());

            } else if (notPaidCheckbox && !paidCheckbox) {

                //filter for members who didn't pay their Fee
                Predicate<IRMember> notPaidPredicate = nonNullPredicate.and(
                    member -> member.getIsFeePaid().equals(notPaidCheckbox)
                );

                rawResults = rawResultsByName.stream()
                        .filter(notPaidPredicate).collect(Collectors.toList());

            } else {

                //both feePaid and notFeePaid are true or false -> return whole list;
                rawResults = rawResultsByName;

            }

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"{}\".", searchString, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByTeamName(String teamName, boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException, ValidationException {
        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByTeamName(teamName);

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"{}\".", teamName, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException, ValidationException {
        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(dateOfBirth, DataType.SQL_DATE)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByDateOfBirth(dateOfBirth);

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while searching for \"{}\".", dateOfBirth, e);
            return null;
        }
    }

    @Override
    public void deleteMember(MemberDTO memberDTO)
    throws RemoteException {
        try {

            PersistenceFacade.getNewMemberDAO().delete(
                MAPPER.map(memberDTO, Member.class)
            );

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurs while deleting member \"{}\".",
                memberDTO.getFirstName(),
                memberDTO.getLastName(),
                e
            );
        }
    }
}
