package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.DepartmentController;
import at.sporty.team1.shared.api.ejb.IDepartmentControllerEJB;
import at.sporty.team1.shared.api.real.IDepartmentController;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
@Stateless(name = "DEPARTMENT_CONTROLLER_EJB")
public class DepartmentControllerEJBAdapter implements IDepartmentControllerEJB {
    private static final long serialVersionUID = 1L;
    private final IDepartmentController _controller;

    public DepartmentControllerEJBAdapter() {
        super();

        _controller = new DepartmentController();
    }

    @Override
    public List<DepartmentDTO> searchAllDepartments() {

        return _controller.searchAllDepartments();
    }

    @Override
    public List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws UnknownEntityException {

        return _controller.loadDepartmentTeams(departmentId);
    }

    @Override
    public MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
    throws UnknownEntityException, NotAuthorisedException {

        return _controller.loadDepartmentHead(departmentId, session);
    }
}
