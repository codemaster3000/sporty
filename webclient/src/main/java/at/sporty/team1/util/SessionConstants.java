package at.sporty.team1.util;

/**
 * Created by sereGkaluv on 27-Dec-15.
 */
public enum SessionConstants {
    CLIENT_KEY_PAIR("ACTIVE_KEY_PAIR"),
    ACTIVE_SESSION("ACTIVE_SESSION"),
    ACTIVE_USER("ACTIVE_USER"),
    ACTIVE_TOURNAMENT("ACTIVE_TOURNAMENT"),
    ACTIVE_MATCH("ACTIVE_MATCH"),
    REQUESTED_EDIT("REQUESTED_EDIT");

    private final String _constant;

    SessionConstants(String constant) {
        _constant = constant;
    }

    public String getConstant() {
        return _constant;
    }
}
