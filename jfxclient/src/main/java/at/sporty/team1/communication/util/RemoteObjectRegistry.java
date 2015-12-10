package at.sporty.team1.communication.util;

public enum RemoteObjectRegistry {
    MEMBER_CONTROLLER("MEMBER_CONTROLLER"),
    TEAM_CONTROLLER("TEAM_CONTROLLER"),
    DEPARTMENT_CONTROLLER("DEPARTMENT_CONTROLLER"),
    LOGIN_CONTROLLER("LOGIN_CONTROLLER"),
	TOURNAMENT_CONTROLLER("TOURNAMENT_CONTROLLER"),
    NOTIFICATION_CONTROLLER("NOTIFICATION_CONTROLLER");

    private static final String RMI_SUFFIX = "_RMI";
    private static final String EJB_SUFFIX = "_EJB";

    private final String _naming;

    RemoteObjectRegistry(String naming) {
        _naming = naming;
    }

    public String getNamingRMI() {
        return _naming + RMI_SUFFIX;
    }

    public String getNamingEJB() {
        return _naming + EJB_SUFFIX;
    }
}
