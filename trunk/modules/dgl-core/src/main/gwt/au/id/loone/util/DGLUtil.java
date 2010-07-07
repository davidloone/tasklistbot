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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.id.loone.util.reflist.RefItem;

/**
 *  A collection of static utility methods.
 *
 *  @author David G Loone
 */
public final class DGLUtil
{

    /**
     *  An empty string array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final String[] EMPTY_STRING_ARRAY = new String[] {};

    /**
     *  Prevent instantiations.
     */
    private DGLUtil()
    {
        super();
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final boolean val1,
            final boolean val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final char val1,
            final char val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final byte val1,
            final byte val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final short val1,
            final short val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final int val1,
            final int val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final long val1,
            final long val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final float val1,
            final float val2
    )
    {
        return val1 == val2;
    }

    /**
     *  Compare two primitive values for equality.
     *
     *  @param val1
     *      The first value.
     *  @param val2
     *      The second value.
     *  @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final double val1,
            final double val2
    )
    {
        return val1 == val2;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final boolean[] val1,
            final boolean[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final char[] val1,
            final char[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final byte[] val1,
            final byte[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final short[] val1,
            final short[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final int[] val1,
            final int[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final long[] val1,
            final long[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final float[] val1,
            final float[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Compare two primitive arrays for equality.
     *
     * @param val1
     *      The first value.
     * @param val2
     *      THe second value.
     * @return
     *      The value <code>true</code> if both <code>val1</code> and
     *      <code>val2</code> are equal.
     */
    public static boolean equals(
            final double[] val1,
            final double[] val2
    )
    {
        boolean result;

        if ((val1 == null) && (val2 == null)) {
            result = true;
        }
        else if (val1 == null) {
            result = false;
        }
        else if (val1.length != val2.length) {
            result = false;
        }
        else {
            result = true;
            for (int i = 0; i < val1.length; i++) {
                if (val1[i] != val2[i]) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    /**
     *  Compare two objects for equality.
     *
     *  @param obj1
     *      The first object.
     *  @param obj2
     *      The second object.
     *  @return
     *      The value <code>true</code> if both <code>obj1</code> and
     *      <code>obj2</code> are equal to <code>null</code>,
     *      or are equal
     *      (as determined by their {@link Object#equals(Object)} methods.
     */
    public static <T> boolean equals(
            final T obj1,
            final T obj2
    )
    {
        final boolean result;

        if ((obj1 == null) && (obj2 == null)) {
            result = true;
        }
        else if (obj1 == null) {
            result = false;
        }
        else if (obj2 == null) {
            result = false;
        }
        else if (obj1.getClass().isArray() &&
                obj2.getClass().isArray()) {
            final Object[] obj1Array = (Object[])obj1;
            final Object[] obj2Array = (Object[])obj2;
            if (obj1Array.length == obj2Array.length) {
                boolean allElementsEq = true;
                for (int i = 0; i < obj1Array.length; i++) {
                    if (!equals(obj1Array[i], obj2Array[i])) {
                        allElementsEq = false;
                        break;
                    }
                }
                result = allElementsEq;
            }
            else {
                result = false;
            }
        }
        else if ((obj1 instanceof Set) &&
                (obj2 instanceof Set)) {
            // Check that each set contains all members of the other set.
            final Set set1 = (Set)obj1;
            final Set set2 = (Set)obj2;
            boolean eq = true;
            for (final Object o : set1) {
                if (!set2.contains(o)) {
                    eq = false;
                    break;
                }
            }
            if (eq) {
                for (final Object o : set2) {
                    if (!set1.contains(o)) {
                        eq = false;
                        break;
                    }
                }
            }
            result = eq;
        }
        else if ((obj1 instanceof Collection) &&
                (obj2 instanceof Collection)) {
            result = equals(((Collection)obj1).toArray(), ((Collection)obj2).toArray());
        }
        else if ((obj1 instanceof Map) &&
                (obj2 instanceof Map)) {
            final Map objMap1 = (Map)obj1;
            final Map objMap2 = (Map)obj2;
            if (objMap1.keySet().size() == objMap2.keySet().size()) {
                // Maps are same size, check their keys/values.
                boolean eq = true;
                for (final Object key: objMap1.keySet()) {
                    if (objMap2.containsKey(key) &&
                            equals(objMap1.get(key), objMap2.get(key))) {
                        // Key and values are equal. Go on to next key.
                    }
                    else {
                        eq = false;
                        break;
                    }
                }
                result = eq;
            }
            else {
                // Maps are different sizes, so not equal.
                result = false;
            }
        }
        else {
            result = obj1.equals(obj2);
        }

        return result;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final boolean value
    )
    {
        return value ? 1 : 0;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final char value
    )
    {
        return (int)value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final byte value
    )
    {
        return (int)value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final short value
    )
    {
        return (int)value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final int value
    )
    {
        return value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final long value
    )
    {
        return (int)value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final float value
    )
    {
        return (int)value;
    }

    /**
     *  Hashcode generator.
     */
    public static int hashCode(
            final double value
    )
    {
        return (int)value;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final boolean[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final boolean aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final char[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final char aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final byte[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final byte aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final short[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final short aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final int[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final int aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final long[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final long aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final float[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final float aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     * Hashcode generator.
     */
    public static int hashCode(
            final double[] value
    )
    {
        int result;

        result = 0;
        if (value != null) {
            for (final double aValue : value) {
                result += hashCode(aValue);
            }
        }

        return result;
    }

    /**
     *  Null safe hashcode generator.
     *
     *  @param obj
     *      The object to return the hashcode of.
     *  @return
     *      The value zero if <code>obj</code> is equal to <code>null</code>,
     *      or the {@link Object#hashCode() hashcode} of <code>obj</code> otherwise.
     */
    public static int hashCode(
            final Object obj
    )
    {
        final int result;

        if (obj == null) {
            result = 0;
        }
        else if (obj.getClass().isArray()) {
            int hashCode = 0;
            for (final Object el: (Object[])obj) {
                hashCode += hashCode(el);
            }
            result = hashCode;
        }
        else if (obj instanceof Collection) {
            int hashCode = 0;
            for (final Object el: (Collection)obj) {
                hashCode += hashCode(el);
            }
            result = hashCode;
        }
        else if (obj instanceof Map) {
            final Map map = (Map)obj;
            int hashCode = 0;
            for (final Object key: map.keySet()) {
                hashCode += hashCode(key);
                hashCode += hashCode(map.get(key));
            }
            result = hashCode;
        }
        else {
            result = obj.hashCode();
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static boolean clone(
            final boolean value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static char clone(
            final char value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static byte clone(
            final byte value
    )
    {
        return value;
    }

    /**
     */
    public static short clone(
            final short value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static int clone(
            final int value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static long clone(
            final long value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static float clone(
            final float value
    )
    {
        return (int)value;
    }

    /**
     *  Cloner.
     */
    public static double clone(
            final double value
    )
    {
        return value;
    }

    /**
     *  Cloner.
     */
    public static boolean[] clone(
            final boolean[] value
    )
    {
        boolean[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new boolean[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static char[] clone(
            final char[] value
    )
    {
        char[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new char[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static byte[] clone(
            final byte[] value
    )
    {
        byte[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new byte[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static short[] clone(
            final short[] value
    )
    {
        short[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new short[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static int[] clone(
            final int[] value
    )
    {
        int[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new int[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static long[] clone(
            final long[] value
    )
    {
        long[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new long[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static float[] clone(
            final float[] value
    )
    {
        float[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new float[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     *  Cloner.
     */
    public static double[] clone(
            final double[] value
    )
    {
        double[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = new double[value.length];
            System.arraycopy(value, 0, result, 0, value.length);
        }

        return result;
    }

    /**
     * Cloner.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] clone(
            final T[] value,
            final T[] newValue
    )
    {
        final T[] result;

        if (value == null) {
            result = null;
        }
        else {
            result = newValue;
            for (int i = 0; i < value.length; i++) {
                newValue[i] = clone(value[i]);
            }
        }

        return result;
    }

    /**
     * Cloner.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T clone(
            final T valueObj
    )
    {
        final Object resultObj;

        if (valueObj == null) {
            resultObj = null;
        }
        else if (valueObj instanceof Object[]) {
            final Object[] value = (Object[])valueObj;
            final Object[] result = new Object[value.length];
            for (int i = 0; i < value.length; i++) {
                result[i] = clone(value[i]);
            }
            resultObj = result;
        }
        else if (valueObj instanceof Set) {
            final Set value = (Set)valueObj;
            final Set result = new HashSet();
            for (final Object element : value) {
                result.add(clone(element));
            }
            resultObj = result;
        }
        else if (valueObj instanceof List) {
            final List value = (List)valueObj;
            final List result = new LinkedList();
            for (final Object element : value) {
                result.add(clone(element));
            }
            resultObj = result;
        }
        else if (valueObj instanceof Map) {
            final Map value = (Map)valueObj;
            final Map result = new HashMap();
            for (final Object key : value.keySet()) {
                result.put(clone(key), clone(value.get(key)));
            }
            resultObj = result;
        }
        else if (valueObj instanceof ReallyCloneable) {
            resultObj = ((ReallyCloneable)valueObj).clone();
        }
        else if (valueObj instanceof RefItem) {
            resultObj = valueObj;
        }
        else {
            resultObj = valueObj;
        }

        return (T)resultObj;
    }

    /**
     *  Test a float for equality with zero.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean equalsZero(
            final float a
    )
    {
        // Need to figure out a better way of doing this.
        return a == 0.0F;
    }

    /**
     *  Test a double for equality with zero.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static boolean equalsZero(
            final double a
    )
    {
        // Need to figure out a better way of doing this.
        return a == 0.0D;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final char obj1,
            final char obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final byte obj1,
            final byte obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final short obj1,
            final short obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final int obj1,
            final int obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final long obj1,
            final long obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final float obj1,
            final float obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  Compare two primitive type values.
     */
    public static int compare(
            final double obj1,
            final double obj2
    )
    {
        final int result;

        if (obj1 == obj2) {
            result = 0;
        }
        else if (obj1 < obj2) {
            result = -1;
        }
        else {
            result = 1;
        }

        return result;
    }

    /**
     *  A null-safe compare function.
     */
    public static <T extends Comparable<? super T>> int compare(
            final T obj1,
            final T obj2
    )
    {
        final int result;

        if ((obj1 == null) && (obj2 == null)) {
            result = 0;
        }
        else if (obj1 == null) {
            result = -1;
        }
        else if (obj2 == null) {
            result = 1;
        }
        else {
            result = obj1.compareTo(obj2);
        }

        return result;
    }

    /**
     * Find the union of two sets.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static <E> Set<E> union(
            final Set<E>... sets
    )
    {
        final HashSet<E> result;

        result = new HashSet<E>();
        for (final Set<E> s : sets) {
            if (s != null) {
                for (final E i : s) {
                    result.add(i);
                }
            }
        }

        return Collections.unmodifiableSet(result);
    }

    /**
     * A comparator that for comparing modulo numbers.
     *
     * @author David G Loone
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class ModuloComparator<T extends Number>
            implements Comparator<T>
    {

        /**
         * Current value of the <b>modulo</b> property.
         */
        private int modulo;

        /**
         */
        public ModuloComparator(
                final int modulo
        )
        {
            super();

            this.modulo = modulo;
        }

        /**
         */
        public int compare(
                final T i1,
                final T i2
        )
        {
            int result;

            final long diff = i1.longValue() - i2.longValue();
            if (((diff < 0) && (diff > -(modulo / 2))) ||
                ((diff > 0) && (diff > (modulo / 2)))) {
                result = -1;
            }
            else if (diff == 0) {
                result = 0;
            }
            else {
                result = 1;
            }

            return result;
        }

    }

    /**
     * Choose a random element of an array.
     */
    public static boolean random(
            final boolean[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static char random(
            final char[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static byte random(
            final byte[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static short random(
            final short[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static int random(
            final int[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static long random(
            final long[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static float random(
            final float[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static double random(
            final double[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of an array.
     */
    public static <T> T random(
            final T[] values
    )
    {
        return values[(int)(Math.random() * values.length)];
    }

    /**
     * Choose a random element of collection.
     */
    public static <T> T random(
            final Collection<T> values
    )
    {
        final List<T> valuesList = new ArrayList<T>(values);
        return (values.size() == 0) ? null : valuesList.get((int)(Math.random() * values.size()));
    }

    /**
     * Generate a random integer in a given range.
     *
     * @param low
     *      The minimum value that the result can be (inclusive).
     * @param high
     *      The maximum value that the result can be (inclusive).
     */
    public static int random(
            final int low,
            final int high
    )
    {
        return (int)(Math.random() * (high - low + 1));
    }

    /**
     * Format a number as hexadecimal using (byte) little-endian ordering.
     *
     * <p>The first two characters of the result will represent the least significant byte of the input,
     *      the next two characters will represent the second least significant byte of the input,
     *      etc.</p>
     *
     * @param num
     *      The number to format.
     * @param nDigits
     *      The number of digits (characters) to be output.
     *      This should be an even number.
     * @return
     *      The hexadecimal representation of <code>num</code>.
     *      The length of this string will always be equal to <code>nDigits</code>,
     *      with the number left zero filled as required.
     */
    public static String formatAsHexLE(
            final long num,
            final int nDigits
    )
    {
        final StringBuilder buf;

        buf = new StringBuilder();
        for (int i = 0; i < (nDigits / 2); i++) {
            buf.append(formatAsHexBE((num >>> (i * 8)) & 0xFF, 2));
        }

        return buf.toString();
    }

    /**
     * Format a number as hexadecimal using big-endian ordering.
     *
     * @param num
     *      The number to format.
     * @param nDigits
     *      The number of digits (characters) to be output.
     * @return
     *      The hexadecimal representation of <code>num</code>.
     *      The length of this string will always be equal to <code>nDigits</code>,
     *      with the number left zero filled as required.
     */
    public static String formatAsHexBE(
            final long num,
            final int nDigits
    )
    {
        final StringBuilder buf;

        buf = new StringBuilder();
        for (int i = (nDigits - 1); i >= 0; i--) {
            final int offset = i * 4;
            final long nibble = (num & (0xF << offset)) >>> offset;
            buf.append(Long.toHexString(nibble).toUpperCase());
        }

        return buf.toString();
    }

    /**
     * Dump a byte array as hex.
     */
    public static String dumpHex(
            final byte[] data
    )
    {
        final StringBuilder buf;

        if (data == null) {
            buf = null;
        }
        else {
            buf = new StringBuilder();
            boolean first = true;
            for (final byte b : data) {
                if (first) {
                    first = false;
                }
                else {
                    buf.append(" ");
                }
                buf.append("0x");
                buf.append(formatAsHexBE(b, 2));
            }
        }

        return (buf == null) ? null : buf.toString();
    }

}