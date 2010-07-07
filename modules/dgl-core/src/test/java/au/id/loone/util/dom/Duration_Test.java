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

package au.id.loone.util.dom;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link au.id.loone.util.dom.Duration}.
 *
 * @author David G Loone
 */
public class Duration_Test
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(Duration_Test.class);

    /**
     */
    public Duration_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_00_00()
    {
        final Duration duration = new Duration();
        assertEquals(0, duration.getMillisecondsTotal());
        assertEquals("00:00", duration.formatAsHM());
        assertEquals("00:00:00", duration.formatAsHMS());
        assertEquals("00:00:00.000", duration.formatAsHMSS());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_00_01()
    {
        final Duration duration = new Duration("00:00");
        assertEquals(0, duration.getMillisecondsTotal());
        assertEquals("00:00", duration.formatAsHM());
        assertEquals("00:00:00", duration.formatAsHMS());
        assertEquals("00:00:00.000", duration.formatAsHMSS());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_00_02()
    {
        final Duration duration = new Duration("00:00:00");
        assertEquals(0, duration.getMillisecondsTotal());
        assertEquals("00:00", duration.formatAsHM());
        assertEquals("00:00:00", duration.formatAsHMS());
        assertEquals("00:00:00.000", duration.formatAsHMSS());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_00_03()
    {
        final Duration duration = new Duration("00:00:00.000");
        assertEquals(0, duration.getMillisecondsTotal());
        assertEquals("00:00", duration.formatAsHM());
        assertEquals("00:00:00", duration.formatAsHMS());
        assertEquals("00:00:00.000", duration.formatAsHMSS());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_00_04()
    {
        final Duration duration = new Duration("00:00:00.020");
        assertEquals(20, duration.getMillisecondsTotal());
        assertEquals("00:00", duration.formatAsHM());
        assertEquals("00:00:00", duration.formatAsHMS());
        assertEquals("00:00:00.020", duration.formatAsHMSS());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(20, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_00()
    {
        final Duration duration = new Duration("12:00");
        assertEquals(43200000, duration.getMillisecondsTotal());
        assertEquals("12:00", duration.formatAsHM());
        assertEquals("12:00:00", duration.formatAsHMS());
        assertEquals("12:00:00.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_01()
    {
        final Duration duration = new Duration("12:01");
        assertEquals(43260000, duration.getMillisecondsTotal());
        assertEquals("12:01", duration.formatAsHM());
        assertEquals("12:01:00", duration.formatAsHMS());
        assertEquals("12:01:00.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(1, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_02()
    {
        final Duration duration = new Duration("12:59");
        assertEquals(46740000, duration.getMillisecondsTotal());
        assertEquals("12:59", duration.formatAsHM());
        assertEquals("12:59:00", duration.formatAsHMS());
        assertEquals("12:59:00.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(59, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_03()
    {
        final Duration duration = new Duration("12:01:00");
        assertEquals(43260000, duration.getMillisecondsTotal());
        assertEquals("12:01", duration.formatAsHM());
        assertEquals("12:01:00", duration.formatAsHMS());
        assertEquals("12:01:00.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(1, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_04()
    {
        final Duration duration = new Duration("12:59:00");
        assertEquals(46740000, duration.getMillisecondsTotal());
        assertEquals("12:59", duration.formatAsHM());
        assertEquals("12:59:00", duration.formatAsHMS());
        assertEquals("12:59:00.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(59, duration.getMinutes());
        assertEquals(0, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_05()
    {
        final Duration duration = new Duration("12:00:01");
        assertEquals(43201000, duration.getMillisecondsTotal());
        assertEquals("12:00", duration.formatAsHM());
        assertEquals("12:00:01", duration.formatAsHMS());
        assertEquals("12:00:01.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(1, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_06()
    {
        final Duration duration = new Duration("12:00:59");
        assertEquals(43259000, duration.getMillisecondsTotal());
        assertEquals("12:00", duration.formatAsHM());
        assertEquals("12:00:59", duration.formatAsHMS());
        assertEquals("12:00:59.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(59, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_07()
    {
        final Duration duration = new Duration("12:01:01");
        assertEquals(43261000, duration.getMillisecondsTotal());
        assertEquals("12:01", duration.formatAsHM());
        assertEquals("12:01:01", duration.formatAsHMS());
        assertEquals("12:01:01.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(1, duration.getMinutes());
        assertEquals(1, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

    /**
     */
    @Test
    public void test_01_08()
    {
        final Duration duration = new Duration("12:01:59");
        assertEquals(43319000, duration.getMillisecondsTotal());
        assertEquals("12:01", duration.formatAsHM());
        assertEquals("12:01:59", duration.formatAsHMS());
        assertEquals("12:01:59.000", duration.formatAsHMSS());
        assertEquals(12, duration.getHours());
        assertEquals(1, duration.getMinutes());
        assertEquals(59, duration.getSeconds());
        assertEquals(0, duration.getMilliseconds());
    }

}