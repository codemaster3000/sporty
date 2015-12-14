package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.TeamController;
import at.sporty.team1.application.controller.real.api.ITeamController;
import at.sporty.team1.shared.api.ejb.ITeamControllerEJB;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by f00 on 13.12.15.
 */
@Stateless(name ="TEAM_CONTROLLER_EJB")
public class TeamControllerEJBAdapter implements ITeamControllerEJB {
    private static final long serialVersionUID = 1L;
    private transient final ITeamController _controller;

    public TeamControllerEJBAdapter() {
        _controller = new TeamController();
    }

    @Override
    public Integer createOrSaveTeam(TeamDTO teamDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

        return _controller.createOrSaveTeam(teamDTO,session);
    }

    @Override
    public List<TeamDTO> searchTeamsByMember(Integer memberId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        return _controller.searchTeamsByMember(memberId,session);
    }

    @Override
    public List<MemberDTO> loadTeamMembers(Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        return _controller.loadTeamMembers(teamId,session);
    }

    @Override
    public DepartmentDTO loadTeamDepartment(Integer teamId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        return _controller.loadTeamDepartment(teamId,session);
    }
}
