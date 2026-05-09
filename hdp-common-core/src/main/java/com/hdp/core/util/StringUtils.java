package com.hdp.core.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class StringUtils {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final int MAX_SLUG_LENGTH = 100;

    private StringUtils() {
    }

    public static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String nowhitespace = input.trim().toLowerCase(Locale.forLanguageTag("vi"));
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        slug = WHITESPACE.matcher(slug).replaceAll("-");

        if (slug.length() > MAX_SLUG_LENGTH) {
            slug = slug.substring(0, MAX_SLUG_LENGTH);
        }

        return slug;
    }

    public static String truncate(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"']", "");
    }

    public static boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }
}