package at.sporty.team1.rmi;

public enum RemoteObject {
    MEMBER_CONTROLLER("MEMBER_CONTROLLER"),
    TEAM_CONTROLLER("TEAM_CONTROLLER");


    private final String _naming;

    RemoteObject(String naming) {
        _naming = naming;
    }

    public String getNaming() {
        return _naming;
    }
}
