package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.ITeamControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.ITeamControllerRMI;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

public class TeamControllerRMIAdapter implements ITeamControllerUniversal {

	private final ITeamControllerRMI _iTeamControllerRMI;
	
	public TeamControllerRMIAdapter(ITeamControllerRMI iTeamControllerRMI) {
		_iTeamControllerRMI = iTeamControllerRMI;
	}

	@Override
	public List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iTeamControllerRMI.searchTeamsByMember(memberId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iTeamControllerRMI.loadTeamMembers(teamId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iTeamControllerRMI.loadTeamDepartment(teamId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

}
