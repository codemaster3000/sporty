package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class DepartmentDTO implements IDTO {
    private Integer _departmentId;
    private String _sport;
    private MemberDTO _head;

    public DepartmentDTO() {
    }

    public Integer getDepartmentId() {
        return _departmentId;
    }

    public DepartmentDTO setDepartmentId(Integer departmentId) {
        _departmentId = departmentId;
        return this;
    }

    public String getSport() {
        return _sport;
    }

    public DepartmentDTO setSport(String sport) {
        _sport = sport;
        return this;
    }
    
    public MemberDTO getDepartmentHead() {
        return _head;
    }

    public DepartmentDTO setDepartmentHead(MemberDTO head) {
        _head = head;
        return this;
    }
}
