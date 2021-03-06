package at.sporty.team1.application.controller.rmi;

import at.sporty.team1.application.controller.real.TeamController;
import at.sporty.team1.application.controller.real.api.ITeamController;
import at.sporty.team1.application.controller.util.RemoteObject;
import at.sporty.team1.shared.api.rmi.ITeamControllerRMI;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
@RemoteObject(name = "TEAM_CONTROLLER_RMI")
public class TeamControllerRMIAdapter extends UnicastRemoteObject implements ITeamControllerRMI {
    private static final long serialVersionUID = 1L;
    private final ITeamController _controller;

    public TeamControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new TeamController();
    }

    @Override
    public List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.searchTeamsByMember(memberId, session);
    }

    @Override
    public List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.loadTeamMembers(teamId, session);
    }

    @Override
    public DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {

        return _controller.loadTeamDepartment(teamId, session);
    }
}
