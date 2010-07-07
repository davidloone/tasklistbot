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

package au.id.loone.util.tracing;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.StringTokenizer;

import au.id.loone.util.DGLDateTimeUtil;
import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.beans.DGLBeanUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *  Trace utilities.
 *
 *  @author David G Loone
 */
public final class TraceUtil
{

    /**
     *  Maximum length of a trace string.
     */
    public static int MAX_TRACE_LEN = 2048;

    /**
     *  An empty object array for calling static no-arg methods.
     */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {
            // No elements.
    };

    /**
     *  This class only contains static methods,
     *  so cannot be instantiated.
     */
    private TraceUtil()
    {
        super();
    }

    /**
     *  Get a logger for a string.
     *
     *  @param name
     *      The string to get the logger for.
     *  @return
     *      The trace logger.
     */
    public static Logger getLogger(
            final String name
    )
    {
        init();
        return Logger.getLogger(name);
    }

    /**
     *  Get a logger for a class.
     *
     *  @param clazz
     *      The class to get the logger for.
     *  @return
     *      The trace logger.
     */
    public static Logger getLogger(
            final Class clazz
    )
    {
        init();
        return Logger.getLogger(clazz.getName());
    }

    /**
     *  Make sure Log4j is initialised.
     */
    public static void init() {
        try {
            new Init();
        }
        catch (final Throwable e) {
            // If anything untoward happens here, print the stacktrace.
            System.out.println("*** Error calling Init: " + e.getClass() +
                    ":" + e.getMessage());
            e.printStackTrace(System.out);
            System.out.println("*** Error ignored, Log4j not initialised.");
        }
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
                result = "0x" + DGLUtil.formatAsHexBE((Byte)obj, 2) + "'" + (char)b + "'";
            }
            else {
                result = "0x" + DGLUtil.formatAsHexBE((Byte)obj, 2);
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
            result = DGLDateTimeUtil.formatAsISO8601((Date)obj);
        }
        else if (obj instanceof GregorianCalendar) {
            // A date. Format as ISO8601.
            result = DGLDateTimeUtil.formatAsISO8601((GregorianCalendar)obj);
        }
        else if (obj.getClass().isArray()) {
            // An array. Format each element as a string separately.
            final Object[] objArray;
            final Class componentType = obj.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                // An array of primitives. Convert it to an array of the corresponding wrapper objects, then call
                // the format method.
                if (componentType.equals(Boolean.TYPE)) {
                    final boolean[] primitiveArray = (boolean[])obj;
                    objArray = new Boolean[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Character.TYPE)) {
                    final char[] primitiveArray = (char[])obj;
                    objArray = new Character[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Byte.TYPE)) {
                    final byte[] primitiveArray = (byte[])obj;
                    objArray = new Byte[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Short.TYPE)) {
                    final short[] primitiveArray = (short[])obj;
                    objArray = new Short[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Integer.TYPE)) {
                    final int[] primitiveArray = (int[])obj;
                    objArray = new Integer[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Long.TYPE)) {
                    final long[] primitiveArray = (long[])obj;
                    objArray = new Long[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Float.TYPE)) {
                    final float[] primitiveArray = (float[])obj;
                    objArray = new Float[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Double.TYPE)) {
                    final double[] primitiveArray = (double[])obj;
                    objArray = new Double[primitiveArray.length];
                    for (int i = 0; i < primitiveArray.length; i++) {
                        objArray[i] = primitiveArray[i];
                    }
                }
                else if (componentType.equals(Void.TYPE)) {
                    throw new IllegalArgumentException();
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
            else {
                objArray = (Object[])obj;
            }
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
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  <p>For example:
     *      <ul>
     *          <code>LOG.trace("method: " + TraceUtil.formatObj(x, "x.b.c.d"))</code>
     *      </ul>
     *      will log the message
     *      <ul>
     *          <code>method: x.b.c.d = "xyz"</code>
     *      </ul>
     *  </p>
     *
     *  @param obj
     *      The object to be formatted.
     *  @param name
     *      The name of the JavaBeans property to be extracted.
     *  @return
     *      A string containing the property name,
     *      and the string representation of the property value
     *      (as returned by the {@link #formatObj(Object)} method.
     *      The requested and actual nested property names may be different
     *      if it is not possible to descend to the full depth of the requested nested property
     *      (if, for example, an intermediate property has value <code>null</code>).
     */
     public static String formatObj(
            final Object obj,
            final String name
     )
     {
        // The value to return.
        final String result;

        // Figure out the actual property name, being the string after the first dot.
        final String beanName;
        final String propertyName;
        if (name == null) {
            result = TraceUtil.formatObj(obj);
        }
        else {
            final int dotIdx = name.indexOf('.');
            if (dotIdx == -1) {
                beanName = name;
                propertyName = null;
            }
            else {
                beanName = name.substring(0, dotIdx);
                propertyName = name.substring(dotIdx + 1);
            }
            result = formatObj(obj, beanName, propertyName);
        }

         return result;
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  <p>For example:
     *      <ul>
     *          <code>LOG.trace("method: " + TraceUtil.formatObj(x, "x", "b.c.d", "e.f.g"))</code>
     *      </ul>
     *      will log the message
     *      <ul>
     *          <code>method: x.b.c.d = "xyz", x.e.f.g = "zyx"</code>
     *      </ul>
     *  </p>
     *
     *  @param bean
     *      The bean containing the property is to be formatted.
     *  @param beanName
     *      The name of the bean to be printed.
     *  @param propertyNames
     *      The name of the property of the bean.
     *  @return
     *      A string containing the property name,
     *      and the string representation of the property value
     *      (as returned by the {@link #formatObj(Object)} method.
     *      The requested and actual nested property names may be different
     *      if it is not possible to descend to the full depth of the requested nested property
     *      (if, for example, an intermediate property has value <code>null</code>).
     */
    @SuppressWarnings({"ConstantConditions"})
    public static String formatObj(
            final Object bean,
            final String beanName,
            final String ... propertyNames
    )
    {
        // The value to return.
        final String result;

        final StringBuffer buf = new StringBuffer();
        if (propertyNames == null) {
            buf.append(beanName);
        }
        else if (bean == null) {
            buf.append(beanName);
            buf.append(" = null");
        }
        else {
            for (int i = 0; i < propertyNames.length; i++) {
                final String propertyName = propertyNames[i];
                boolean beanFound = true;
                Object residualBean = bean;
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(beanName);
                if (propertyName != null) {
                    for (final StringTokenizer st = new StringTokenizer(propertyName, "."); st.hasMoreTokens(); ) {
                        try {
                            if (residualBean == null) {
                                // Do nothing, just exit.
                                break;
                            }
                            else {
                                final String propertyComponentName = st.nextToken();
                                if (DGLStringUtil.equals(propertyComponentName, "#") ||
                                            propertyComponentName.startsWith("#")) {
                                    buf.append(".");
                                    buf.append(propertyComponentName);
                                    final int nLines;
                                    if (DGLStringUtil.equals(propertyComponentName, "#")) {
                                        nLines = 0;
                                    }
                                    else {
                                        nLines = Integer.parseInt(propertyComponentName.substring(1));
                                    }
                                    final String[] lines = StringUtils.split(residualBean.toString(), "\n");
                                    final StringBuffer outBuf = new StringBuffer();
                                    for (int j = 0; (j < lines.length) && (j < nLines); j++) {
                                        outBuf.append(lines[j]);
                                        outBuf.append("\n");
                                    }
                                    outBuf.append("...");
                                    residualBean = outBuf.toString();
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        (residualBean instanceof String) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((String)residualBean).length();
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Boolean.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((boolean[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Character.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((char[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Byte.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((byte[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Short.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((short[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Integer.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((int[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Long.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((long[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Float.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((float[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (residualBean.getClass().getComponentType() == Double.TYPE) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((double[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        residualBean.getClass().isArray() &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".length");
                                    residualBean = ((Object[])residualBean).length;
                                    beanFound = true;
                                    break;
                                }
                                else if (!st.hasMoreTokens() &&
                                        (residualBean instanceof Collection) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".size");
                                    residualBean = ((Collection)residualBean).size();
                                    beanFound = true;
                                }
                                else if (!st.hasMoreTokens() &&
                                        (residualBean instanceof Map) &&
                                        (DGLStringUtil.equals(propertyComponentName, "length") ||
                                            DGLStringUtil.equals(propertyComponentName, "size"))) {
                                    buf.append(".size");
                                    residualBean = ((Map)residualBean).size();
                                    beanFound = true;
                                }
                                else {
                                    final PropertyDescriptor propertyDescriptor =
                                            DGLBeanUtil.getPropertyDescriptor(residualBean.getClass(), propertyComponentName);
                                    if (propertyDescriptor == null) {
                                        if (!st.hasMoreTokens() &&
                                                DGLStringUtil.equals(propertyComponentName, "size")) {
                                            try {
                                                final Method sizeMethod = residualBean.getClass().getMethod("size");
                                                if (!Modifier.isStatic(sizeMethod.getModifiers()) &&
                                                        Modifier.isPublic(sizeMethod.getModifiers()) &&
                                                        (Integer.class.isAssignableFrom(sizeMethod.getReturnType()) ||
                                                            Integer.TYPE.isAssignableFrom(sizeMethod.getReturnType()))) {
                                                    buf.append("size");
                                                    residualBean = sizeMethod.invoke(residualBean);
                                                }
                                                else {
                                                    buf.append(".");
                                                    buf.append(propertyComponentName);
                                                    buf.append("(?)");
                                                    residualBean = null;
                                                    beanFound = false;
                                                }
                                            }
                                            catch (final NoSuchMethodException e) {
                                                buf.append(".");
                                                buf.append(propertyComponentName);
                                                buf.append("(?)");
                                                residualBean = null;
                                                beanFound = false;
                                            }
                                        }
                                        else {
                                            buf.append(".");
                                            buf.append(propertyComponentName);
                                            buf.append("(?)");
                                            residualBean = null;
                                            beanFound = false;
                                        }
                                        break;
                                    }
                                    else if (propertyDescriptor.getReadMethod() == null) {
                                        buf.append(".");
                                        buf.append(propertyComponentName);
                                        buf.append("(?)");
                                        residualBean = null;
                                        beanFound = false;
                                        break;
                                    }
                                    else {
                                        propertyDescriptor.getReadMethod().setAccessible(true);
                                        if (propertyDescriptor.getReadMethod() == null) {
                                            buf.append(".");
                                            buf.append(propertyComponentName);
                                            buf.append("(?)");
                                            residualBean = null;
                                            beanFound = false;
                                            break;
                                        }
                                        else {
                                            buf.append(".");
                                            buf.append(propertyComponentName);
                                            residualBean = propertyDescriptor.getReadMethod().invoke(residualBean, EMPTY_OBJECT_ARRAY);
                                            if ((residualBean == null) && st.hasMoreTokens()) {
                                                buf.append("(");
                                                while (st.hasMoreTokens()) {
                                                    buf.append(".");
                                                    buf.append(st.nextToken());
                                                }
                                                buf.append(")");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        catch (final IllegalAccessException e) {
                            buf.append(".?");
                            beanFound = false;
                            break;
                        }
                        catch (final InvocationTargetException e) {
                            buf.append(".?");
                            beanFound = false;
                            break;
                        }
                    }
                }
                if (beanFound) {
                    buf.append(" = ");
                    buf.append(formatObj(residualBean));
                }
            }
        }
        result = buf.toString();

        return result;
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final boolean obj,
            final String name
    )
    {
        return formatObj(Boolean.valueOf(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final char obj,
            final String name
    )
    {
        return formatObj(new Character(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final byte obj,
            final String name
    )
    {
        return formatObj(new Byte(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final short obj,
            final String name
    )
    {
        return formatObj(new Short(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final int obj,
            final String name
    )
    {
        return formatObj(new Integer(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final long obj,
            final String name
    )
    {
        return formatObj(new Long(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final float obj,
            final String name
    )
    {
        return formatObj(new Float(obj), name);
    }

    /**
     *  Format a nested JavaBeans property of an object for trace logging.
     *
     *  @see #formatObj(Object, String)
     */
    public static String formatObj(
            final double obj,
            final String name
    )
    {
        return formatObj(new Double(obj), name);
    }

    /**
     *  Format the properties of an object for printing.
     *
     *  <p><b>Note</b>:
     *      This is not safe against circular references within the bean tree.</p>
     *
     *  @param bean
     *      The bean to get the properties from.
     *  @param propNames
     *      The property names to list.
     *  @return
     *      All the property names paired with their values.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String formatProperties(
            final Object bean,
            final Collection<String> propNames
    )
    {
        return formatProperties(DGLBeanUtil.getPropertyValues(bean, propNames));
    }

    /**
     *  Format the properties of an object for printing.
     *
     *  <p><b>Note</b>:
     *      This is not safe against circular references within the bean tree.</p>
     *
     *  @param bean
     *      The bean to get the properties from.
     *  @param stopClass
     *      Stop finding properties just before this class,
     *      as per {@link DGLBeanUtil#getPropertyNames(Class, Class)}.
     *  @return
     *      All the property names paired with their values.
     */
    public static String formatProperties(
            final Object bean,
            final Class stopClass
    )
    {
        return formatPropertiesMap(DGLBeanUtil.getPropertyValues(bean, stopClass));
    }

    /**
     */
    public static String formatPropertiesMap(
            final Map<String, Object> props
    )
    {
        final StringBuilder buf = new StringBuilder();
        buf.append("[");
        boolean first = true;
        for (final String propName: props.keySet()) {
            final Object propValue = props.get(propName);
            if (first) {
                first = false;
            }
            else {
                buf.append(", ");
            }
            buf.append(propName);
            buf.append(" = ");
            buf.append(TraceUtil.formatObj(propValue));
        }
        buf.append("]");

        return buf.toString();
    }

    /**
     *  Format the properties of an object for printing.
     *
     *  @param bean
     *      The bean to get the properties from.
     *  @return
     *      All the property names paired with their values.
     */
    public static String formatProperties(
            final Object bean
    )
    {
        return formatProperties(bean, Object.class);
    }

}