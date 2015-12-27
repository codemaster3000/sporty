package at.sporty.team1.shared.api.entity;

/**
 * Created by sereGkaluv on 27-Dec-15.
 */
public interface IBiThrowingFunction<T, R, U extends Throwable, V extends Throwable> {
    R apply(T t) throws U, V;
}