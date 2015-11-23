package at.sporty.team1.misc.functional;

import org.hibernate.Criteria;

/**
 * Created by sereGkaluv on 23-Nov-15.
 */
public interface AliasContainer {
    Criteria applyAlias(Criteria criteria);
}
