package at.sporty.team1.rmi.enums;

public enum UserRole {
    UNSUCCESSFUL_LOGIN(0),
    MEMBER(1),
    TRAINER(2),
    MANAGER(3),
    DEPARTMENT_HEAD(4),
    ADMIN(5);

    private final int _roleLevel;

    UserRole(int roleLevel) {
        _roleLevel = roleLevel;
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
}
