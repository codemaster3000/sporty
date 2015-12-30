package at.sporty.team1.shared.exceptions;

/**
 * Created by sereGkaluv on 25-Nov-15.
 */
public class NotAuthorisedException extends Exception {
    public NotAuthorisedException() {
        super();
    }

    public NotAuthorisedException(String message) {
        super(message);
    }
}
