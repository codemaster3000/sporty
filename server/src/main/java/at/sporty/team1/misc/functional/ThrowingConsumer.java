package at.sporty.team1.misc.functional;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface ThrowingConsumer<T, U extends Throwable> {
    void accept(T t) throws U;
}
