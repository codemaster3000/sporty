package at.sporty.team1.rmi.enums;

public enum UserRole {
    GUEST(0, "guest"),
    MEMBER(1, "member"),
    TRAINER(2, "trainer"),
    MANAGER(3, "manager"),
    DEPARTMENT_HEAD(4, "departmentHead"),
    ADMIN(5, "admin");

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
        if (requiredRoleLevel != null) {
            return getRoleLevel() >= requiredRoleLevel.getRoleLevel();
        }
        return false;
    }

    public String getTextForm() {
        return _textForm;
    }
}
