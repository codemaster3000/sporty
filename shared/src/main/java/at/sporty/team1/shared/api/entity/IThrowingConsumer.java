package at.sporty.team1.shared.api.entity;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public interface IThrowingConsumer<T, U extends Throwable> {
    void accept(T t) throws U;
}
