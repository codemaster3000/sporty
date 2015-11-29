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

    static <T extends IMember> AccessPolicy<T> and(AccessPolicy<T> proxy, AccessPolicy<T> proxy2) {
        Objects.requireNonNull(proxy);
        Objects.requireNonNull(proxy2);

        return (T t) -> proxy.isFollowedBy(t) && proxy2.isFollowedBy(t);
    }

    @SafeVarargs
    static <T extends IMember> AccessPolicy<T> and(AccessPolicy<T>... proxies) {
        return (T t) -> {
            for (AccessPolicy<T> proxy : proxies) {
                Objects.requireNonNull(proxy);

                if (!proxy.isFollowedBy(t)) return false;
            }
            return true;
        };
    }

    static <T extends IMember> AccessPolicy<T> or(AccessPolicy<T> proxy, AccessPolicy<T> proxy2) {
        Objects.requireNonNull(proxy);
        Objects.requireNonNull(proxy2);

        return (T t) -> proxy.isFollowedBy(t) || proxy2.isFollowedBy(t);
    }

    @SafeVarargs
    static <T extends IMember> AccessPolicy<T> or(AccessPolicy<T>... proxies) {
        return (T t) -> {
            for (AccessPolicy<T> proxy : proxies) {
                Objects.requireNonNull(proxy);

                if (proxy.isFollowedBy(t)) return true;
            }
            return false;
        };
    }
}
