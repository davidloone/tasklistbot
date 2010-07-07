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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;

/**
 *  A collection of static methods for performing date/time manipulations.
 *
 *  @author David G Loone
 */
public final class DGLDateTimeUtil
{

    /**
     * Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLDateTimeUtil.class);

    /**
     *  Prevent instantiation.
     */
    private DGLDateTimeUtil()
    {
        super();
    }

    private final static Pattern ISO8601_PATTERN =
            Pattern.compile("^(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)([T ](\\d\\d):(\\d\\d):(\\d\\d)([.](\\d\\d?\\d?))?)?([+-](\\d\\d)(:?(\\d\\d))?|Z)?$");

    /**
     *  The format string for an ISO8601 formatted date/time.
     */
    public final static String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**
     *  The format string for a user friendly ISO8601 formatted date/time.
     */
    public final static String ISO8601_USER_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     *  The time now.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static Date now()
    {
        return new Date();
    }

    /**
     *  Retrieve an ISO9660 formatter object.
     */
    public static DateFormat getISO8601Format()
    {
        return new SimpleDateFormat(ISO8601_FORMAT);
    }

    /**
     *  Retrieve an ISO9660 user formatter object.
     */
    public static DateFormat getISO8601UserFormat()
    {
        return new SimpleDateFormat(ISO8601_USER_FORMAT);
    }

    /**
     *  Format a date as ISO8601.
     */
    public static String formatAsISO8601(
            final Date d
    )
    {
        return (d == null) ? null : getISO8601Format().format(d);
    }

    /**
     *  Format a date as user friendly ISO8601.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String formatAsUserISO8601(
            final Date d
    )
    {
        return (d == null) ? null : getISO8601UserFormat().format(d);
    }

    /**
     *  Format a calendar as ISO8601.
     */
    public static String formatAsISO8601Lenient(
            final Calendar c
    )
    {
        final String result;

        if (c == null) {
            result = null;
        }
        else {
            final Format decFormat = new DecimalFormat("0");
            final Format dec2Format = new DecimalFormat("00");
            final Format dec4Format = new DecimalFormat("0000");

            final StringBuilder buf = new StringBuilder();
            buf.append(dec4Format.format(c.get(Calendar.YEAR)));
            buf.append('-');
            buf.append(dec2Format.format(c.get(Calendar.MONTH) + 1));
            buf.append('-');
            buf.append(dec2Format.format(c.get(Calendar.DAY_OF_MONTH)));
            if ((c.get(Calendar.HOUR_OF_DAY) != 0) ||
                    (c.get(Calendar.MINUTE) != 0) ||
                    (c.get(Calendar.SECOND) != 0) ||
                    (c.get(Calendar.MILLISECOND) != 0)) {
                buf.append(' ');
                buf.append(dec2Format.format(c.get(Calendar.HOUR_OF_DAY)));
                buf.append(':');
                buf.append(dec2Format.format(c.get(Calendar.MINUTE)));
                buf.append(':');
                buf.append(dec2Format.format(c.get(Calendar.SECOND)));
                if (c.get(Calendar.MILLISECOND) != 0) {
                    buf.append('.');
                    buf.append(decFormat.format(c.get(Calendar.MILLISECOND)));
                }
            }
            final int tz = c.get(Calendar.ZONE_OFFSET);
            if (tz == 0) {
                buf.append("Z");
            }
            else {
                final int tzMinsTotal = tz / 1000 / 60;
                final int tzHours = Math.abs(tzMinsTotal) / 60;
                final int tzMins = Math.abs(tzMinsTotal) % 60;
                buf.append((tzMinsTotal < 0) ? "-" : "+");
                buf.append(dec2Format.format(tzHours));
                if (tzMins != 0) {
                    buf.append(":");
                    buf.append(dec2Format.format(tzMins));
                }
            }
            result = buf.toString();
        }

        return result;
    }

    /**
     *  Format a calendar as ISO8601.
     */
    public static String formatAsISO8601(
            final Calendar c
    )
    {
        final String result;

        if (c == null) {
            result = null;
        }
        else {
            final Format dec2Format = new DecimalFormat("00");
            final Format dec3Format = new DecimalFormat("000");
            final Format dec4Format = new DecimalFormat("0000");

            final StringBuilder buf = new StringBuilder();
            buf.append(dec4Format.format(c.get(Calendar.YEAR)));
            buf.append('-');
            buf.append(dec2Format.format(c.get(Calendar.MONTH) + 1));
            buf.append('-');
            buf.append(dec2Format.format(c.get(Calendar.DAY_OF_MONTH)));
            buf.append('T');
            buf.append(dec2Format.format(c.get(Calendar.HOUR_OF_DAY)));
            buf.append(':');
            buf.append(dec2Format.format(c.get(Calendar.MINUTE)));
            buf.append(':');
            buf.append(dec2Format.format(c.get(Calendar.SECOND)));
            buf.append('.');
            buf.append(dec3Format.format(c.get(Calendar.MILLISECOND)));
            final int tz = c.get(Calendar.ZONE_OFFSET);
            if (tz == 0) {
                buf.append("Z");
            }
            else {
                final int tzMinsTotal = tz / 1000 / 60;
                final int tzHours = Math.abs(tzMinsTotal) / 60;
                final int tzMins = Math.abs(tzMinsTotal) % 60;
                buf.append((tzMinsTotal < 0) ? "-" : "+");
                buf.append(dec2Format.format(tzHours));
                if (tzMins != 0) {
                    buf.append(":");
                    buf.append(dec2Format.format(tzMins));
                }
            }
            result = buf.toString();
        }

        return result;
    }

    /**
     *  Parse a date as ISO8601.
     */
    public static Calendar parseAsISO8601(
            final String str
    )
            throws ParseException
    {
        // The value to return.
        final Calendar result = new GregorianCalendar();

        final Matcher m = ISO8601_PATTERN.matcher(str);
        if (m.matches()) {
            result.set(Calendar.YEAR, Integer.parseInt(m.group(1)));
            result.set(Calendar.MONTH, Integer.parseInt(m.group(2)) - 1);
            result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(m.group(3)));
            if (m.group(4) != null) {
                result.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(5)));
                result.set(Calendar.MINUTE, Integer.parseInt(m.group(6)));
                result.set(Calendar.SECOND, Integer.parseInt(m.group(7)));
                if (m.group(8) != null) {
                    result.set(Calendar.MILLISECOND, Integer.parseInt(m.group(9)));
                }
                else {
                    result.set(Calendar.MILLISECOND, 0);
                }
            }
            else {
                result.set(Calendar.HOUR_OF_DAY, 0);
                result.set(Calendar.MINUTE, 0);
                result.set(Calendar.SECOND, 0);
                result.set(Calendar.MILLISECOND, 0);
            }
            if (m.group(10) != null) {
                final String tzStr = m.group(10);
                if (DGLStringUtil.equals(tzStr, "Z")) {
                    result.set(Calendar.ZONE_OFFSET, 0);
                }
                else {
                    final String tzHourStr = m.group(11);
                    final String tzMinStr = m.group(13);
                    final int tzMins = Integer.parseInt(tzHourStr) * 60 +
                            ((tzMinStr == null) ? 0 : Integer.parseInt(tzMinStr));
                    final int tzMilliseconds = tzMins * 60 * 1000 *
                            (tzStr.startsWith("-") ? -1 : 1);
                    result.set(Calendar.ZONE_OFFSET, tzMilliseconds);
                }
            }
        }
        else {
            throw new ParseException(str, 0);
        }

        return result;
    }

    /**
     */
    public static class ISO8601Duration
    {

        /**
         * Logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private final static Logger LOG = TraceUtil.getLogger(ISO8601Duration.class);

        /**
         * Regular expression pattern for a duration.
         */
        private static final Pattern PATTERN =
                Pattern.compile("(-?)P((\\d*)Y)?((\\d*)M)?((\\d*)D)?(T((\\d*)H)?((\\d*)M)?((\\d*(\\.\\d*)?)S)?)?");

        /**
         * Current value of the <b>days</b> property.
         */
        private int days;

        /**
         * Current value of the <b>hours</b> property.
         */
        private int hours;

        /**
         * Current value of the <b>minutes</b> property.
         */
        private int minutes;

        /**
         * Current value of the <b>months</b> property.
         */
        private int months;

        /**
         * Current value of the <b>seconds</b> property.
         */
        private float seconds;

        /**
         * Current value of the <b>years</b> property.
         */
        private int years;

        /**
         */
        private ISO8601Duration()
        {
            super();
        }

        /**
         */
        public static ISO8601Duration factory(
                final String str
        )
                throws NumberFormatException
        {
            final ISO8601Duration result;

            final Matcher matcher = PATTERN.matcher(str);
            if (!matcher.matches()) {
                throw new NumberFormatException(str);
            }
            else {
                result = new ISO8601Duration();

                @SuppressWarnings({"UnusedDeclaration"})
                final boolean minus = !DGLStringUtil.isNullOrEmpty(matcher.group(1));
                // TODO

                final String yearsStr = matcher.group(3);
                result.years = DGLStringUtil.isNullOrEmpty(yearsStr) ? 0 : Integer.parseInt(yearsStr);

                final String monthsStr = matcher.group(5);
                result.months = DGLStringUtil.isNullOrEmpty(monthsStr) ? 0 : Integer.parseInt(monthsStr);

                final String daysStr = matcher.group(7);
                result.days = DGLStringUtil.isNullOrEmpty(daysStr) ? 0 : Integer.parseInt(daysStr);

                final String hoursStr = matcher.group(10);
                result.hours = DGLStringUtil.isNullOrEmpty(hoursStr) ? 0 : Integer.parseInt(hoursStr);

                final String minutesStr = matcher.group(12);
                result.minutes = DGLStringUtil.isNullOrEmpty(minutesStr) ? 0 : Integer.parseInt(minutesStr);

                final String secondsStr = matcher.group(14);
                result.seconds = DGLStringUtil.isNullOrEmpty(secondsStr) ? 0.0F : Float.parseFloat(secondsStr);
            }

            return result;
        }

        /**
         * Getter method for the <b>days</b> property.
         */
        public int getDays() {return days;}

        /**
         * Getter method for the <b>hours</b> property.
         */
        public int getHours() {return hours;}

        /**
         * Getter method for the <b>minutes</b> property.
         */
        public int getMinutes() {return minutes;}

        /**
         * Getter method for the <b>months</b> property.
         */
        public int getMonths() {return months;}

        /**
         * Getter method for the <b>seconds</b> property.
         */
        public float getSeconds() {return seconds;}

        /**
         * Getter method for the <b>years</b> property.
         */
        public int getYears() {return years;}

    }

}