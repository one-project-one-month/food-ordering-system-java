package org._p1m.foodorderingsystem.common.util;

public final class PluralizeUtil {

    public PluralizeUtil() {}

    public static String pluralize(final String word) {
        if (word == null || word.trim().isEmpty()) {
            return word;
        }

        final String lowerCaseWord = word.toLowerCase();

        if (lowerCaseWord.endsWith("y") && !isVowel(lowerCaseWord.charAt(lowerCaseWord.length() - 2))) {
            return word.substring(0, word.length() - 1) + "ies";
        } else if (lowerCaseWord.endsWith("s") || lowerCaseWord.endsWith("x") || lowerCaseWord.endsWith("z") || lowerCaseWord.endsWith("ch") || lowerCaseWord.endsWith("sh")) {
            return word + "es";
        } else {
            return word + "s";
        }
    }

    private static boolean isVowel(final char c) {
        return "aeiou".indexOf(c) != -1;
    }

    public static String toSnakeCase(final String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String pluralizeSnakeCase(String snakeCase) {
        final String[] parts = snakeCase.split("_");
        if (parts.length == 0) return snakeCase;

        final int lastIndex = parts.length - 1;
        parts[lastIndex] = pluralize(parts[lastIndex]);

        return String.join("_", parts);
    }
}