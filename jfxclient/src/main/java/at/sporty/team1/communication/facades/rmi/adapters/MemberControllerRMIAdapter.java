package at.sporty.team1.communication.facades.rmi.adapters;

import java.rmi.RemoteException;
import java.util.List;

import at.sporty.team1.communication.facades.api.IMemberControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.IMemberControllerRMI;
import at.sporty.team1.shared.dtos.DTOPair;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

public class MemberControllerRMIAdapter implements IMemberControllerUniversal {

	private final IMemberControllerRMI _iMemberControllerRMI;
	
	public MemberControllerRMIAdapter(IMemberControllerRMI iMemberControllerRMI) {
		_iMemberControllerRMI = iMemberControllerRMI;
	}
	
	@Override
	public MemberDTO findMemberById(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.findMemberById(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> searchAllMembers(Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.searchAllMembers(isFeePaid, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public Integer createOrSaveMember(MemberDTO memberDTO, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.createOrSaveMember(memberDTO, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> searchMembersByNameString(String searchString, Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.searchMembersByNameString(searchString, isFeePaid, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> searchMembersByCommonTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
			throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.searchMembersByCommonTeamName(teamName, isFeePaid, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> searchMembersByTournamentTeamName(String teamName, Boolean isFeePaid, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.searchMembersByTournamentTeamName(teamName, isFeePaid, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> searchMembersByDateOfBirth(String dateOfBirth, Boolean isFeePaid, SessionDTO session)
			throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.searchMembersByDateOfBirth(dateOfBirth, isFeePaid, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<DTOPair<DepartmentDTO, TeamDTO>> loadFetchedDepartmentTeamList(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.loadFetchedDepartmentTeamList(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<DepartmentDTO> loadMemberDepartments(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.loadMemberDepartments(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void assignMemberToDepartment(Integer memberId, Integer departmentId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iMemberControllerRMI.assignMemberToDepartment(memberId, departmentId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void removeMemberFromDepartment(Integer memberId, Integer departmentId, SessionDTO session)
			throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TeamDTO> loadMemberTeams(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iMemberControllerRMI.loadMemberTeams(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void assignMemberToTeam(Integer memberId, Integer teamId, SessionDTO session)
			throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iMemberControllerRMI.assignMemberToTeam(memberId, teamId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void removeMemberFromTeam(Integer memberId, Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iMemberControllerRMI.removeMemberFromTeam(memberId, teamId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void deleteMember(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iMemberControllerRMI.deleteMember(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}
}
