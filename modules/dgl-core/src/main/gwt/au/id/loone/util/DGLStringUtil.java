/*
 * Copyright 2007, David G Loone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.loone.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Container for static string manipulation methods.
 *
 * @author David G Loone
 */
public class DGLStringUtil
{

    /**
     * An empty string array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final String[] EMPTY_ARRAY = new String[] {};

    /**
     * An array containing the numeric digits.
     */
    public static final char[] DIGITS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * Don't allow this class to be instantiated.
     */
    private DGLStringUtil()
    {
        super();
    }

    /**
     * Test for a string being either null or empty.
     *
     * @param str
     *      The string to test.
     * @return
     *      The value <code>true</code> if <code>str</code> is either equal to
     *      <code>null</code> or an empty string,
     *      <code>false</code> otherwise.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean isNullOrEmpty(
            final String str
    )
    {
        return (str == null) || str.equals("");
    }

    /**
     * Convert a string to null if it is empty or null.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String nullIfEmpty(
            final String str
    )
    {
        return (str == null) ? null :
                ((str.length() == 0) ? null : str);
    }

    /**
     * Convert a string to an empty string if it is null.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String emptyIfNull(
            final String str
    )
    {
        return (str == null) ? "" : str;
    }

    /**
     * Compare two strings (or other objects) for equality.
     *
     * @param obj1
     *      The first object.
     * @param obj2
     *      The second object.
     * @return
     *      The value <code>true</code> if both <code>obj1</code> and
     *      <code>obj2</code> are equal to <code>null</code>,
     *      or are equal
     *      (as determined by their {@link Object#equals(Object)} methods.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean equals(
            final Object obj1,
            final Object obj2
    )
    {
        return ((obj1 == null) && (obj2 == null)) ||
                ((obj1 != null) && obj1.equals(obj2));
    }

    /**
     * Check for equality of two strings (ignoring case),
     * allowing either or both to be equal to <code>null</code>.
     *
     * @param str1
     *      The first string.
     * @param str2
     *      The second string.
     * @return
     *      The value <code>true</code> if either both <code>str1</code> and <code>str2</code>
     *      are equal to <code>null</code>
     *      or they satisfy the {@link String#equals(Object)} method.
     *      Otherwise, <code>false</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean equalsIgnoreCase(
            final String str1,
            final String str2
    )
    {
        return ((str1 == null) && (str2 == null)) ||
                ((str1 != null) && str1.equalsIgnoreCase(str2));
    }

    /**
     * Parse a string into a boolean.
     * Expands the set of values that return <code>true</code>
     * over what {@link Boolean#Boolean(String)} accepts
     * to things like "T", "t", "y", "YES".
     *
     * @param str
     *      The string to parse.
     * @return
     *      The boolean derived from <code>str</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean parseBoolean(
            final String str
    )
    {
        return parseBoolean(str, false);
    }

    /**
     * Parse a string into a boolean.
     * Expands the set of values that return <code>true</code>
     * over what {@link Boolean#Boolean(String)} accepts
     * to things like "T", "t", "y", "YES".
     *
     * @param str
     *      The string to parse.
     * @return
     *      The boolean derived from <code>str</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean parseBoolean(
            final String str,
            final boolean def
    )
    {
        // The value to return.
        final Boolean result;

        if (equalsIgnoreCase(str, "y") ||
                equalsIgnoreCase(str, "yes") ||
                equalsIgnoreCase(str, "t") ||
                equalsIgnoreCase(str, "true") ||
                equalsIgnoreCase(str, "on") ||
                equalsIgnoreCase(str, "x")) {
            result = Boolean.TRUE;
        }
        else if (equalsIgnoreCase(str, "n") ||
                equalsIgnoreCase(str, "no") ||
                equalsIgnoreCase(str, "f") ||
                equalsIgnoreCase(str, "false") ||
                equalsIgnoreCase(str, "off")) {
            result = Boolean.FALSE;
        }
        else {
            result = def;
        }

        return result;
    }

    /**
     * Null-safe trim.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String trim(
            final String str
    )
    {
        return (str == null) ? null : str.trim();
    }

    /**
     * Find the first index of any of the indicated characters in a string.
     *
     * @param str
     *      The string to search.
     * @param chars
     *      The chars to find.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static int indexOf(
            final String str,
            final char[] chars
    )
    {
        int result;

        final int strLength = str.length();
        result = strLength;
        for (final char ch: chars) {
            final int idx = str.indexOf(ch);
            if ((idx != -1) && (idx < result)) {
                result = idx;
            }
        }

        return (result == strLength) ? -1 : result;
    }

    /**
     * Find the first index of any of the indicated characters in a string.
     *
     * @param str
     *      The string to search.
     *      The starting point for the search in <code>str</code>.
     * @param chars
     *      The chars to find.
     * @param startIdx
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static int indexOf(
            final String str,
            final char[] chars,
            final int startIdx
    )
    {
        int result;

        final int strLength = str.length();
        result = strLength;
        for (final char ch: chars) {
            final int idx = str.indexOf(ch, startIdx);
            if ((idx != -1) && (idx < result)) {
                result = idx;
            }
        }

        return (result == strLength) ? -1 : result;
    }

    /**
     * Make a new string that by repeating an existing string.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String repeat(
            final CharSequence str,
            final int n
    )
    {
        final String result;

        if (n < 0) {
            throw new IllegalArgumentException("Negative repeat factor (" + n + ").");
        }
        else if (n == 0) {
            result = "";
        }
        else if (n == 1) {
            result = str.toString();
        }
        else {
            final StringBuilder buf = new StringBuilder(n * str.length());
            for (int i = 0; i < n; i++) {
                buf.append(str);
            }
            result = buf.toString();
        }

        return result;
    }

    /**
     * Safe truncate.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static CharSequence truncate(
            final CharSequence str,
            final int n
    )
    {
        final CharSequence result;

        if (str == null) {
            result = null;
        }
        else if (str.length() <= n) {
            result = str;
        }
        else {
            result = str.subSequence(0, n);
        }

        return result;
    }

    /**
     * Safe truncate.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String truncate(
            final String str,
            final int n
    )
    {
        final String result;

        if (str == null) {
            result = null;
        }
        else if (str.length() <= n) {
            result = str;
        }
        else {
            result = str.substring(0, n);
        }

        return result;
    }

    /**
     * Split into tokens.
     */
    public static String[] split(
            final String str,
            final char sep
    )
    {
        final List<String> result;

        if (str == null) {
            result = null;
        }
        else {
            result = new LinkedList<String>();
            int startIdx = 0;
            int endIdx = 0;
            while (endIdx < str.length()) {
                if (str.charAt(endIdx) == sep) {
                    result.add(str.substring(startIdx, endIdx));
                    startIdx = endIdx + 1;
                    endIdx = startIdx;
                }
                else {
                    endIdx++;
                }
            }
            result.add(str.substring(startIdx, endIdx));
        }

        return (result == null) ? null : result.toArray(new String[result.size()]);
    }

    /**
     * Join some tokens with a separator character.
     */
    public static String join(
            final String[] tokens,
            final char sep
    )
    {
        final StringBuilder result;

        result = new StringBuilder();
        if (tokens != null) {
            for (int i = 0; i < tokens.length; i++) {
                if (i != 0) {
                    result.append(sep);
                }
                result.append(tokens[i]);
            }
        }

        return result.toString();
    }

}