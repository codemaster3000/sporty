package at.sporty.team1.rmi.exceptions;

/**
 * Created by sereGkaluv on 12-Nov-15.
 */
public class UnknownEntityException extends Exception {
    private static final String MESSAGE =
        "Entity \"%s\" doesn't have id value. " +
        "Normally this happens while invoking DAO methods for a new DTO object" +
        "that was converted to Entity directly without saving to Data Storage.\n" +
        "Normal DTO lifecycle:\n" +
        "\tnew DTO() -> convert to entity -> save to Data Storage.\n" +
        "\tupdate entity object -> ready for operations.";

    public UnknownEntityException() {
        super();
    }

    public UnknownEntityException(Class entityClass) {
        super(String.format(MESSAGE, entityClass.getCanonicalName()));
    }
}
