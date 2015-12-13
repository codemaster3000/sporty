package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.IDepartmentControllerUniversal;
import at.sporty.team1.communication.util.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.IDepartmentControllerRMI;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;

import java.rmi.RemoteException;
import java.util.List;

public class DepartmentControllerRMIAdapter implements IDepartmentControllerUniversal {

	private final IDepartmentControllerRMI _iDepartmentControllerRMI;
	
	public DepartmentControllerRMIAdapter(IDepartmentControllerRMI iDepartmentControllerRMI) {
		_iDepartmentControllerRMI = iDepartmentControllerRMI;
	}
	@Override
	public List<DepartmentDTO> searchAllDepartments() throws RemoteCommunicationException {
		try {
			return _iDepartmentControllerRMI.searchAllDepartments();
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<TeamDTO> loadDepartmentTeams(Integer departmentId)
			throws RemoteCommunicationException, UnknownEntityException {
		try {
			return _iDepartmentControllerRMI.loadDepartmentTeams(departmentId);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
			throws RemoteCommunicationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iDepartmentControllerRMI.loadDepartmentHead(departmentId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

}
