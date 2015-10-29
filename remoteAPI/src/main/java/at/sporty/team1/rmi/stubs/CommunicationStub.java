package at.sporty.team1.rmi.stubs;

public enum CommunicationStub {
    MEMBER_CONTROLLER("MEMBER_CONTROLLER");


    private final String _naming;

    CommunicationStub(String naming) {
        _naming = naming;
    }

    public String getNaming() {
        return _naming;
    }
}
