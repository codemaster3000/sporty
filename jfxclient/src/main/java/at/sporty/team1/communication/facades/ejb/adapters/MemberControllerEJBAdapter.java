package at.sporty.team1.communication.facades.ejb.adapters;

import at.sporty.team1.communication.facades.api.IMemberControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.IMemberControllerEJB;
import at.sporty.team1.shared.dtos.*;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

public class MemberControllerEJBAdapter implements IMemberControllerUniversal {

	private final IMemberControllerEJB _iMemberController;
	
	public MemberControllerEJBAdapter(IMemberControllerEJB iMemberControllerEJB) {
		_iMemberController = iMemberControllerEJB;
	}
	
	@Override
	public MemberDTO findMemberById(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iMemberController.findMemberById(memberId, session);
	}

	@Override
	public List<MemberDTO> searchAllMembers(Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, NotAuthorisedException {
		return _iMemberController.searchAllMembers(isFeePaid, session);
	}

	@Override
	public Integer createOrSaveMember(MemberDTO memberDTO, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iMemberController.createOrSaveMember(memberDTO, session);
	}

	@Override
	public List<MemberDTO> searchMembersByNameString(String searchString, Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iMemberController.searchMembersByNameString(searchString, isFeePaid, session);
	}

	@Override
	public List<MemberDTO> searchMembersByCommonTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iMemberController.searchMembersByCommonTeamName(teamName, isFeePaid, session);
	}

	@Override
	public List<MemberDTO> searchMembersByTournamentTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
			throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iMemberController.searchMembersByTournamentTeamName(teamName, isFeePaid, session);
	}

	@Override
	public List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iMemberController.searchMembersByDateOfBirth(dateOfBirth, isFeePaid, session);
	}

	@Override
	public List<DTOPair<DepartmentDTO, TeamDTO>> loadFetchedDepartmentTeamList(Integer memberId, SessionDTO session)
			throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iMemberController.loadFetchedDepartmentTeamList(memberId, session);
	}

	@Override
	public List<DepartmentDTO> loadMemberDepartments(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iMemberController.loadMemberDepartments(memberId, session);
	}

	@Override
	public void assignMemberToDepartment(Integer memberId, Integer departmentId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		_iMemberController.assignMemberToDepartment(memberId, departmentId, session);
	}

	@Override
	public void removeMemberFromDepartment(Integer memberId, Integer departmentId, SessionDTO session)
			throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		_iMemberController.removeMemberFromDepartment(memberId, departmentId, session);
	}

	@Override
	public List<TeamDTO> loadMemberTeams(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iMemberController.loadMemberTeams(memberId, session);
	}

	@Override
	public void assignMemberToTeam(Integer memberId, Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		_iMemberController.assignMemberToTeam(memberId, teamId, session);
	}

	@Override
	public void removeMemberFromTeam(Integer memberId, Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		_iMemberController.removeMemberFromTeam(memberId, teamId, session);
	}

	@Override
	public void deleteMember(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		_iMemberController.deleteMember(memberId, session);
	}
}
