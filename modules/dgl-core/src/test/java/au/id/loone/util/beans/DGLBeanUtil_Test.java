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

package au.id.loone.util.beans;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.exception.FatalException;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for {@link DGLBeanUtil} class.
 *
 * @author David G Loone
 */
public class DGLBeanUtil_Test
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(DGLBeanUtil_Test.class);

    /**
     */
    @Test
    public void test_setProperty_Map_String_Object_00()
    {
        final Map<String, String> m = new HashMap<String, String>();
        DGLBeanUtil.setProperty(m, "[abc]", "x");
        DGLBeanUtil.setProperty(m, "[def]", "y");
        assertEquals("x", m.get("abc"));
        assertEquals("y", m.get("def"));
    }

    /**
     */
    @Test
    public void test_setProperty_Properties_String_Object_00()
    {
        final Properties m = new Properties();
        DGLBeanUtil.setProperty(m, "[abc]", "x");
        DGLBeanUtil.setProperty(m, "[def]", "y");
        assertEquals("x", m.get("abc"));
        assertEquals("y", m.get("def"));
    }

    /**
     */
    @Test
    public void test_setProperty_List_String_Object_00()
    {
        final List<String> l = new LinkedList<String>();
        DGLBeanUtil.setProperty(l, "[0]", "x");
        DGLBeanUtil.setProperty(l, "[1]", "y");
        assertEquals("x", l.get(0));
        assertEquals("y", l.get(1));
    }

    /**
     */
    @Test
    public void test_setProperty_Set_String_Object_00()
    {
        final Set<String> s = new HashSet<String>();
        DGLBeanUtil.setProperty(s, "[0]", "x");
        DGLBeanUtil.setProperty(s, "[1]", "y");
        assertEquals(true, s.contains("x"));
        assertEquals(true, s.contains("y"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_00()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[abc]", "x");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[def]", "y");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[ghi]", "z");
        assertEquals(3, tb1.getPropTestBean2().getPropMapStringString().size());
        assertEquals("x", tb1.getPropTestBean2().getPropMapStringString().get("abc"));
        assertEquals("y", tb1.getPropTestBean2().getPropMapStringString().get("def"));
        assertEquals("z", tb1.getPropTestBean2().getPropMapStringString().get("ghi"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_StringBuilder_Object_00()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[abc]", new StringBuilder("x"));
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[def]", new StringBuilder("y"));
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[ghi]", new StringBuilder("z"));
        assertEquals(3, tb1.getPropTestBean2().getPropMapStringString().size());
        assertEquals("x", tb1.getPropTestBean2().getPropMapStringString().get("abc"));
        assertEquals("y", tb1.getPropTestBean2().getPropMapStringString().get("def"));
        assertEquals("z", tb1.getPropTestBean2().getPropMapStringString().get("ghi"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_StringBuffer_Object_00()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[abc]", new StringBuffer("x"));
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[def]", new StringBuffer("y"));
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringString[ghi]", new StringBuffer("z"));
        assertEquals(3, tb1.getPropTestBean2().getPropMapStringString().size());
        assertEquals("x", tb1.getPropTestBean2().getPropMapStringString().get("abc"));
        assertEquals("y", tb1.getPropTestBean2().getPropMapStringString().get("def"));
        assertEquals("z", tb1.getPropTestBean2().getPropMapStringString().get("ghi"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_01()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[abc].prop1", "x1");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[abc].prop2", "2");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[def].prop1", "y1");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[def].prop2", "3");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[ghi].prop1", "z1");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propMapStringTestBean3[ghi].prop2", "4");
        assertEquals(3, tb1.getPropTestBean2().getPropMapStringTestBean3().size());
        LOG.debug("test_setProperty_Object_String_Object_01: " + TraceUtil.formatObj(tb1, "tb1.propTestBean2.propMapStringTestBean3"));
        assertEquals("x1", tb1.getPropTestBean2().getPropMapStringTestBean3().get("abc").getProp1());
        assertEquals(2, tb1.getPropTestBean2().getPropMapStringTestBean3().get("abc").getProp2());
        assertEquals("y1", tb1.getPropTestBean2().getPropMapStringTestBean3().get("def").getProp1());
        assertEquals(3, tb1.getPropTestBean2().getPropMapStringTestBean3().get("def").getProp2());
        assertEquals("z1", tb1.getPropTestBean2().getPropMapStringTestBean3().get("ghi").getProp1());
        assertEquals(4, tb1.getPropTestBean2().getPropMapStringTestBean3().get("ghi").getProp2());
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_02()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propProperties[abc]", "x");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propProperties[def]", "y");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propProperties[ghi]", "z");
        assertEquals(3, tb1.getPropTestBean2().getPropProperties().size());
        assertEquals("x", tb1.getPropTestBean2().getPropProperties().get("abc"));
        assertEquals("y", tb1.getPropTestBean2().getPropProperties().get("def"));
        assertEquals("z", tb1.getPropTestBean2().getPropProperties().get("ghi"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_03()
    {
        try {
            final TestBean1 tb1 = new TestBean1();
            tb1.setPropTestBean2(new TestBean2());
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[0].prop1", "x1");
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[0].prop2", "2");
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[1].prop1", "y1");
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[1].prop2", "3");
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[3].prop1", "z1");
            DGLBeanUtil.setProperty(tb1, "propTestBean2.propListTestBean3[3].prop2", "4");
            assertEquals(4, tb1.getPropTestBean2().getPropListTestBean3().size());
            assertEquals("x1", tb1.getPropTestBean2().getPropListTestBean3().get(0).getProp1());
            assertEquals(2, tb1.getPropTestBean2().getPropListTestBean3().get(0).getProp2());
            assertEquals("y1", tb1.getPropTestBean2().getPropListTestBean3().get(1).getProp1());
            assertEquals(3, tb1.getPropTestBean2().getPropListTestBean3().get(1).getProp2());
            assertEquals(null, tb1.getPropTestBean2().getPropListTestBean3().get(2));
            assertEquals("z1", tb1.getPropTestBean2().getPropListTestBean3().get(3).getProp1());
            assertEquals(4, tb1.getPropTestBean2().getPropListTestBean3().get(3).getProp2());
        }
        catch (final FatalException e) {
            LOG.warn(e);
            throw e;
        }
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_04()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propListString[0]", "x");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propListString[1]", "y");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propListString[2]", "z");
        assertEquals(3, tb1.getPropTestBean2().getPropListString().size());
        assertEquals("x", tb1.getPropTestBean2().getPropListString().get(0));
        assertEquals("y", tb1.getPropTestBean2().getPropListString().get(1));
        assertEquals("z", tb1.getPropTestBean2().getPropListString().get(2));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_05()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propSetString[abc]", "x");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propSetString[def]", "y");
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propSetString[ghi]", "z");
        assertEquals(3, tb1.getPropTestBean2().getPropSetString().size());
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("x"));
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("y"));
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("z"));
    }

    /**
     */
    @Test
    public void test_setProperty_Object_String_Object_06()
    {
        final TestBean1 tb1 = new TestBean1();
        tb1.setPropTestBean2(new TestBean2());
        DGLBeanUtil.setProperty(tb1, "propTestBean2.propSetString", new String[] {"x", "y", "z"});
        assertEquals(3, tb1.getPropTestBean2().getPropSetString().size());
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("x"));
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("y"));
        assertEquals(true, tb1.getPropTestBean2().getPropSetString().contains("z"));
    }

    /**
     */
    @Test
    public void test_getPropertyDescriptor_Class_String_00()
    {
        final String data = "abcde";
        final PropertyDescriptor pd = DGLBeanUtil.getPropertyDescriptor(data.getClass(), "class");
        assertEquals("java.lang.Class", pd.getPropertyType().getName());
    }

    /**
     */
    @Test
    public void test_getPropertyDescriptor_Class_String_01()
    {
        final String data = "abcde";
        final PropertyDescriptor pd = DGLBeanUtil.getPropertyDescriptor(data.getClass(), "bytes");
        assertEquals("[B", pd.getPropertyType().getName());
    }

    /**
     */
    @Test
    public void test_getPropertyDescriptorForBean_Class_String_00()
    {
        final String data = "abcde";
        final PropertyDescriptor pd = DGLBeanUtil.getPropertyDescriptorForBean(data, "class");
        assertEquals("java.lang.Class", pd.getPropertyType().getName());
    }

    /**
     */
    @Test
    public void test_getPropertyDescriptorForBean_Class_String_01()
    {
        final String data = "abcde";
        final PropertyDescriptor pd = DGLBeanUtil.getPropertyDescriptorForBean(data, "class.name");
        assertEquals("java.lang.String", pd.getPropertyType().getName());
    }

    /**
     */
    @Test
    public void test_getPropertyDescriptorForBean_Class_String_02()
    {
        final String data = "abcde";
        final PropertyDescriptor pd = DGLBeanUtil.getPropertyDescriptorForBean(data, "bytes");
        assertEquals("[B", pd.getPropertyType().getName());
    }

    /**
     */
    public static class TestBean1
    {

        /**
         *  Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(TestBean1.class);

        /**
         * Current value of the <b>propTestBean2</b> property.
         */
        private TestBean2 propTestBean2;

        /**
         */
        public TestBean1()
        {
            super();
        }

        /**
         * Setter method for the <b>propTestBean2</b> property.
         */
        public void setPropTestBean2(
                final TestBean2 propTestBean2
        )
        {
            this.propTestBean2 = propTestBean2;
        }

        /**
         * Getter method for the <b>propTestBean2</b> property.
         */
        public TestBean2 getPropTestBean2()
        {
            return propTestBean2;
        }

    }

    /**
     */
    public static class TestBean2
    {

        /**
         *  Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(TestBean2.class);

        /**
         * Current value of the <b>propListString</b> property.
         */
        private List<String> propListString;

        /**
         * Current value of the <b>propListTestBean3</b> property.
         */
        private List<TestBean3> propListTestBean3;

        /**
         * Current value of the <b>propMapStringString</b> property.
         */
        private Map<String, String> propMapStringString;

        /**
         * Current value of the <b>propMapStringTestBean3</b> property.
         */
        private Map<String, TestBean3> propMapStringTestBean3;

        /**
         * Current value of the <b>propProperties</b> property.
         */
        private Properties propProperties;

        /**
         * Current value of the <b>propSetString</b> property.
         */
        private Set<String> propSetString;

        /**
         * Current value of the <b>propTestBean3</b> property.
         */
        private TestBean3 propTestBean3;

        /**
         */
        public TestBean2()
        {
            super();
        }

        /**
         * Setter method for the <b>propListString</b> property.
         */
        public void setPropListString(
                final List<String> propListString
        )
        {
            this.propListString = propListString;
        }

        /**
         * Getter method for the <b>propListString</b> property.
         */
        public List<String> getPropListString()
        {
            return propListString;
        }

        /**
         * Setter method for the <b>propListTestBean3</b> property.
         */
        public void setPropListTestBean3(
                final List<TestBean3> propListTestBean3
        )
        {
            this.propListTestBean3 = propListTestBean3;
        }

        /**
         * Getter method for the <b>propListTestBean3</b> property.
         */
        public List<TestBean3> getPropListTestBean3()
        {
            return propListTestBean3;
        }

        /**
         * Setter method for the <b>propMapStringString</b> property.
         */
        public void setPropMapStringString(
                final Map<String, String> propMapStringString
        )
        {
            this.propMapStringString = propMapStringString;
        }

        /**
         * Getter method for the <b>propMapStringString</b> property.
         */
        public Map<String, String> getPropMapStringString()
        {
            return propMapStringString;
        }

        /**
         * Setter method for the <b>propMapStringTestBean3</b> property.
         */
        public void setPropMapStringTestBean3(
                final Map<String, TestBean3> propMapStringTestBean3
        )
        {
            this.propMapStringTestBean3 = propMapStringTestBean3;
        }

        /**
         * Getter method for the <b>propMapStringTestBean3</b> property.
         */
        public Map<String, TestBean3> getPropMapStringTestBean3()
        {
            return propMapStringTestBean3;
        }

        /**
         * Setter method for the <b>propProperties</b> property.
         */
        public void setPropProperties(
                final Properties propProperties
        )
        {
            this.propProperties = propProperties;
        }

        /**
         * Getter method for the <b>propProperties</b> property.
         */
        public Properties getPropProperties()
        {
            return propProperties;
        }

        /**
         * Setter method for the <b>propSetString</b> property.
         */
        public void setPropSetString(
                final Set<String> propSetString
        )
        {
            this.propSetString = propSetString;
        }

        /**
         * Getter method for the <b>propSetString</b> property.
         */
        public Set<String> getPropSetString()
        {
            return propSetString;
        }

        /**
         * Setter method for the <b>propTestBean3</b> property.
         */
        public void setPropTestBean3(
                final TestBean3 propTestBean3
        )
        {
            this.propTestBean3 = propTestBean3;
        }

        /**
         * Getter method for the <b>propTestBean3</b> property.
         */
        public TestBean3 getPropTestBean3()
        {
            return propTestBean3;
        }

    }

    /**
     */
    public static class TestBean3
    {

        /**
         * Current value of the <b>prop1</b> property.
         */
        private String prop1;

        /**
         * Current value of the <b>prop2</b> property.
         */
        private int prop2;

        /**
         */
        public TestBean3()
        {
            super();
        }

        /**
         */
        public String toString()
        {
            return TestBean3.class.getName() + "{" +
                    TraceUtil.formatObj(prop1, "prop1") + ", " +
                    TraceUtil.formatObj(prop2, "prop2") + "}";
        }

        /**
         * Setter method for the <b>prop1</b> property.
         */
        public void setProp1(
                final String prop1
        )
        {
            this.prop1 = prop1;
        }

        /**
         * Getter method for the <b>prop1</b> property.
         */
        public String getProp1()
        {
            return prop1;
        }

        /**
         * Setter method for the <b>prop2</b> property.
         */
        public void setProp2(
                final int prop2
        )
        {
            this.prop2 = prop2;
        }

        /**
         * Getter method for the <b>prop2</b> property.
         */
        public int getProp2()
        {
            return prop2;
        }

    }

}
