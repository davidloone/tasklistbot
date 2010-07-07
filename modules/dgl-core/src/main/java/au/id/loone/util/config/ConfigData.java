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

package au.id.loone.util.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;

import au.id.loone.util.ConfigResource;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.ResourceNotFoundException;
import au.id.loone.util.beans.DGLBeanUtil;
import au.id.loone.util.exception.FatalException;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.PropertyConfigurator;

/**
 * Base class for config data xstreamClasses.
 *
 * <p>See {@linkplain au.id.loone.util.config package documentation} for details.</p>
 *
 * @author David G Loone
 */
@SuppressWarnings({"unchecked"})
public abstract class ConfigData
{

    /**
     * The user home directory, obtained from system properties.
     * This is used to substitute for "~" in filenames.
     */
    private static final File USER_HOME;
    static {
        // The user.home system property might not be present, eg GAE.
        final String userHome = System.getProperty("user.home");
        USER_HOME = (userHome == null) ? null : new File(userHome);
    }

    /**
     * A substitution map that all values read from properties resources is passed through.
     *
     * <p>This is constructed to contain the following values:</p>
     * <ul>
     *      <li>All {@linkplain System#getProperties() system properties},
     *          prefixed by "<code>system.</code>".
     *          For example, "<code>system.user.home</code>".</li>
     *      <li>All {@linkplain System#getenv() environment values},
     *          prefixed by "<code>env.</code>".</li>
     * </ul>
     *
     * @see org.apache.commons.lang.text.StrSubstitutor
     */
    public static final Map<String, String> SUBSTITUTION_MAP;
    static {
        final Map<String, String> substitutionMap = new HashMap<String, String>();
        final Properties sysProps = System.getProperties();
        for (final Object keyObj : sysProps.keySet()) {
            final String key = (String)keyObj;
            substitutionMap.put("system." + key, sysProps.getProperty(key));
        }
        for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
            substitutionMap.put("env." + entry.getKey(), entry.getValue());
        }
        SUBSTITUTION_MAP = Collections.unmodifiableMap(substitutionMap);
    }

    /**
     * The name of the bootstrap properties file for this class.
     */
    public static final String BOOTSTRAP_CONFIG_RESOURCE_PATH = "ConfigDataBootstrap.properties";

    /**
     * The bootstrap properties.
     */
    private static final Map<String, String> BOOTSTRAP_PROPS;
    private static final boolean ALLOW_PROCEED_WITHOUT_OVERRIDE;
    static {
        // Read the bootstrap properties from the resource at BOOTSTRAP_CONFIG_RESOURCE_PATH. If there is no
        // bootstrap properties, that isn't treated as an error. The config framework will still work to the
        // extent that config beans can still be created and populated from their local properties resources.

        final Properties bootstrapProps = new Properties();
        InputStream is = null;
        try {
            is = ConfigData.class.getResourceAsStream(BOOTSTRAP_CONFIG_RESOURCE_PATH);
            if (is != null) {
                bootstrapProps.load(is);
                final StrSubstitutor subs = new StrSubstitutor(SUBSTITUTION_MAP);
                for (final Object keyObj : bootstrapProps.keySet()) {
                    final String key = (String)keyObj;
                    bootstrapProps.setProperty(key, subs.replace(bootstrapProps.getProperty(key)));
                }
            }
        }
        catch (final IOException e) {
            throw new RuntimeException(ConfigData.class.getName() +
                    ": Exception while reading config data bootstrap resource \"" +
                    BOOTSTRAP_CONFIG_RESOURCE_PATH + "\": " +
                    e.getClass().getName() + ":" + e.getMessage());
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (final IOException e) {
                    // Do nothing.
                }
            }
        }

        // Turn it into a map.
        final Map<String, String> bootstrapPropsMap = new HashMap<String, String>();
        for (final Map.Entry e : bootstrapProps.entrySet()) {
            bootstrapPropsMap.put((String)e.getKey(), (String)e.getValue());
        }

        BOOTSTRAP_PROPS = Collections.unmodifiableMap(bootstrapPropsMap);
        ALLOW_PROCEED_WITHOUT_OVERRIDE = BOOTSTRAP_PROPS.containsKey("allowProceedWithoutOverrides") &&
                Boolean.parseBoolean(BOOTSTRAP_PROPS.get("allowProceedWithoutOverrides"));
    }

    /**
     * Map of tags to override finder xstreamClasses.
     */
    private static final Map<String, Class> OVERRIDE_FINDER_CLASS_MAP;
    static {
        // Build the override finder class map by looking for the bootstrap property keys that begin with
        // "overrideFinderClassMap". Need to do this first so that we know the xstreamClasses later on when we come
        // to instantiate the individual override finder objects.

        final Map<String, Class> overrideFinderClassMap = new HashMap<String, Class>();
        final Pattern p = Pattern.compile("^overrideFinderClassMap\\[([A-Za-z]*)\\]$");
        for (final Object keyObj : BOOTSTRAP_PROPS.keySet()) {
            final String key = (String)keyObj;
            final Matcher m = p.matcher(key);
            if (m.matches()) {
                final String tag = m.group(1);
                final String className = BOOTSTRAP_PROPS.get(key);
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (classLoader == null) {
                        classLoader = ConfigData.class.getClassLoader();
                    }
                    final Class cl = classLoader.loadClass(className);
                    if (!OverrideFinder.class.isAssignableFrom(cl)) {
                        throw new RuntimeException(ConfigData.class.getName() +
                                ": Override finder class \"" + className + "\" for tag \"" + tag +
                                "\" specified in bootstrap resource \"" + BOOTSTRAP_CONFIG_RESOURCE_PATH +
                                "\" is not a subclass of \"" + OverrideFinder.class.getName() + "\".");
                    }
                    overrideFinderClassMap.put(tag, cl);
                }
                catch (final ClassNotFoundException e) {
                    throw new RuntimeException(ConfigData.class.getName() +
                            ": Override finder class \"" + className + "\" for tag \"" + tag +
                            "\" specified in bootstrap resource \"" + BOOTSTRAP_CONFIG_RESOURCE_PATH +
                            "\" not found.", e);
                }
            }
        }

        OVERRIDE_FINDER_CLASS_MAP = Collections.unmodifiableMap(overrideFinderClassMap);
    }

    /**
     * The override finders specified in the bootstrap file.
     */
    private static final List<OverrideFinder> OVERRIDE_FINDERS;

    /**
     * The index in {@link #OVERRIDE_FINDERS} of the override finder that is actually in use.
     * If no override finder was found,
     * this will be equal to <code>null</code>.
     */
    private static final Integer OVERRIDE_FINDER_INDEX;
    static {
        // Instantiate each override finder so that we can log them. But just choose one to use from now on. That
        // will be the first one in the list (in order of index) that claims to exist.

        final OverrideFinder[] overrideFinders;

        final Pattern p = Pattern.compile("^overrideFinder\\[([0-9]*)\\].([A-Za-z]*)$");

        // First find how many there are so that we can make an array of the correct size.
        int numOverrides = 0;
        int maxOverrideIdx = -1;
        {
            for (final Object keyObj : BOOTSTRAP_PROPS.keySet()) {
                final String key = (String)keyObj;
                final Matcher m = p.matcher(key);
                if (m.matches() &&
                        (m.group(2) != null) &&
                        m.group(2).equals("type")) {
                    final int idx = Integer.parseInt(m.group(1));
                    maxOverrideIdx = (idx > maxOverrideIdx) ? idx : maxOverrideIdx;
                    numOverrides++;
                }
            }
            if (numOverrides != (maxOverrideIdx + 1)) {
                throw new RuntimeException(ConfigData.class.getName() +
                        ": Override index discontiguity in bootstrap resource \"" +
                        BOOTSTRAP_CONFIG_RESOURCE_PATH + "\".");
            }
        }
        if (numOverrides > 100) {
            throw new RuntimeException(ConfigData.class.getName() + ": Too many override finders (" +
                    numOverrides + ") in bootstrap resource \"" + BOOTSTRAP_CONFIG_RESOURCE_PATH + "\".");
        }
        overrideFinders = new OverrideFinder[numOverrides];

        // Then construct the override finder objects and put them into the right places in the array.
        {
            for (final Object keyObj : BOOTSTRAP_PROPS.keySet()) {
                final String key = (String)keyObj;
                final Matcher m = p.matcher(key);
                if (m.matches() &&
                        (m.group(2) != null) &&
                        m.group(2).equals("type")) {
                    final int idx = Integer.parseInt(m.group(1));
                    final String tag = BOOTSTRAP_PROPS.get(key);
                    final Class cl = OVERRIDE_FINDER_CLASS_MAP.get(tag);
                    if (cl == null) {
                        throw new RuntimeException(ConfigData.class.getName() +
                                ": Tag \"" + tag + "\" not defined in bootstrap resource \"" +
                                BOOTSTRAP_CONFIG_RESOURCE_PATH + "\".");
                    }
                    else {
                        try {
                            final OverrideFinder finder = (OverrideFinder)cl.newInstance();
                            finder.setTag(tag);
                            overrideFinders[idx] = finder;
                        }
                        catch (final IllegalAccessException e) {
                            throw new RuntimeException(ConfigData.class.getName() +
                                    ": Exception while constructing override finder bean with index " + idx +
                                    " of class \"" + cl.getName() + "\".", e);
                        }
                        catch (final InstantiationException e) {
                            throw new RuntimeException(ConfigData.class.getName() +
                                    ": Exception while constructing override finder bean with index " + idx +
                                    " of class \"" + cl.getName() + "\".", e);
                        }
                    }
                }
            }
        }

        // Then fill in the bean properties on each finder in the array.
        {
            for (final Object keyObj : BOOTSTRAP_PROPS.keySet()) {
                final String key = (String)keyObj;
                final Matcher m = p.matcher(key);
                if (m.matches()) {
                    final int idx = Integer.parseInt(m.group(1));
                    final String propName = m.group(2);
                    if (!propName.equals("type")) {
                        try {
                            final BeanInfo bi = Introspector.getBeanInfo(overrideFinders[idx].getClass());
                            for (final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                                if ((pd != null) && pd.getName().equals(propName)) {
                                    final Method setter = pd.getWriteMethod();
                                    if (setter == null) {
                                        throw new RuntimeException(ConfigData.class.getName() +
                                                ": Override finder property \"" + propName +
                                                "\" is not writable on bean class \"" +
                                                overrideFinders[idx].getClass().getName() + "\".");
                                    }
                                    if ((setter.getParameterTypes().length != 1) ||
                                            !setter.getParameterTypes()[0].getName().equals(String.class.getName())) {
                                        throw new RuntimeException(ConfigData.class.getName() +
                                                ": Override finder property \"" + propName +
                                                "\" is not of type \"" + String.class.getName() + "\".");
                                    }
                                    setter.invoke(overrideFinders[idx], BOOTSTRAP_PROPS.get(key));
                                    break;
                                }
                            }
                        }
                        catch (final IntrospectionException e) {
                            throw new RuntimeException(ConfigData.class.getName() +
                                    ": Exception while setting bean property \"" + propName +
                                    "\" on override finder bean with index " + idx + ".", e);
                        }
                        catch (final IllegalAccessException e) {
                            throw new RuntimeException(ConfigData.class.getName() +
                                    ": Exception while setting bean property \"" + propName +
                                    "\" on override finder bean with index " + idx + ".", e);
                        }
                        catch (final InvocationTargetException e) {
                            throw new RuntimeException(ConfigData.class.getName() +
                                    ": Exception while setting bean property \"" + propName +
                                    "\" on override finder bean with index " + idx + ".", e);
                        }
                    }
                }
            }
        }

        OVERRIDE_FINDERS = Arrays.asList(overrideFinders);

        // Log them, and find the first one that exists. That is the one that we will use.
        int overrideFinderIndex = -1;
        for (int i = 0; i < OVERRIDE_FINDERS.size(); i++) {
            System.out.println(ConfigData.class.getName() + ": OVERRIDE_FINDERS[" + i + "] = " +
                    OVERRIDE_FINDERS.get(i));
            if (OVERRIDE_FINDERS.get(i).getExists() &&
                    (overrideFinderIndex == -1)) {
                overrideFinderIndex = i;
            }
        }
        OVERRIDE_FINDER_INDEX = (overrideFinderIndex == -1) ? null : overrideFinderIndex;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ConfigData.class);

    static {
        DGLBeanUtil.class.getName();
    }

    /**
     * Map of config data xstreamClasses to config data properties descriptor objects.
     */
    private static final Map<Class, ConfigResource> PROPS_DESCR_MAP_BY_CLASS =
            new HashMap<Class, ConfigResource>();

    /**
     * Map of config data xstreamClasses to config data cumulative properties objects.
     * That is,
     * taking into account all xstreamClasses higher in the inheritance hierarchy.
     */
    private static final Map<Class, Map<String, String>> PROPS_MAP_BY_CLASS =
            new HashMap<Class, Map<String, String>>();

    /**
     * The underlying properties for this object.
     */
    @SuppressWarnings({"FieldCanBeLocal"})
    private Map<String, String> props;

    /**
     */
    public ConfigData()
    {
        super();

        final Map<String, String> overrideProps = (OVERRIDE_FINDER_INDEX == null) ? null :
                OVERRIDE_FINDERS.get(OVERRIDE_FINDER_INDEX).getProps();
        if ((overrideProps == null) && !ALLOW_PROCEED_WITHOUT_OVERRIDE) {
            throw new FatalException("NO_OVERRIDE_PROPS_FOUND");
        }

        // The actual class that we are.
        final Class implementationClass = getClass();

        // Get the cumulative properties for this class.
        final Map<String, String> cumulativeClassProps;
        synchronized (PROPS_MAP_BY_CLASS) {
            // First see if we have this cached.
            if (PROPS_MAP_BY_CLASS.containsKey(implementationClass)) {
                cumulativeClassProps = PROPS_MAP_BY_CLASS.get(implementationClass);
            }
            else {
                // Not cached, so need to build it.

                // Make a list of xstreamClasses in the hierarchy from top (this class) to bottom (the class being
                // instantiated). Do that by iterating up the class hierarchy, then reversing the list.
                final List<Class> classes;
                {
                    classes = new LinkedList<Class>();
                    Class cl = getClass();
                    do {
                        classes.add(cl);
                        cl = cl.getSuperclass();
                    } while (ConfigData.class.isAssignableFrom(cl));
                    Collections.reverse(classes);
                }

                // Iterate down the class hierarchy, so that we set properties on higher xstreamClasses first, then
                // override those values from more specific xstreamClasses.
                final StrSubstitutor subs = new StrSubstitutor(SUBSTITUTION_MAP);
                cumulativeClassProps = new HashMap<String, String>();
                for (final Class cl : classes) {
                    if (!cl.isSynthetic()) {
                        final Properties classProps = getConfigResourceForClass(cl);
                        if (classProps != null) {
                            for (final Map.Entry entry : classProps.entrySet()) {
                                cumulativeClassProps.put((String)entry.getKey(), subs.replace((String)entry.getValue()));
                            }
                        }
                    }
                }
                PROPS_MAP_BY_CLASS.put(implementationClass, Collections.unmodifiableMap(cumulativeClassProps));
            }
        }

        // Then set the bean properties on ourself.
        props = merge(cumulativeClassProps, overrideProps, implementationClass);
        DGLBeanUtil.applyProperties(this, props);
    }

    /**
     */
    private Map<String, String> merge(
            final Map<String, String> classProps,
            final Map<String, String> overrideProps,
            final Class implementationClass
    )
    {
        final Map<String, String> result;

        final String implementationClassName = implementationClass.getName();

        result = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : classProps.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        if (overrideProps != null) {
            for (final Map.Entry<String, String> entry : overrideProps.entrySet()) {
                if (entry.getKey().startsWith(implementationClassName) &&
                        (entry.getKey().length() > (implementationClassName.length() + 1))) {
                    result.put(entry.getKey().substring(implementationClassName.length() + 1), entry.getValue());
                }
            }
        }

        return result;
    }

    /**
     * Retrieve the default properties object for a config class.
     */
    private static Properties getConfigResourceForClass(
            final Class cl
    )
    {
        Properties result;

        synchronized (PROPS_DESCR_MAP_BY_CLASS) {
            ConfigResource propsDesc;
            if (PROPS_DESCR_MAP_BY_CLASS.containsKey(cl)) {
                result = PROPS_DESCR_MAP_BY_CLASS.get(cl).getProperties();
            }
            else if (cl.equals(Init.class)) {
                // Do nothing.
                result = null;
            }
            else {
                try {
                    propsDesc = ConfigResource.factory(cl, cl, "properties");
                    propsDesc.setSubstitutions(SUBSTITUTION_MAP);
                    PROPS_DESCR_MAP_BY_CLASS.put(cl, propsDesc);
                    result = propsDesc.getProperties();
                }
                catch (final ResourceNotFoundException e) {
                    // This is ok. It just means that there isn't a config properties file for a particular
                    // class in the hierarchy.
                    result = null;
                }
            }
        }

        return result;
    }

    /**
     * Abstract base class for override finders.
     *
     * <p>A instance of this class represents a pointer to an external overrides property file.
     *      The actual target of the pointer may or may not exist,
     *      as determined by reading the <b>{@link #getExists() exists}</b> property.</p>
     *
     * <p>The framework automatically instantiated instances of this class,
     *      calling the no-args constructor.
     *      After instantiation,
     *      it calls setter methods to set bean properties that are specified in the bootstrap file.</p>
     *
     * <p>Some useful implementations of this class are provided as standard,
     *      but users are free to implement others for special applications.
     *      The provided standard implementations are:</p>
     * <ul>
     *      <li>{@link FileOverrideFinder}:
     *          Points to a file on the filesystem.
     *          The <b>{@link FileOverrideFinder#setPath(String) path}</b> property is the name of the file.</li>
     *      <li>{@link JNDIFileOverrideFinder}:
     *          Points to a JNDI environment object that in turn points to a file on the filesystem.
     *          The <b>{@link JNDIFileOverrideFinder#setUrl(String) url}</b> property
     *          is the URL in JNDI space of the {@link java.lang.String string} object containing the filepath.</li>
     *      <li>{@link ResourceOverrideFinder}:
     *          Points to a classloader resource.
     *          The <b>{@link ResourceOverrideFinder#setPath(String) path}</b> property is the name of the resource.</li>
     * </ul>
     *
     * @author David G Loone
     */
    public abstract static class OverrideFinder
    {

        /**
         * Current value of the <b>tag</b> property.
         */
        private String tag;

        /**
         */
        public OverrideFinder()
        {
            super();
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
            else if (otherObj instanceof OverrideFinder) {
                final OverrideFinder other = (OverrideFinder)otherObj;
                result = DGLUtil.equals(tag, other.tag);
            }
            else {
                result = false;
            }

            return result;
        }

        /**
         */
        public int hashCode()
        {
            return DGLUtil.hashCode(tag);
        }

        /**
         * Getter method for the <b>exists</b> property.
         *
         * <p>The concrete class should implement this method to compute the appropriate value.
         *      The framework calls this method to determine whether this override finder should be used.
         *      If the value of the <b>exists</b> method is <code>false</code>,
         *      then the <b>{@link #getProps() props}</b> property will never be accessed.</p>
         */
        public abstract boolean getExists();

        /**
         * Getter method for the <b>props</b> property.
         *
         * <p>The concrete class should implement this method to compute the appropriate value.
         *      The framework only calls this method if the <b>{@link #getExists() exists}</b> property
         *      is <code>true</code>.</p>
         *
         * <p>The framework will call this method whenever the override properties is required.
         *      It is up to the implementation class to properly synchronize this method,
         *      and to re-read any external resource as appropriate for the functionality defined for that class.</p>
         */
        public abstract Map<String, String> getProps();

        /**
         * Setter for the <b>tag</b> property.
         */
        public void setTag(
            final String tag
        )
        {
            this.tag = tag;
        }

        /**
         * Getter method for the <b>tag</b> property.
         */
        public String getTag()
        {
            return tag;
        }

    }

    /**
     * A override finder that always claims to exists,
     * and always returns an empty properties map.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class NullOverrideFinder
            extends OverrideFinder
    {

        /**
         */
        private static final Map<String, String> EMPTY_PROPS =
                Collections.unmodifiableMap(new HashMap<String, String>());

        /**
         */
        public NullOverrideFinder()
        {
            super();
        }

        /**
         */
        public String toString()
        {
            return getTag();
        }

        /**
         * Getter method for the <b>exists</b> property.
         */
        public boolean getExists()
        {
            return true;
        }

        /**
         * Getter method for the <b>props</b> property.
         */
        public Map<String, String> getProps()
        {
            return EMPTY_PROPS;
        }

    }

    /**
     * An override finder that looks for a file on the filesystem.
     *
     * <p>The <b>{@link #setPath(String) path}</b> property should be the name of a file on the filesystem.
     *      The path is interpreted by {@link File#File(String)},
     *      and the documentation for that class should be consulted for information about
     *      treatment of absolute and relative filenames.</p>
     *
     * <p>The class will automatically load updates to the override properties file file.
     *      Calls to {@link #getProps()} method compare the current modified timestamp of the file
     *      with the modified timestamp as it was last time the method was called.
     *      If the file has been modified then it is re-read.
     *      If the file has not been modified,
     *      the previous object is returned.</p>
     *
     * @author David G Loone
     */
    public static class FileOverrideFinder
            extends OverrideFinder
    {

        /**
         * Current value of the <b>file</b> property.
         */
        private File file;

        /**
         * Current value of the <b>exists</b> property.
         */
        private Boolean exists;

        /**
         * Current value of the <b>lastModified</b> property.
         */
        private long lastModified;

        /**
         * Current value of the <b>path</b> property.
         */
        private String path;

        /**
         * Current value of the <b>props</b> property.
         */
        private Map<String, String> props;

        /**
         */
        public FileOverrideFinder()
        {
            super();

            exists = null;
            file = null;
            lastModified = 0;
        }

        /**
         */
        public boolean equals(
                final Object otherObj
        )
        {
            final boolean result;

            if (otherObj == this) {
                result = true;
            }
            else if (otherObj == null) {
                result = false;
            }
            else if (otherObj instanceof FileOverrideFinder) {
                final FileOverrideFinder other = (FileOverrideFinder)otherObj;
                result = super.equals(other);
            }
            else {
                result = false;
            }

            return result;
        }

        /**
         */
        public String toString()
        {
            return getTag() + ":" + getPath() + " (" +
                    (getExists() ? ("found at " + getFile().toURI().toString()) : "not found") +
                    ")";
        }

        /**
         * Setter for the <b>exists</b> property.
         */
        protected void setExists(
            final boolean exists
        )
        {
            this.exists = exists;
        }

        /**
         * Getter method for the <b>exists</b> property.
         */
        public synchronized boolean getExists()
        {
            if (exists == null) {
                exists = (file != null) &&
                        file.exists() &&
                        file.isFile() &&
                        file.canRead();

            }

            return exists;
        }

        /**
         * Setter for the <b>file</b> property.
         */
        protected void setFile(
            final File file
        )
        {
            this.file = file;
        }

        /**
         * Getter method for the <b>file</b> property.
         */
        public synchronized File getFile()
        {
            return file;
        }

        /**
         * Setter for the <b>path</b> property.
         */
        public void setPath(
            final String path
        )
        {
            this.path = path;

            if (path.startsWith("~/")) {
                if (USER_HOME == null) {
                    throw new IllegalArgumentException("User home directory (~) not available for path \"" +
                            path + "\".");
                }
                else {
                    setFile(new File(USER_HOME, path.substring(1)));
                }
            }
            else {
                setFile(new File(path));
            }
        }

        /**
         * Getter method for the <b>path</b> property.
         */
        public String getPath()
        {
            return path;
        }

        /**
         * Getter method for the <b>props</b> property.
         */
        public synchronized Map<String, String> getProps()
        {
            if (getExists()) {
                final File f = getFile();
                final long newLastModified = f.lastModified();
                if (newLastModified > lastModified) {
                    lastModified = newLastModified;
                    final Properties props = new Properties();
                    InputStream is = null;
                    try {
                        is = new FileInputStream(f);
                        props.load(is);
                        final Map<String, String> propsMap = new HashMap<String, String>();
                        for (final Object key: props.keySet()) {
                            propsMap.put((String)key, (String)props.get(key));
                        }
                        this.props = Collections.unmodifiableMap(propsMap);
                    }
                    catch (final IOException e) {
                        throw new RuntimeException(FileOverrideFinder.class.getName() +
                                ": Exception while reading properties file at \"" +
                                path + "\".", e);
                    }
                    finally {
                        if (is != null) {
                            try {
                                is.close();
                            }
                            catch (final IOException e) {
                                // Do noting.
                            }
                        }
                    }
                }
            }

            return props;
        }

    }

    /** An override finder that looks for a file on the filesystem.
     *
     * <p>The <b>{@link #setUrl(String) url}</b> property
     *      should be the URL of a {@link java.lang.String} object in the JNDI tree,
     *      that contains the filepath of a file on the filesystem.
     *      The path is interpreted by {@link File#File(String)},
     *      and the documentation for that class should be consulted for information about
     *      treatment of absolute and relative filenames.</p>
     *
     * <p>Once the file has been found,
     *      this class behaves in the same way as the {@link FileOverrideFinder} class.</p>
     *
     * @author David G Loone
     */
    public static class JNDIFileOverrideFinder
            extends FileOverrideFinder
    {

        /**
         * Current value of the <b>url</b> property.
         */
        private String url;

        /**
         */
        public JNDIFileOverrideFinder()
        {
            super();
        }

        /**
         */
        public String toString()
        {
            return getTag() + ":" + getUrl() + " (" +
                    (getExists() ? ("found at " + getFile().toURI().toString()) : "not found") +
                    ")";
        }

        /**
         * Setter for the <b>url</b> property.
         */
        public void setUrl(
            final String url
        )
        {
            this.url = url;

            try {
                final Context initCtx = new InitialContext();
                final Object filePathObj = initCtx.lookup(url);
                if (filePathObj == null) {
                    setExists(false);
                }
                else if (filePathObj instanceof String) {
                    final String filePath = (String)filePathObj;
                    if (filePath.startsWith("~/")) {
                        if (USER_HOME == null) {
                            throw new IllegalArgumentException("User home directory (~) not available for path \"" +
                                    filePath + "\".");
                        }
                        else {
                            setFile(new File(USER_HOME, filePath.substring(1)));
                        }
                    }
                    else {
                        setFile(new File(filePath));
                    }
                }
                else {
                    throw new IllegalArgumentException("JNDI object at \"" + url + "\" is of type \"" +
                            filePathObj.getClass().getName() + "\", should be \"" + String.class.getName() + "\".");
                }
            }
            catch (final NoInitialContextException e) {
                setExists(false);
            }
            catch (final NamingException e) {
                setExists(false);
            }
        }

        /**
         * Getter method for the <b>url</b> property.
         */
        public String getUrl()
        {
            return url;
        }

    }

    /**
     * An override finder that looks for a classloader resource.
     *
     * <p>The <b>{@link #setPath(String) path}</b> property should be the name of a classloader resource.
     *      The path is interpreted by {@link ClassLoader#getResource(String)}.
     *      The path should generally be absolute
     *      (<i>ie</i>, start with a "/" character).</p>
     *
     * <p>The resource is only read once on the first call to {@link #getProps()}.
     *      Subsequent calls to that method return the same object.
     *      It is assumed that classloader resources do not change.</p>
     *
     * @author David G Loone
     */
    public static class ResourceOverrideFinder
            extends OverrideFinder
    {

        /**
         * Current value of the <b>exists</b> property.
         */
        private Boolean exists;

        /**
         * Current value of the <b>path</b> property.
         */
        private String path;

        /**
         * Current value of the <b>props</b> property.
         */
        private Map<String, String> props;

        /**
         */
        public ResourceOverrideFinder()
        {
            super();

            exists = null;
        }

        /**
         */
        public boolean equals(
                final Object otherObj
        )
        {
            final boolean result;

            if (otherObj == this) {
                result = true;
            }
            else if (otherObj == null) {
                result = false;
            }
            else if (otherObj instanceof ResourceOverrideFinder) {
                final ResourceOverrideFinder other = (ResourceOverrideFinder)otherObj;
                result = super.equals(other);
            }
            else {
                result = false;
            }

            return result;
        }

        /**
         */
        public String toString()
        {
            return getTag() + ":" + getPath() + " (" +
                    (getExists() ? ("found at " + getClass().getResource(getPath())) : "not found") +
                    ")";
        }

        /**
         * Getter method for the <b>exists</b> property.
         */
        public synchronized boolean getExists()
        {
            if (exists == null) {
                exists = (getClass().getResource(getPath()) != null);
            }

            return exists;
        }

        /**
         * Setter for the <b>path</b> property.
         */
        public void setPath(
            final String path
        )
        {
            this.path = path;
        }

        /**
         * Getter method for the <b>path</b> property.
         */
        public String getPath()
        {
            return path;
        }

        /**
         * Getter method for the <b>props</b> property.
         */
        public synchronized Map<String, String> getProps()
        {
            if (getExists() && (props == null)) {
                final Properties props = new Properties();
                InputStream is = null;
                try {
                    is = getClass().getResourceAsStream(getPath());
                    if (is == null) {
                        System.err.println("Could not make input stream from props element: " + this);
                    }
                    else {
                        props.load(is);
                        final Map<String, String> propsMap = new HashMap<String, String>();
                        for (final Object key: props.keySet()) {
                            propsMap.put((String)key, (String)props.get(key));
                        }
                        this.props = Collections.unmodifiableMap(propsMap);
                    }
                }
                catch (final IOException e) {
                    throw new RuntimeException(FileOverrideFinder.class.getName() +
                            ": Exception while reading properties resource at \"" +
                            path + "\".", e);
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (final IOException e) {
                            // Do noting.
                        }
                    }
                }
            }

            return props;
        }

    }

    /**
     * A {@linkplain Properties properties} object that is populated from a given resource location,
     * but with overrides from the normal overrides file.
     *
     * @author David G Loone
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class ConfigProps
            extends Properties
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ConfigProps.class);

        /**
         * @param resourceLocation
         *      The location of the classloader resource that is the basis of the values in the this object.
         * @param prefixes
         *      The prefixes of properties in the overrides file
         *      that will be used to override values from the resource at <code>resourceLocation</code>.
         *      If this is <code>null</code>,
         *      all values from the overrides file will be appear in this object.
         *      If this is an empty array,
         *      then no values from the overrides file will be used.
         */
        public ConfigProps(
                final String resourceLocation,
                final String[] prefixes
        )
        {
            super();

            if ((resourceLocation != null) &&
                    !resourceLocation.equals("")) {
                final ConfigResource configResource = ConfigResource.factory(ConfigData.class, resourceLocation);
                configResource.setSubstitutions(SUBSTITUTION_MAP);
                final Properties propsRaw = configResource.getProperties();
                for (final Map.Entry entry : propsRaw.entrySet()) {
                    setProperty((String)entry.getKey(), (String)entry.getValue());
                }
                final Map<String, String> overrideProps = OVERRIDE_FINDERS.get(OVERRIDE_FINDER_INDEX).getProps();
                for (final Map.Entry<String, String> entry : overrideProps.entrySet()) {
                    if (prefixes == null) {
                        setProperty(entry.getKey(), entry.getValue());
                    }
                    else {
                        for (final String prefix : prefixes) {
                            if (entry.getKey().startsWith(prefix)) {
                                setProperty(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * A properties object that can be used directly for Log4j configuration.
     *
     * <p>The properties object is initialised from the properties resource
     *      at the location supplied to the constructor,
     *      This is overridden by all override properties
     *      that begin with strings in {@link #LOG4J_CONFIG_PREFIXES}.</p>
     *
     * @author David G Loone
     */
    public static class Log4jConfigProperties
            extends ExternalConfigProperties
    {

        /**
         * The name of the configuration property that contains the location of the Log4j configuration resource.
         */
        public static final String LOG4J_CONFIG_RESOURCE_LOCATION_PROPNAME = "log4jConfigResourceLocation";

        /**
         * The default Log4j config resource location.
         */
        public static final String DEFAULT_LOG4J_CONFIG_RESOURCE_LOCATION="/log4j.properties";

        /**
         * The location of the Log4j configuration resource.
         */
        public static final String LOG4J_CONFIG_RESOURCE_LOCATION;

        /**
         * The list of property prefixes to extract for the Log4j configuration.
         *
         * <p>Consists of:</p>
         * <ul>
         *      <li><code>log4j.</code></li>
         * </ul>
         */
        public static final String[] LOG4J_CONFIG_PREFIXES = new String[] {
                "log4j."
        };

        /**
         * Figure out the Log4j configuration resource location.
         */
        static {
            LOG4J_CONFIG_RESOURCE_LOCATION = BOOTSTRAP_PROPS.containsKey(LOG4J_CONFIG_RESOURCE_LOCATION_PROPNAME) ?
                    BOOTSTRAP_PROPS.get(LOG4J_CONFIG_RESOURCE_LOCATION_PROPNAME) :
                    DEFAULT_LOG4J_CONFIG_RESOURCE_LOCATION;
        }

        /**
         * Make a Log4j configuration from the properties resource at
         * {@link #LOG4J_CONFIG_RESOURCE_LOCATION}.
         */
        public Log4jConfigProperties()
        {
            this(LOG4J_CONFIG_RESOURCE_LOCATION);
        }

        /**
         * Make a Log4j configuration from the indicated properties resource.
         *
         * @param configResourceLocation
         *      The path to the classloader resource that contains the base Log4j configuration.
         */
        public Log4jConfigProperties(
                final String configResourceLocation
        )
        {
            super(new String[] {"log4j."}, false, configResourceLocation);
        }

    }

    /**
     * A Log4j configurator.
     *
     * <p>Just constructing one of these objects will configure Log4j.</p>
     *
     * @see Log4jConfigProperties
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class Log4jConfigurator
    {

        /**
         */
        public Log4jConfigurator()
        {
            super();

            PropertyConfigurator.configure(new Log4jConfigProperties());
        }

        /**
         */
        public Log4jConfigurator(
                final String resourceLocation
        )
        {
            super();

            PropertyConfigurator.configure(new Log4jConfigProperties(resourceLocation));
        }

    }

    /**
     * A properties object that can be used directly for Hibernate configuration.
     *
     * <p>The properties object is initialised from the properties resource
     *      at the location supplied to the constructor,
     *      This is overridden by all override properties
     *      that begin with strings in {@link #HIBERNATE_CONFIG_PREFIXES}.</p>
     *
     * @author David G Loone
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class HibernateConfigProperties
            extends ExternalConfigProperties
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(HibernateConfigProperties.class);

        /**
         * The name of the configuration property that contains the location of the Hibernate configuration resource.
         */
        public static final String HIBERNATE_CONFIG_RESOURCE_LOCATION_PROPNAME = "hibernateConfigResourceLocation";

        /**
         * The default Hibernate config resource location.
         */
        public static final String DEFAULT_HIBERNATE_CONFIG_RESOURCE_LOCATION="/hibernate-local.properties";

        /**
         * The location of the Hibernate configuration resource.
         */
        public static final String HIBERNATE_CONFIG_RESOURCE_LOCATION;

        /**
         * The list of property prefixes to extract for the hibernate configuration.
         *
         * <p>Consists of:</p>
         * <ul>
         *      <li><code>hibernate.</code></li>
         * </ul>
         */
        public static final String[] HIBERNATE_CONFIG_PREFIXES = new String[] {
                "hibernate."
        };

        /**
         * Figure out the Hibernate configuration resource location.
         */
        static {
            HIBERNATE_CONFIG_RESOURCE_LOCATION = BOOTSTRAP_PROPS.containsKey(HIBERNATE_CONFIG_RESOURCE_LOCATION_PROPNAME) ?
                    BOOTSTRAP_PROPS.get(HIBERNATE_CONFIG_RESOURCE_LOCATION_PROPNAME) :
                    DEFAULT_HIBERNATE_CONFIG_RESOURCE_LOCATION;
        }

        /**
         * Make a Hibernate configuration from the properties resource at
         * {@link #HIBERNATE_CONFIG_RESOURCE_LOCATION}.
         */
        public HibernateConfigProperties()
        {
            this(HIBERNATE_CONFIG_RESOURCE_LOCATION);
        }

        /**
         * Make a Hibernate configuration from the indicated properties resource.
         *
         * @param configResourceLocation
         *      The path to the classloader resource that contains the base Hibernate configuration.
         */
        public HibernateConfigProperties(
                final String configResourceLocation
        )
        {
            super(new String[] {"hibernate."}, false, HIBERNATE_CONFIG_RESOURCE_LOCATION);
        }

    }

    /**
     * A properties object that can be used directly for an external subsystem configuration.
     *
     * @author David G Loone
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static class ExternalConfigProperties
            extends Properties
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(ExternalConfigProperties.class);

        /**
         * Make a properties object for an external subsystem.
         *
         * @param prefixes
         *      The property key prefixes to recognise.
         * @param removePrefix
         *      Whether to remove the prefix from the property keys
         *      when constructing the properties object.
         * @param configResourceLocation
         *      The path to the classloader properties resource that contains the base configuration.
         */
        public ExternalConfigProperties(
                final String[] prefixes,
                final boolean removePrefix,
                final String configResourceLocation
        )
        {
            super();

            if ((configResourceLocation != null) &&
                    !configResourceLocation.equals("")) {
                // Load us from the resource.
                InputStream is = null;
                try {
                    is = ConfigData.class.getResourceAsStream(configResourceLocation);
                    if (is == null) {
                        throw new IllegalArgumentException("Cannot find Hibernate configuration resource at " +
                                TraceUtil.formatObj(configResourceLocation) + " (using " +
                                ConfigData.class.getName() + ")");
                    }
                    load(is);
                }
                catch (final IOException e) {
                    throw new RuntimeException(ConfigData.class.getName() +
                            ": Exception while reading config resource \"" +
                            configResourceLocation + "\": " +
                            e.getClass().getName() + ":" + e.getMessage());
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (final IOException e) {
                            // Nothing to do.
                        }
                    }
                }

                // Do substitutions.
                final StrSubstitutor subs = new StrSubstitutor(SUBSTITUTION_MAP);
                for (final Map.Entry entry : entrySet()) {
                    setProperty((String)entry.getKey(), subs.replace((String)entry.getValue()));
                }

                // Set overrides.
                if ((OVERRIDE_FINDERS != null) &&
                        (OVERRIDE_FINDER_INDEX != null) &&
                        (OVERRIDE_FINDERS.get(OVERRIDE_FINDER_INDEX) != null)) {
                    final Map<String, String> overrideProps = OVERRIDE_FINDERS.get(OVERRIDE_FINDER_INDEX).getProps();
                    for (final Map.Entry<String, String> entry : overrideProps.entrySet()) {
                        for (final String prefix : prefixes) {
                            if (entry.getKey().startsWith(prefix)) {
                                if (removePrefix) {
                                    setProperty(entry.getKey(), entry.getValue().substring(prefix.length()));
                                }
                                else {
                                    setProperty(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Class for retrieving instrumentation data from the config system.
     *
     * @author David G Loone
     */
    public static class Instrumentation
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(Instrumentation.class);

        /**
         * The singleton instance.
         */
        private static Instrumentation INSTANCE = new Instrumentation();

        /**
         */
        private Instrumentation()
        {
            super();
        }

        /**
         * Getter method for the <b>boostrapProperties</b> property.
         *
         * <p>This is the bootstrap properties that was obtained at class loading time.
         *      It never changes.</p>
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public Map<String, String> getBoostrapProperties()
        {
            return BOOTSTRAP_PROPS;
        }

        /**
         * Getter method for the <b>classProps</b> property.
         *
         * <p>This is an unmodifiable snapshot of the map of config data xstreamClasses
         *      to the descriptors of the corresponding properties resources.
         *      The keys/values of this map will never change,
         *      but new keys/values may be added to the map from which this was derived.</p>
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public Map<Class, ConfigResource> getClassProps()
        {
            final Map<Class, ConfigResource> result;

            synchronized (PROPS_DESCR_MAP_BY_CLASS) {
                result = new HashMap<Class, ConfigResource>(PROPS_DESCR_MAP_BY_CLASS);
            }

            return Collections.unmodifiableMap(result);
        }

        /**
         * Getter method for the <b>overrideFinders</b> property.
         *
         * <p>This is the list of override finders that was obtained from the bootstrap properties
         *      at class loading time.
         *      It never changes.</p>
         */
        public List<OverrideFinder> getOverrideFinders()
        {
            return OVERRIDE_FINDERS;
        }

        /**
         * Getter method for the <b>overrideFinderIndex</b> property.
         *
         * <p>This is the index in the <b>{@link #getOverrideFinders}</b> property
         *      of the override finder that is in use in this environment.
         *      It never changes.</p>
         */
        @SuppressWarnings({"UnusedDeclaration"})
        public static int getOverrideFindersIndex()
        {
            return OVERRIDE_FINDER_INDEX;
        }

        /**
         * Static factory method.
         */
        public static Instrumentation factory()
        {
            return INSTANCE;
        }

    }

    /**
     */
    public static class Init
            extends ConfigData
    {

        /**
         */
        public Init()
        {
            super();
        }

    }

}
