package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.io.Serializable;

/**
 *
 */
public class DepartmentDTO implements Serializable, IDTO{
	
    private Integer _departmentId;
    private String _sport;
    private Integer _headId;

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
    
    public Integer getDepartmentHeadId() {
        return _headId;
    }

    public DepartmentDTO setDepartmentHeadId(Integer headId) {
        _headId = headId;
        return this;
    }
	
}
