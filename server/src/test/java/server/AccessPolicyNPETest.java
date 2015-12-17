package server;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.domain.Member;
import org.junit.Test;

import javax.naming.*;
import java.util.Hashtable;

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

//    @Test
//    public void LDAPTest() throws NamingException {
//        Hashtable<String, String> env = new Hashtable<>();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.PROVIDER_URL, "ldaps://ldap.fhv.at:636/dc=uclv,dc=net");
//
//
//        Context ctx = new InitialContext(env);
//        browseRecursive(ctx, 0);
//    }
//
//    public static void browseRecursive(Context ctx, int depth)
//    throws NamingException {
//        NamingEnumeration<Binding> namingEnum = ctx.listBindings("");
//        while (namingEnum.hasMore()) {
//            Binding bnd = namingEnum.next();
//            if (bnd.getObject() instanceof Context) {
//                Context curCtx = (Context) bnd.getObject();
//                for (int i = 0; i < depth * 2; ++i)
//                    System.out.print(' ');
//                System.out.println(bnd.getName());
//                if (bnd.getName().startsWith("ou=apps") || !bnd.getName().startsWith("ou")) {
//                    browseRecursive(curCtx, depth + 1);
//                }
//            }
//        }
//    }
}
