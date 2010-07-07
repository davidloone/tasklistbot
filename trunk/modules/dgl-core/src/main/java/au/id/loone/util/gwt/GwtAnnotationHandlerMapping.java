/*
 * Copyright 2010, David G Loone
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

package au.id.loone.util.gwt;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

/**
 * Spring HandlerMapping that detects beans annotated with @GwtRpcEndPoint and registers their
 * URLs.
 *
 * <p>From the original at:
 *      <a href="http://blog.digitalascent.com/2007/11/gwt-rpc-with-spring-2x_12.html">GWT-RPC with Spring 2.x</a>.</p>
 *
 * @author Chris Lee, David G Loone
 */
public class GwtAnnotationHandlerMapping
        extends AbstractDetectingUrlHandlerMapping
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(GwtAnnotationHandlerMapping.class);

    /**
     */
    public GwtAnnotationHandlerMapping()
    {
        super();
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    @Override
    protected final String[] determineUrlsForHandler(
            final String beanName
    )
    {
        LOG.trace("determineUrlsForHandler(" + TraceUtil.formatObj(beanName) + ")");

        final String[] urls;
        final Class handlerType = getApplicationContext().getType(beanName);
        if (handlerType.isAnnotationPresent(GwtRpcEndPoint.class)) {
            final GwtRpcEndPoint endPointAnnotation = (GwtRpcEndPoint)handlerType.getAnnotation(GwtRpcEndPoint.class);
            urls = new String[] {
                    endPointAnnotation.path()
            };
        }
        else {
            urls = DGLStringUtil.EMPTY_ARRAY;
        }

        LOG.trace("~determineUrlsForHandler = " + TraceUtil.formatObj(urls));
        return urls;
    }

}
