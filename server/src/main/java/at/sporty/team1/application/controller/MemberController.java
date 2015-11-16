package at.sporty.team1.application.controller;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.domain.readonly.IRMember;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.dtos.TeamDTO;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    public void createOrSaveMember(MemberDTO memberDTO)
    throws RemoteException, ValidationException {

        if (memberDTO == null) return;

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (
            !inputSanitizer.isValid(memberDTO.getFirstName(), DataType.NAME) ||
            !inputSanitizer.isValid(memberDTO.getLastName(), DataType.NAME) ||
            !inputSanitizer.isValid(memberDTO.getDateOfBirth(), DataType.SQL_DATE) ||
            !inputSanitizer.isValid(memberDTO.getEmail(), DataType.EMAIL) ||
            !inputSanitizer.isValid(memberDTO.getAddress(), DataType.ADDRESS) ||
            !inputSanitizer.isValid(memberDTO.getGender(), DataType.GENDER)
        ) {
            // There has been bad input, throw the Exception
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            /* pulling a MemberDAO and save the Member */
            PersistenceFacade.getNewMemberDAO().saveOrUpdate(
                MAPPER.map(memberDTO, Member.class)
            );

            LOGGER.info("Member \"{} {}\" was successfully saved.", memberDTO.getFirstName(), memberDTO.getLastName());

        } catch (PersistenceException e) {
            LOGGER.error("Error occurs while communicating with DB.", e);
        }
    }
    
    @Override
    public List<MemberDTO> searchAllMembers(boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException {

        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findAll();

            //filtering results for fee
            rawResults = filterWithFee(rawResults, paidCheckbox, notPaidCheckbox);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

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

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(searchString, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }
        
        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByNameString(searchString);

            //filtering results for fee
            rawResults = filterWithFee(rawResults, paidCheckbox, notPaidCheckbox);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

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
    public List<MemberDTO> searchMembersByCommonTeamName(String teamName, boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException, ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByCommonTeamName(teamName);

            //filtering results for fee
            rawResults = filterWithFee(rawResults, paidCheckbox, notPaidCheckbox);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

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
    public List<MemberDTO> searchMembersByTournamentTeamName(String teamName, boolean notPaidCheckbox, boolean paidCheckbox)
    throws RemoteException, ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByTournamentTeamName(teamName);

            //filtering results for fee
            rawResults = filterWithFee(rawResults, paidCheckbox, notPaidCheckbox);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

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

            //filtering results for fee
            rawResults = filterWithFee(rawResults, paidCheckbox, notPaidCheckbox);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

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

    /**
     * Helping method which filters rawResults dependent on FeePaid state.
     *
     * @param rawResults List to be filtered.
     * @param paid value of the feePaidCheckbox
     * @param notPaid value of the notFeePaidCheckbox
     * @return filtered rawResults list or null if rawResults were null or empty.
     */
    private List<? extends IMember> filterWithFee(List<? extends IMember> rawResults, boolean paid, boolean notPaid) {

        //check if list is not null
        if (rawResults == null || rawResults.isEmpty()) return null;

        //creating basic predicate for filtering with (Object != null)
        Predicate<IRMember> nonNullPredicate = Objects::nonNull;

        //filtering all rawResultsByName for isFeePaid
        if (paid && !notPaid) {

            //creating predicate for members who paid their Fee
            Predicate<IRMember> paidPredicate = nonNullPredicate.and(
                member -> member.getIsFeePaid().equals(true)
            );

            //filter for members who paid their Fee
            return rawResults.stream().filter(paidPredicate).collect(Collectors.toList());

        } else if (notPaid && !paid) {

            //creating predicate for members who didn't pay their Fee
            Predicate<IRMember> notPaidPredicate = nonNullPredicate.and(
                member -> member.getIsFeePaid().equals(false)
            );

            //filter for members who didn't pay their Fee
            return rawResults.stream().filter(notPaidPredicate).collect(Collectors.toList());

        }
        return rawResults;
    }
}
