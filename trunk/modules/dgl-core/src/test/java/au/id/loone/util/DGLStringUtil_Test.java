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

import au.id.loone.util.tracing.TraceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link DGLStringUtil}.
 *
 * @author David G Loone
 */
public class DGLStringUtil_Test
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(DGLStringUtil_Test.class);

    /**
     */
    public DGLStringUtil_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_isNullOrEmpty_00()
            throws Exception
    {
        assertTrue(DGLStringUtil.isNullOrEmpty(null));
        assertTrue(DGLStringUtil.isNullOrEmpty(""));
        assertFalse(DGLStringUtil.isNullOrEmpty("a"));
    }

    /**
     */
    @Test
    public void test_equals_00()
            throws Exception
    {
        assertTrue(DGLStringUtil.equals(null, null));
        assertTrue(DGLStringUtil.equals("", ""));
        assertFalse(DGLStringUtil.equals(null, "abc"));
        assertFalse(DGLStringUtil.equals("abc", null));
        assertFalse(DGLStringUtil.equals("abc", ""));
        assertFalse(DGLStringUtil.equals("", "abc"));
        assertTrue(DGLStringUtil.equals("abc", "abc"));
        assertTrue(DGLStringUtil.equals("ABC", "ABC"));
        assertFalse(DGLStringUtil.equals("abc", "ABC"));
        assertFalse(DGLStringUtil.equals("ABC", "abc"));
        assertFalse(DGLStringUtil.equals("aBc", "abC"));
        assertFalse(DGLStringUtil.equals("aBc", "abd"));
        assertFalse(DGLStringUtil.equals("aBc", "abCd"));
    }

    /**
     */
    @Test
    public void test_equalsIgnoreCase_00()
            throws Exception
    {
        assertTrue(DGLStringUtil.equalsIgnoreCase(null, null));
        assertTrue(DGLStringUtil.equalsIgnoreCase("", ""));
        assertFalse(DGLStringUtil.equalsIgnoreCase(null, "abc"));
        assertFalse(DGLStringUtil.equalsIgnoreCase("abc", null));
        assertFalse(DGLStringUtil.equalsIgnoreCase("abc", ""));
        assertFalse(DGLStringUtil.equalsIgnoreCase("", "abc"));
        assertTrue(DGLStringUtil.equalsIgnoreCase("abc", "abc"));
        assertTrue(DGLStringUtil.equalsIgnoreCase("ABC", "ABC"));
        assertTrue(DGLStringUtil.equalsIgnoreCase("abc", "ABC"));
        assertTrue(DGLStringUtil.equalsIgnoreCase("ABC", "abc"));
        assertTrue(DGLStringUtil.equalsIgnoreCase("aBc", "abC"));
        assertFalse(DGLStringUtil.equalsIgnoreCase("aBc", "abd"));
        assertFalse(DGLStringUtil.equalsIgnoreCase("aBc", "abCd"));
    }

    /**
     */
    @Test
    public void test_parseBoolean_String_00()
            throws Exception
    {
        assertFalse(DGLStringUtil.parseBoolean(null));
        assertFalse(DGLStringUtil.parseBoolean(""));
        assertFalse(DGLStringUtil.parseBoolean("a"));
        assertFalse(DGLStringUtil.parseBoolean("A"));
        assertTrue(DGLStringUtil.parseBoolean("y"));
        assertTrue(DGLStringUtil.parseBoolean("Y"));
        assertTrue(DGLStringUtil.parseBoolean("yes"));
        assertTrue(DGLStringUtil.parseBoolean("YES"));
        assertTrue(DGLStringUtil.parseBoolean("t"));
        assertTrue(DGLStringUtil.parseBoolean("T"));
        assertTrue(DGLStringUtil.parseBoolean("true"));
        assertTrue(DGLStringUtil.parseBoolean("TRUE"));
        assertTrue(DGLStringUtil.parseBoolean("on"));
        assertTrue(DGLStringUtil.parseBoolean("ON"));
        assertTrue(DGLStringUtil.parseBoolean("x"));
        assertTrue(DGLStringUtil.parseBoolean("X"));
    }

    /**
     */
    @Test
    public void test_parseBoolean_String_boolean_00()
            throws Exception
    {
        assertFalse(DGLStringUtil.parseBoolean(null, false));
        assertTrue(DGLStringUtil.parseBoolean(null, true));
        assertFalse(DGLStringUtil.parseBoolean("", false));
        assertTrue(DGLStringUtil.parseBoolean("", true));
        assertFalse(DGLStringUtil.parseBoolean("a", false));
        assertTrue(DGLStringUtil.parseBoolean("a", true));
        assertFalse(DGLStringUtil.parseBoolean("A", false));
        assertTrue(DGLStringUtil.parseBoolean("A", true));
        assertTrue(DGLStringUtil.parseBoolean("y", false));
        assertTrue(DGLStringUtil.parseBoolean("y", true));
        assertTrue(DGLStringUtil.parseBoolean("Y", false));
        assertTrue(DGLStringUtil.parseBoolean("Y", true));
        assertTrue(DGLStringUtil.parseBoolean("yes", false));
        assertTrue(DGLStringUtil.parseBoolean("yes", true));
        assertTrue(DGLStringUtil.parseBoolean("YES", false));
        assertTrue(DGLStringUtil.parseBoolean("YES", true));
        assertTrue(DGLStringUtil.parseBoolean("t", false));
        assertTrue(DGLStringUtil.parseBoolean("t", true));
        assertTrue(DGLStringUtil.parseBoolean("T", false));
        assertTrue(DGLStringUtil.parseBoolean("T", true));
        assertTrue(DGLStringUtil.parseBoolean("true", false));
        assertTrue(DGLStringUtil.parseBoolean("true", true));
        assertTrue(DGLStringUtil.parseBoolean("TRUE", false));
        assertTrue(DGLStringUtil.parseBoolean("TRUE", true));
        assertTrue(DGLStringUtil.parseBoolean("on", false));
        assertTrue(DGLStringUtil.parseBoolean("on", true));
        assertTrue(DGLStringUtil.parseBoolean("ON", false));
        assertTrue(DGLStringUtil.parseBoolean("ON", true));
        assertTrue(DGLStringUtil.parseBoolean("x", false));
        assertTrue(DGLStringUtil.parseBoolean("x", true));
        assertTrue(DGLStringUtil.parseBoolean("X", false));
        assertTrue(DGLStringUtil.parseBoolean("X", true));
    }

    /**
     */
    @Test
    public void test_indexOf_String_chars_00()
            throws Exception
    {
        assertEquals(0, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'a'}));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'c'}));
        assertEquals(13, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'n'}));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'c', 'k'}));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'x'}));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {}));
    }

    /**
     */
    @Test
    public void test_indexOf_String_int_chars_00()
            throws Exception
    {
        assertEquals(0, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'a'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'c'}, 0));
        assertEquals(13, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'n'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'c', 'k'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'c', 'z'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'z', 'c'}, 0));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {'x'}, 0));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmn", new char[] {}, 0));
    }

    /**
     */
    @Test
    public void test_indexOf_String_int_chars_01()
            throws Exception
    {
        assertEquals(0, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'a'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'c'}, 0));
        assertEquals(13, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'n'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'c', 'k'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'c', 'z'}, 0));
        assertEquals(2, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'z', 'c'}, 0));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'x'}, 0));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {}, 0));
    }

    /**
     */
    @Test
    public void test_indexOf_String_int_chars_02()
            throws Exception
    {
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'a'}, 15));
        assertEquals(17, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'d'}, 15));
        assertEquals(27, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'n'}, 15));
        assertEquals(16, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'c', 'k'}, 15));
        assertEquals(16, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'c', 'z'}, 15));
        assertEquals(16, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'z', 'c'}, 15));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {'x'}, 15));
        assertEquals(-1, DGLStringUtil.indexOf("abcdefghijklmnabcdefghijklmn", new char[] {}, 15));
    }

    /**
     */
    @Test
    public void test_split_String_char_00()
            throws Exception
    {
        assertEquals("[\"\"]", TraceUtil.formatObj(DGLStringUtil.split("", ':')));
        assertEquals("[\"a\"]", TraceUtil.formatObj(DGLStringUtil.split("a", ':')));
        assertEquals("[\"abc\"]", TraceUtil.formatObj(DGLStringUtil.split("abc", ':')));
        assertEquals("[\"abc\", \"def\"]", TraceUtil.formatObj(DGLStringUtil.split("abc:def", ':')));
        assertEquals("[\"abc\", \"\"]", TraceUtil.formatObj(DGLStringUtil.split("abc:", ':')));
        assertEquals("[\"\", \"def\"]", TraceUtil.formatObj(DGLStringUtil.split(":def", ':')));
        assertEquals("[\"\", \"def\", \"ghi\"]", TraceUtil.formatObj(DGLStringUtil.split(":def:ghi", ':')));
        assertEquals("[\"abc\", \"\", \"ghi\"]", TraceUtil.formatObj(DGLStringUtil.split("abc::ghi", ':')));
    }

}
