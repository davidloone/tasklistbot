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

import java.util.StringTokenizer;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import au.id.loone.util.DGLStringUtil;

/**
 * Utilities for manipulating exceptions and exception data.
 *
 * @author David G Loone
 */
public class ExceptionUtils
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(ExceptionUtils.class);

    /**
     *  The array of known root cause factories.
     *  These are used by the {@link #getRootCause(Throwable)} method.
     *
     *  <p>The following exception root cause factory xstreamClasses are checked:
     *      <ul>
     *          <li>{@link ExceptionInInitializerErrorRootCauseFactory}</li>
     *          <li>{@link InvocationTargetExceptionRootCauseFactory}</li>
     *          <li>{@link UndeclaredThrowableExceptionRootCauseFactory}</li>
     *          <li>{@link ServletExceptionRootCauseFactory}</li>
     *          <li>{@link RemoteExceptionRootCauseFactory}</li>
     *      </ul>
     *      Using the root cause factories makes this method work ok even if one or more of the exception xstreamClasses
     *      are unloadable.
     *      They may be unloadable if the application was compiled against the exception xstreamClasses,
     *      but they do not exist in the runtime.</p>
     *
     *  @see #getRootCause(Throwable)
     */
    public static final RootCauseFactory[] ROOT_CAUSE_FACTORIES = new RootCauseFactory[] {
        new ExceptionInInitializerErrorRootCauseFactory(),
        new InvocationTargetExceptionRootCauseFactory(),
        new UndeclaredThrowableExceptionRootCauseFactory(),
        new ServletExceptionRootCauseFactory(),
        new RemoteExceptionRootCauseFactory()
    };

    /**
     * Gets the stack trace for the given {@link Throwable} as a {@link String}.
     *
     * @param t The exception to get the stack trace for.
     * @return The stack trace for <code>t</code>
     */
    public static String getStackTraceAsString(
            final Throwable t
    )
    {
        final StringBuilder buf = new StringBuilder();
        final StackTraceElement[] stes = t.getStackTrace();
        for (final StackTraceElement ste : stes) {
            buf.append("    at ");
            buf.append(ste);
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Gets the nested stack trace for the given {@link Throwable} as a {@link String}.
     *
     * @param t The exception to get the nested stack trace for.
     * @return The stack trace for <code>t</code>
     */
    public static String getNestedStackTraceAsString(
            final Throwable t
    )
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        getNestedStackTraceAsString(pw, t);
        return sw.toString();
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void getNestedStackTraceAsString(
            final Appendable buf,
            final Throwable t
    )
            throws IOException
    {
        buf.append(getNestedMessageAsString(t));
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void getNestedStackTraceAsString(
            final StringBuffer buf,
            final Throwable t
    )
    {
        buf.append(getNestedMessageAsString(t));
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void getNestedStackTraceAsString(
            final StringBuilder buf,
            final Throwable t
    )
    {
        buf.append(getNestedStackTraceAsString(t));
    }

    /**
     */
    public static void getNestedStackTraceAsString(
            final PrintWriter pw,
            Throwable t
    )
    {
        final String prefixAtom = ExceptionConfig.factory().getStackTracePrefixAtom();
        String prefix = "";
        while (t != null) {
            final StringBuffer messageBuffer = new StringBuffer();
            messageBuffer.append(t.getClass().getName());
            if (t instanceof FatalException) {
                messageBuffer.append(":");
                messageBuffer.append(((FatalException)t).getId());
            }
            if (!DGLStringUtil.isNullOrEmpty(t.getMessage())) {
                messageBuffer.append(":");
                messageBuffer.append(t.getMessage());
            }
            messageBuffer.append("\n");
            final String stackTrace;
            if (t instanceof java.rmi.RemoteException) {
                stackTrace = trimLines(getStackTraceAsString(t), 2);
            }
            else {
                stackTrace = getStackTraceAsString(t);
            }
            messageBuffer.append(stackTrace);
            if (t instanceof FatalException) {
                final FatalException fe = (FatalException)t;
                fe.formatAttributes("    ", messageBuffer);
                if ((fe.getMessageObject() != null) &&
                        (fe.getMessageObject().getFaults() != null) &&
                        (fe.getMessageObject().getFaults().size() != 0)) {
                    messageBuffer.append("Diagnostics:\n");
                    for (final ErrorMessagesRepository.Fault fault : ((FatalException)t).getMessageObject().getFaults()) {
                        messageBuffer.append("Cause: ");
                        messageBuffer.append(fault.getCause(fe.getAttributes()));
                        messageBuffer.append("\n");
                        messageBuffer.append("Action: ");
                        messageBuffer.append(fault.getAction(fe.getAttributes()));
                        messageBuffer.append("\n");
                    }
                }
            }

            // Append the message buffer, with the prefix.
            for (final StringTokenizer st = new StringTokenizer(messageBuffer.toString(), "\n");
                    st.hasMoreTokens();) {
                pw.print(prefix);
                pw.print(st.nextToken());
                pw.println();
            }
            prefix = prefix + prefixAtom;
            t = getRootCause(t);
        }
    }

    /**
     * Get the deepest nested exception.
     */
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "UnusedDeclaration"})
    public static Throwable findDeepestRootCause(
            Throwable t
    )
    {
        // Do this in such a way as to minimise calls to getRootCause, since it's a bit expensive.
        Throwable rootCause = getRootCause(t);
        while (rootCause != null) {
            t = rootCause;
            rootCause = getRootCause(t);
        }
        return t;
    }

    /**
     * Get the deepest nested {@link FatalException}.
     */
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "UnusedDeclaration"})
    public static FatalException findDeepestFatalException(
            Throwable t
    )
    {
        FatalException result = (t instanceof FatalException) ? (FatalException)t : null;
        Throwable rootCause = getRootCause(t);
        while (rootCause != null) {
            t = rootCause;
            result = (t instanceof FatalException) ? (FatalException)t : null;
            rootCause = getRootCause(t);
        }
        return result;
    }

    /**
     * Get the shallowest nested {@link FatalException}.
     */
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    public static FatalException findShallowestFatalException(
            Throwable t
    )
    {
        FatalException result;

        Throwable rootCause = getRootCause(t);
        result = (t instanceof FatalException) ? (FatalException)t : null;
        while ((rootCause != null) && (result == null)) {
            t = rootCause;
            rootCause = getRootCause(t);
            result = (t instanceof FatalException) ? (FatalException)t : null;
        }
        return result;
    }

    /**
     * Gets the nested stack trace for the given {@link Throwable} as a {@link String}.
     *
     * @param t The exception to get the nested stack trace for.
     * @return The stack trace for <code>t</code>
     */
    public static String getNestedMessageAsString(
            final Throwable t
    )
    {
        // The value to return.
        String result;
        // The root cause.
        final Throwable rootCause = getRootCause(t);

        result = t.getMessage();
        if (rootCause != null) {
            result = result + "\ncaused by:\n" + getNestedMessageAsString(rootCause);
        }

        return result;
    }

    /**
     * Get the nested (root cause) exception were possible from an arbitrary exception object.
     *
     * <p>This method contains all the smarts about what exceptions might contain root causes,
     *      and the method by which the root cause can be extracted.</p>
     *
     * @param exception The exception that may have a root cause.
     * @return The root cause exception for <code>exception</code>
     *         or <code>null</code> if there was no root cause.
     * @see #ROOT_CAUSE_FACTORIES
     */
    public static Throwable getRootCause(
            final Throwable exception
    )
    {
        // The value to return.
        Throwable result;

        if (exception == null) {
            result = null;
        }
        else {
            result = null;
            for (final RootCauseFactory rootCauseFactory : ROOT_CAUSE_FACTORIES) {
                try {
                    result = rootCauseFactory.getRootCause(exception);
                }
                catch (final Throwable e) {
                    // Ignore this.
                }
                if (result != null) {
                    break;
                }
            }
            if (result == null) {
                result = exception.getCause();
            }
        }

        return result;
    }

    /**
     * Trim a string to a given number of lines.
     *
     * @param str    The string to trim.
     * @param nLines The number of lines to return.
     * @return The first <code>nLines</code> lines of <code>str</code>.
     *         If the input string was truncated,
     *         then an extra line is appended with elipses ("<code>...</code>").
     */
    public static String trimLines(
            final String str,
            final int nLines
    )
    {
        // The value to return.
        final String result;

        if (str == null) {
            result = null;
        }
        else {
            // The buffer for building the return value.
            final StringBuffer buf = new StringBuffer();
            // The tokenizer.
            final StringTokenizer st = new StringTokenizer(str, "\n");

            // Iterate until either we run out of lines or we reach the indicated maximum number
            // of lines.
            for (int i = 0; (i < nLines) && st.hasMoreTokens(); i++) {
                buf.append(st.nextToken());
                if (st.hasMoreTokens()) {
                    buf.append("\n");
                }
            }
            // Indicate that the string was truncated.
            if (st.hasMoreTokens()) {
                buf.append("...\n");
            }

            result = buf.toString();
        }

        return result;
    }

    /**
     *  Describes a class for extracting a root cause of an exception.
     *
     *  <p>This provides the basis of a system for finding the root cause of an exception
     *      in a way that is resilient against the exception xstreamClasses that we want to test for
     *      being unloadable.</p>
     */
    public interface RootCauseFactory
    {

        /**
         *  Get the throwable class that we are designed to disentangle.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        Class getThrowableClass();

        /**
         *  Get the full name of the throwable class.
         *  This works, even if the throwable class does not exist in this environment.
         */
        String getThrowableClassName();

        /**
         *  Get the root cause of an exception.
         *
         *  @return
         *      The root cause of the exception,
         *      or <code>null</code> if there is no root cause for whatever reason.
         */
        Throwable getRootCause(
                Throwable exception
        );

    }

    /**
     *  Abstract implementation of {@link RootCauseFactory} to ease concrete implementations.
     */
    public abstract static class RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         *  Current value of the <b>throwableClass</b> property.
         */
        private Class itsThrowableClass;

        /**
         *  Current value of the <b>throwableClassName</b> property.
         */
        private final String itsThrowableClassName;

        /**
         *  Create the root cause factory without throwing an exception if the class cannot be loaded.
         *
         *  @param throwableClassName
         *      The fully qualified name of the exception class that this root cause factory targets.
         *      If the class cannot be loaded in the runtime,
         *      then an error message is printed to <code>System.out</code>.
         */
        public RootCauseFactoryImpl(
                final String throwableClassName
        )
        {
            super();
            itsThrowableClassName = throwableClassName;

            try {
                itsThrowableClass = Class.forName(getThrowableClassName());
            }
            catch (final Throwable e) {
                itsThrowableClass = null;
                System.out.println("RootCauseFactory: " +
                        "Warning: will not be able to find root cause of exceptions of class " +
                        itsThrowableClassName + ": " + e.getClass().getName() + ":" + e.getMessage());
            }
        }

        /**
         *  Getter method for the <b>throwableClass</b> property.
         */
        public final Class getThrowableClass()
        {
            return itsThrowableClass;
        }

        /**
         *  Getter method for the <b>throwableClassName</b> property.
         */
        public final String getThrowableClassName()
        {
            return itsThrowableClassName;
        }

        /**
         */
        public final Throwable getRootCause(
                final Throwable exception
        )
        {
            Throwable result;

            if (itsThrowableClass == null) {
                result = null;
            }
            else {
                try {
                    result = getRootCause1(exception);
                }
                catch (final Throwable e) {
                    result = null;
                }
            }

            return result;
        }

        /**
         *  Concrete xstreamClasses should implement this method to translate the exception into a root cause.
         *  It can throw whatever exception it likes to indicate that the root cause is not available.
         */
        public abstract Throwable getRootCause1(
                Throwable exception
        )
                throws Throwable;

    }

    /**
     *  Root cause factory for class <code>java.lang.ExceptionInInitializerError</code>.
     */
    public static final class ExceptionInInitializerErrorRootCauseFactory
            extends RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         */
        public ExceptionInInitializerErrorRootCauseFactory()
        {
            super("java.lang.ExceptionInInitializerError");
        }

        /**
         */
        public Throwable getRootCause1(
                final Throwable exception
        )
                throws Throwable
        {
            return ((java.lang.ExceptionInInitializerError)exception).getException();
        }

    }

    /**
     *  Root cause factory for class {@link java.lang.reflect.InvocationTargetException}.
     */
    public static final class InvocationTargetExceptionRootCauseFactory
            extends RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         */
        public InvocationTargetExceptionRootCauseFactory()
        {
            super("java.lang.reflect.InvocationTargetException");
        }

        /**
         */
        public Throwable getRootCause1(
                final Throwable exception
        )
                throws Throwable
        {
            return ((java.lang.reflect.InvocationTargetException)exception).getTargetException();
        }

    }

    /**
     *  Root cause factory for class <code>java.lang.reflect.UndeclaredThrowableException</code>.
     */
    public static final class UndeclaredThrowableExceptionRootCauseFactory
            extends RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         */
        public UndeclaredThrowableExceptionRootCauseFactory()
        {
            super("java.lang.reflect.UndeclaredThrowableException");
        }

        /**
         */
        public Throwable getRootCause1(
                final Throwable exception
        )
                throws Throwable
        {
            return ((java.lang.reflect.UndeclaredThrowableException)exception).getUndeclaredThrowable();
        }

    }

    /**
     *  Root cause factory for class {@link javax.servlet.ServletException}.
     */
    public static final class ServletExceptionRootCauseFactory
            extends RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         */
        public ServletExceptionRootCauseFactory()
        {
            super("javax.servlet.ServletException");
        }

        /**
         */
        public Throwable getRootCause1(
                final Throwable exception
        )
                throws Throwable
        {
            return ((javax.servlet.ServletException)exception).getRootCause();
        }

    }

    /**
     *  Root cause factory for class {@link java.rmi.RemoteException}.
     */
    public static final class RemoteExceptionRootCauseFactory
            extends RootCauseFactoryImpl
            implements RootCauseFactory
    {

        /**
         */
        public RemoteExceptionRootCauseFactory()
        {
            super("java.rmi.RemoteException");
        }

        /**
         */
        public Throwable getRootCause1(
                final Throwable exception
        )
                throws Throwable
        {
            return ((java.rmi.RemoteException)exception).detail;
        }

    }

}
