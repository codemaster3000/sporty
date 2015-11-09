package at.sporty.team1.domain.readonly;

import at.sporty.team1.domain.Member;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IRDepartment {
    Integer getDepartmentId();

    String getSport();
    
    Member getDepartmentHead();
}
