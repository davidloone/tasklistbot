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

import au.id.loone.util.reflists.MeridiemRI;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.log4j.Logger;

/**
 * Test class for {@link TimeOfDay}.
 *
 * @author David G Loone
 */
public class TimeOfDay_Test
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(TimeOfDay_Test.class);

    /**
     */
    public TimeOfDay_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_00_00()
    {
        final TimeOfDay tod = new TimeOfDay();
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_00_01()
    {
        final TimeOfDay tod = new TimeOfDay("00:00");
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_00_02()
    {
        final TimeOfDay tod = new TimeOfDay("00:00:00");
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_00_03()
    {
        final TimeOfDay tod = new TimeOfDay("00:00:00.000");
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_00_04()
    {
        final TimeOfDay tod = new TimeOfDay("00:00:00.020");
        assertEquals(20, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.020", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.020", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(20, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_00()
    {
        final TimeOfDay tod = new TimeOfDay("12:00");
        assertEquals(43200000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:00", tod.formatAsHMS());
        assertEquals("12:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_01()
    {
        final TimeOfDay tod = new TimeOfDay("12:01");
        assertEquals(43260000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:00", tod.formatAsHMS());
        assertEquals("12:01:00.000", tod.formatAsHMSS());
        assertEquals("12:01:00", tod.formatAsHMS12());
        assertEquals("12:01:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_02()
    {
        final TimeOfDay tod = new TimeOfDay("12:59");
        assertEquals(46740000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:59", tod.formatAsHM());
        assertEquals("12:59", tod.formatAsHM12());
        assertEquals("12:59:00", tod.formatAsHMS());
        assertEquals("12:59:00.000", tod.formatAsHMSS());
        assertEquals("12:59:00", tod.formatAsHMS12());
        assertEquals("12:59:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(59, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_03()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:00");
        assertEquals(43260000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:00", tod.formatAsHMS());
        assertEquals("12:01:00.000", tod.formatAsHMSS());
        assertEquals("12:01:00", tod.formatAsHMS12());
        assertEquals("12:01:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_04()
    {
        final TimeOfDay tod = new TimeOfDay("12:59:00");
        assertEquals(46740000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:59", tod.formatAsHM());
        assertEquals("12:59", tod.formatAsHM12());
        assertEquals("12:59:00", tod.formatAsHMS());
        assertEquals("12:59:00.000", tod.formatAsHMSS());
        assertEquals("12:59:00", tod.formatAsHMS12());
        assertEquals("12:59:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(59, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_05()
    {
        final TimeOfDay tod = new TimeOfDay("12:00:01");
        assertEquals(43201000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:01", tod.formatAsHMS());
        assertEquals("12:00:01.000", tod.formatAsHMSS());
        assertEquals("12:00:01", tod.formatAsHMS12());
        assertEquals("12:00:01.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(1, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_06()
    {
        final TimeOfDay tod = new TimeOfDay("12:00:59");
        assertEquals(43259000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:59", tod.formatAsHMS());
        assertEquals("12:00:59.000", tod.formatAsHMSS());
        assertEquals("12:00:59", tod.formatAsHMS12());
        assertEquals("12:00:59.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(59, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_07()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:01");
        assertEquals(43261000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:01", tod.formatAsHMS());
        assertEquals("12:01:01.000", tod.formatAsHMSS());
        assertEquals("12:01:01", tod.formatAsHMS12());
        assertEquals("12:01:01.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(1, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_01_08()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:59");
        assertEquals(43319000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:59", tod.formatAsHMS());
        assertEquals("12:01:59.000", tod.formatAsHMSS());
        assertEquals("12:01:59", tod.formatAsHMS12());
        assertEquals("12:01:59.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(59, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_00()
    {
        final TimeOfDay tod = new TimeOfDay("12:00", MeridiemRI.AM);
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_01()
    {
        final TimeOfDay tod = new TimeOfDay("12:00:00", MeridiemRI.AM);
        assertEquals(0, tod.getMillisecondsSinceMidnight());
        assertEquals("00:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("00:00:00", tod.formatAsHMS());
        assertEquals("00:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(0, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.AM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_02()
    {
        final TimeOfDay tod = new TimeOfDay("12:00", MeridiemRI.PM);
        assertEquals(43200000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:00", tod.formatAsHMS());
        assertEquals("12:00:00.000", tod.formatAsHMSS());
        assertEquals("12:00:00", tod.formatAsHMS12());
        assertEquals("12:00:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_03()
    {
        final TimeOfDay tod = new TimeOfDay("12:01", MeridiemRI.PM);
        assertEquals(43260000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:00", tod.formatAsHMS());
        assertEquals("12:01:00.000", tod.formatAsHMSS());
        assertEquals("12:01:00", tod.formatAsHMS12());
        assertEquals("12:01:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_04()
    {
        final TimeOfDay tod = new TimeOfDay("12:59", MeridiemRI.PM);
        assertEquals(46740000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:59", tod.formatAsHM());
        assertEquals("12:59", tod.formatAsHM12());
        assertEquals("12:59:00", tod.formatAsHMS());
        assertEquals("12:59:00.000", tod.formatAsHMSS());
        assertEquals("12:59:00", tod.formatAsHMS12());
        assertEquals("12:59:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(59, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_05()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:00", MeridiemRI.PM);
        assertEquals(43260000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:00", tod.formatAsHMS());
        assertEquals("12:01:00.000", tod.formatAsHMSS());
        assertEquals("12:01:00", tod.formatAsHMS12());
        assertEquals("12:01:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_06()
    {
        final TimeOfDay tod = new TimeOfDay("12:59:00", MeridiemRI.PM);
        assertEquals(46740000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:59", tod.formatAsHM());
        assertEquals("12:59", tod.formatAsHM12());
        assertEquals("12:59:00", tod.formatAsHMS());
        assertEquals("12:59:00.000", tod.formatAsHMSS());
        assertEquals("12:59:00", tod.formatAsHMS12());
        assertEquals("12:59:00.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(59, tod.getMinutes());
        assertEquals(0, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_07()
    {
        final TimeOfDay tod = new TimeOfDay("12:00:01", MeridiemRI.PM);
        assertEquals(43201000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:01", tod.formatAsHMS());
        assertEquals("12:00:01.000", tod.formatAsHMSS());
        assertEquals("12:00:01", tod.formatAsHMS12());
        assertEquals("12:00:01.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(1, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_08()
    {
        final TimeOfDay tod = new TimeOfDay("12:00:59", MeridiemRI.PM);
        assertEquals(43259000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:00", tod.formatAsHM());
        assertEquals("12:00", tod.formatAsHM12());
        assertEquals("12:00:59", tod.formatAsHMS());
        assertEquals("12:00:59.000", tod.formatAsHMSS());
        assertEquals("12:00:59", tod.formatAsHMS12());
        assertEquals("12:00:59.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(0, tod.getMinutes());
        assertEquals(59, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_09()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:01", MeridiemRI.PM);
        assertEquals(43261000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:01", tod.formatAsHMS());
        assertEquals("12:01:01.000", tod.formatAsHMSS());
        assertEquals("12:01:01", tod.formatAsHMS12());
        assertEquals("12:01:01.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(1, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

    /**
     */
    @Test
    public void test_02_10()
    {
        final TimeOfDay tod = new TimeOfDay("12:01:59", MeridiemRI.PM);
        assertEquals(43319000, tod.getMillisecondsSinceMidnight());
        assertEquals("12:01", tod.formatAsHM());
        assertEquals("12:01", tod.formatAsHM12());
        assertEquals("12:01:59", tod.formatAsHMS());
        assertEquals("12:01:59.000", tod.formatAsHMSS());
        assertEquals("12:01:59", tod.formatAsHMS12());
        assertEquals("12:01:59.000", tod.formatAsHMSS12());
        assertEquals(12, tod.getHours());
        assertEquals(1, tod.getMinutes());
        assertEquals(59, tod.getSeconds());
        assertEquals(0, tod.getMilliseconds());
        assertEquals(MeridiemRI.PM, tod.getMeridiem());
    }

}
