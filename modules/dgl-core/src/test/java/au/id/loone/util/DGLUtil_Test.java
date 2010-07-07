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
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *  Test class for {@link DGLUtil_Test}.
 *
 *  @author David G Loone
 *  @version $Id$
 */
public class DGLUtil_Test
{

    /**
     *  Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLUtil_Test.class);

    /**
     */
    public DGLUtil_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_equals_boolean_00()
    {
        assertEquals(true, DGLUtil.equals(false, false));
    }

    /**
     */
    @Test
    public void test_equals_boolean_01()
    {
        assertEquals(true, DGLUtil.equals(true, true));
    }

    /**
     */
    @Test
    public void test_equals_boolean_02()
    {
        assertEquals(false, DGLUtil.equals(false, true));
    }

    /**
     */
    @Test
    public void test_equals_boolean_array_00()
    {
        assertEquals(true, DGLUtil.equals(new boolean[] {true, false, true}, new boolean[] {true, false, true}));
    }

    /**
     */
    @Test
    public void test_equals_boolean_array_01()
    {
        assertEquals(false, DGLUtil.equals(new boolean[] {true, false, true}, new boolean[] {true, false, false}));
    }

    /**
     */
    @Test
    public void test_equals_boolean_array_02()
    {
        assertEquals(false, DGLUtil.equals(new boolean[] {true, false, true}, new boolean[] {true, false}));
    }

    /**
     */
    @Test
    public void test_equals_char_00()
    {
        assertEquals(true, DGLUtil.equals('a', 'a'));
    }

    /**
     */
    @Test
    public void test_equals_char_01()
    {
        assertEquals(false, DGLUtil.equals('a', 'b'));
    }

    /**
     */
    @Test
    public void test_equals_char_array_00()
    {
        assertEquals(true, DGLUtil.equals(new char[] {'a', 'b', 'c'}, new char[] {'a', 'b', 'c'}));
    }

    /**
     */
    @Test
    public void test_equals_char_array_01()
    {
        assertEquals(false, DGLUtil.equals(new char[] {'a', 'b', 'c'}, new char[] {'a', 'b', 'd'}));
    }

    /**
     */
    @Test
    public void test_equals_char_array_02()
    {
        assertEquals(false, DGLUtil.equals(new char[] {'a', 'b', 'c'}, new char[] {'a', 'b'}));
    }

    /**
     */
    @Test
    public void test_hashCode_boolean_00()
    {
        assertEquals(0, DGLUtil.hashCode(false));
        assertEquals(1, DGLUtil.hashCode(true));
    }

    /**
     */
    @Test
    public void test_hashCode_char_00()
    {
        assertEquals('0', DGLUtil.hashCode('0'));
        assertEquals('a', DGLUtil.hashCode('a'));
    }

    /**
     */
    @Test
    public void test_hashCode_byte_00()
    {
        assertEquals(0, DGLUtil.hashCode((byte)0));
        assertEquals(23, DGLUtil.hashCode((byte)23));
    }

    /**
     */
    @Test
    public void test_hashCode_short_00()
    {
        assertEquals(0, DGLUtil.hashCode((short)0));
        assertEquals(23, DGLUtil.hashCode((short)23));
    }

    /**
     */
    @Test
    public void test_hashCode_int_00()
    {
        assertEquals(0, DGLUtil.hashCode(0));
        assertEquals(23, DGLUtil.hashCode(23));
    }

    /**
     */
    @Test
    public void test_hashCode_long_00()
    {
        assertEquals(0, DGLUtil.hashCode(0L));
        assertEquals(23, DGLUtil.hashCode(23L));
        assertEquals(-1, DGLUtil.hashCode(Long.MAX_VALUE));
    }

    /**
     */
    @Test
    public void test_hashCode_float_00()
    {
        assertEquals(0, DGLUtil.hashCode((float)0.0));
        assertEquals(23, DGLUtil.hashCode((float)23.3));
    }

    /**
     */
    @Test
    public void test_hashCode_double_00()
    {
        assertEquals(0, DGLUtil.hashCode(0.0));
        assertEquals(23, DGLUtil.hashCode(23.3));
    }

    /**
     */
    @Test
    public void test_hashCode_Object_00()
    {
        assertEquals(0, DGLUtil.hashCode((Object)null));
        assertEquals(Boolean.FALSE.hashCode(), DGLUtil.hashCode(Boolean.FALSE));
        assertEquals(Boolean.TRUE.hashCode(), DGLUtil.hashCode(Boolean.TRUE));
        assertEquals((new Character('0')).hashCode(), DGLUtil.hashCode(new Character('0')));
        assertEquals((new Character('a')).hashCode(), DGLUtil.hashCode(new Character('a')));
        assertEquals((new Byte((byte)0)).hashCode(), DGLUtil.hashCode(new Byte((byte)0)));
        assertEquals((new Byte((byte)23)).hashCode(), DGLUtil.hashCode(new Byte((byte)23)));
        assertEquals((new Short((short)0)).hashCode(), DGLUtil.hashCode(new Short((short)0)));
        assertEquals((new Short((short)23)).hashCode(), DGLUtil.hashCode(new Short((short)23)));
        assertEquals((new Integer(0)).hashCode(), DGLUtil.hashCode(new Integer(0)));
        assertEquals((new Integer(23)).hashCode(), DGLUtil.hashCode(new Integer(23)));
        assertEquals((new Long(0L)).hashCode(), DGLUtil.hashCode(new Long(0L)));
        assertEquals((new Long(23L)).hashCode(), DGLUtil.hashCode(new Long(23L)));
        assertEquals((new Long(Long.MAX_VALUE)).hashCode(), DGLUtil.hashCode(new Long(Long.MAX_VALUE)));
        assertEquals((new Float((float)0.0)).hashCode(), DGLUtil.hashCode(new Float((float)0.0)));
        assertEquals((new Float((float)23.3)).hashCode(), DGLUtil.hashCode(new Float((float)23.3)));
        assertEquals((new Double(0.0)).hashCode(), DGLUtil.hashCode(new Double(0.0)));
        assertEquals((new Double(23.3)).hashCode(), DGLUtil.hashCode(new Double(23.3)));
        assertEquals("abcde".hashCode(), DGLUtil.hashCode("abcde"));
    }

    /**
     */
    @Test
    public void test_compare_char_char_00()
    {
        assertEquals(0, DGLUtil.compare('0', '0'));
        assertEquals(-1, DGLUtil.compare('0', '1'));
        assertEquals(1, DGLUtil.compare('1', '0'));
    }

    /**
     */
    @Test
    public void test_compare_byte_byte_00()
    {
        assertEquals(0, DGLUtil.compare((byte)0, (byte)0));
        assertEquals(0, DGLUtil.compare((byte)23, (byte)23));
        assertEquals(-1, DGLUtil.compare((byte)23, (byte)25));
        assertEquals(1, DGLUtil.compare((byte)25, (byte)23));
    }

    /**
     */
    @Test
    public void test_compare_short_short_00()
    {
        assertEquals(0, DGLUtil.compare((short)0, (short)0));
        assertEquals(0, DGLUtil.compare((short)23, (short)23));
        assertEquals(-1, DGLUtil.compare((short)23, (short)25));
        assertEquals(1, DGLUtil.compare((short)25, (short)23));
    }

    /**
     */
    @Test
    public void test_compare_int_int_00()
    {
        assertEquals(0, DGLUtil.compare(0, 0));
        assertEquals(0, DGLUtil.compare(23, 23));
        assertEquals(-1, DGLUtil.compare(23, 25));
        assertEquals(1, DGLUtil.compare(25, 23));
    }

    /**
     */
    @Test
    public void test_compare_long_long_00()
    {
        assertEquals(0, DGLUtil.compare((long)0, (long)0));
        assertEquals(0, DGLUtil.compare((long)23, (long)23));
        assertEquals(-1, DGLUtil.compare((long)23, (long)25));
        assertEquals(1, DGLUtil.compare((long)25, (long)23));
    }

    /**
     */
    @Test
    public void test_compare_float_float_00()
    {
        assertEquals(0, DGLUtil.compare((float)0.0, (float)0.0));
        assertEquals(0, DGLUtil.compare((float)23.0, (float)23.0));
        assertEquals(-1, DGLUtil.compare((float)23.0, (float)25.0));
        assertEquals(1, DGLUtil.compare((float)25.0, (float)23.0));
    }

    /**
     */
    @Test
    public void test_compare_double_double_00()
    {
        assertEquals(0, DGLUtil.compare(0.0, 0.0));
        assertEquals(0, DGLUtil.compare(23.0, 23.0));
        assertEquals(-1, DGLUtil.compare(23.0, 25.0));
        assertEquals(1, DGLUtil.compare(25.0, 23.0));
    }

    /**
     */
    @Test
    public void test_compare_T_T_00()
    {
        //noinspection RedundantCast
        assertEquals(0, DGLUtil.compare((String)null, null));
        assertEquals(0, DGLUtil.compare("", ""));
        assertEquals(0, DGLUtil.compare("abc", "abc"));
        assertEquals(-1, DGLUtil.compare(null, "abc"));
        assertEquals(1, DGLUtil.compare("abc", null));
        assertEquals("abc".compareTo("abcd"), DGLUtil.compare("abc", "abcd"));
        assertEquals("abc".compareTo("def"), DGLUtil.compare("abc", "def"));
        assertEquals("def".compareTo("abc"), DGLUtil.compare("def", "abc"));
    }

    /**
     */
    @Test
    public void test_moduloComparator_00()
    {
        final DGLUtil.ModuloComparator<Integer> mc = new DGLUtil.ModuloComparator<Integer>(1000);
        assertEquals(0, mc.compare(0, 0));
        assertEquals(0, mc.compare(1, 1));
        assertEquals(0, mc.compare(999, 999));
        assertEquals(0, mc.compare(1000, 1000));

        assertEquals(-1, mc.compare(0, 1));
        assertEquals(-1, mc.compare(0, 100));
        assertEquals(-1, mc.compare(0, 499));
        assertEquals(-1, mc.compare(1, 500));
        assertEquals(-1, mc.compare(2, 501));
        assertEquals(1, mc.compare(0, 500));
        assertEquals(1, mc.compare(1, 501));
        assertEquals(1, mc.compare(2, 502));
        assertEquals(1, mc.compare(92, 592));
        assertEquals(1, mc.compare(1, 0));
        assertEquals(1, mc.compare(2, 0));

        assertEquals(1, mc.compare(500, 0));
        assertEquals(-1, mc.compare(501, 0));
        assertEquals(-1, mc.compare(502, 0));
        assertEquals(1, mc.compare(998, 502));
        assertEquals(1, mc.compare(999, 501));
        assertEquals(1, mc.compare(999, 501));
        assertEquals(1, mc.compare(999, 499));
        assertEquals(-1, mc.compare(999, 498));
        assertEquals(1, mc.compare(998, 997));
        assertEquals(1, mc.compare(2, 998));
        assertEquals(-1, mc.compare(2, 403));
    }

}