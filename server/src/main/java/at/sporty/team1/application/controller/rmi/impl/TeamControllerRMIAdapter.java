package at.sporty.team1.application.controller.rmi.impl;

import at.sporty.team1.application.controller.real.api.ITeamController;
import at.sporty.team1.application.controller.real.impl.TeamController;
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
public class TeamControllerRMIAdapter extends UnicastRemoteObject implements ITeamControllerRMI {
    private static final long serialVersionUID = 1L;
    private final ITeamController _controller;

    public TeamControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new TeamController();
    }

    @Override
    public Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.createOrSaveTeam(teamDTO, session);
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
