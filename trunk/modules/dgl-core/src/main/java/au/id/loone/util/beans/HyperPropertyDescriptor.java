/*
 * Copyright 2009, David G Loone
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

package au.id.loone.util.beans;

import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.DGLStringUtil;

/**
 * Abstract base class for hyperbean property descriptors.
 *
 * <p>A hyperbean property is to a hyperbean as a Javabean property is to a Javabean.
 *      This class is like {@link PropertyDescriptor} for the hyperbaen world.</p>
 *
 * <p>The usual way of constructing instances of this class
 *      is via the {@link #factory(Type, String)} method.
 *      That method accepts a starting type and a path string,
 *      and generates an array of concrete instances of this class that represent the path.</p>
 *
 * @author David G Loone
 */
public abstract class HyperPropertyDescriptor
{

    /**
     * Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(HyperPropertyDescriptor.class);

    /**
     * Current value of the <b>hyperBeanType</b> property.
     */
    private Type hyperBeanType;

    /**
     * Current value of the <b>isReadable</b> property.
     */
    private boolean isReadable;

    /**
     * Current value of the <b>isWriteable</b> property.
     */
    private boolean isWriteable;

    /**
     * Current value of the <b>name</b> property.
     */
    private String name;

    /**
     * @param hyperBeanType
     *      The type information for the hyperbean of which this is a property.
     * @param name
     *      The name of the hyperbean property.
     */
    HyperPropertyDescriptor(
            final Type hyperBeanType,
            final String name,
            final boolean isWriteable,
            final boolean isReadable
    )
    {
        super();

        this.hyperBeanType = hyperBeanType;
        this.isReadable = isReadable;
        this.isWriteable = isWriteable;
        this.name = name;
    }

    /**
     */
    @Override
    public String toString()
    {
        return HyperPropertyDescriptor.class.getName() + "{" +
                TraceUtil.formatObj(hyperBeanType, "hyperBeanType") + ", " +
                TraceUtil.formatObj(isReadable, "isReadable") + ", " +
                TraceUtil.formatObj(isWriteable, "isWriteable") + ", " +
                TraceUtil.formatObj(name, "name") + "}";
    }

    /**
     * Write a new value to the hyperbean property.
     * By default, this method does nothing,
     * but can be overridden by the extending class
     * to set the property value in a bean specific way.
     */
    public void write(
            final Object hyperBean,
            final Object value
    )
            throws IllegalAccessException, InvocationTargetException
    {
        // By default, do nothing.
    }

    /**
     * Read the value of the hyperbean property.
     * By default, this method returns <code>null</code>,
     * but can be overridden by the extending class
     * to read the property value in a bean specific way.
     *
     * @return
     *      Always returns <code>null</code>.
     */
    public Object read(
            final Object hyperBean
    )
            throws IllegalAccessException, InvocationTargetException
    {
        // By default, return null.
        return null;
    }

    /**
     * Getter method for the <b>isReadable</b> property.
     */
    public boolean getIsReadable()
    {
        return isReadable;
    }

    /**
     * Getter method for the <b>isWriteable</b> property.
     */
    public boolean getIsWriteable()
    {
        return isWriteable;
    }

    /**
     * Getter method for the <b>name</b> property.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter method for the <b>valueType</b> property.
     */
    public abstract Type getValueType();

    /**
     * Create an array of descriptors for a hyperbean property path.
     *
     * <p>The hyperbean property path (given by <code>hyperPropPath</code> is parsed
     *      in the context of a hyperbean class,
     *      and an instance of this class is constructed for each element of the path.
     *      The actual class of each path element is determined by the syntax of the property path.</p>
     *
     * @param hyperbeanType
     *      The type of the hyperbean at the top of the path.
     *      This should never be equal to <code>null</code>.
     * @param hyperPropPath
     *      The path down the hyperbean tree.
     *      This should never be equal to <code>null</code>.
     * @return
     *      An array of hyperbean descriptors that represents the given hyperbean property path.
     *      This will never be equal to <code>null</code>.
     */
    public static List<HyperPropertyDescriptor> factory(
            final Type hyperbeanType,
            final String hyperPropPath
    )
            throws MalformedHyperBeanPathException
    {
        final List<HyperPropertyDescriptor> result;

        result = new LinkedList<HyperPropertyDescriptor>();
        final List<PathElement> pathElements = PathElement.factory(hyperPropPath);
        Type propType = hyperbeanType;
        for (int i = 0; i < pathElements.size(); i++) {
            final HyperPropertyDescriptor propDescriptor =
                    pathElements.get(i).hyperPropertyDescriptorFactory(propType, i == (pathElements.size() - 1));
            propType = propDescriptor.getValueType();
            result.add(propDescriptor);
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Hyper property descriptor for no property.
     *
     * @author David G Loone
     */
    public static class ForNull
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForNull.class);

        /**
         * Current value of the <b>valueType</b> property.
         */
        private Type valueType;

        /**
         */
        ForNull(
                final String name,
                final Type valueType
        )
        {
            super(null, name, false, false);

            this.valueType = valueType;
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForNull.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(valueType, "valueType") + "}";
        }

        /**
         */
        @Override
        public Type getValueType()
        {
            return valueType;
        }

    }

    /**
     * Hyperbean property descriptor for a property that has an index.
     *
     * @author David G Loone
     */
    public static abstract class ForIndexedStructure
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForIndexedStructure.class);

        /**
         * Current value of the <b>index</b> property.
         */
        private int index;

        /**
         */
        ForIndexedStructure(
                final Type beanType,
                final String indexStr,
                final boolean isWriteable,
                final boolean isReadable
        )
        {
            super(beanType, indexStr, isWriteable, isReadable);

            this.index = Integer.parseInt(indexStr);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForIndexedStructure.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(index) + "}";
        }

        /**
         * Getter method for the <b>index</b> property.
         */
        public int getIndex()
        {
            return index;
        }

    }

    /**
     * Hyperbean property descriptor for a property that is a regular JavaBean property
     * (and therefore, described by a {@link PropertyDescriptor}.
     *
     * @author David G Loone
     */
    public static class ForBeanProperty
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForBeanProperty.class);

        /**
         * Current value of the <b>propertyDescriptor</b> property.
         */
        private PropertyDescriptor propertyDescriptor;

        /**
         */
        ForBeanProperty(
                final Type beanType,
                final PropertyDescriptor propertyDescriptor
        )
        {
            super(beanType, propertyDescriptor.getName(),
                    (propertyDescriptor.getWriteMethod() != null),
                    (propertyDescriptor.getReadMethod() != null));

            this.propertyDescriptor = propertyDescriptor;
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForBeanProperty.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(propertyDescriptor, "propertyDescriptor") + "}";
        }

        /**
         */
        @Override
        public void write(
                final Object hyperBean,
                final Object value
        )
                throws IllegalAccessException, InvocationTargetException
        {
            propertyDescriptor.getWriteMethod().invoke(hyperBean, value);
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            return propertyDescriptor.getReadMethod().invoke(hyperBean);
        }

        /**
         * Getter method for the <b>propertyDescriptor</b> property.
         */
        public PropertyDescriptor getPropertyDescriptor()
        {
            return propertyDescriptor;
        }

        /**
         * @return
         *      Equivalent to
         *      "{@link #getPropertyDescriptor getPropertyDescriptor}().{@link PropertyDescriptor#getReadMethod() getReadMethod}().{@link java.lang.reflect.Method#getGenericReturnType() getGenericReturnType}()".
         */
        @Override
        public Type getValueType()
        {
            return propertyDescriptor.getReadMethod().getGenericReturnType();
        }

    }

    /**
     * Hyperbean property descriptor for a property that is an array.
     *
     * @author David G Loone
     */
    public static class ForArray
            extends ForIndexedStructure
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForArray.class);

        /**
         * Current value of the <b>valueType</b> property.
         */
        private final Class valueType;

        /**
         */
        ForArray(
                final Type beanType,
                final String indexStr
        )
        {
            super(beanType, indexStr, true, true);

            valueType = ((Class)beanType).getComponentType();
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForArray.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(valueType, "valueType") + "}";
        }

        /**
         */
        @SuppressWarnings({"RedundantCast"})
        @Override
        public void write(
                final Object hyperBean,
                final Object value
        )
                throws IllegalAccessException, InvocationTargetException
        {
            ((Object[])hyperBean)[getIndex()] = value;
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((Object[])hyperBean)[getIndex()];

            return result;
        }

        /**
         */
        @Override
        public Type getValueType()
        {
            return valueType;
        }

    }

    /**
     * Hyperbean propery descriptor for the <b>length</b> pseudo-property of a array hyperbean.
     *
     * @author David G Loone
     */
    public static class ForArrayLength
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForArrayLength.class);

        /**
         */
        ForArrayLength(
                final Type beanType,
                final String name
        )
        {
            super(beanType, name, false, true);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForArrayLength.class.getName() + "{" +
                    super.toString() + "}";
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((Collection)hyperBean).size();

            return result;
        }

        /**
         */
        @Override
        public Type getValueType()
        {
            return Integer.TYPE;
        }

    }

    /**
     * Hyperbean property descriptor for a property that is a {@link Collection}.
     *
     * @author David G Loone
     */
    public static abstract class ForCollection
            extends ForIndexedStructure
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForCollection.class);

        /**
         * Current value of the <b>valueClass</b> property.
         */
        private final Class valueType;

        /**
         */
        ForCollection(
                final Type beanType,
                final String indexStr,
                final boolean isWriteable,
                final boolean isReadable
        )
        {
            super(beanType, indexStr, isWriteable, isReadable);

            if (beanType instanceof Class) {
                valueType = String.class;
            }
            else {
                valueType = (Class)((ParameterizedType)beanType).getActualTypeArguments()[0];
            }
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForCollection.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(valueType, "valueType") + "}";
        }

        /**
         * Getter method for the <b>valueType</b> property.
         */
        @Override
        public Type getValueType()
        {
            return valueType;
        }

    }

    /**
     * Hyperbean propery descriptor for the <b>size</b> pseudo-property of a {@link Collection} hyperbean.
     *
     * @author David G Loone
     */
    public static class ForCollectionSize
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForCollectionSize.class);

        /**
         */
        ForCollectionSize(
                final Type beanType,
                final String name
        )
        {
            super(beanType, name, false, true);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForCollectionSize.class.getName() + "{" +
                    super.toString() + "}";
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((Collection)hyperBean).size();

            return result;
        }

        /**
         */
        @Override
        public Type getValueType()
        {
            return Integer.TYPE;
        }

    }

    /**
     * Hyperbean property descriptor for a property that is a {@link List}.
     *
     * @author David G Loone
     */
    public static class ForList
            extends ForCollection
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForList.class);

        /**
         */
        ForList(
                final Type beanType,
                final String indexStr
        )
        {
            super(beanType, indexStr, true, true);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForList.class.getName() + "{" +
                    super.toString() + "}";
        }

        /**
         */
        @Override
        @SuppressWarnings({"unchecked"})
        public void write(
                final Object hyperBean,
                final Object value
        )
                throws IllegalAccessException, InvocationTargetException
        {
            ((List)hyperBean).add(getIndex(), value);
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((List)hyperBean).get(getIndex());

            return result;
        }

    }

    /**
     * Hyperbean property descriptor for a property that is a {@link Set}.
     *
     * @author David G Loone
     */
    public static class ForSet
            extends ForCollection
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForSet.class);

        /**
         */
        ForSet(
                final Type beanType,
                final String indexStr
        )
        {
            super(beanType, indexStr, true, true);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForSet.class.getName() + "{" +
                    super.toString() + "}";
        }

        /**
         */
        @Override
        @SuppressWarnings({"unchecked"})
        public void write(
                final Object hyperBean,
                final Object value
        )
                throws IllegalAccessException, InvocationTargetException
        {
            ((Set)hyperBean).add(value);
        }

        /**
         */
        @Override
        @SuppressWarnings({"unchecked"})
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = (new LinkedList((Set)hyperBean)).get(getIndex());

            return result;
        }

    }

    /**
     * Hyperbean property descriptor for a property that is a {@link Map}.
     *
     * @author David G Loone
     */
    public static class ForMap
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForMap.class);

        /**
         * Current value of the <b>valueClass</b> property.
         */
        private final Class valueType;

        /**
         */
        ForMap(
                final Type beanType,
                final String name
        )
        {
            super(beanType, name, true, true);

            if (beanType instanceof Class) {
                valueType = String.class;
            }
            else {
                valueType = (Class)((ParameterizedType)beanType).getActualTypeArguments()[1];
            }
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForMap.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(valueType, "valueType") + "}";
        }

        /**
         */
        @Override
        @SuppressWarnings({"unchecked"})
        public void write(
                final Object hyperBean,
                final Object value
        )
                throws IllegalAccessException, InvocationTargetException
        {
            ((Map)hyperBean).put(getName(), value);
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((Map)hyperBean).get(getName());

            return result;
        }

        /**
         * Getter method for the <b>valueType</b> property.
         */
        @Override
        public Type getValueType()
        {
            return valueType;
        }

    }

    /**
     * Hyperbean propery descriptor for the <b>size</b> pseudo-property of a {@link Map} hyperbean.
     *
     * @author David G Loone
     */
    public static class ForMapSize
            extends HyperPropertyDescriptor
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForMapSize.class);

        /**
         */
        ForMapSize(
                final Type beanType,
                final String name
        )
        {
            super(beanType, name, false, true);
        }

        /**
         */
        @Override
        public String toString()
        {
            return ForMapSize.class.getName() + "{" +
                    super.toString() + "}";
        }

        /**
         */
        @Override
        public Object read(
                final Object hyperBean
        )
                throws IllegalAccessException, InvocationTargetException
        {
            final Object result;

            result = ((Map)hyperBean).size();

            return result;
        }

        /**
         */
        @Override
        public Type getValueType()
        {
            return Integer.TYPE;
        }

    }

    /**
     * Provides the functionality to split a path string into components.
     *
     * <p>We have two basic operators, viz:</p>
     * <ul>
     *      <li>{@link HyperPropertyDescriptor.PropertyPathElement}:
     *          Indicated by a path element of the form
     *          "<code>.<i>name</i></code>".</li>
     *      <li>{@link HyperPropertyDescriptor.SubscriptPathElement}:
     *          Indicated by a path element of the orm
     *          "<code>[<i>name</i>]</code>".</li>
     * </ul>
     *
     * <p>The {@link #factory(String)} method
     *      breaks up a path string into an array of path element objects
     *      that represent the string.
     *      The class of each individual path element
     *      indicates which operator is represented by that place.</p>
     */
    private static abstract class PathElement
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ForMapSize.class);

        /**
         * Current value of the <b>name</b> property.
         */
        private String name;

        /**
         */
        PathElement(
                final String name
        )
        {
            super();

            this.name = name;
        }

        /**
         */
        public static List<PathElement> factory(
                final String path
        )
                throws MalformedHyperBeanPathException
        {
            LOG.trace("factoryWithPrefix(" + TraceUtil.formatObj(path) + ", " +
                    TraceUtil.formatObj(path) + ")");

            final List<PathElement> result = new LinkedList<PathElement>();

            // Find the first path element.
            int idx = DGLStringUtil.indexOf(path, new char[] {'.', '['}, 0);
            if (idx == -1) {
                result.add(new NullPathElement(path));
            }
            else if (idx == 0) {
                // Do nothing.
            }
            else {
                result.add(new NullPathElement(path.substring(0, idx)));
            }

            // Then the rest.
            while (idx != -1) {
                if (path.charAt(idx) == '.') {
                    int pathElementStartIdx = idx + 1;
                    int nextIdx = DGLStringUtil.indexOf(path, new char[] {'.', '['}, pathElementStartIdx);
                    final String pathElementStr;
                    if (nextIdx == -1) {
                        pathElementStr = path.substring(pathElementStartIdx);
                    }
                    else {
                        pathElementStr = path.substring(pathElementStartIdx, nextIdx);
                    }
                    result.add(new PropertyPathElement(pathElementStr));
                    idx = nextIdx;
                }
                else if (path.charAt(idx) == '[') {
                    int pathElementStartIdx = idx + 1;
                    int nextIdx = path.indexOf(']', pathElementStartIdx);
                    final String pathElementStr;
                    if (nextIdx == -1) {
                        throw new IllegalArgumentException("Malformed hyperbean path \"" + path + "\".");
                    }
                    else {
                        pathElementStr = path.substring(idx + 1, nextIdx);
                    }
                    result.add(new SubscriptPathElement(pathElementStr));
                    idx = nextIdx + 1;
                    idx = (idx == path.length()) ? -1 : idx;
                }
                else {
                    throw new IllegalArgumentException("Malformed hyperbean path \"" + path + "\".");
                }
            }

            LOG.trace("factory: " + TraceUtil.formatObj(result, "result"));
            return Collections.unmodifiableList(result);
        }

        /**
         * Factory for a property descriptor for this path element.
         */
        public abstract HyperPropertyDescriptor hyperPropertyDescriptorFactory(
                final Type propType,
                final boolean isLast
        );

        /**
         * Getter method for the <b>name</b> property.
         */
        public String getName()
        {
            return name;
        }

    }

    /**
     */
    private static class NullPathElement
        extends PathElement
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(NullPathElement.class);

        /**
         */
        public NullPathElement(
                final String name
        )
        {
            super(name);
        }

        /**
         */
        public String toString()
        {
            return getName();
        }

        /**
         * Factory for a property descriptor for this path element.
         */
        public HyperPropertyDescriptor hyperPropertyDescriptorFactory(
                final Type propType,
                final boolean isLast
        )
        {
            return new ForNull(getName(), propType);
        }

    }

    /**
     */
    private static class PropertyPathElement
            extends PathElement
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(PropertyPathElement.class);

        /**
         */
        public PropertyPathElement(
                final String name
        )
        {
            super(name);
        }

        /**
         */
        public String toString()
        {
            return '.' + getName();
        }

        /**
         */
        public HyperPropertyDescriptor hyperPropertyDescriptorFactory(
                final Type propType,
                final boolean isLast
        )
        {
            final HyperPropertyDescriptor result;

            if (Collection.class.isAssignableFrom((Class)propType) &&
                    (DGLStringUtil.equals(getName(), "length") ||
                        DGLStringUtil.equals(getName(), "size"))) {
                if (!isLast) {
                    throw new IllegalArgumentException("Collection \"" + getName() +
                            "\" pseudo-property must be last in path.");
                }
                result = new ForCollectionSize(propType, getName());
            }
            else if (Map.class.isAssignableFrom((Class)propType) &&
                    (DGLStringUtil.equals(getName(), "length") ||
                        DGLStringUtil.equals(getName(), "size"))) {
                if (!isLast) {
                    throw new IllegalArgumentException("Map \"" + getName() +
                            "\" pseudo-property must be last in path.");
                }
                result = new ForMapSize(propType, getName());
            }
            else {
                try {
                    final BeanInfo bi = Introspector.getBeanInfo((Class)propType);
                    PropertyDescriptor beanPropertyDescriptor = null;
                    for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                        if (DGLStringUtil.equals(pd.getName(), getName())) {
                            beanPropertyDescriptor = pd;
                        }
                    }
                    if (beanPropertyDescriptor == null) {
                        throw new IllegalArgumentException("JavaBean property \"" + getName() +
                                "\" not found in class \"" + propType + "\".");
                    }
                    result = new ForBeanProperty(propType, beanPropertyDescriptor);
                }
                catch (final IntrospectionException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            return result;
        }

    }

    /**
     */
    private static class SubscriptPathElement
            extends PathElement
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(SubscriptPathElement.class);

        /**
         */
        public SubscriptPathElement(
                final String name
        )
        {
            super(name);
        }

        /**
         */
        public String toString()
        {
            return '[' + getName() + ']';
        }

        /**
         */
        public HyperPropertyDescriptor hyperPropertyDescriptorFactory(
                final Type propType,
                final boolean isLast
        )
        {
            final HyperPropertyDescriptor result;

            // Figure out the raw class.
            final Class cl;
            if (propType instanceof Class) {
                cl = (Class)propType;
            }
            else if (propType instanceof ParameterizedType) {
                final Type rawType = ((ParameterizedType)propType).getRawType();
                if (rawType instanceof Class) {
                    cl = (Class)rawType;
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
            else {
                throw new IllegalArgumentException();
            }

            // Then see if we have an array, list, set or map.
            if (cl.isArray()) {
                result = new ForArray(propType, getName());
            }
            else if (List.class.isAssignableFrom(cl)) {
                result = new ForList(propType, getName());
            }
            else if (Set.class.isAssignableFrom(cl)) {
                result = new ForSet(propType, getName());
            }
            else if (Map.class.isAssignableFrom(cl)) {
                result = new ForMap(propType, getName());
            }
            else {
                throw new IllegalArgumentException();
            }

            return result;
        }

    }

}
