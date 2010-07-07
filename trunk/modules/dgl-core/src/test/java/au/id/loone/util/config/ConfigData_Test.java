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

package au.id.loone.util.config;

import au.id.loone.util.tracing.TraceUtil;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *  Test clas for {@link ConfigData}.
 *
 *  @author David G Loone
 */
public class ConfigData_Test
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ConfigData_Test.class);

    /**
     */
    public ConfigData_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_00()
    {
        final ConfigData1 configData = new ConfigData1();
        assertEquals("x", configData.getA());
        assertEquals("y", configData.getB());
    }

    /**
     */
    @Test
    public void test_01()
    {
        final ConfigData2 configData = new ConfigData2();
        assertEquals("x", configData.getA());
        assertEquals("y", configData.getB());
        assertEquals("z", configData.getC());
        assertEquals("t", configData.getD());
    }

    /**
     */
    public static class ConfigData1
            extends ConfigData
    {

        /**
         *  Current value of the <b>a</b> property.
         */
        private String a;

        /**
         *  Current value of the <b>b</b> property.
         */
        private String b;

        /**
         */
        public ConfigData1()
        {
            super();
        }

        /**
         *  Setter method for the <b>a</b> property.
         */
        public void setA(
                final String a
        )
        {
            this.a = a;
        }

        /**
         *  Getter method for the <b>a</b> property.
         */
        public String getA()
        {
            return a;
        }

        /**
         *  Setter method for the <b>b</b> property.
         */
        public void setB(
                final String b
        )
        {
            this.b = b;
        }

        /**
         *  Getter method for the <b>b</b> property.
         */
        public String getB()
        {
            return b;
        }

    }

    /**
     */
    public static class ConfigData2
            extends ConfigData1
    {

        /**
         *  Current value of the <b>c</b> property.
         */
        private String c;

        /**
         *  Current value of the <b>d</b> property.
         */
        private String d;

        /**
         */
        public ConfigData2()
        {
            super();
        }

        /**
         *  Setter method for the <b>c</b> property.
         */
        public void setC(
                final String c
        )
        {
            this.c = c;
        }

        /**
         *  Getter method for the <b>c</b> property.
         */
        public String getC()
        {
            return c;
        }

        /**
         *  Setter method for the <b>d</b> property.
         */
        public void setD(
                final String d
        )
        {
            this.d = d;
        }

        /**
         *  Getter method for the <b>d</b> property.
         */
        public String getD()
        {
            return d;
        }

    }

}
