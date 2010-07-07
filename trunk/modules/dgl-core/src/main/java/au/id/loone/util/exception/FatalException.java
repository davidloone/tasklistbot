/*
 * Copyright 2008, David G Loone
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

package au.id.loone.util.exception;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.io.PrintWriter;
import java.io.IOException;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;

/**
 *  Indicates a fatal error that is not expected to be recoverable.
 *
 *  <p>Since this exception is caught and logged at the highest level of the application,
 *      it is recommended to <b>not</b> log this exception
 *      at the point where it is thrown.
 *      Logging exceptions twice is wastful and confusing.</p>
 *
 *  <p>The actual error message is supplied by an
 *      {@link ErrorMessagesRepository error messages repository}.
 *      It is usually best to supply the error messages repository
 *      to the exception at construction time.
 *      But where the error messages repository is not known at construction time,
 *      it can be set to <code>null</code>,
 *      and supplied later via {@link #setMessages(ErrorMessagesRepository)}.
 *      For example,
 *      at a low layer of the application where an SQL is performed,
 *      the error messages repository is not known,
 *      but in the calling method it is known.
 *      The calling method can catch the exception,
 *      set the error messages repository,
 *      then rethrow the exception.</p>
 *
 *  <p>The following attributes are automatically set in the internal map:
 *      <ul>
 *          <li><code>rootCauseClass</code>:
 *              The fully qualified classname of the root cause object,
 *              if it is non-<code>null</code>.</li>
 *          <li><code>rootCauseMessage</code>:
 *              The message from the root cause object,
 *              if it is non-<code>null</code>.</li>
 *          <li><code>username</code>:
 *              The username that was in effect at the time the exception was created,
 *              it it was known.</li>
 *      </ul>
 *  </p>
 *
 *  @author David G Loone
 *
 *  @see au.id.loone.util.tracing.PatternLayout
 */
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public class FatalException
        extends RuntimeException
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(FatalException.class);

    /**
     * The name of the fatal error logger.
     */
    public static final String FATAL_ERROR_LOGGER_NAME = "FATAL_ERRORS";

    /**
     * A logger for fatal exceptions.
     */
    public static final Logger FATAL_ERROR_LOGGER = Logger.getLogger(FATAL_ERROR_LOGGER_NAME);

    /**
     * The default message key for a fatal exception.
     */
    public static final String DEFAULT_MESSAGE_KEY = "FATAL_EXCEPTION";

    /**
     * An empty object array for calling static no-arg methods.
     */
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

    /**
     * A map associated with the exception to record other information. Used to substitute values into
     * user defined messages
     */
    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * The message.
     */
    protected ErrorMessagesRepository.Message message;

    /**
     * The highest timestamp generated so far.
     */
    private static long highestId;

    /**
     * Current value of the <b>id</b> property.
     */
    private String id;

    /**
     * The message key.
     */
    private String messageKey;

    /**
     * The subsystem that originated this exception.
     */
    private String subsystem;

    /**
     * Current value of the <b>timestamp</b> property.
     */
    private long timestamp;

    /**
     * Create a new fatal exception using the {@link #DEFAULT_MESSAGE_KEY default message key}.
     */
    public FatalException()
    {
        this(null, null, null);
    }

    /**
     * Create a new fatal exception using the {@link #DEFAULT_MESSAGE_KEY default message key}.
     *
     * @return
     *      A new fatal exception object.
     *
     * @see #FatalException()
     */
    public static FatalException factory()
    {
        return new FatalException();
    }

    /**
     * Create a new fatal exception using an empty error messages repository.
     *
     * @param messageKey
     *      The key of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     */
    public FatalException(
            final String messageKey
    )
    {
        this(ErrorMessagesRepository.EMPTY, messageKey, null);
    }

    /**
     * Create a new fatal exception using an empty error messages repository.
     *
     * @param messageKey
     *      The key of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @return
     *      A new fatal exception object.
     *
     * @see #FatalException(String)
     */
    public static FatalException factory(
            final String messageKey
    )
    {
        return new FatalException(messageKey);
    }

    /**
     * Create a new fatal exception using an error messages repository.
     *
     * @param messages
     *      The error messages repository to use.
     *      This can be equal to <code>null</code>,
     *      but if so then {@link #setMessages(ErrorMessagesRepository)} must be called ASAP,
     *      possibly by catching the exception and rethrowing it
     *      in a higer level of the application.
     * @param messageKey
     *      The key of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     */
    public FatalException(
            final ErrorMessagesRepository messages,
            final String messageKey
    )
    {
        this(messages, messageKey, null);
    }

    /**
     * Create a new fatal exception using an error messages repository.
     *
     * @param messages
     *      The error messages repository to use.
     *      This can be equal to <code>null</code>,
     *      but if so then {@link #setMessages(ErrorMessagesRepository)} must be called ASAP,
     *      possibly by catching the exception and rethrowing it
     *      in a higer level of the application.
     * @param messageKey
     *      The key of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @return
     *      A new fatal exception object.
     *
     * @see #FatalException(ErrorMessagesRepository, String)
     */
    public static FatalException factory(
            final ErrorMessagesRepository messages,
            final String messageKey
    )
    {
        return new FatalException(messages, messageKey);
    }

    /**
     * Create a new fatal exception using a wrapped exception and the default message key.
     *
     * @param rootCause
     *      The exception that occurred to generate this exception.
     */
    public FatalException(
            final Throwable rootCause
    )
    {
        this(ErrorMessagesRepository.EMPTY, null, rootCause);
    }

    /**
     * Create a new fatal exception using a wrapped exception and the default message key.
     *
     * @param rootCause
     *      The exception that occurred to generate this exception.
     * @return
     *      If <code>rootCause</code> is an instance of {@link FatalException} then that object is returned,
     *      otherwise a new fatal exception object.
     *
     * @see #FatalException(Throwable)
     */
    public static FatalException factory(
            final Throwable rootCause
    )
    {
        return (rootCause instanceof FatalException) ? (FatalException)rootCause : new FatalException(rootCause);
    }

    /**
     * Create a new fatal exception using a wrapped exception and a given message key.
     *
     * @param messageKey
     *      The id of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @param rootCause
     *      The exception that occurred to generate this exception.
     */
    public FatalException(
            final String messageKey,
            final Throwable rootCause
    )
    {
        this(ErrorMessagesRepository.EMPTY, messageKey, rootCause);
    }

    /**
     * Create a new fatal exception using a wrapped exception and a given message key.
     *
     * @param messageKey
     *      The id of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @param rootCause
     *      The exception that occurred to generate this exception.
     * @return
     *      If <code>rootCause</code> is an instance of {@link FatalException} then that object is returned,
     *      otherwise a new fatal exception object.
     *
     * @see #FatalException(String, Throwable)
     */
    public static FatalException factory(
            final String messageKey,
            final Throwable rootCause
    )
    {
        return (rootCause instanceof FatalException) ? (FatalException)rootCause :
                new FatalException(messageKey, rootCause);
    }

    /**
     * Create a new fatal exception using an error messages repository.
     *
     * @param messages
     *      The error messages repository to use.
     *      This can be equal to <code>null</code>,
     *      but if so then {@link #setMessages(ErrorMessagesRepository)} must be called ASAP,
     *      possibly by catching the exception and rethrowing it
     *      in a higer level of the application.
     * @param messageKey
     *      The id of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @param rootCause
     *      The exception that occurred to generate this exception.
     */
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public FatalException(
            final ErrorMessagesRepository messages,
            final String messageKey,
            final Throwable rootCause
    )
    {
        super((ExceptionUtils.findShallowestFatalException(rootCause)  == null) ? messageKey :
                ExceptionUtils.findShallowestFatalException(rootCause).getMessageKey(), rootCause);

        this.messageKey = (messageKey == null) ? DEFAULT_MESSAGE_KEY : messageKey;
        this.timestamp = System.currentTimeMillis();

        // If there is a wrapped FatalException, use the id from there.
        final FatalException nestedFatalException = ExceptionUtils.findShallowestFatalException(rootCause);
        if (nestedFatalException == null) {
            // Generate an id, making some attempt at making sure it is unique. This only guarantes a unique id across the
            // scope of the classloader of this class.
            synchronized (FatalException.class) {
                final DecimalFormat f = new DecimalFormat("000000000000");
                final long idNum = Math.max(System.currentTimeMillis(), highestId + 1);
                highestId = idNum;
                final String timestampStr = f.format(idNum % 1000000000000L);
                id = timestampStr.substring(0, 4) + "-" + timestampStr.substring(4, 8) + "-" + timestampStr.substring(8, 12);
            }
        }
        else {
            id = nestedFatalException.getId();
        }

        // Put the root cause related items into the attributes object.
        if (rootCause != null) {
            attributes.put("fatalException.rootCause.class.name", rootCause.getClass().getName());
            attributes.put("fatalException.rootCause.message", ExceptionUtils.trimLines(rootCause.getMessage(), 10));
        }

        attributes.put("fatalException.messageKey", messageKey);
        if (messages != null) {
            setMessages(messages);
            if (message != null) {
                if (subsystem != null) {
                    attributes.put("fatalException.subsystem", subsystem);
                }
                for (final ErrorMessagesRepository.Audience audience : ErrorMessagesRepository.Audience.getValues()) {
                    attributes.put("fatalException.message.rawMessage." + audience.getName(),
                            message.getRawMessage(audience));
                    attributes.put("fatalException.message.message." + audience.getName(),
                            message.getMessage(audience, attributes));
                }
            }
        }

        // Put some standard common stuff there.
        addProperty(this, "fatalException.id");
        addProperty(this, "fatalException.timestamp");
    }

    /**
     * Create a new fatal exception using an error messages repository.
     *
     * @param messages
     *      The error messages repository to use.
     *      This can be equal to <code>null</code>,
     *      but if so then {@link #setMessages(ErrorMessagesRepository)} must be called ASAP,
     *      possibly by catching the exception and rethrowing it
     *      in a higer level of the application.
     * @param messageKey
     *      The id of the error message to create.
     *      If <code>null</code> then the value from {@link #DEFAULT_MESSAGE_KEY} is used.
     * @param rootCause
     *      The exception that occurred to generate this exception.
     * @return
     *      If <code>rootCause</code> is an instance of {@link FatalException} then that object is returned,
     *      otherwise a new fatal exception object.
     *
     * @see #FatalException(ErrorMessagesRepository, String, Throwable)
     */
    public static FatalException factory(
            final ErrorMessagesRepository messages,
            final String messageKey,
            final Throwable rootCause
    )
    {
        return (rootCause instanceof FatalException) ? (FatalException)rootCause :
                new FatalException(messages, messageKey, rootCause);
    }

    /**
     * Getter method for the <b>id</b> property.
     *
     * <p>The id of this exception is probably unique,
     *      although that is not absolutely guaranteed.
     *      For any application that does not require an <b>absolute</b> guarantee that the id is unique,
     *      it can probably be considered to be so for most practical purposes.</p>
     */
    public String getId()
    {
        return id;
    }

    /**
     * Get the message key.
     *
     * @return
     *      The message key.
     */
    public final String getMessageKey()
    {
        return messageKey;
    }

    /**
     * Supply the messages repository.
     *
     * <p>If the messages repository has already been set,
     *      the this method has no effect.</p>
     *
     * @param messages
     *      The messages repository to use.
     *      This must not be equal to <code>null</code>.
     */
    public final void setMessages(
            final ErrorMessagesRepository messages
    )
    {
        if ((messages != null) && (message == null)) {
            message = messages.getMessage(messageKey);
            subsystem = messages.getSubsystem();
        }
    }

    /**
     * Get the message.
     *
     * @return
     *      The message.
     */
    public String getMessage()
    {
        // The value to return.
        final String result;

        if (message == null) {
            result = super.getMessage();
        }
        else {
            result = message.getMessage(attributes);
        }

        return result;
    }

    /**
     * Get the message.
     *
     * @return
     *      The message.
     */
    public final String getMessage(
            final ErrorMessagesRepository.Audience audience
    )
    {
        return message.getMessage(audience, attributes);
    }

    /**
     * Getter method for the <b>timestamp</b> property.
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Get the message object.
     *
     * @return
     *      The message object.
     */
    public ErrorMessagesRepository.Message getMessageObject()
    {
        return message;
    }

    /**
     * Get the user message for the exception.
     *
     * @return
     *      The substituted user message,
     *      or <code>null</code> if none could be found.
     */
    public final String getUserMessage()
    {
        // The value to return.
        final String result;

        result = getMessageObject().getMessage(ErrorMessagesRepository.Audience.USER, getAttributes());

        return result;
    }

    /**
     * Get the user message for an exception.
     *
     * @param exception
     *      The exception to get the user message for.
     *      If this is an object of class {@link FatalException},
     *      then we delegate to {@link #getUserMessage()}.
     * @return
     *      The substituted user message,
     *      or <code>null</code> if none could be found.
     */
    public static String getUserMessage(
            final Throwable exception
    )
    {
        // The value to return.
        final String result;

        if (exception instanceof FatalException) {
            result = ((FatalException)exception).getUserMessage();
        }
        else {
            result = null;
        }

        return result;
    }

    /**
     * Get all the user messages for the exception.
     * Iterate through the nested exceptions if necessary.
     *
     * @return
     *      The substituted user messages.
     *      This will never be equal to <code>null</code>.
     *      Elements of this list are objects of type {@link String}.
     */
    public List<String> getUserMessages()
    {
        // The value to return.
        final List<String> result = new LinkedList<String>();

        // Start from this exception, and iterate down.
        Throwable exception = this;
        while (exception != null) {
            result.add(getUserMessage(exception));
            exception = ExceptionUtils.getRootCause(exception);
        }

        return result;
    }

    /**
     * Get a list of the context keys for this exception.
     *
     * @return
     *      The list of context keys for this exception.
     *      Elements of this list are objects of class {@link String}.
     */
    public List<String> getUserContextKeys()
    {
        return Collections.unmodifiableList(
                message.getContextKeys(ErrorMessagesRepository.Audience.USER));
    }

    /**
     * Get the attributes object that contains extra information about the exception.
     *
     * @return
     *      The attributes map.
     *      This value will never be equal to <code>null</code>.
     *      Keys of this map are objects of type {@link String},
     *      and will never be equal to <code>null</code>.
     *      Values of this map are objects of class {@link String},
     *      and may be equal to <code>null</code>.
     */
    public Map<String, Object> getAttributes()
    {
        return attributes;
    }

    /**
     * Set an individual attribute.
     *
     * @param name
     *      The name of the attribute to set.
     *      This must not be equal to <code>null</code>.
     * @param value
     *      The value to set the attribute to.
     */
    public void setAttribute(
            final String name,
            final boolean value
    )
    {
        attributes.put(name, String.valueOf(value));
    }

    /**
     * Set an individual attribute.
     *
     * @param name
     *      The name of the attribute to set.
     *      This must not be equal to <code>null</code>.
     * @param value
     *      The value to set the attribute to.
     */
    public void setAttribute(
            final String name,
            final int value
    )
    {
        attributes.put(name, String.valueOf(value));
    }

    /**
     * Set an individual attribute.
     *
     * @param name
     *      The name of the attribute to set.
     *      This must not be equal to <code>null</code>.
     * @param value
     *      The value to set the attribute to.
     */
    public void setAttribute(
            final String name,
            final long value
    )
    {
        attributes.put(name, String.valueOf(value));
    }

    /**
     * Set an individual attribute.
     *
     * @param name
     *      The name of the attribute to set.
     *      This must not be equal to <code>null</code>.
     * @param value
     *      The value to set the attribute to.
     */
    public void setAttribute(
            final String name,
            Object value
    )
    {
        // Yes, a crude attempt!
        if ((name != null) &&
                (name.indexOf("assword") != -1)) {
            value = Void.TYPE;
        }
        attributes.put(name, value);
    }

    /**
     * Get the subsystem that originated this exception.
     *
     * @return
     *      The subsystem.
     */
    public String getSubsystem()
    {
        return subsystem;
    }

    /**
     * Add the properties of a JavaBean to the properties object of this exception.
     *
     * @param bean
     *      The bean to add the properties from.
     * @param prefix
     *      The prefix to use in the attribute names.
     */
    public final void addProperties(
            final Object bean,
            final String prefix
    )
    {
        if (bean != null) {
            try {
                final BeanInfo bi = Introspector.getBeanInfo(bean.getClass(), Object.class);
                final PropertyDescriptor[] pds = bi.getPropertyDescriptors();
                for (final PropertyDescriptor pd : pds) {
                    final Method readMethod = pd.getReadMethod();
                    if (readMethod != null) {
                        try {
                            final String name = prefix + "." + pd.getName();
                            setAttribute(name, readMethod.invoke(bean));
                        }
                        catch (final IllegalAccessException e) {
                            LOG.warn("addProperties: exception adding properties to fatal exception: " +
                                    TraceUtil.formatObj(e), e);
                        }
                        catch (final InvocationTargetException e) {
                            LOG.warn("addProperties: exception adding properties to fatal exception: " +
                                    TraceUtil.formatObj(e), e.getCause());
                        }
                    }
                }
            }
            catch (final IntrospectionException e) {
                LOG.warn("addProperties: exception adding properties to fatal exception", e);
            }
        }
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * <p>For example:
     *      <ul>
     *          <code>addProperty(x, "x.b.c.d"))</code>
     *      </ul>
     *      will add the name/value pair
     *      <ul>
     *          <code>x.b.c.d = "xyz"</code>
     *      </ul>
     *      The method is robust against intermediate null values in the bean path.</p>
     *
     * @param obj
     *      The bean containing the property is to be formatted.
     * @param name
     *      The name of the bean.
     */
    public final void addProperty(
            final Object obj,
            final String name
    )
    {
        // Figure out the actual property path, being the string after the first dot.
        final int dotIdx = name.indexOf((int)'.');
        if (dotIdx == -1) {
            addProperty(obj, name, null);
        }
        else {
            addProperty(obj, name.substring(0, dotIdx), name.substring(dotIdx + 1));
        }
    }

    /**
     */
    private void addProperty(
            final Object bean,
            final String beanName,
            final String propertyPath
    )
    {

        if (bean == null) {
            setAttribute(beanName, null);
        }
        else if (propertyPath == null) {
            setAttribute(beanName, bean);
        }
        else {
            if ((bean instanceof Object[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((Object[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof boolean[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((boolean[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof char[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((char[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof byte[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((byte[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof short[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((short[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof int[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((int[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof long[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((long[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof float[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((float[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof double[]) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((double[])bean).length, beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof Collection) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((Collection)bean).size(), beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof Map) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(((Map)bean).keySet().size(), beanName + "." + propertyPath, null);
            }
            else if ((bean instanceof Map) &&
                    DGLStringUtil.equals(propertyPath, "keys")) {
                addProperty(((Map)bean).keySet(), beanName + "." + propertyPath, null);
            }
            else if (isMapAdaptable(bean) &&
                    (DGLStringUtil.equals(propertyPath, "length") || DGLStringUtil.equals(propertyPath, "size"))) {
                addProperty(asMap(bean).keySet().size(), beanName + "." + propertyPath, null);
            }
            else if (isMapAdaptable(bean) &&
                    DGLStringUtil.equals(propertyPath, "keys")) {
                addProperty(asMap(bean).keySet(), beanName + "." + propertyPath, null);
            }
            else if (DGLStringUtil.equals(propertyPath, "hashCode")) {
                addProperty(hashCode(), beanName + "." + propertyPath, null);
            }
            else {
                final int dotIdx = propertyPath.indexOf((int)'.');
                final String propertyPathComponent;
                final String newPropertyPath;
                if (dotIdx == -1) {
                    propertyPathComponent = propertyPath;
                    newPropertyPath = null;
                }
                else {
                    propertyPathComponent = propertyPath.substring(0, dotIdx);
                    newPropertyPath = propertyPath.substring(dotIdx + 1);
                }

                try {
                    if (propertyPathComponent.endsWith("[*]")) {
                        final String propertyPathComponentBody = propertyPathComponent.substring(0, propertyPathComponent.length() - 3);
                        final PropertyDescriptor[] propertyDescriptors =
                                Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
                        PropertyDescriptor propertyDescriptor = null;
                        for (final PropertyDescriptor pd : propertyDescriptors) {
                            if (pd.getName().equals(propertyPathComponentBody)) {
                                propertyDescriptor = pd;
                                break;
                            }
                        }
                        if (propertyDescriptor == null) {
                            setAttribute(beanName + "." + propertyPathComponentBody + "(?)", Void.TYPE);
                        }
                        else if (propertyDescriptor.getReadMethod() == null) {
                            setAttribute(beanName + "." + propertyPathComponentBody + "(?)", Void.TYPE);
                        }
                        else {
                            propertyDescriptor.getReadMethod().setAccessible(true);
                            final Object newBean = propertyDescriptor.getReadMethod().invoke(bean, EMPTY_OBJECT_ARRAY);

                            if (newBean == null) {
                                addProperty(newBean, beanName + "." + propertyPathComponentBody, newPropertyPath);
                            }
                            else if (newBean instanceof Object[]) {
                                final Object[] newBeanArr = (Object[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof boolean[]) {
                                final boolean[] newBeanArr = (boolean[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof char[]) {
                                final char[] newBeanArr = (char[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof byte[]) {
                                final byte[] newBeanArr = (byte[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof short[]) {
                                final short[] newBeanArr = (short[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof int[]) {
                                final int[] newBeanArr = (int[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof long[]) {
                                final long[] newBeanArr = (long[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof float[]) {
                                final float[] newBeanArr = (float[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof double[]) {
                                final double[] newBeanArr = (double[])newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanArr.length);
                                for (int i = 0; i < newBeanArr.length; i++) {
                                    addProperty(newBeanArr[i], beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                }
                            }
                            else if (newBean instanceof Collection) {
                                final Collection newBeanCollection = (Collection)newBean;
                                final Format fmt = mkFormatterForMaxValue(newBeanCollection.size());
                                int i = 0;
                                for (final Object element : newBeanCollection) {
                                    addProperty(element, beanName + "." + propertyPathComponentBody + "[" + fmt.format(i) + "]", newPropertyPath);
                                    i++;
                                }
                            }
                            else if (newBean instanceof Map) {
                                final Map newBeanMap = (Map)newBean;
                                for (final Object key : newBeanMap.keySet()) {
                                    addProperty(newBeanMap.get(key), beanName + "." + propertyPathComponentBody + "[" + key + "]", newPropertyPath);
                                }
                            }
                            else if (isMapAdaptable(newBean)) {
                                final Map newBeanMap = asMap(newBean);
                                for (final Object key : newBeanMap.keySet()) {
                                    addProperty(newBeanMap.get(key), beanName + "." + propertyPathComponentBody + "[" + key + "]", newPropertyPath);
                                }
                            }
                            else {
                                addProperty(newBean, beanName + "." + propertyPathComponentBody, newPropertyPath);
                            }
                        }
                    }
                    else {
                        final PropertyDescriptor[] propertyDescriptors =
                                Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
                        PropertyDescriptor propertyDescriptor = null;
                        for (final PropertyDescriptor pd : propertyDescriptors) {
                            if (pd.getName().equals(propertyPathComponent)) {
                                propertyDescriptor = pd;
                                break;
                            }
                        }
                        if (propertyDescriptor == null) {
                            setAttribute(beanName + "." + propertyPathComponent + "(?)", Void.TYPE);
                        }
                        else if (propertyDescriptor.getReadMethod() == null) {
                            setAttribute(beanName + "." + propertyPathComponent + "(?)", Void.TYPE);
                        }
                        else {
                            propertyDescriptor.getReadMethod().setAccessible(true);
                            final Object newBean = propertyDescriptor.getReadMethod().invoke(bean, EMPTY_OBJECT_ARRAY);
                            addProperty(newBean, beanName + "." + propertyPathComponent, newPropertyPath);
                        }
                    }
                }
                catch (final IntrospectionException e) {
                    LOG.warn("addProperty: exception adding properties to fatal exception: " + TraceUtil.formatObj(e));
                }
                catch (final IllegalAccessException e) {
                    LOG.warn("addProperty: exception adding properties to fatal exception: " + TraceUtil.formatObj(e));
                }
                catch (final InvocationTargetException e) {
                    LOG.warn("addProperty: exception adding properties to fatal exception: " + TraceUtil.formatObj(e));
                }
            }
        }
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final boolean value,
            final String name
    )
    {
        addProperty(value ? Boolean.TRUE : Boolean.FALSE, name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final char value,
            final String name
    )
    {
        addProperty(new Character(value), name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final short value,
            final String name
    )
    {
        addProperty(new Short(value), name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final int value,
            final String name
    )
    {
        addProperty(new Integer(value), name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final long value,
            final String name
    )
    {
        addProperty(new Long(value), name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final float value,
            final String name
    )
    {
        addProperty(new Float(value), name);
    }

    /**
     * Format a nested JavaBeans property of an object into the exception attributes map.
     *
     * @see #addProperty(Object, String)
     */
    public void addProperty(
            final double value,
            final String name
    )
    {
        addProperty(new Double(value), name);
    }

    /**
     */
    private static boolean isMapAdaptable(
            final Object bean
    )
    {
        final boolean result;

        if (bean == null) {
            result = false;
        }
        else {
            return findAsMapMethod(bean) != null;
        }

        return result;
    }

    /**
     */
    private static Map asMap(
            final Object bean
    )
    {
        Map result;

        final Method m = findAsMapMethod(bean);
        if (m == null) {
            result = null;
        }
        else {
            try {
                result = (Map)m.invoke(bean);
            }
            catch (final IllegalAccessException e) {
                result = null;
            }
            catch (final InvocationTargetException e) {
                result = null;
            }
        }

        return result;
    }

    /**
     */
    private static Method findAsMapMethod(
            final Object bean
    )
    {
        Method result;

        if (bean == null) {
            result = null;
        }
        else {
            try {
                result = bean.getClass().getMethod("asMap");
                if (DGLStringUtil.equals(result.getReturnType().getSimpleName(), Map.class.getName())) {
                    result = null;
                }
            }
            catch (final NoSuchMethodException e) {
                result = null;
            }
        }

        return result;
    }

    /**
     */
    public void formatAttributes(
            final String prefix,
            final PrintWriter w
    )
    {
        final Map<String, Object> props = getAttributes();
        for (final String key : new TreeSet<String>(props.keySet())) {
            if (prefix != null) {
                w.print(prefix);
            }
            w.print(key);
            if (!DGLUtil.equals(props.get(key), Void.TYPE)) {
                w.print(" = ");
                w.print(TraceUtil.formatObj(props.get(key)));
            }
            w.println();
        }
    }

    /**
     */
    public void formatAttributes(
            final String prefix,
            final Appendable w
    )
            throws IOException
    {
        final Map<String, Object> props = getAttributes();
        for (final String key : new TreeSet<String>(props.keySet())) {
            if (prefix != null) {
                w.append(prefix);
            }
            w.append(key);
            if (!DGLUtil.equals(props.get(key), Void.TYPE)) {
                w.append(" = ");
                w.append(TraceUtil.formatObj(props.get(key)));
            }
            w.append("\n");
        }
    }

    /**
     */
    public void formatAttributes(
            final String prefix,
            final StringBuffer w
    )
    {
        try {
            formatAttributes(prefix, (Appendable)w);
        }
        catch (final IOException e) {
            // Should never happen.
        }
    }

    /**
     */
    public void formatAttributes(
            final String prefix,
            final StringBuilder w
    )
    {
        try {
            formatAttributes(prefix, (Appendable)w);
        }
        catch (final IOException e) {
            // Should never happen.
        }
    }

    /**
     */
    private static Format mkFormatterForMaxValue(
            final int maxValue
    )
    {
        return new DecimalFormat(DGLStringUtil.repeat("0", String.valueOf(maxValue).length()));
    }

    /**
     */
    public static class Init
            extends FatalException
    {

        /**
         */
        public Init()
        {
            super();
        }

    }
}
