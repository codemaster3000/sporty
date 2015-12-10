package at.sporty.team1.application.auth;

import at.sporty.team1.application.controller.real.LoginController;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.interfaces.IMember;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.enums.UserRole;

import java.util.function.BiFunction;

/**
 * Created by sereGkaluv on 29-Nov-15.
 */
public class BasicAccessPolicies {

    private BasicAccessPolicies() {
    }

    public static <T extends IMember> AccessPolicy<T> isInPermissionBound(UserRole userRole) {
        return AccessPolicy.simplePolicy(user -> LoginController.isInPermissionBound(user, userRole));
    }

    public static <T extends IMember> AccessPolicy<T> isNotEscalatedPermissionBound(MemberDTO memberDTO) {
        return AccessPolicy.simplePolicy(user -> LoginController.isNotEscalatedPermissionBound(
            memberDTO.getRole(),
            user.getRole()
        ));
    }

    public static <T extends IMember> AccessPolicy<T> isSelf(Integer memberId) {
        return AccessPolicy.and(
            isInPermissionBound(UserRole.MEMBER),
            AccessPolicy.simplePolicy(user -> user.getMemberId().equals(memberId))
        );
    }

    public static <T extends IMember> AccessPolicy<T> isMemberOfTeam(Integer teamId) {
        return canManage(
            teamId,
            UserRole.MEMBER,
            PersistenceFacade.getNewTeamDAO()::isMemberOfTeam
        );
    }

    public static <T extends IMember> AccessPolicy<T> isTrainerOfMember(Integer memberId) {
        return canManage(
            memberId,
            UserRole.TRAINER,
            PersistenceFacade.getNewTeamDAO()::isTrainerOfMember
        );
    }

    public static <T extends IMember> AccessPolicy<T> isTrainerOfTeam(Integer teamId) {
        return canManage(
            teamId,
            UserRole.TRAINER,
            PersistenceFacade.getNewTeamDAO()::isTrainerOfTeam
        );
    }

    public static <T extends IMember> AccessPolicy<T> isTrainerOfTournament(Integer tournamentId) {
        return canManage(
            tournamentId,
            UserRole.TRAINER,
            PersistenceFacade.getNewTournamentDAO()::isTrainerOfTournament
        );
    }

    public static <T extends IMember> AccessPolicy<T> isDepartmentHead(Integer departmentId) {
        return canManage(
            departmentId,
            UserRole.DEPARTMENT_HEAD,
            PersistenceFacade.getNewDepartmentDAO()::isDepartmentHead
        );
    }

    public static <T extends IMember> AccessPolicy<T> isDepartmentHeadOfMember(Integer memberId) {
        return canManage(
            memberId,
            UserRole.DEPARTMENT_HEAD,
            PersistenceFacade.getNewDepartmentDAO()::isDepartmentHeadOfMember
        );
    }

    public static <T extends IMember> AccessPolicy<T> isDepartmentHeadOfTeam(Integer teamId) {
        return canManage(
            teamId,
            UserRole.DEPARTMENT_HEAD,
            PersistenceFacade.getNewDepartmentDAO()::isDepartmentHeadOfTeam
        );
    }

    public static <T extends IMember> AccessPolicy<T> isDepartmentHeadOfTournament(Integer tournamentId) {
        return canManage(
            tournamentId,
            UserRole.DEPARTMENT_HEAD,
            PersistenceFacade.getNewDepartmentDAO()::isDepartmentHeadOfTournament
        );
    }

    public static <T extends IMember> AccessPolicy<T> canManage(
        Integer unitId,
        UserRole userRole,
        BiFunction<Member, Integer, Boolean> validationFunction
    ) {

        return AccessPolicy.and(
            isInPermissionBound(userRole),
            AccessPolicy.simplePolicy(user -> validationFunction.apply((Member) user, unitId))
        );
    }
}
