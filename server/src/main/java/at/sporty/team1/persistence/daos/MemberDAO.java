package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Member;
import at.sporty.team1.persistence.api.IMemberDAO;

/**
 * Created by f00 on 27.10.15.
 */
public class MemberDAO extends HibernateGenericDAO<Member> implements IMemberDAO {
    public MemberDAO() {
        super(Member.class);
    }

    //IF NO SPECIFIC FUNCTIONALITY IS REQUIRED --> USE GENERIC DAO INSTEAD.
}
