package at.sporty.team1.application.auth;

import at.sporty.team1.domain.interfaces.IMember;

import java.util.Objects;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public interface AccessPolicy<T extends IMember> {

    Boolean isFollowedBy(T member);

    static <T extends IMember> AccessPolicy<T> simplePolicy(AccessPolicy<T> proxy) {
        return proxy;
    }

    static <T extends IMember> AccessPolicy<T> complexAndPolicy(AccessPolicy<T> proxy, AccessPolicy<T> proxy2) {
        Objects.requireNonNull(proxy);
        Objects.requireNonNull(proxy2);

        return (T t) -> proxy.isFollowedBy(t) && proxy2.isFollowedBy(t);
    }

    static <T extends IMember> AccessPolicy<T> complexOrPolicy(AccessPolicy<T> proxy, AccessPolicy<T> proxy2) {
        Objects.requireNonNull(proxy);
        Objects.requireNonNull(proxy2);

        return (T t) -> proxy.isFollowedBy(t) || proxy2.isFollowedBy(t);
    }
}
