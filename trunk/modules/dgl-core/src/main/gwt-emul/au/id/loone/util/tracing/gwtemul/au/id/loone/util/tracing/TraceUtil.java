/*
 * Copyright (c) 2009-2010 Voiamo PLC. All rights reserved.
 */

package au.id.loone.util.tracing;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Trace logging utilities.
 *
 * <p>Based the original (TraceUtil) by David G Loone.</p>
 *
 * @author David G Loone
 */
public final class TraceUtil
{

    /**
     *  Maximum length of a trace string.
     */
    public static int MAX_TRACE_LEN = 2048;

    /**
     */
    public TraceUtil()
    {
        super();
    }

    /**
     *  Format an object for trace logging.
     *
     *  @param obj
     *      The object to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     *      Normally, this will be the <code>toString</code> representation of the object,
     *      but if <code>obj</code> is a string (and non-null),
     *      it will be enclosed in quotes.
     */
    @SuppressWarnings({"ConstantConditions"})
    public static String formatObj(
            final Object obj
    )
    {
        // The value to return.
        final String result;

        if (obj == null) {
            result = null;
        }
        else if (obj instanceof Byte) {
            // A byte object.
            final byte b = (Byte)obj;
            if ((b >= 32) && (b <= 126)) {
                result = obj.toString() + "'" + (char)b + "'";
            }
            else {
                result = obj.toString();
            }
        }
        else if (obj instanceof Character) {
            // A single character.
            result = "'" + obj + "'";
        }
        else if (obj instanceof String) {
            // A string. Format it between quotes.
            result = "\"" + obj + "\"";
        }
        else if (obj instanceof StringBuffer) {
            // A string. Format it between quotes.
            result = "\"" + obj + "\"";
        }
        else if (obj instanceof StringBuilder) {
            // A string. Format it between quotes.
            result = "\"" + obj + "\"";
        }
        else if (obj instanceof Date) {
            // A date. Format as ISO8601.
            result = String.valueOf(obj);
        }
        else if (obj.getClass().isArray()) {
            // An array. Format each element as a string separately.
            final Object[] objArray = (Object[])obj;
            final StringBuilder resultBuf = new StringBuilder();
            resultBuf.append("[");
            for (int i = 0; i < objArray.length; i++) {
                resultBuf.append(formatObj(objArray[i]));
                if (i != (objArray.length - 1)) {
                    resultBuf.append(", ");
                }
            }
            resultBuf.append("]");
            result = resultBuf.toString();
        }
        else if (obj instanceof Collection) {
            // A collection. Format each element as a string separately.
            final StringBuilder resultBuf = new StringBuilder();
            boolean first = true;
            resultBuf.append("{");
            for (final Object element : ((Collection)obj)) {
                if (first) {
                    first = false;
                }
                else {
                    resultBuf.append(", ");
                }
                resultBuf.append(formatObj(element));
            }
            resultBuf.append("}");
            result = resultBuf.toString();
        }
        else if (obj instanceof Map) {
            // A map. Format each key/value pair separately.
            final StringBuilder resultBuf = new StringBuilder();
            boolean first = true;
            resultBuf.append("{");
            for (final Object key : ((Map)obj).keySet()) {
                if (first) {
                    first = false;
                }
                else {
                    resultBuf.append(", ");
                }
                resultBuf.append(formatObj(key));
                resultBuf.append(" = ");
                resultBuf.append(formatObj(((Map)obj).get(key)));
            }
            resultBuf.append("}");
            result = resultBuf.toString();
        }
        else if (obj instanceof Throwable) {
            result = obj.getClass().getName() + ":" + TraceUtil.formatObj(((Throwable)obj).getMessage());
        }
        else {
            // Otherwise, just use {@link Object#toString()}.
            result = String.valueOf(obj);
        }

        return (result == null) ? null :
                ((result.length() > (MAX_TRACE_LEN - 4)) ? (result.substring(0, (MAX_TRACE_LEN - 4)) + " ...") : result);
    }

    /**
     *  Format a boolean for trace logging.
     *
     *  @param obj
     *      The boolean to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
           final boolean obj
    )
    {
        return String.valueOf(obj);
    }

    /**
     *  Format a char for trace logging.
     *
     *  @param obj
     *      The char to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final char obj
    )
    {
        return formatObj(new Character(obj));
    }

    /**
     *  Format an integer for trace logging.
     *
     *  @param obj
     *      The integer to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final byte obj
    )
    {
        return formatObj(new Byte(obj));
    }

    /**
     *  Format a char for trace logging.
     *
     *  @param obj
     *      The char to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final short obj
    )
    {
        return formatObj(new Short(obj));
    }

    /**
     *  Format an integer for trace logging.
     *
     *  @param obj
     *      The integer to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final int obj
    )
    {
        return TraceUtil.formatObj(new Integer(obj));
    }

    /**
     *  Format a long for trace logging.
     *
     *  @param obj
     *      The long to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final long obj
    )
    {
        return formatObj(new Long(obj));
    }

    /**
     *  Format a char for trace logging.
     *
     *  @param obj
     *      The char to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final float obj
    )
    {
        return formatObj(new Float(obj));
    }

    /**
     *  Format a char for trace logging.
     *
     *  @param obj
     *      The char to be formatted.
     *  @return
     *      A string representation of <code>obj</code>.
     */
    public static String formatObj(
            final double obj
    )
    {
        return formatObj(new Double(obj));
    }

    /**
     *  Convenience for printing an object with its name.
     *
     *  @param obj
     *      The object to be formatted.
     *  @param name
     *      The name of the object.
     */
     public static String formatObj(
            final Object obj,
            final String name
     )
     {
        // The value to return.
        final String result;

        if (name == null) {
            result = TraceUtil.formatObj(obj);
        }
        else {
            result = name + " = " + formatObj(obj);
        }

         return result;
    }

}