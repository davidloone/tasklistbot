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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link DGLDateTimeUtil}.
 *
 * @author David G Loone
 */
public class DGLDateTimeUtil_Test
{

    /**
     *  Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLDateTimeUtil_Test.class);

    /**
     *  A date to work with.
     */
    private Date itsDate1;

    /**
     *  The date as a string.
     */
    private String itsDate1AsString;

    /**
     * The default timezone for this environment.
     */
    private String defaultTimezone;

    /**
     */
    public DGLDateTimeUtil_Test()
    {
        super();
    }

    /**
     */
    @Before
    public void setUp()
    {
        {
            final GregorianCalendar c = new GregorianCalendar();
            c.set(Calendar.YEAR, 2004);
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            c.set(Calendar.DAY_OF_MONTH, 23);
            c.set(Calendar.HOUR_OF_DAY, 6);
            c.set(Calendar.MINUTE, 17);
            c.set(Calendar.SECOND, 46);
            c.set(Calendar.MILLISECOND, 0);
            itsDate1 = c.getTime();
            itsDate1AsString = "2004-02-23T06:17:46.000";
        }

        {
            final Calendar c = new GregorianCalendar();
            final int tzMinsTotal = c.get(Calendar.ZONE_OFFSET) / 1000 / 60;
            final int tzMins = Math.abs(tzMinsTotal) % 60;
            final int tzHours = Math.abs(tzMinsTotal) / 60;
            defaultTimezone = ((tzMinsTotal < 0) ? '-' : '+') +
                    (new DecimalFormat("00")).format(tzHours) +
                    ((tzMins == 0) ? "" : (new DecimalFormat("00")).format(tzMins));
        }
    }

    /**
     */
    @Test
    public void test_formatAsISO8601_Date_00()
    {
        final String result = DGLDateTimeUtil.formatAsISO8601(itsDate1);
        assertEquals(itsDate1AsString, result);
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_00()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_01()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_02()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_03()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_04()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_05()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_06()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_07()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_08()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23");
        assertEquals(c, result);
        assertEquals("2004-02-23" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000" + defaultTimezone, DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_10()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_11()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_12()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_13()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_14()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_15()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_16()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_17()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46Z");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_18()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 0);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23Z");
        assertEquals(c, result);
        assertEquals("2004-02-23Z", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000Z", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_20()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_21()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_22()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_23()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_24()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_25()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_26()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_27()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46+05");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_28()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23+05");
        assertEquals(c, result);
        assertEquals("2004-02-23+05", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000+05", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_30()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_31()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_32()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_33()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_34()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_35()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_36()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_37()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46-10");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_38()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -10 * 60 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23-10");
        assertEquals(c, result);
        assertEquals("2004-02-23-10", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000-10", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_40()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_41()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_42()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_43()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_44()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_45()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_46()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_47()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_48()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, 5 * 60 * 60 * 1000 + 30 * 60 * 1000);
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23+05:30");
        assertEquals(c, result);
        assertEquals("2004-02-23+05:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000+05:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_50()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.023-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_51()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.23-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_52()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46.0-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_53()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_54()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.023-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_55()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 23);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.23-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46.23-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.023-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_56()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23 06:17:46.0-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_57()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 46);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23T06:17:46-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23 06:17:46-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T06:17:46.000-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601_String_58()
            throws Exception
    {
        final GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2004);
        c.set(Calendar.MONTH, Calendar.FEBRUARY);
        c.set(Calendar.DAY_OF_MONTH, 23);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.ZONE_OFFSET, -(10 * 60 * 60 * 1000 + 30 * 60 * 1000));
        final Calendar result = DGLDateTimeUtil.parseAsISO8601("2004-02-23-10:30");
        assertEquals(c, result);
        assertEquals("2004-02-23-10:30", DGLDateTimeUtil.formatAsISO8601Lenient(result));
        assertEquals("2004-02-23T00:00:00.000-10:30", DGLDateTimeUtil.formatAsISO8601(result));
    }

    /**
     */
    @Test
    public void test_parseAsISO8601Duration_String_00()
            throws Exception
    {
        final DGLDateTimeUtil.ISO8601Duration duration = DGLDateTimeUtil.ISO8601Duration.factory("P");
        assertEquals(0, duration.getYears());
        assertEquals(0, duration.getMonths());
        assertEquals(0, duration.getDays());
        assertEquals(0, duration.getHours());
        assertEquals(0, duration.getMinutes());
        assertEquals(0.0F, duration.getSeconds(), 0.00000001F);
    }

    /**
     */
    @Test
    public void test_parseAsISO8601Duration_String_01()
            throws Exception
    {
        final DGLDateTimeUtil.ISO8601Duration duration = DGLDateTimeUtil.ISO8601Duration.factory("PT3M");
        assertEquals(0, duration.getYears());
        assertEquals(0, duration.getMonths());
        assertEquals(0, duration.getDays());
        assertEquals(0, duration.getHours());
        assertEquals(3, duration.getMinutes());
        assertEquals(0.0F, duration.getSeconds(), 0.00000001F);
    }

    // TODO: More tests.

}