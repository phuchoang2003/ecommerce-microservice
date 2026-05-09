package com.hdp.common.persistence.utils;

import org.springframework.util.StringUtils;

/**
 * Utility class for SQL LIKE pattern handling.
 *
 * <p>Wildcard patterns:</p>
 * <ul>
 *   <li>{@code john} - exact match (no wildcards added)</li>
 *   <li>{@code %john%} - contains</li>
 *   <li>{@code john%} - starts with</li>
 *   <li>{@code %john} - ends with</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * SqlUtils.like("john")           // "%john%"
 * SqlUtils.like("john%", "john")  // "john%"
 * SqlUtils.like("%john", "john")  // "%john"
 * SqlUtils.like("john")           // "john" (exact, no wildcards)
 * }</pre>
 */
public final class SqlUtils {

    /** Default wildcard character */
    public static final char WILDCARD = '%';

    private SqlUtils() {}

    /**
     * Wraps value with % wildcards for contains search.
     *
     * <pre>{@code
     * SqlUtils.like("john")  // "%john%"
     * SqlUtils.like("")      // "%" (empty string wrapped)
     * SqlUtils.like(null)    // null
     * }</pre>
     *
     * @param value the value to make into LIKE pattern
     * @return LIKE pattern with % on both sides, or null if value is null
     */
    public static String like(String value) {
        return like(value, true, true);
    }

    /**
     * Creates a LIKE pattern with configurable wildcard positions.
     *
     * <pre>{@code
     * SqlUtils.like("john", true, true)   // "%john%" (contains)
     * SqlUtils.like("john", true, false)  // "%john"  (ends with)
     * SqlUtils.like("john", false, true)  // "john%"  (starts with)
     * SqlUtils.like("john", false, false) // "john"   (exact match)
     * }</pre>
     *
     * @param value         the value
     * @param prefixWildcard true to add % before
     * @param suffixWildcard true to add % after
     * @return LIKE pattern
     */
    public static String like(String value, boolean prefixWildcard, boolean suffixWildcard) {
        if (value == null) {
            return null;
        }
        StringBuilder pattern = new StringBuilder();
        if (prefixWildcard) {
            pattern.append(WILDCARD);
        }
        pattern.append(value);
        if (suffixWildcard) {
            pattern.append(WILDCARD);
        }
        return pattern.toString();
    }

    /**
     * Checks if the value already contains wildcard characters.
     *
     * <pre>{@code
     * SqlUtils.hasWildcard("%john%")  // true
     * SqlUtils.hasWildcard("john%")   // true
     * SqlUtils.hasWildcard("john")   // false
     * }</pre>
     *
     * @param value the LIKE pattern
     * @return true if contains % or _
     */
    public static boolean hasWildcard(String value) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        return value.contains("%") || value.contains("_");
    }

    /**
     * Escapes special LIKE characters (% _ \) to prevent pattern injection.
     *
     * <pre>{@cde
     * SqlUtils.escape("100%")   // "100\\%"
     * SqlUtils.escape("a_b")    // "a\\_b"
     * SqlUtils.escape("a\\b")   // "a\\\\b"
     * }</pre>
     *
     * @param value the value to escape
     * @return escaped value safe for LIKE
     */
    public static String escape(String value) {
        if (value == null) {
            return null;
        }
        return value
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_");
    }

    /**
     * Creates a LIKE pattern with wildcards, then escapes special characters.
     * Convenience method combining like() and escape().
     *
     * <pre>{@code
     * SqlUtils.likeEscape("100%")  // "\\%100\\%"
     * SqlUtils.likeEscape("john")  // "%john%"
     * }</pre>
     *
     * @param value the search term
     * @return escaped LIKE pattern
     */
    public static String likeEscape(String value) {
        return likeEscape(value, true, true);
    }

    /**
     * Creates a LIKE pattern with wildcards and escaping.
     *
     * <pre>{@code
     * SqlUtils.likeEscape("100%", true, true)  // "\\%100\\%"
     * SqlUtils.likeEscape("john", true, false) // "%john"
     * }</pre>
     *
     * @param value           the search term
     * @param prefixWildcard  true to add % before
     * @param suffixWildcard  true to add % after
     * @return escaped LIKE pattern
     */
    public static String likeEscape(String value, boolean prefixWildcard, boolean suffixWildcard) {
        if (value == null) {
            return null;
        }
        return like(escape(value), prefixWildcard, suffixWildcard);
    }
}