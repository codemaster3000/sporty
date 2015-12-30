package at.sporty.team1.shared.api;

/**
 * Created by sereGkaluv on 27-Dec-15.
 */
public interface IThrowingFunction<T, R, U extends Throwable> {
    R apply(T t) throws U;
}

