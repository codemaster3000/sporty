package at.sporty.team1.application.controller;


import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.interfaces.IDepartment;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.domain.readonly.IRMember;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.dtos.*;
import at.sporty.team1.rmi.enums.UserRole;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
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
    public MemberDTO findMemberById(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (memberId == null) throw new UnknownEntityException(IMember.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            //converting member to member DTO
            return MAPPER.map(member, MemberDTO.class);

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for Member #{}.", memberId, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchAllMembers(Boolean isFeePaid, SessionDTO session)
    throws RemoteException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findAll();

            //filtering results for fee
            rawResults = filterWithFee(rawResults,isFeePaid);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"all Members\".", e);
            return null;
        }
    }

    @Override
    public Integer createOrSaveMember(MemberDTO memberDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        //1 STEP
        if (memberDTO == null) throw new NotAuthorisedException();

        //2 STEP
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),

                AccessPolicy.and(
                    BasicAccessPolicies.isInPermissionBound(UserRole.DEPARTMENT_HEAD),

                    AccessPolicy.or(
                        //create new member (new members doesn't have id))
                        AccessPolicy.simplePolicy(user -> memberDTO.getMemberId() == null),
                        BasicAccessPolicies.isDepartmentHeadOfMember(memberDTO.getMemberId())
                    )
                )
            )
        )) throw new NotAuthorisedException();

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

            Member member = MAPPER.map(memberDTO, Member.class);

            /* pulling a MemberDAO and save the Member */
            PersistenceFacade.getNewMemberDAO().saveOrUpdate(member);

            LOGGER.info("Member \"{} {}\" was successfully saved.", memberDTO.getFirstName(), memberDTO.getLastName());
            return member.getMemberId();
        } catch (PersistenceException e) {
            LOGGER.error("Error occurred while communicating with DB.", e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByNameString(String searchString, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(searchString, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }
        
        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByNameString(searchString);

            //filtering results for fee
            rawResults = filterWithFee(rawResults,isFeePaid);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"{}\".", searchString, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByCommonTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByCommonTeamName(teamName);

            //filtering results for fee
            rawResults = filterWithFee(rawResults,isFeePaid);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"{}\".", teamName, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByTournamentTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.NAME)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByTournamentTeamName(teamName);

            //filtering results for fee
            rawResults = filterWithFee(rawResults,isFeePaid);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"{}\".", teamName, e);
            return null;
        }
    }

    @Override
    public List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.MEMBER)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(dateOfBirth, DataType.SQL_DATE)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends IMember> rawResults = PersistenceFacade.getNewMemberDAO().findByDateOfBirth(dateOfBirth);

            //filtering results for fee
            rawResults = filterWithFee(rawResults,isFeePaid);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to MemberDTO
            return rawResults.stream()
                    .map(member -> MAPPER.map(member, MemberDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for \"{}\".", dateOfBirth, e);
            return null;
        }
    }

    @Override
    public List<DTOPair<DepartmentDTO, TeamDTO>> loadFetchedDepartmentTeamList(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isSelf(memberId),
                BasicAccessPolicies.isTrainerOfMember(memberId),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            //Getting all departments for this entity
            PersistenceFacade.forceLoadLazyProperty(member, Member::getDepartments);
            Set<? extends IDepartment> rawResultsDepartments = new HashSet<>(member.getDepartments());

            //Getting all teams for this entity
            PersistenceFacade.forceLoadLazyProperty(member, Member::getTeams);
            List<? extends ITeam> rawResultsTeam = member.getTeams();

            List<DTOPair<DepartmentDTO, TeamDTO>> precookedResults = new LinkedList<>();

            //Fetching and converting the results
            for (ITeam team : rawResultsTeam) {
                IDepartment department = team.getDepartment();

                //Getting rid of duplicates
                rawResultsDepartments.remove(department);

                //Creating fetched pair
                DTOPair<DepartmentDTO, TeamDTO> dtoPair = new DTOPair<>();
                dtoPair.setDTO2(
                    MAPPER.map(team, TeamDTO.class)
                );

                if (department != null) {
                    dtoPair.setDTO1(
                        MAPPER.map(department, DepartmentDTO.class)
                    );
                }
                precookedResults.add(dtoPair);
            }

            //Check for departments assigned to member without a team
            if (!rawResultsDepartments.isEmpty()) {
                precookedResults.addAll(
                    rawResultsDepartments.stream().map(department -> {

                        //Creating fetched pair (team is null)
                        DTOPair<DepartmentDTO, TeamDTO> dtoPair = new DTOPair<>();
                        dtoPair.setDTO1(
                            MAPPER.map(department, DepartmentDTO.class)
                        );

                        return dtoPair;

                    }).collect(Collectors.toList())
                );
            }

            //checking if there are any results
            if (precookedResults.isEmpty()) return null;

            //Converting results to TeamDTO
            return precookedResults;

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while preparing departments and teams fetch list for Member #{}.",
                memberId,
                e
            );
            return null;
        }
    }

    @Override
    public List<DepartmentDTO> loadMemberDepartments(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isSelf(memberId),
                BasicAccessPolicies.isTrainerOfMember(memberId),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            //getting all departments for this entity
            PersistenceFacade.forceLoadLazyProperty(member, Member::getDepartments);
            List<? extends IDepartment> rawResults = member.getDepartments();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to DepartmentDTO
            return rawResults.stream()
                    .map(department -> MAPPER.map(department, DepartmentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while searching for Member #{} departments.",
                memberId,
                e
            );
            return null;
        }
    }

    @Override
    public void assignMemberToDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            Department department = PersistenceFacade.getNewDepartmentDAO().findById(departmentId);
            if (department == null) throw new UnknownEntityException(IDepartment.class);

            //loading all departments for the given member
            PersistenceFacade.forceLoadLazyProperty(member, Member::getDepartments);
            member.addDepartment(department);

            PersistenceFacade.getNewMemberDAO().saveOrUpdate(member);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while assigning \"Member #{} to Department #{}\".",
                memberId,
                departmentId,
                e
            );
        }
    }

    @Override
    public void removeMemberFromDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (departmentId == null) throw new UnknownEntityException(IDepartment.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            Department department = PersistenceFacade.getNewDepartmentDAO().findById(departmentId);
            if (department == null) throw new UnknownEntityException(IDepartment.class);

            //loading all departments for the given member
            PersistenceFacade.forceLoadLazyProperty(member, Member::getDepartments);
            member.removeDepartment(department);

            PersistenceFacade.getNewMemberDAO().saveOrUpdate(member);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while removing \"Member #{} from Department #{}\".",
                memberId,
                departmentId,
                e
            );
        }
    }

    @Override
    public List<TeamDTO> loadMemberTeams(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isSelf(memberId),
                BasicAccessPolicies.isTrainerOfMember(memberId),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            //getting all teams for this entity
            PersistenceFacade.forceLoadLazyProperty(member, Member::getTeams);
            List<? extends ITeam> rawResults = member.getTeams();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TeamDTO
            return rawResults.stream()
                    .map(team -> MAPPER.map(team, TeamDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while searching for Member #{} teams.",
                memberId,
                e
            );
            return null;
        }
    }

    @Override
    public void assignMemberToTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isTrainerOfMember(memberId),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            PersistenceFacade.forceLoadLazyProperty(member, Member::getTeams);
            member.addTeam(team);

            PersistenceFacade.getNewMemberDAO().saveOrUpdate(member);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while assigning \"Member #{} to Team #{}\".",
                memberId,
                teamId,
                e
            );
        }
    }

    @Override
    public void removeMemberFromTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isTrainerOfMember(memberId),
                BasicAccessPolicies.isDepartmentHeadOfMember(memberId)
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            PersistenceFacade.forceLoadLazyProperty(member, Member::getTeams);
            member.removeTeam(team);

            PersistenceFacade.getNewMemberDAO().saveOrUpdate(member);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while removing \"Member #{} from Team #{}\".",
                memberId,
                teamId,
                e
            );
        }
    }

    @Override
    public void deleteMember(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN)
        )) throw new NotAuthorisedException();

        /* Validating Input */
        if (memberId == null) throw new UnknownEntityException(IMember.class);

        /* Is valid, moving forward */
        try {

            Member member = PersistenceFacade.getNewMemberDAO().findById(memberId);
            if (member == null) throw new UnknownEntityException(IMember.class);

            PersistenceFacade.getNewMemberDAO().delete(member);

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while deleting Member #{}.",
                memberId,
                e
            );
        }
    }

    /**
     * Helping method which filters rawResults dependent on FeePaid state.
     *
     * @param rawResults List to be filtered.
     * @param isFeePaid value of the feePaid true = paid, false = not paid
     * @return filtered rawResults list or null if rawResults were null or empty.
     */
    private List<? extends IMember> filterWithFee(List<? extends IMember> rawResults, Boolean isFeePaid) {

        //check if list is not null
        if (rawResults == null || rawResults.isEmpty()) return null;

        //if nothing specified return full list
        if (isFeePaid == null) return rawResults;

        //creating basic predicate for filtering with (Object != null)
        Predicate<IRMember> nonNullPredicate = Objects::nonNull;

        //filtering all rawResultsByName for isFeePaid
        if (isFeePaid) {

            //creating predicate for members who paid their Fee
            Predicate<IRMember> paidPredicate = nonNullPredicate.and(
                member -> member.getIsFeePaid().equals(true)
            );

            //filter for members who paid their Fee
            return rawResults.stream().filter(paidPredicate).collect(Collectors.toList());

        } else {

            //creating predicate for members who didn't pay their Fee
            Predicate<IRMember> notPaidPredicate = nonNullPredicate.and(
                member -> member.getIsFeePaid().equals(false)
            );

            //filter for members who didn't pay their Fee
            return rawResults.stream().filter(notPaidPredicate).collect(Collectors.toList());
        }
    }
}
