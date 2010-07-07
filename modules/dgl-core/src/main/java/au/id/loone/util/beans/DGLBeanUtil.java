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

package au.id.loone.util.beans;

import javax.naming.ldap.LdapName;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.beans.converters.FileConverter;
import au.id.loone.util.beans.converters.LdapNameConverter;
import au.id.loone.util.beans.converters.TimeZoneConverter;
import au.id.loone.util.exception.FatalException;
import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.log4j.Logger;

/**
 *  Class containing JavaBean utility static methods.
 *
 *  @author David G Loone
 */
public class DGLBeanUtil
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DGLBeanUtil.class);

    static {
        ConvertUtils.register(new FileConverter(), File.class);
        ConvertUtils.register(new LdapNameConverter(), LdapName.class);
        ConvertUtils.register(new TimeZoneConverter(), TimeZone.class);
    }

    /**
     */
    private DGLBeanUtil()
    {
        super();
    }

    /**
     */
    public static void setProperty(
            final Object bean,
            final String propName,
            final Object propValue
    )
    {
        setProperty(bean, null, propName, propValue);
    }

    /**
     */
    private static void setPropertyInProperties(
            final Properties bean,
            final String key,
            final String propNameSuffix,
            final Object propValue
    )
    {
        if (DGLStringUtil.isNullOrEmpty(propNameSuffix)) {
            bean.setProperty(key, propValue.toString());
        }
        else {
            throw new IllegalArgumentException("Only string value can be assigned to value of Properties object.");
        }
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    private static void setProperty(
            final Object bean,
            final Type beanType,
            final String propName,
            final Object propValue
    )
    {
        if (LOG.isTraceEnabled()) {
            LOG.trace("setProperty: " + TraceUtil.formatObj(bean, "bean.class.name"));
            LOG.trace("setProperty: " + TraceUtil.formatObj(beanType, "beanType"));
            LOG.trace("setProperty: " + TraceUtil.formatObj(propName, "propName"));
            LOG.trace("setProperty: " + TraceUtil.formatObj(propValue, "propValue.class.name"));
            LOG.trace("setProperty: " + TraceUtil.formatObj(propValue, "propValue.class.typeParameters"));
            LOG.trace("setProperty: " + TraceUtil.formatObj(propValue, "propValue.class.componentType"));
        }
        try {
            if ((propValue instanceof CharSequence) &&
                    propName.startsWith("[")) {
                // Find the corresponding close bracket.
                final int propNameLength = propName.length();
                final int startKeyIdx = 1;
                int endKeyIdx = 1;
                while ((endKeyIdx < propNameLength) &&
                        (propName.charAt(endKeyIdx) != ']')) {
                    endKeyIdx++;
                }
                if (endKeyIdx == propNameLength) {
                    // No matching close bracket.
                    throw new IllegalArgumentException("Property " + TraceUtil.formatObj(propName) +
                            " key has opening index bracket, but not closing index bracket.");
                }
                final String key = propName.substring(startKeyIdx, endKeyIdx);
                final String propNameSuffix = propName.substring(endKeyIdx + 1);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("setProperty: " + TraceUtil.formatObj(key, "key"));
                    LOG.trace("setProperty: " + TraceUtil.formatObj(propNameSuffix, "propNameSuffix"));
                }
                if (bean instanceof Properties) {
                    setPropertyInProperties((Properties)bean, key, propNameSuffix, propValue);
                }
                else if (bean instanceof Map) {
                    // For a map, use the contents of the square brackets as the key of the map.
                    final Map<Object, Object> map = (Map<Object, Object>)bean;
                    if (beanType == null) {
                        // No parameterized type information, so don't do any conversions.
                        map.put(key, propValue.toString());
                    }
                    else if (beanType instanceof ParameterizedType) {
                        final Type[] typeArgs = ((ParameterizedType)beanType).getActualTypeArguments();
                        final Class keyClass = (Class)typeArgs[0];
                        final Class valueClass = (Class)typeArgs[1];
                        if (DGLStringUtil.isNullOrEmpty(propNameSuffix)) {
                            map.put(key, convert(propValue.toString(), valueClass));
                        }
                        else if (propNameSuffix.startsWith(".")) {
                            try {
                                final Object value;
                                if (map.containsKey(key)) {
                                    // TODO: Turn this into a recursive call.
                                    value = map.get(key);
                                }
                                else {
                                    value = valueClass.newInstance();
                                    // noinspection unchecked
                                    map.put(key, value);
                                }
                                setProperty(value, propNameSuffix.substring(1), propValue.toString());
                            }
                            catch (final InstantiationException e) {
                                final FatalException fe = new FatalException("BEAN_PROPERTY_CREATION_EXCEPTION", e);
                                fe.addProperty(keyClass, "keyClass.name");
                                fe.addProperty(valueClass, "valueClass.name");
                                fe.addProperty(propNameSuffix, "propNameSuffix");
                                throw fe;
                            }
                        }
                        else {
                            final FatalException fe = new FatalException("MALFORMED_BEAN_PROPERTY_SUFFIX");
                            fe.addProperty(keyClass, "keyClass.name");
                            fe.addProperty(valueClass, "valueClass.name");
                            fe.addProperty(propNameSuffix, "propNameSuffix");
                            throw fe;
                        }
                    }
                    else {
                        final FatalException fe = new FatalException("MALFORMED_MAP_DECLARATION");
                        fe.addProperty(beanType, "beanType");
                        fe.addProperty(beanType, "beanType.class.name");
                        throw fe;
                    }
                }
                else if (bean instanceof List) {
                    // For a list, use the contents of the square brackets as the index of the list.
                    final List<Object> list = (List<Object>)bean;
                    final int keyIdx = Integer.parseInt(key);
                    if (beanType == null) {
                        // No parameterized type information, so don't do any conversions.
                        list.add(keyIdx, propValue.toString());
                    }
                    else if (beanType instanceof ParameterizedType) {
                        final Type[] typeArgs = ((ParameterizedType)beanType).getActualTypeArguments();
                        final Class valueClass = (Class)typeArgs[0];
                        if (DGLStringUtil.isNullOrEmpty(propNameSuffix)) {
                            final Object value = convert(propValue.toString(), valueClass);
                            if (!valueClass.isAssignableFrom(value.getClass())) {
                                final FatalException fe = new FatalException("CANNOT_CONVERT_VALUE");
                                fe.addProperty(valueClass, "valueClass");
                                fe.addProperty(propValue, "propValue");
                                throw fe;
                            }
                            // Make sure there are enough elements in the list.
                            for (int i = list.size(); i <= keyIdx; i++) {
                                list.add(i, null);
                            }
                            list.set(keyIdx, convert(propValue.toString(), valueClass));
                        }
                        else if (propNameSuffix.startsWith(".")) {
                            try {
                                // Ensure the list size.
                                while (list.size() <= keyIdx) {
                                    list.add(null);
                                }
                                final Object value;
                                if (list.get(keyIdx) == null) {
                                    value = valueClass.newInstance();
                                    list.set(keyIdx, value);
                                }
                                else {
                                    // TODO: Turn this into a recursive call.
                                    value = list.get(keyIdx);
                                }
                                setProperty(value, propNameSuffix.substring(1), propValue.toString());
                            }
                            catch (final InstantiationException e) {
                                final FatalException fe = new FatalException("BEAN_PROPERTY_CREATION_EXCEPTION", e);
                                fe.addProperty(keyIdx, "keyIdx");
                                fe.addProperty(valueClass, "valueClass.name");
                                fe.addProperty(propNameSuffix, "propNameSuffix");
                                throw fe;
                            }
                        }
                        else {
                            final FatalException fe = new FatalException("MALFORMED_BEAN_PROPERTY_SUFFIX");
                            fe.addProperty(keyIdx, "keyIdx");
                            fe.addProperty(valueClass, "valueClass.name");
                            fe.addProperty(propNameSuffix, "propNameSuffix");
                            throw fe;
                        }
                    }
                    else {
                        final FatalException fe = new FatalException("MALFORMED_LIST_DECLARATION");
                        fe.addProperty(beanType, "beanType");
                        fe.addProperty(beanType, "beanType.class.name");
                        throw fe;
                    }
                }
                else if (bean instanceof Set) {
                    // For a set, ignore the contents of the square brackets (it just has to be unique for each
                    // entry of the set). Note that for a set, we cannot have a general bean class as the value,
                    // since the unordered nature of a set means that we cannot know which member to go back to
                    // when we need to set another property of the member.
                    final Set<Object> set = (Set<Object>)bean;
                    if (beanType == null) {
                        // No parameterized type information, so don't do any conversions.
                        set.add(propValue.toString());
                    }
                    else if (beanType instanceof ParameterizedType) {
                        final Type[] typeArgs = ((ParameterizedType)beanType).getActualTypeArguments();
                        final Class valueClass = (Class)typeArgs[0];
                        set.add(convert(propValue.toString(), valueClass));
                    }
                    else {
                        final FatalException fe = new FatalException("MALFORMED_SET_DECLARATION");
                        fe.addProperty(beanType, "beanType");
                        fe.addProperty(beanType, "beanType.class.name");
                        throw fe;
                    }
                }
                else {
                    // Let BeanUtils do its thing.
                    BeanUtils.setProperty(bean, propName, propValue.toString());
                }
            }
            else if ((propValue instanceof CharSequence) &&
                    (propName.indexOf('[') != -1)) {
                final int startKeyIdx = propName.indexOf('[');
                final String propPrefix = propName.substring(0, startKeyIdx);
                final String propSuffix = propName.substring(startKeyIdx);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("setProperty: " + TraceUtil.formatObj(propPrefix, "propPrefix"));
                    LOG.trace("setProperty: " + TraceUtil.formatObj(propSuffix, "propSuffix"));
                }
                final PropertyDescriptor propPrefixDescriptor = getPropertyDescriptorForBean(bean, propPrefix);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("setProperty: " + TraceUtil.formatObj(propPrefixDescriptor, "propPrefixDescriptor.propertyType"));
                }
                if (propPrefixDescriptor == null) {
                    throw new IllegalArgumentException("No bean property for property name " +
                            TraceUtil.formatObj(propPrefix) + " on class " +
                            TraceUtil.formatObj(bean.getClass().getName()));
                }
                if (Properties.class.isAssignableFrom(propPrefixDescriptor.getPropertyType())) {
                    // Property type is a properties object. Get the key from inside the square brackets.
                    Properties props = (Properties)PropertyUtils.getProperty(bean, propPrefix);
                    // Create the properties object if it does not already exist.
                    if (props == null) {
                        props = new Properties();
                        PropertyUtils.setProperty(bean, propPrefix, props);
                    }
                    // Go back and set the value within.
                    setProperty(props, null, propSuffix, propValue.toString());
                }
                else if (Map.class.isAssignableFrom(propPrefixDescriptor.getPropertyType())) {
                    // Property type is a map object. Create the property value if it is null.
                    if (propPrefixDescriptor.getReadMethod() == null) {
                        final FatalException fe = new FatalException("PROPERTY_NOT_READABLE");
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                    Map map = (Map)PropertyUtils.getProperty(bean, propPrefix);
                    if (map == null) {
                        if (propPrefixDescriptor.getWriteMethod() == null) {
                            final FatalException fe = new FatalException("PROPERTY_NOT_WRITABLE");
                            fe.addProperty(propPrefix, "propPrefix");
                            fe.addProperty(propSuffix, "propSuffix");
                            throw fe;
                        }
                        map = new HashMap();
                        PropertyUtils.setProperty(bean, propPrefix, map);
                    }
                    try {
                        final Type type = propPrefixDescriptor.getReadMethod().getGenericReturnType();
                        setProperty(map, type, propSuffix, propValue.toString());
                    }
                    catch (final FatalException fe) {
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                }
                else if (List.class.isAssignableFrom(propPrefixDescriptor.getPropertyType())) {
                    // Property type is a list object. Create the property value if it is null.
                    if (propPrefixDescriptor.getReadMethod() == null) {
                        final FatalException fe = new FatalException("PROPERTY_NOT_READABLE");
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                    List list = (List)PropertyUtils.getProperty(bean, propPrefix);
                    if (list == null) {
                        if (propPrefixDescriptor.getWriteMethod() == null) {
                            final FatalException fe = new FatalException("PROPERTY_NOT_WRITABLE");
                            fe.addProperty(propPrefix, "propPrefix");
                            fe.addProperty(propSuffix, "propSuffix");
                            throw fe;
                        }
                        list = new LinkedList();
                        PropertyUtils.setProperty(bean, propPrefix, list);
                    }
                    try {
                        final Type type = propPrefixDescriptor.getReadMethod().getGenericReturnType();
                        setProperty(list, type, propSuffix, propValue.toString());
                    }
                    catch (final FatalException fe) {
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                }
                else if (Set.class.isAssignableFrom(propPrefixDescriptor.getPropertyType())) {
                    // Property type is a set object. Create the property value if it is null.
                    if (propPrefixDescriptor.getReadMethod() == null) {
                        final FatalException fe = new FatalException("PROPERTY_NOT_READABLE");
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                    Set set = (Set)PropertyUtils.getProperty(bean, propPrefix);
                    if (set == null) {
                        if (propPrefixDescriptor.getWriteMethod() == null) {
                            final FatalException fe = new FatalException("PROPERTY_NOT_WRITABLE");
                            fe.addProperty(propPrefix, "propPrefix");
                            fe.addProperty(propSuffix, "propSuffix");
                            throw fe;
                        }
                        set = new HashSet();
                        PropertyUtils.setProperty(bean, propPrefix, set);
                    }
                    try {
                        final Type type = propPrefixDescriptor.getReadMethod().getGenericReturnType();
                        setProperty(set, type, propSuffix, propValue.toString());
                    }
                    catch (final FatalException fe) {
                        fe.addProperty(propPrefix, "propPrefix");
                        fe.addProperty(propSuffix, "propSuffix");
                        throw fe;
                    }
                }
                else {
                    // Let BeanUtils do its thing.
                    BeanUtils.setProperty(bean, propName, propValue.toString());
                }
            }
            else {
                final PropertyDescriptor propDescriptor = getPropertyDescriptorForBean(bean, propName);
                if (propDescriptor == null) {
                    throw new IllegalArgumentException("No such property \"" + propName + "\" on class " +
                            bean.getClass().getName());
                }
                if ((propValue instanceof String[]) &&
                        Set.class.isAssignableFrom(propDescriptor.getPropertyType())) {
                    // The value is an array, and the property type is a collection. Fill the collection from the array.
                    final Type genericPropType = propDescriptor.getReadMethod().getGenericReturnType();
                    if (genericPropType instanceof ParameterizedType) {
                        final Type[] typeArgs = ((ParameterizedType)genericPropType).getActualTypeArguments();
                        final Class valueClass = (Class)typeArgs[0];
                        Set propList = (Set)PropertyUtils.getProperty(bean, propName);
                        if (propList == null) {
                            if (propDescriptor.getWriteMethod() == null) {
                                throw new IllegalArgumentException("Property " +
                                        TraceUtil.formatObj(propDescriptor.getName()) + " on class " +
                                        bean.getClass().getName() + " is read-only.");
                            }
                            propList = new HashSet();
                            PropertyUtils.setProperty(bean, propName, propList);
                        }
                        for (final String propValueElement : (String[])propValue) {
                            propList.add(convert(propValueElement, valueClass));
                        }
                    }
                    else {
                        // Let BeanUtils do its thing.
                        BeanUtils.setProperty(bean, propName, propValue);
                    }
                }
                else if ((propValue instanceof String[]) &&
                        List.class.isAssignableFrom(propDescriptor.getPropertyType())) {
                    // The value is an array, and the property type is a collection. Fill the collection from the array.
                    final Type genericPropType = propDescriptor.getReadMethod().getGenericReturnType();
                    if (genericPropType instanceof ParameterizedType) {
                        final Type[] typeArgs = ((ParameterizedType)genericPropType).getActualTypeArguments();
                        final Class valueClass = (Class)typeArgs[0];
                        List propList = (List)PropertyUtils.getProperty(bean, propName);
                        if (propList == null) {
                            if (propDescriptor.getWriteMethod() == null) {
                                throw new IllegalArgumentException("Property " +
                                        TraceUtil.formatObj(propDescriptor.getName()) + " on class " +
                                        bean.getClass().getName() + " is read-only.");
                            }
                            propList = new LinkedList();
                            PropertyUtils.setProperty(bean, propName, propList);
                        }
                        for (final String propValueElement : (String[])propValue) {
                            propList.add(convert(propValueElement, valueClass));
                        }
                    }
                    else {
                        // Let BeanUtils do its thing.
                        BeanUtils.setProperty(bean, propName, propValue);
                    }
                }
                else if ((propValue instanceof String[]) &&
                        propDescriptor.getPropertyType().isArray()) {
                    LOG.trace("setProperty: " + TraceUtil.formatObj(propDescriptor, "propDescriptor.propertyType"));
                    final String[] propValueStr = (String[])propValue;
                    final Class componentType = propDescriptor.getPropertyType().getComponentType();
                    final Object[] newPropValue = (Object[])Array.newInstance(componentType, propValueStr.length);
                    for (int i = 0; i < propValueStr.length; i++) {
                        newPropValue[i] = convert(propValueStr[i], componentType);
                    }

                    // Reflist hack.
                    if (RefItem.class.isAssignableFrom(bean.getClass())) {
                        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, propName);
                        final String readMethodName = propertyDescriptor.getReadMethod().getName();
                        final String writeMethodName = "s" + readMethodName.substring(1);
                        final Method writeMethod = bean.getClass().getDeclaredMethod(writeMethodName, propDescriptor.getPropertyType());
                        writeMethod.setAccessible(true);
                        writeMethod.invoke(bean, (Object)newPropValue);
                    }
                    else {
                        // Let BeanUtils do its thing.
                        BeanUtils.setProperty(bean, propName, newPropValue);
                    }
                }
                else {
                    // Let BeanUtils do its thing.
                    BeanUtils.setProperty(bean, propName, propValue);
                }
            }
        }
        catch (final IllegalAccessException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
        }
        catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
        }
        catch (final InvocationTargetException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " +
                    TraceUtil.formatObj(e.getTargetException()), e.getTargetException());
        }
    }

    /**
     * Get the JavaBean property descriptor for the given property of a JavaBean class.
     *
     * @param beanClass
     *      The JavaBean class.
     * @param propName
     *      The name of the property to return the property descriptor for.
     * @return
     *      The property descriptor.
     */
    public static PropertyDescriptor getPropertyDescriptor(
            final Class beanClass,
            final String propName
    )
    {
        // The value to return.
        PropertyDescriptor result;

        final PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(beanClass);
        result = null;
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (DGLStringUtil.equals(propertyDescriptor.getName(), propName)) {
                result = propertyDescriptor;
                break;
            }
        }

        return result;
    }

    /**
     * Get the JavaBean property descriptor for the given property of a JavaBean class.
     *
     * @param bean
     *      The JavaBean object.
     * @param propName
     *      The name of the property to return the property descriptor for.
     * @return
     *      The property descriptor.
     */
    public static PropertyDescriptor getPropertyDescriptorForBean(
            final Object bean,
            final String propName
    )
    {
        // The value to return.
        PropertyDescriptor result;

        try {
            result = PropertyUtils.getPropertyDescriptor(bean, propName);
        }
        catch (final IllegalAccessException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
        }
        catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
        }
        catch (final InvocationTargetException e) {
            throw new IllegalArgumentException("Exception while setting bean property " +
                    TraceUtil.formatObj(propName) + " on config bean of class " +
                    bean.getClass().getName() + ": " +
                    TraceUtil.formatObj(e.getTargetException()), e.getTargetException());
        }

        return result;
    }

    /**
     * Get the JavaBean property value.
     *
     * @param bean
     *      The bean to get the property from.
     * @param propName
     *      The property name to get.
     * @return
     *      The value of the named property.
     *      If there was any problem getting the property,
     *      a value of <code>null</code> is returned.
     */
    public static Object getProperty(
            final Object bean,
            final String propName
    )
    {
        Object result;

        final PropertyDescriptor pd = getPropertyDescriptorForBean(bean, propName);
        if (pd == null) {
            result = null;
        }
        else {
            try {
                result = PropertyUtils.getProperty(bean, propName);
            }
            catch (final IllegalAccessException e) {
                throw new IllegalArgumentException("Exception while getting bean property " +
                        TraceUtil.formatObj(propName) + " on config bean of class " +
                        bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
            }
            catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException("Exception while getting bean property " +
                        TraceUtil.formatObj(propName) + " on config bean of class " +
                        bean.getClass().getName() + ": " + TraceUtil.formatObj(e), e);
            }
            catch (final InvocationTargetException e) {
                throw new IllegalArgumentException("Exception while getting bean property " +
                        TraceUtil.formatObj(propName) + " on config bean of class " +
                        bean.getClass().getName() + ": " +
                        TraceUtil.formatObj(e.getTargetException()), e.getTargetException());
            }
        }

        return result;
    }

    /**
     *  Get the JavaBean property names of an object.
     *
     *  @param beanClass
     *      The class to get the property names from.
     *  @param stopClass
     *      The class to stop looking for properties at,
     *      as per {@link Introspector#getBeanInfo(Class, Class)}.
     *  @return
     *      An unmodifiable collection containing the property names,
     *      in alphabetical order.
     */
    public static Collection<String> getPropertyNames(
            final Class beanClass,
            final Class stopClass
    )
    {
        final Set<String> result = new TreeSet<String>();

        final BeanInfo bi;
        try {
            bi = Introspector.getBeanInfo(beanClass, stopClass);
            final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for (final PropertyDescriptor pd: pds) {
                result.add(pd.getName());
            }
        }
        catch (final IntrospectionException e) {
            // Do nothing.
        }

        return Collections.unmodifiableCollection(result);
    }

    /**
     *  Get the JavaBean property names of an object.
     *
     *  @param beanClass
     *      The class to get the property names from.
     *  @param stopClass
     *      The class to stop looking for properties at,
     *      as per {@link Introspector#getBeanInfo(Class, Class)}.
     *  @return
     *      An unmodifiable collection containing the property names,
     *      in alphabetical order.
     */
    public static Collection<String> getReadablePropertyNames(
            final Class beanClass,
            final Class stopClass
    )
    {
        final Set<String> result = new TreeSet<String>();

        final BeanInfo bi;
        try {
            bi = Introspector.getBeanInfo(beanClass, stopClass);
            final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for (final PropertyDescriptor pd: pds) {
                if (pd.getReadMethod() != null) {
                    result.add(pd.getName());
                }
            }
        }
        catch (final IntrospectionException e) {
            // Do nothing.
        }

        return Collections.unmodifiableCollection(result);
    }

    /**
     *  Get the JavaBean properties for an object.
     */
    public static Map<String, Object> getPropertyValues(
            final Object bean,
            final Class stopClass
    )
    {
        final Map<String, Object> result;

        result = new TreeMap<String, Object>();
        for (final String propName: getReadablePropertyNames(bean.getClass(), stopClass)) {
            final Object propValue = DGLBeanUtil.getProperty(bean, propName);
            result.put(propName, propValue);
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     *  Get the JavaBean properties or an object.
     */
    public static Map<String, Object> getPropertyValues(
            final Object bean,
            final Collection<String> propNames
    )
    {
        final Map<String, Object> result;

        result = new TreeMap<String, Object>();
        for (final String propName: propNames) {
            result.put(propName, getProperty(bean, propName));
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     *  Get all interfaces implemented by a class or extended by an interface.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static Class[] getAllInterfaces(
            final Class cl
    )
    {
        final List<Class> result = new LinkedList<Class>();

        final Class[] declaredInterfaces = cl.getInterfaces();
        for (final Class declaredInterface: declaredInterfaces) {
            result.add(declaredInterface);
            result.addAll(Arrays.asList(getAllInterfaces(declaredInterface)));
        }

        return result.toArray(new Class[result.size()]);
    }

    /**
     */
    public static void applyProperties(
            final Object bean,
            final Map<String, String> props
    )
    {
        for (final Map.Entry<String, String> entry : props.entrySet()) {
            try {
                DGLBeanUtil.setProperty(bean, entry.getKey(), entry.getValue());
            }
            catch (final RuntimeException e) {
                final FatalException fe = FatalException.factory("CONFIG_PROPERTY_SETTER", e);
                fe.addProperty(bean, "bean.class.name");
                fe.addProperty(entry, "entry.key");
                fe.addProperty(entry, "entry.value");
                throw fe;
            }
        }
    }

    /**
     */
    public static void applyProperties(
            final Object bean,
            final Properties props
    )
    {
        for (final Map.Entry<Object, Object> entryObj : props.entrySet()) {
            final String key = (String)entryObj.getKey();
            final String value = (String)entryObj.getValue();
            try {
                DGLBeanUtil.setProperty(bean, key, value);
            }
            catch (final RuntimeException e) {
                final FatalException fe = FatalException.factory("CONFIG_PROPERTY_SETTER", e);
                fe.addProperty(bean, "bean.class.name");
                fe.addProperty(entryObj, "entryObj.key");
                fe.addProperty(entryObj, "entryObj.value");
                throw fe;
            }
        }
    }

    /**
     * A lookup class for use with {@link org.apache.commons.lang.text.StrSubstitutor},
     * and uses {@link DGLBeanUtil#getProperty(Object, String)}.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class BeanLookup
            extends StrLookup
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = TraceUtil.getLogger(BeanLookup.class);

        /**
         * Current value of the <b>context</b> property.
         */
        private Map<String, Object> context;

        /**
         * @param context
         *      A map of strings to objects.
         *      Keys of this map are arbitrary strings.
         *      Values of this map are arbitrary beans.
         */
        public BeanLookup(
                final Map<String, Object> context
        )
        {
            super();

            this.context = context;
        }

        /**
         * @param key
         *      The key string to use for the lookup.
         *      If the key does not contain a dot character,
         *      then the key is used as the key into the context.
         *      If the key contains a dot character,
         *      then the part of the key before the dot is used as the key into the context,
         *      and the resulting value is used as a bean,
         *      dereferenced according to the remainder of <code>code</code>
         *      using {@link DGLBeanUtil#getProperty(Object, String)}.
         */
        public String lookup(
                final String key
        )
        {
            final String result;

            final int dotIdx = key.indexOf('.');
            if (dotIdx == -1) {
                result = String.valueOf(getProperty(context, key));
            }
            else {
                final String mapKey = key.substring(0, dotIdx);
                if (!context.containsKey(mapKey)) {
                    throw new IllegalArgumentException("No map key \"" + mapKey + "\".");
                }
                final Object mapValue = context.get(mapKey);
                final String propName = key.substring(dotIdx + 1);
                result = String.valueOf(getProperty(mapValue, propName));
            }

            return result;
        }

    }

    /**
     */
    private static Object convert(
            final String propValue,
            final Class propClass
    )
    {
        LOG.trace("convert(" + TraceUtil.formatObj(propValue) + ", " + TraceUtil.formatObj(propClass) + ")");

        return ConvertUtils.convert(propValue, propClass);
    }

    /**
     */
//    private static PathElement[] split(
//            final Class beanClass,
//            final String path
//    )
//    {
//        LOG.trace("split(" + TraceUtil.formatObj(beanClass) + ", " + TraceUtil.formatObj(path) + ")");
//
//        final List<PathElement> resultList = new LinkedList<PathElement>();
//
//        boolean inProperty = true;
//        boolean inIndex = false;
//
//        for (final PathElement pe: resultList) {
//            LOG.trace("split: " + TraceUtil.formatObj(pe, "pe"));
//        }
//        return resultList.toArray(new PathElement[resultList.size()]);
//    }

}
