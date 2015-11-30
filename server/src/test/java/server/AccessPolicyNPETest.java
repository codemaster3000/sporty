package server;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.domain.Member;
import org.junit.Test;

/**
 * Created by sereGkaluv on 29-Nov-15.
 */
public class AccessPolicyNPETest {

    //This is a fast and dirty implementation of test for potential AccessPolicy NPE methods

    @Test
    public void NPETest() {
        Member member = null;

        AccessPolicy policy = BasicAccessPolicies.isSelf(null);
        System.out.println(policy.isFollowedBy(null));
    }
}
