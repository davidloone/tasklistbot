/*
 * Copyright 2009, David G Loone
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

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import au.id.loone.util.tracing.TraceUtil;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test class for {@link HyperPropertyDescriptor}.
 *
 * @author David G Loone
 */
public final class HyperPropertyDescriptor_Test
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(HyperPropertyDescriptor_Test.class);

    /**
     */
    public HyperPropertyDescriptor_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_factory_Class_String_01()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, ".propTestBean2");
        LOG.debug("test_factory_Object_String_01: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factory_Object_String_01: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(1, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
    }

    /**
     */
    @Test
    public void test_factory_Class_String_02()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, ".propTestBean2.propListString");
        LOG.debug("test_factory_Object_String_02: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factory_Object_String_02: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(2, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(1).getClass());
        Assert.assertEquals("propListString", pds.get(1).getName());
    }

    /**
     */
    @Test
    public void test_factory_Class_String_03()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, ".propTestBean2.propListString[23]");
        LOG.debug("test_factory_Object_String_03: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factory_Object_String_03: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(3, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(1).getClass());
        Assert.assertEquals("propListString", pds.get(1).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForList.class, pds.get(2).getClass());
        Assert.assertEquals("23", pds.get(2).getName());
    }

    /**
     */
    @Test
    public void test_factoryWithPrefix_Class_String_00()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, "propTestBean2");
        LOG.debug("test_factoryWithPrefix_Object_String_00: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factoryWithPrefix_Object_String_00: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(1, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForNull.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
    }

    /**
     */
    @Test
    public void test_factoryWithPrefix_Class_String_01()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, "propTestBean2.propTestBean2");
        LOG.debug("test_factoryWithPrefix_Object_String_01: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factoryWithPrefix_Object_String_01: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(2, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForNull.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(1).getClass());
        Assert.assertEquals("propTestBean2", pds.get(1).getName());
    }

    /**
     */
    @Test
    public void test_factoryWithPrefix_Class_String_02()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, "propTestBean2.propTestBean2.propListString");
        LOG.debug("test_factoryWithPrefix_Object_String_02: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factoryWithPrefix_Object_String_02: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(3, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForNull.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(1).getClass());
        Assert.assertEquals("propTestBean2", pds.get(1).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(2).getClass());
        Assert.assertEquals("propListString", pds.get(2).getName());
    }

    /**
     */
    @Test
    public void test_factoryWithPrefix_Class_String_03()
            throws Exception
    {
        final List<HyperPropertyDescriptor> pds = HyperPropertyDescriptor.factory(TestBean1.class, "propTestBean2.propTestBean2.propListString[23]");
        LOG.debug("test_factoryWithPrefix_Object_String_03: " + TraceUtil.formatObj(pds, "pds.size"));
        for (int i = 0; i < pds.size(); i++) {
            LOG.debug("test_factoryWithPrefix_Object_String_03: " + TraceUtil.formatObj(pds.get(i), "pds[" + i + "]"));
        }
        Assert.assertEquals(4, pds.size());
        Assert.assertEquals(HyperPropertyDescriptor.ForNull.class, pds.get(0).getClass());
        Assert.assertEquals("propTestBean2", pds.get(0).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(1).getClass());
        Assert.assertEquals("propTestBean2", pds.get(1).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForBeanProperty.class, pds.get(2).getClass());
        Assert.assertEquals("propListString", pds.get(2).getName());
        Assert.assertEquals(HyperPropertyDescriptor.ForList.class, pds.get(3).getClass());
        Assert.assertEquals("23", pds.get(3).getName());
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
