package at.sporty.team1.persistence.util;

/**
 * Created by sereGkaluv on 06-Nov-15.
 */
public class Util {
    private static final String WILDCARD = "%";
    private static final String ALIAS_DELIMITER = ".";

    /**
     * Wraps search String in wildcard.
     *
     * @param searchString String to be wrapped.
     * @return String wrapped search String.
     */
    public static String wrapInWildcard(String searchString) {
        return WILDCARD + searchString + WILDCARD;
    }

    /**
     * Builds Alias reference from prefix and suffix.
     *
     * @param prefix Prefix for the Alias.
     * @param suffix Suffix for the Alias.
     * @return String Alias reference.
     */
    public static String buildAliasReference(String prefix, String suffix) {
        return prefix + ALIAS_DELIMITER + suffix;
    }
}
