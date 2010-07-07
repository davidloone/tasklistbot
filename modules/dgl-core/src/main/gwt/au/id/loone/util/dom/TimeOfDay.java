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

import java.io.Serializable;
import java.text.DecimalFormat;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.reflists.MeridiemRI;
import au.id.loone.util.tracing.TraceUtil;

/**
 * A time-of-day specification to an accuracy of 1 second.
 *
 * <h3>Treatment of the Meridiem at Midnight and Noon in 12 Hour Times</h3>
 *
 * <p>There is some <a href="http://en.wikipedia.org/wiki/12-hour_clock">confusion</a>
 *      about treatment of meridiem values in 12 hour time specifications.
 *      This class takes the simplest approach of treating 12 hours as zero.
 *      For example,
 *      12:00AM is midnight,
 *      12:01AM is one minute past midnight,
 *      12:00PM is noon,
 *      etc.</p>
 *
 * <p>My advice:
 *      use 24-hour time formats.</p>
 *
 * @author David G Loone
 */
public class TimeOfDay
        implements Comparable, Serializable
{

    /**
     * The number of seconds in a minute.
     */
    public static final int SECONDS_PER_MINUTE = 60;

    /**
     * The number of minutes in an hour.
     */
    public static final int MINUTES_PER_HOUR = 60;

    /**
     * The number of seconds in an hour.
     */
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    /**
     * The number of hours in a day.
     */
    public static final int HOURS_PER_DAY = 24;

    /**
     * The number of minutes in a day.
     */
    public static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;

    /**
     * The number of seconds in a day.
     */
    public static final int SECONDS_PER_DAY = MINUTES_PER_DAY * SECONDS_PER_MINUTE;

    /**
     * The number of seconds in half a day.
     */
    public static final int SECONDS_PER_HALF_DAY = SECONDS_PER_DAY / 2;

    /**
     * The number of minutes in half a day.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final int MINUTES_PER_HALF_DAY = MINUTES_PER_DAY / 2;

    /**
     * The number of hours in half a day.
     */
    public static final int HOURS_PER_HALF_DAY = HOURS_PER_DAY / 2;

    /**
     * The internal representation is a number of milliseconds since midnight.
     */
    private int millisecondsSinceMidnight;

    /**
     */
    public TimeOfDay()
    {
        super();
    }

    /**
     * @param millisecondsSinceMidnight
     *      The number of milliseconds since midnight for the time contained herein.
     */
    public TimeOfDay(
            final int millisecondsSinceMidnight
    )
    {
        super();

        this.millisecondsSinceMidnight = millisecondsSinceMidnight;
    }

    /**
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public TimeOfDay(
            final int hours,
            final int minutes
    )
            throws IllegalArgumentException
    {
        super();

        init(hours, minutes, 0, 0);
    }

    /**
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public TimeOfDay(
            final int hours,
            final int minutes,
            final int seconds
    )
            throws IllegalArgumentException
    {
        super();

        init(hours, minutes, seconds, 0);
    }

    /**
     * Create a value from a 24 hour time string.
     *
     * @param str
     *      The time of day in 24 hour format
     *      (<i>ie</i>, "<code>HH:MM</code>").
     * @throws NumberFormatException
     *      Thrown if the value of <code>str</code> is not a valid time-of-day.
     * @throws IllegalArgumentException
     *      Thrown if <code>str</code> is <code>null</code>.
     */
    @SuppressWarnings({"DuplicateThrows", "NonJREEmulationClassesInClientCode"})
    public TimeOfDay(
            final String str
    )
            throws NumberFormatException, IllegalArgumentException
    {
        super();

        if (DGLStringUtil.isNullOrEmpty(str)) {
            throw new IllegalArgumentException();
        }

        try {
            final String[] segments = DGLStringUtil.split(str, ':');
            if ((segments.length == 2) || (segments.length == 3)) {
                if (segments[0].length() != 2) {
                    throw new NumberFormatException();
                }
                final int hours = Integer.parseInt(segments[0]);

                if (segments[1].length() != 2) {
                    throw new NumberFormatException();
                }
                final int minutes = Integer.parseInt(segments[1]);
                if (minutes >= 60) {
                    throw new NumberFormatException();
                }

                final int seconds;
                final int milliseconds;
                if (segments.length == 3) {
                    final String[] secondsSegments = DGLStringUtil.split(segments[2], '.');
                    if (secondsSegments.length == 1) {
                        if (secondsSegments[0].length() != 2) {
                            throw new NumberFormatException();
                        }
                        seconds = Integer.parseInt(secondsSegments[0]);
                        if (seconds >= 60) {
                            throw new NumberFormatException();
                        }

                        milliseconds = 0;
                    }
                    else if (secondsSegments.length == 2) {
                        if (secondsSegments[0].length() != 2) {
                            throw new NumberFormatException();
                        }
                        seconds = Integer.parseInt(secondsSegments[0]);
                        if (seconds >= 60) {
                            throw new NumberFormatException();
                        }

                        if (secondsSegments[1].length() > 3) {
                            throw new NumberFormatException();
                        }
                        milliseconds = Integer.parseInt(secondsSegments[1]);
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                else {
                    seconds = 0;
                    milliseconds = 0;
                }
                try {
                    init(hours, minutes, seconds, milliseconds);
                }
                catch (final IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
                }
            }
            else {
                throw new NumberFormatException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
            }
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
        }
    }

    /**
     */
    private void init(
            final int hours,
            final int minutes,
            final int seconds,
            final int milliseconds
    )
            throws IllegalArgumentException
    {
        if ((hours < 0) ||
                (hours > 23) ||
                (minutes < 0) ||
                (minutes > 59) ||
                (seconds < 0) ||
                (seconds > 59) ||
                (milliseconds < 0) ||
                (milliseconds > 999)) {
            throw new IllegalArgumentException();
        }

        millisecondsSinceMidnight = (seconds +
                minutes * SECONDS_PER_MINUTE +
                hours * SECONDS_PER_HOUR) * 1000 +
                milliseconds;
    }

    /**
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public TimeOfDay(
            final int hours,
            final int minutes,
            final int seconds,
            final MeridiemRI meridiem
    )
            throws IllegalArgumentException
    {
        super();

        init(hours, minutes, seconds, 0, meridiem);
    }

    /**
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public TimeOfDay(
            final int hours,
            final int minutes,
            final MeridiemRI meridiem
    )
            throws IllegalArgumentException
    {
        super();

        init(hours, minutes, 0, 0, meridiem);
    }

    /**
     * Create a value from a 12 hour time string.
     *
     * @param str
     *      The time of day in 12 hour format
     *      (<i>ie</i>, "<code>HH:MM</code>").
     * @param meridiem
     *      Indicates whether the time is AM or PM.
     * @throws NumberFormatException
     *      Thrown if the value of <code>str</code> is not a valid time-of-day.
     * @throws IllegalArgumentException
     *      Thrown if <code>str</code> or <code>meridiem</code> is <code>null</code>.
     */
    @SuppressWarnings({"DuplicateThrows", "NonJREEmulationClassesInClientCode"})
    public TimeOfDay(
            final String str,
            final MeridiemRI meridiem
    )
            throws NumberFormatException, IllegalArgumentException
    {
        super();

        if (DGLStringUtil.isNullOrEmpty(str)) {
            throw new IllegalArgumentException();
        }
        if (meridiem == null) {
            throw new IllegalArgumentException();
        }

        try {
            final String[] segments = DGLStringUtil.split(str, ':');
            if ((segments.length == 2) || (segments.length == 3)) {
                if (segments[0].length() != 2) {
                    throw new NumberFormatException();
                }
                final int hours = Integer.parseInt(segments[0]);

                if (segments[1].length() != 2) {
                    throw new NumberFormatException();
                }
                final int minutes = Integer.parseInt(segments[1]);
                if (minutes >= 60) {
                    throw new NumberFormatException();
                }

                final int seconds;
                final int milliseconds;
                if (segments.length == 3) {
                    final String[] secondsSegments = DGLStringUtil.split(segments[2], '.');
                    if (secondsSegments.length == 1) {
                        if (secondsSegments[0].length() != 2) {
                            throw new NumberFormatException();
                        }
                        seconds = Integer.parseInt(secondsSegments[0]);
                        if (seconds >= 60) {
                            throw new NumberFormatException();
                        }

                        milliseconds = 0;
                    }
                    else if (secondsSegments.length == 2) {
                        if (secondsSegments[0].length() != 2) {
                            throw new NumberFormatException();
                        }
                        seconds = Integer.parseInt(secondsSegments[0]);
                        if (seconds >= 60) {
                            throw new NumberFormatException();
                        }

                        if (secondsSegments[1].length() > 3) {
                            throw new NumberFormatException();
                        }
                        milliseconds = Integer.parseInt(secondsSegments[1]);
                    }
                    else {
                        throw new NumberFormatException();
                    }
                }
                else {
                    seconds = 0;
                    milliseconds = 0;
                }
                try {
                    init(hours, minutes, seconds, milliseconds, meridiem);
                }
                catch (final IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
                }
            }
            else {
                throw new NumberFormatException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
            }
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Invalid time-of-day format: " + TraceUtil.formatObj(str));
        }
    }

    /**
     */
    private void init(
            final int hours,
            final int minutes,
            final int seconds,
            final int milliseconds,
            final MeridiemRI meridiem
    )
    {
        if ((hours < 1) ||
                (hours > 12) ||
                (minutes < 0) ||
                (minutes > 59) ||
                (seconds < 0) ||
                (seconds > 59) ||
                (milliseconds < 0) ||
                (milliseconds > 999)) {
            throw new IllegalArgumentException();
        }

        // Just treat 12 hours as zero.
        millisecondsSinceMidnight = (seconds +
                minutes * SECONDS_PER_MINUTE +
                ((hours == 12) ? 0 : hours) * SECONDS_PER_HOUR +
                (DGLUtil.equals(meridiem, MeridiemRI.PM) ? SECONDS_PER_HALF_DAY : 0)) * 1000 +
                milliseconds;
    }

    /**
     */
    public int hashCode()
    {
        return millisecondsSinceMidnight;
    }

    /**
     */
    public boolean equals(
            final Object otherObj
    )
    {
        final boolean result;

        if (otherObj == null) {
            result = false;
        }
        else if (otherObj == this) {
            result = true;
        }
        else if (otherObj instanceof TimeOfDay) {
            final TimeOfDay other = (TimeOfDay)otherObj;
            result = (millisecondsSinceMidnight == other.millisecondsSinceMidnight);
        }
        else {
            result = false;
        }

        return result;
    }

    /**
     */
    public int compareTo(
            final Object otherObj
    )
    {
        return DGLUtil.compare(millisecondsSinceMidnight, ((TimeOfDay)otherObj).millisecondsSinceMidnight);
    }

    /**
     * @see #formatAsHMS()
     */
    public String toString()
    {
        return formatAsHMS();
    }

    /**
     * Format the time as a 24 hour string like "<code>HH:MM</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHM()
    {
        final int hours = getHours();
        final int minutes = getMinutes();
        final DecimalFormat f = new DecimalFormat("00");
        return f.format(hours) + ":" + f.format(minutes);
    }

    /**
     * Format the time as a 12 hour string like "<code>HH:MM</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHM12()
    {
        final int hours = getHours();
        final int minutes = getMinutes();
        final DecimalFormat f = new DecimalFormat("00");
        return f.format((hours == 0) ? HOURS_PER_HALF_DAY : hours) + ":" + f.format(minutes);
    }

    /**
     * Format the time as a 24 hour string like "<code>HH:MM:SS</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHMS()
    {
        final int hours = getHours();
        final int minutes = getMinutes();
        final int seconds = getSeconds();
        final DecimalFormat f = new DecimalFormat("00");
        return f.format(hours) + ":" + f.format(minutes) + ":" + f.format(seconds);
    }

    /**
     * Format the time as a 24 hour string like "<code>HH:MM:SS.SSS</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHMSS()
    {
        final int hours = getHours();
        final int minutes = getMinutes();
        final int seconds = getSeconds();
        final int milliseconds = getMilliseconds();
        final DecimalFormat f1 = new DecimalFormat("00");
        final DecimalFormat f2 = new DecimalFormat("000");
        return f1.format(hours) + ":" + f1.format(minutes) + ":" + f1.format(seconds) + "." + f2.format(milliseconds);
    }

    /**
     * Format the time as a 12 hour string like "<code>HH:MM:SS</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHMS12()
    {
        final int hours = getHours() % HOURS_PER_HALF_DAY;
        final int minutes = getMinutes();
        final int seconds = getSeconds();
        final DecimalFormat f = new DecimalFormat("00");
        return f.format((hours == 0) ? HOURS_PER_HALF_DAY : hours) + ":" + f.format(minutes) + ":" + f.format(seconds);
    }

    /**
     * Format the time as a 12 hour string like "<code>HH:MM:SS.SSS</code>".
     */
    @SuppressWarnings({"NonJREEmulationClassesInClientCode"})
    public String formatAsHMSS12()
    {
        final int hours = getHours() % HOURS_PER_HALF_DAY;
        final int minutes = getMinutes();
        final int seconds = getSeconds();
        final int milliseconds = getMilliseconds();
        final DecimalFormat f1 = new DecimalFormat("00");
        final DecimalFormat f2 = new DecimalFormat("000");
        return f1.format((hours == 0) ? HOURS_PER_HALF_DAY : hours) + ":" + f1.format(minutes) + ":" +
                f1.format(seconds) + "." + f2.format(milliseconds);
    }

    /**
     * Add a number of seconds to this value.
     *
     * @param duration
     *      The duration to add.
     * @return
     *      A new time-of-day object that is the time represented by this object,
     *      plus <code>duration</code>.
     *      The returned value is unnormalised.
     */
    public TimeOfDay add(
            final Duration duration
    )
    {
        return new TimeOfDay(millisecondsSinceMidnight + duration.getMilliseconds());
    }

    /**
     * Normalise a time-of-day.
     *
     * @return
     *      A new, normalised, time-of-day object.
     *      The returned value is the same as the current object,
     *      modulo 24 hours.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public TimeOfDay normalise()
    {
        return new TimeOfDay(millisecondsSinceMidnight % (24 * 60 * 60 * 1000));
    }

    /**
     * Getter method for the <b>hours</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getHours()
    {
        return (millisecondsSinceMidnight / (1000 * SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
    }

    /**
     * Getter method for the <b>Meridiem</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public MeridiemRI getMeridiem()
    {
        return (millisecondsSinceMidnight >= (SECONDS_PER_HALF_DAY * 1000)) ? MeridiemRI.PM : MeridiemRI.AM;
    }

    /**
     * Getter method for the <b>milliseconds</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMilliseconds()
    {
        return (millisecondsSinceMidnight % 1000);
    }

    /**
     * Getter method for the <b>millisecondsSinceMidnight</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMillisecondsSinceMidnight()
    {
        return millisecondsSinceMidnight;
    }

    /**
     * Getter method for the <b>minutes</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMinutes()
    {
        return ((millisecondsSinceMidnight / (1000 * SECONDS_PER_MINUTE)) % MINUTES_PER_HOUR);
    }

    /**
     * Getter for the <b>seconds</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getSeconds()
    {
        return ((millisecondsSinceMidnight / 1000) % SECONDS_PER_MINUTE);
    }

}
