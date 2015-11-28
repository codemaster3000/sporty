package at.sporty.team1.application.auth;

import at.sporty.team1.domain.interfaces.IMember;

import java.util.function.Function;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public interface AccessPolicy extends Function<IMember, Boolean> {

    static <T extends IMember> AccessPolicy simplePolicy(Function<T, Boolean> proxy) {
        return (AccessPolicy) proxy;
    }

    static <T extends IMember> boolean hasMatched(T t, AccessPolicy... policies) {
        for (AccessPolicy policy : policies) {
            //if policy is not fulfilled return negative result
            if(!policy.apply(t)) return false;
        }
        return true;
    }
}
