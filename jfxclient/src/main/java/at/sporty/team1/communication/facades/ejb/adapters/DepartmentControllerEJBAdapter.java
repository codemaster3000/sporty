package at.sporty.team1.communication.facades.ejb.adapters;

import java.util.List;

import at.sporty.team1.communication.facades.api.IDepartmentControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.IDepartmentControllerEJB;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;

public class DepartmentControllerEJBAdapter implements IDepartmentControllerUniversal {

	private final IDepartmentControllerEJB _iDepartmentController;
	
	public DepartmentControllerEJBAdapter(IDepartmentControllerEJB iDepartmentControllerEJB) {
		_iDepartmentController = iDepartmentControllerEJB;
	}

	@Override
	public List<DepartmentDTO> searchAllDepartments()
	throws RemoteCommunicationException {
		return _iDepartmentController.searchAllDepartments();
	}

	@Override
	public List<TeamDTO> loadDepartmentTeams(Integer departmentId)
	throws RemoteCommunicationException, UnknownEntityException {
		return _iDepartmentController.loadDepartmentTeams(departmentId);
	}

	@Override
	public MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
	throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		return _iDepartmentController.loadDepartmentHead(departmentId, session);
	}

}
