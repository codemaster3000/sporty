package at.sporty.team1.application.controller.rmi.impl;

import at.sporty.team1.application.controller.real.api.IDepartmentController;
import at.sporty.team1.application.controller.real.impl.DepartmentController;
import at.sporty.team1.shared.api.rmi.IDepartmentControllerRMI;
import at.sporty.team1.shared.dtos.*;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.SecurityException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
public class DepartmentControllerRMIAdapter extends UnicastRemoteObject implements IDepartmentControllerRMI {
    private static final long serialVersionUID = 1L;
    private final IDepartmentController _controller;

    public DepartmentControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new DepartmentController();
    }

    @Override
    public List<DepartmentDTO> searchAllDepartments()
    throws RemoteException {
        return _controller.searchAllDepartments();
    }

    @Override
    public List<TeamDTO> loadDepartmentTeams(Integer departmentId)
    throws RemoteException, UnknownEntityException {
        return _controller.loadDepartmentTeams(departmentId);
    }

    @Override
    public MemberDTO loadDepartmentHead(Integer departmentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException {
        return _controller.loadDepartmentHead(departmentId, session);
    }
}
