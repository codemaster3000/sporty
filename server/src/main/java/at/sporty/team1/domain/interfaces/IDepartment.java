package at.sporty.team1.domain.interfaces;

import at.sporty.team1.domain.readonly.IRDepartment;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IDepartment extends IRDepartment {
    void setDepartmentId(int departmentId);

    void setSport(String sport);
    
    void setDepartmentHeadId(int headId);
}
