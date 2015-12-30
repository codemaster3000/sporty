package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.IDTO;

/**
 * This IDTO Object implements Object Builder pattern.
 */
public class DepartmentDTO implements IDTO {
    private static final long serialVersionUID = 1L;

    private Integer _departmentId;
    private String _sport;

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
}
