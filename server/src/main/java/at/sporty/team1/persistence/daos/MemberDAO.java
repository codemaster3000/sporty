package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Member;

/**
 * Created by f00 on 27.10.15.
 */
public class MemberDAO extends HibernateGenericDao<Member> {
    public MemberDAO(Class<Member> domainClass) {
        super(domainClass);
    }
}
