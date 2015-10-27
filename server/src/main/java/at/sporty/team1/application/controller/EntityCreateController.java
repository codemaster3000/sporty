package at.sporty.team1.application.controller;

import at.sporty.team1.application.builder.MemberBuilder;

/**
 * Created by sereGkaluv on 27-Oct-15.
 */
public class EntityCreateController {
    public static MemberBuilder createNewMember() {
        return new MemberBuilder();
    }
}
