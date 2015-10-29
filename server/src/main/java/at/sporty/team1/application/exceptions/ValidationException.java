package at.sporty.team1.application.exceptions;

/**
 * Created by f00 on 29.10.15.
 */
public class ValidationException extends Exception {
    private String reason;

    public ValidationException() {

        super();
    }

    /**
     *********************************************************************
     * Getter for property 'reason'.
     *
     * @return Value for property 'reason'.
     *********************************************************************/
    public String getReason() {
        return reason;
    }

    /**
     *********************************************************************
     * Setter for property 'reason'.
     *
     * @param reason Value to set for property 'reason'.
     *********************************************************************/
    public void setReason(String reason) {
        this.reason = reason;
    }
}
