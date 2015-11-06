package at.sporty.team1.application.controller;


import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.misc.DataType;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.MemberDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
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
                     MAPPER.map(memberDTO, Member.class)
	             );
	
	             LOGGER.info("Member \"{} {}\" was successfully saved.", memberDTO.getFirstName(), memberDTO.getLastName());
	
	         } catch (PersistenceException e) {
	             LOGGER.error("Error occurs while communicating with DB.", e);
	         }
        	
     
        } else {
            // There has been bad input, throw the Exception
            LOGGER.error("Wrong input saving Member: {}", inputSanitizer.getLastFailedValidation());

            ValidationException validationException = new ValidationException();
            validationException.setReason(inputSanitizer.getLastFailedValidation());
            
            throw validationException;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByNameString(String searchString)
    throws RemoteException {
        try {
            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByNameString(searchString);

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
