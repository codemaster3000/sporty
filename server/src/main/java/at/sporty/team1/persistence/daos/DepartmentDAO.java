package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.persistence.api.IDepartmentDAO;
import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.TooManyResultsException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
public class DepartmentDAO extends HibernateGenericDAO<Department> implements IDepartmentDAO {

    private static final String PROP_DEPARTMENT_ID = "departmentId";
    private static final String PROP_SPORT = "sport";
    private static final String PROP_DEPARTMENT_HEAD = "departmentHead";
    private static final String PROP_DEPARTMENT_HEAD_ID = "departmentHeadId";
    private static final String PROP_MEMBER_ID = "memberId";
    private static final String PROP_TEAM_ID = "teamId";
    private static final String PROP_TOURNAMENT_ID = "tournamentId";

    /**
     * Creates a new department DAO.
     */
    public DepartmentDAO() {
        super(Department.class);
    }

    @Override
    public Department findBySport(String sport)
    throws PersistenceException {

        List<Department> dept = findByCriteria(Restrictions.like(PROP_SPORT, sport, MatchMode.ANYWHERE));
        if (dept != null && dept.size() == 1) {
            return dept.get(0);
        }
        throw new PersistenceException("Unexpected results amount (size > 1), while searching department by sport.");
    }

    @Override
    public Boolean isDepartmentHead(Member member, Integer departmentId)
    throws PersistenceException {
        try {

            Department dept = findSingleResultByCriteria(Restrictions.and(
                Restrictions.eq(PROP_DEPARTMENT_ID, departmentId)),
                Restrictions.eq(PROP_DEPARTMENT_HEAD, member)
            );

            return dept != null;

        } catch (TooManyResultsException e) {
            return false;
        }
    }

    @Override
    public Boolean isDepartmentHeadOfMember(Member departmentHead, Integer memberId)
    throws PersistenceException {

        if (departmentHead == null || memberId == null) return false;

        String rawQuery =
            "SELECT department.* " +
            "FROM memberDepartments " +
            "INNER JOIN department " +
            "WHERE " +
            "department.departmentId = memberDepartments.departmentId " +
            "AND " +
            "department.headId = :" + PROP_DEPARTMENT_HEAD_ID + " " +
            "AND " +
            "memberDepartments.memberId = :" + PROP_MEMBER_ID;

        List<Department> departments = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_DEPARTMENT_HEAD_ID, departmentHead.getMemberId()),
            new PropertyPair<>(PROP_MEMBER_ID, memberId)
        );

        return departments != null && !departments.isEmpty();
    }

    @Override
    public Boolean isDepartmentHeadOfTeam(Member departmentHead, Integer teamId)
    throws PersistenceException {

        if (departmentHead == null || teamId == null) return false;

        String rawQuery =
            "SELECT department.* " +
            "FROM department " +
            "INNER JOIN team " +
            "WHERE " +
            "department.departmentId = team.departmentId " +
            "AND " +
            "department.headId = :" + PROP_DEPARTMENT_HEAD_ID + " " +
            "AND " +
            "team.teamId = :" + PROP_TEAM_ID;

        List<Department> departments = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_DEPARTMENT_HEAD_ID, departmentHead.getMemberId()),
            new PropertyPair<>(PROP_TEAM_ID, teamId)
        );

        return departments != null && !departments.isEmpty();
    }

    @Override
    public Boolean isDepartmentHeadOfTournament(Member departmentHead, Integer tournamentId)
    throws PersistenceException {

        if (departmentHead == null || tournamentId == null) return false;

        String rawQuery =
            "SELECT department.* " +
            "FROM tournament " +
            "INNER JOIN department " +
            "WHERE " +
            "department.departmentId = tournament.departmentId " +
            "AND " +
            "tournament.tournamentId = :" + PROP_TOURNAMENT_ID + " " +
            "AND " +
            "department.headId = :" + PROP_DEPARTMENT_HEAD_ID;

        List<Department> departments = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_DEPARTMENT_HEAD_ID, departmentHead.getMemberId()),
            new PropertyPair<>(PROP_TOURNAMENT_ID, tournamentId)
        );

        return departments != null && !departments.isEmpty();
    }
}
