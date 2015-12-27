package at.sporty.team1.communication.facades.ejb.adapters;

import at.sporty.team1.communication.facades.api.ITeamControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.ITeamControllerEJB;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

public class TeamControllerEJBAdapter implements ITeamControllerUniversal {

	private final ITeamControllerEJB _iTeamController;
	
	public TeamControllerEJBAdapter(ITeamControllerEJB iTeamControllerEJB) {
		_iTeamController = iTeamControllerEJB;
	}

	@Override
	public Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iTeamController.createOrSaveTeam(teamDTO, session);
	}

	@Override
	public List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iTeamController.searchTeamsByMember(memberId, session);
	}

	@Override
	public List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iTeamController.loadTeamMembers(teamId, session);
	}

	@Override
	public DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iTeamController.loadTeamDepartment(teamId, session);
	}
}
