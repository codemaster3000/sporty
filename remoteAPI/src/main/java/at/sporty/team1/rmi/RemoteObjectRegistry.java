package at.sporty.team1.rmi;

public enum RemoteObjectRegistry {
    MEMBER_CONTROLLER("MEMBER_CONTROLLER"),
    TEAM_CONTROLLER("TEAM_CONTROLLER"),
    DEPARTMENT_CONTROLLER("DEPARTMENT_CONTROLLER"),
    LOGIN_CONTROLLER("LOGIN_CONTROLLER");


    private final String _naming;

    RemoteObjectRegistry(String naming) {
        _naming = naming;
    }

    public String getNaming() {
        return _naming;
    }
}
