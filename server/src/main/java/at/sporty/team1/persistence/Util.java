package at.sporty.team1.persistence;

/**
 * Created by sereGkaluv on 06-Nov-15.
 */
public class Util {
    private static final String WILDCARD = "%";

    /**
     * Wraps search String in wildcard.
     *
     * @param searchString String to be wrapped.
     * @return String wrapped search String.
     */
    public static String wrapInWildcard(String searchString) {
        return WILDCARD + searchString + WILDCARD;
    }
}
