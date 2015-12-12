package at.sporty.team1.application.controller.rmi;

import at.sporty.team1.application.controller.util.RemoteObject;
import at.sporty.team1.application.controller.real.api.IMemberController;
import at.sporty.team1.application.controller.real.MemberController;
import at.sporty.team1.shared.api.rmi.IMemberControllerRMI;
import at.sporty.team1.shared.dtos.*;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
@RemoteObject(name = "MEMBER_CONTROLLER_RMI")
public class MemberControllerRMIAdapter extends UnicastRemoteObject implements IMemberControllerRMI {
    private static final long serialVersionUID = 1L;
    private final IMemberController _controller;

    public MemberControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new MemberController();
    }

    @Override
    public MemberDTO findMemberById(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        return _controller.findMemberById(memberId, session);
    }

    @Override
    public List<MemberDTO> searchAllMembers(Boolean isFeePaid, SessionDTO session)
    throws RemoteException, NotAuthorisedException {

        return _controller.searchAllMembers(isFeePaid, session);
    }

    @Override
    public Integer createOrSaveMember(MemberDTO memberDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.createOrSaveMember(memberDTO, session);
    }

    @Override
    public List<MemberDTO> searchMembersByNameString(String searchString, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.searchMembersByNameString(searchString, isFeePaid, session);
    }

    @Override
    public List<MemberDTO> searchMembersByCommonTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.searchMembersByCommonTeamName(teamName, isFeePaid, session);
    }

    @Override
    public List<MemberDTO> searchMembersByTournamentTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.searchMembersByTournamentTeamName(teamName, isFeePaid, session);
    }

    @Override
    public List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, Boolean isFeePaid, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.searchMembersByDateOfBirth(dateOfBirth, isFeePaid, session);
    }

    @Override
    public List<DTOPair<DepartmentDTO, TeamDTO>> loadFetchedDepartmentTeamList(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.loadFetchedDepartmentTeamList(memberId, session);
    }

    @Override
    public List<DepartmentDTO> loadMemberDepartments(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.loadMemberDepartments(memberId, session);
    }

    @Override
    public void assignMemberToDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        _controller.assignMemberToDepartment(memberId, departmentId, session);
    }

    @Override
    public void removeMemberFromDepartment(Integer memberId, Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        _controller.removeMemberFromDepartment(memberId, departmentId, session);
    }

    @Override
    public List<TeamDTO> loadMemberTeams(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.loadMemberTeams(memberId, session);
    }

    @Override
    public void assignMemberToTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        _controller.assignMemberToTeam(memberId, teamId, session);
    }

    @Override
    public void removeMemberFromTeam(Integer memberId, Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        _controller.removeMemberFromTeam(memberId, teamId, session);
    }

    @Override
    public void deleteMember(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        _controller.deleteMember(memberId, session);
    }
}
