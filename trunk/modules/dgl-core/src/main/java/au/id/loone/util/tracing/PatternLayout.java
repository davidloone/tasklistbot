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

package au.id.loone.util.tracing;

import java.util.Map;
import java.util.TreeSet;

import au.id.loone.util.exception.ExceptionUtils;
import au.id.loone.util.exception.FatalException;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * An extension of {@link org.apache.log4j.PatternLayout} that provides support for exceptions,
 * particularly {@link FatalException}
 * (with its {@link FatalException#getAttributes() properties}).
 *
 * <p>To use this class,
 *      simply specify it in a Log4j configuration in place of {@link org.apache.log4j.PatternLayout}.
 *      All options remain the same,
 *      except that it will automaticaly log exceptions,
 *      including the attributes attached to {@link FatalException}s.</p>
 *
 * @author David G Loone
 */
public class PatternLayout
        extends org.apache.log4j.PatternLayout
{

    /**
     */
    public PatternLayout()
    {
        super();
    }

    /**
     */
    public PatternLayout(
            final String pattern
    )
    {
        super(pattern);
    }

    /**
     */
    public boolean ignoresThrowable()
    {
        return false;
    }

    /**
     */
    public String format(
            final LoggingEvent event
    )
    {
        final StringBuilder buf = new StringBuilder();
        buf.append(super.format(event));

        final ThrowableInformation throwableInformation = event.getThrowableInformation();
        if (throwableInformation != null) {
            Throwable t = throwableInformation.getThrowable();
            while (t != null) {
                buf.append(t.getClass().getName());
                buf.append(":");
                buf.append(t.getMessage());
                buf.append('\n');
                final StackTraceElement[] stes = t.getStackTrace();
                for (final StackTraceElement ste: stes) {
                    buf.append("    at ");
                    buf.append(ste);
                    buf.append('\n');
                }

                if (t instanceof FatalException) {
                    final FatalException fe = (FatalException)t;
                    final Map<String, Object> props = fe.getAttributes();
                    for (final String key: new TreeSet<String>(props.keySet())) {
                        buf.append("    ");
                        buf.append(key);
                        buf.append(" = ");
                        buf.append(TraceUtil.formatObj(props.get(key)));
                        buf.append("\n");
                    }
                }

                t = ExceptionUtils.getRootCause(t);
                if (t != null) {
                    buf.append("caused by:\n");
                }
            }
        }

        return buf.toString();
    }

}
