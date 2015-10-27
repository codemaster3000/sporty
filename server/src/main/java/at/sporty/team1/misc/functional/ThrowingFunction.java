package at.sporty.team1.misc.functional;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface ThrowingFunction<T, R, U extends Throwable> {
    R apply(T t) throws U;
}
