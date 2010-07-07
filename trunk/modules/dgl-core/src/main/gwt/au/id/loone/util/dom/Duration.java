package au.id.loone.util.dom;

import java.io.Serializable;
import java.text.DecimalFormat;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

/**
 * Represents a time duration.
 *
 * @author David G Loone
 */
public final class Duration
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
     * The internal representation is a number of milliseconds.
     */
    private int milliseconds;

    /**
     */
    public Duration()
    {
        super();
    }

    /**
     * @param milliseconds
     *      The number of milliseconds.
     */
    public Duration(
            final int milliseconds
    )
    {
        super();

        this.milliseconds = milliseconds;
    }

    /**
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public Duration(
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
    public Duration(
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
     * @throws IllegalArgumentException
     *      Thrown if any of the arguments is out of range.
     */
    public Duration(
            final int hours,
            final int minutes,
            final int seconds,
            final int milliseconds
    )
            throws IllegalArgumentException
    {
        super();

        init(hours, minutes, seconds, milliseconds);
    }

    /**
     * Create a value from a 24 hour time string.
     *
     * @param str
     *      The duration
     *      (<i>ie</i>, "<code>HH:MM</code>").
     * @throws NumberFormatException
     *      Thrown if the value of <code>str</code> is not a valid duration.
     * @throws IllegalArgumentException
     *      Thrown if <code>str</code> is <code>null</code>.
     */
    @SuppressWarnings({"DuplicateThrows", "NonJREEmulationClassesInClientCode"})
    public Duration(
            String str
    )
            throws NumberFormatException, IllegalArgumentException
    {
        super();

        if (DGLStringUtil.isNullOrEmpty(str)) {
            throw new IllegalArgumentException();
        }

        final char sign;
        if ((str.charAt(0) == '+') || (str.charAt(0) == '-')) {
            sign = str.charAt(0);
            // Remove it.
            str = str.substring(1);
        }
        else {
            sign = '+';
        }

        try {
            final String[] segments = DGLStringUtil.split(str, ':');
            if ((segments.length == 2) || (segments.length == 3)) {
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
                    init(sign, hours, minutes, seconds, milliseconds);
                }
                catch (final IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid duration format: " + TraceUtil.formatObj(str));
                }
            }
            else {
                throw new NumberFormatException("Invalid duration format: " + TraceUtil.formatObj(str));
            }
        }
        catch (final NumberFormatException e) {
            throw new NumberFormatException("Invalid duration format: " + TraceUtil.formatObj(str));
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
        init('+', hours, minutes, seconds, milliseconds);
    }

    /**
     */
    private void init(
            final char sign,
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

        this.milliseconds = ((seconds +
                minutes * SECONDS_PER_MINUTE +
                hours * SECONDS_PER_HOUR) * 1000 +
                milliseconds) * (DGLStringUtil.equals(sign, "-") ? -1 : 1);
    }

    /**
     */
    public int hashCode()
    {
        return milliseconds;
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
        else if (otherObj instanceof Duration) {
            final Duration other = (Duration)otherObj;
            result = (milliseconds == other.milliseconds);
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
        return DGLUtil.compare(milliseconds, ((Duration)otherObj).milliseconds);
    }

    /**
     * @see #formatAsHMS()
     */
    public String toString()
    {
        return formatAsHMSS();
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
     * Format the time as a string like "<code>HH:MM:SS</code>".
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
     * Format the time as a string like "<code>HH:MM:SS.SSS</code>".
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
     * Add a duration to this value.
     *
     * @param duration
     *      The number of seconds to add.
     * @return
     *      A new duration object that is the duration represented by this object,
     *      plus <code>duration</code>.
     */
    public Duration add(
            final Duration duration
    )
    {
        return new Duration(milliseconds + duration.milliseconds);
    }

    /**
     * Subtract a duration from this value.
     *
     * @param duration
     *      The number of seconds to subtract.
     * @return
     *      A new duration object that is the duration represented by this object,
     *      less <code>duration</code>.
     */
    public Duration subtract(
            final Duration duration
    )
    {
        return new Duration(milliseconds - duration.milliseconds);
    }

    /**
     * Multiply this value by a multiple.
     *
     * @param  multiple
     *      The value to multiply this duration by.
     * @return
     *      A new duration being this value multipled by <code>multiple</code>.
     */
    public Duration multiply(
            final int multiple
    )
    {
        return new Duration(milliseconds * multiple);
    }

    /**
     * Getter method for the <b>hours</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getHours()
    {
        return (milliseconds / (1000 * SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
    }

    /**
     * Getter method for the <b>milliseconds</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMilliseconds()
    {
        return (milliseconds % 1000);
    }

    /**
     * Getter method for the <b>millisecondsTotal</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMillisecondsTotal()
    {
        return milliseconds;
    }

    /**
     * Getter method for the <b>minutes</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getMinutes()
    {
        return ((milliseconds / (1000 * SECONDS_PER_MINUTE)) % MINUTES_PER_HOUR);
    }

    /**
     * Getter for the <b>seconds</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getSeconds()
    {
        return ((milliseconds / 1000) % SECONDS_PER_MINUTE);
    }

}