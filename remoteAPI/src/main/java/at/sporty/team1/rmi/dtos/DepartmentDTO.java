package at.sporty.team1.rmi.dtos;

import java.io.Serializable;
import java.util.List;

import at.sporty.team1.rmi.api.IDTO;

/**
 * Created by f00 on 03.11.15.
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
