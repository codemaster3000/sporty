package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.persistence.api.ITeamDAO;
import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.TooManyResultsException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Represents a DAO for Team
 */
public class TeamDAO extends HibernateGenericDAO<Team> implements ITeamDAO {

    private static final String PROP_TEAM_ID = "teamId";
    private static final String PROP_TEAM_NAME = "teamName";
    private static final String PROP_MEMBER_ID = "memberId";
    private static final String PROP_DEPARTMENT = "department";
    private static final String PROP_TRAINER_ID = "trainerId";
    private static final String PROP_TRAINER = "trainer";


    /**
     * Creates a new team DAO.
     */
    public TeamDAO() {
        super(Team.class);
    }

    @Override
    public List<Team> findTeamsByName(String teamName)
    throws PersistenceException {
        return findByCriteria(Restrictions.like(PROP_TEAM_NAME, teamName, MatchMode.ANYWHERE));
    }

    @Override
    public List<Team> findTeamsByDepartment(Department department)
    throws PersistenceException {
        if (department == null) return null;
        return findByCriteria(Restrictions.eq(PROP_DEPARTMENT, department));
    }

    @Override
    public List<Team> findTeamsByMemberId(Integer memberId)
    throws PersistenceException {

        if (memberId == null) return null;

        String rawQuery =
            "SELECT team.* " +
            "FROM teamMembers " +
            "INNER JOIN team " +
            "WHERE " +
            "team.teamId = teamMembers.teamId " +
            "AND " +
            "teamMembers.memberId = :" + PROP_MEMBER_ID;

        return findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_MEMBER_ID, memberId)
        );
    }

    @Override
    public Boolean isTrainerOfMember(Member trainer, Integer memberId)
    throws PersistenceException {

        if (trainer == null || memberId == null) return false;

        String rawQuery =
            "SELECT team.* " +
            "FROM teamMembers " +
            "INNER JOIN team " +
            "WHERE " +
            "team.teamId = teamMembers.teamId " +
            "AND " +
            "team.trainerId = :" + PROP_TRAINER_ID + " " +
            "AND " +
            "teamMembers.memberId = :" + PROP_MEMBER_ID;

        List<Team> teams = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_TRAINER_ID, trainer.getMemberId()),
            new PropertyPair<>(PROP_MEMBER_ID, memberId)
        );

        return teams != null && !teams.isEmpty();
    }

    @Override
    public Boolean isTrainerOfTeam(Member trainer, Integer teamId)
    throws PersistenceException {
        try {

            Team team = findSingleResultByCriteria(Restrictions.and(
                Restrictions.eq(PROP_TEAM_ID, teamId),
                Restrictions.eq(PROP_TRAINER, trainer)
            ));

            return team != null;

        } catch (TooManyResultsException e) {
            return false;
        }
    }

    @Override
    public Boolean isMemberOfTeam(Member member, Integer teamId)
    throws PersistenceException {

        if (member == null || teamId == null) return false;

        String rawQuery =
            "SELECT team.* " +
            "FROM teamMembers " +
            "INNER JOIN team " +
            "WHERE " +
            "teamMembers.teamId = team.teamId " +
            "AND " +
            "team.teamId = :" + PROP_TEAM_ID + " " +
            "AND " +
            "teamMembers.memberId = :" + PROP_MEMBER_ID;

        List<Team> teams = findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_TEAM_ID, teamId),
            new PropertyPair<>(PROP_MEMBER_ID, member.getMemberId())
        );

        return teams != null && !teams.isEmpty();
    }
}
