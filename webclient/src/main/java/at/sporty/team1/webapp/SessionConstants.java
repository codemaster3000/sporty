package at.sporty.team1.webapp;

/**
 * Created by sereGkaluv on 27-Dec-15.
 */
public enum SessionConstants {
    CLIENT_KEY_PAIR("ACTIVE_SESSION"),
    ACTIVE_SESSION("ACTIVE_SESSION");

    private final String _constant;

    SessionConstants(String constant) {
        _constant = constant;
    }

    public String getConstant() {
        return _constant;
    }
}
