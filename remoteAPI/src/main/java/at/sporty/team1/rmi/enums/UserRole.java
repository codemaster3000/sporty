package at.sporty.team1.rmi.enums;

public enum UserRole {
    GUEST(0, "guest"),
    MEMBER(1, "member"),
    TRAINER(2, "trainer"),
    DEPARTMENT_HEAD(3, "departmentHead"),
    ADMIN(4, "admin");

    private final int _roleLevel;
    private final String _textForm;

    UserRole(int roleLevel, String textFrom) {
        _roleLevel = roleLevel;
        _textForm = textFrom;
    }

    public int getRoleLevel() {
        return _roleLevel;
    }

    public boolean isInBound(UserRole requiredRoleLevel) {
        return requiredRoleLevel != null && getRoleLevel() >= requiredRoleLevel.getRoleLevel();
    }

    public String getTextForm() {
        return _textForm;
    }
}
