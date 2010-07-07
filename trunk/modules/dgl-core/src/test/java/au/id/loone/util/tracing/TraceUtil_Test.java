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

package au.id.loone.util.tracing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import au.id.loone.util.DGLDateTimeUtil;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for {@link TraceUtil}.
 *
 * @author David G Loone
 */
public class TraceUtil_Test
{

    /**
     * Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(TraceUtil_Test.class);

    /**
     */
    public TraceUtil_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void testFormatObj_Object_00()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data);
        assertEquals("\"abcde\"", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_01()
    {
        final Object data = null;
        final String result = TraceUtil.formatObj(data);
        assertNull(result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_02()
    {
        final char[] data = "abcde".toCharArray();
        final String result = TraceUtil.formatObj(data);
        assertEquals("['a', 'b', 'c', 'd', 'e']", result);
    }

    /**
     *  @throws Exception
     *      Whenever anything bad happens.
     */
    @Test
    public void testFormatObj_Object_03()
            throws Exception
    {
        final Date data = DGLDateTimeUtil.parseAsISO8601("2004-06-23T00:00:00.000").getTime();
        final String result = TraceUtil.formatObj(data);
        assertEquals("2004-06-23T00:00:00.000", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_04()
    {
        final String[] data = new String[] {"abcde", "fghij"};
        final String result = TraceUtil.formatObj(data);
        assertEquals("[\"abcde\", \"fghij\"]", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_05()
    {
        final Long[] data = new Long[] {2L, 567L};
        final String result = TraceUtil.formatObj(data);
        assertEquals("[2, 567]", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_06()
    {
        final Collection data = Arrays.asList("abcde", "fghij");
        final String result = TraceUtil.formatObj(data);
        assertEquals("{\"abcde\", \"fghij\"}", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_07()
    {
        final Collection data = Arrays.asList(2L, 567L);
        final String result = TraceUtil.formatObj(data);
        assertEquals("{2, 567}", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_08()
    {
        final Map<String, String> data = new TreeMap<String, String>();
        data.put("first", "abcde");
        data.put("second", "fghij");
        final String result = TraceUtil.formatObj(data);
        assertEquals("{\"first\" = \"abcde\", \"second\" = \"fghij\"}", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_09()
    {
        final Map<String, Long> data = new TreeMap<String, Long>();
        data.put("first", 2L);
        data.put("second", 567L);
        final String result = TraceUtil.formatObj(data);
        assertEquals("{\"first\" = 2, \"second\" = 567}", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_10()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data.getBytes());
        assertEquals("[97'a', 98'b', 99'c', 100'd', 101'e']", result);
    }

    /**
     */
    @Test
    public void testFormatObj_boolean_00()
    {
        final String result = TraceUtil.formatObj(false);
        assertEquals("false", result);
    }

    /**
     */
    @Test
    public void testFormatObj_boolean_01()
    {
        final String result = TraceUtil.formatObj(true);
        assertEquals("true", result);
    }

    /**
     */
    @Test
    public void testFormatObj_byte_00()
    {
        final String result = TraceUtil.formatObj((byte)3);
        assertEquals("3", result);
    }

    /**
     */
    @Test
    public void testFormatObj_short_00()
    {
        final String result = TraceUtil.formatObj((short)3);
        assertEquals("3", result);
    }

    /**
     */
    @Test
    public void testFormatObj_int_00()
    {
        final String result = TraceUtil.formatObj(3);
        assertEquals("3", result);
    }

    /**
     */
    @Test
    public void testFormatObj_int_01()
    {
        final int[] a = new int[] {1, 2, 3};
        final String result = TraceUtil.formatObj(a);
        assertEquals("[1, 2, 3]", result);
        assertEquals("a.length = 3", TraceUtil.formatObj(a, "a.length"));
    }

    /**
     */
    @Test
    public void testFormatObj_long_00()
    {
        final String result = TraceUtil.formatObj(3L);
        assertEquals("3", result);
    }

    /**
     */
    @Test
    public void testFormatObj_float_00()
    {
        final String result = TraceUtil.formatObj(3.78);
        assertEquals("3.78", result);
    }

    /**
     */
    @Test
    public void testFormatObj_double_00()
    {
        final String result = TraceUtil.formatObj(3.78d);
        assertEquals("3.78", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_00()
    {
        final String data = null;
        final String result = TraceUtil.formatObj(data, "name");
        assertEquals("name = null", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_01()
    {
        final String data = null;
        final String result = TraceUtil.formatObj(data, null);
        assertNull(result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_02()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, null);
        assertEquals("\"abcde\"", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_03()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name");
        assertEquals("name = \"abcde\"", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_04()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name.bytes");
        assertEquals("name.bytes = [97'a', 98'b', 99'c', 100'd', 101'e']", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_05()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name.class.name");
        assertEquals("name.class.name = \"java.lang.String\"", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_06()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name.class.componentType.xyz");
        assertEquals("name.class.componentType(.xyz) = null", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_07()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name.class.xyz");
        assertEquals("name.class.xyz(?)", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_08()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name.class.componentType");
        assertEquals("name.class.componentType = null", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_String_00()
    {
        final String data = null;
        final String result = TraceUtil.formatObj(data, "name", "bytes");
        assertEquals("name = null", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_String_01()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name", "bytes");
        assertEquals("name.bytes = [97'a', 98'b', 99'c', 100'd', 101'e']", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_String_02()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name", "bytes.xyz");
        assertEquals("name.bytes.xyz(?)", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_String_03()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name", "class.componentType");
        assertEquals("name.class.componentType = null", result);
    }

    /**
     */
    @Test
    public void testFormatObj_Object_String_String_04()
    {
        final String data = "abcde";
        final String result = TraceUtil.formatObj(data, "name", "class.name.bytes.class.componentType");
        assertEquals("name.class.name.bytes.class.componentType = byte", result);
    }

    /**
     */
    @Test
    public void testFormatProperties_Object_Class_00()
    {
        final C c = new C();
        final String result = TraceUtil.formatProperties(c, Object.class);
        assertEquals("[prop1 = \"abc\", prop2 = B:[def], prop3 = \"ghi\"]", result);
    }

    /**
     */
    @Test
    public void testFormatProperties_Object_Class_01()
    {
        final C c = new C();
        final String result = TraceUtil.formatProperties(c, A.class);
        assertEquals("[prop3 = \"ghi\"]", result);
    }

    /**
     */
    public static class A
    {

        public String getProp1()
        {
            return "abc";
        }

        public B getProp2()
        {
            return new B("def");
        }

    }

    /**
     */
    public static class B
    {

        private String itsProp2;

        public B(
                final String prop2
        )
        {
            itsProp2 = prop2;
        }

        public String toString()
        {
            return "B:[" + getProp2() + "]";
        }

        public String getProp2()
        {
            return itsProp2;
        }

    }

    public static class C
            extends A
    {

        public String getProp3()
        {
            return "ghi";
        }

    }

}