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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.id.loone.util.tracing.TraceUtil;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Spring HandlerAdapter to dispatch GWT-RPC requests. Relies on handlers registered by GwtAnnotationHandlerMapper
 *
 * <p>From the original at:
 *      <a href="http://blog.digitalascent.com/2007/11/gwt-rpc-with-spring-2x_12.html">GWT-RPC with Spring 2.x</a>.</p>
 *
 * @author Chris Lee, Martin Zdila, David G Loone
 */
public class GwtRcpEndPointHandlerAdapter
        extends RemoteServiceServlet
        implements HandlerAdapter, ApplicationContextAware
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(GwtRcpEndPointHandlerAdapter.class);

    private static ThreadLocal<Object> handlerHolder = new ThreadLocal<Object>();

    private ApplicationContext applicationContext;

    /**
     */
    public GwtRcpEndPointHandlerAdapter()
    {
        super();
    }

    /**
     */
    public long getLastModified(
            final HttpServletRequest request,
            final Object handler
    )
    {
        return -1;
    }

    /**
     */
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    )
            throws Exception
    {
        try {
            // store the handler for retrieval in processCall()
            handlerHolder.set(handler);
            doPost(request, response);
        }
        finally {
            // clear out thread local to avoid resource leak
            handlerHolder.set(null);
        }

        return null;
    }

    /**
     */
    protected Object getCurrentHandler()
    {
        return handlerHolder.get();
    }

    /**
     */
    public boolean supports(
            final Object handler
    )
    {
        return handler instanceof RemoteService && handler.getClass().isAnnotationPresent(GwtRpcEndPoint.class);
    }

    /**
     */
    @Override
    public String processCall(
            final String payload
    )
            throws SerializationException
    {
        /*
        * The code below is borrowed from RemoteServiceServet.processCall, with
        * the following changes:
        *
        * 1) Changed object for decoding and invocation to be the handler
        * (versus the original 'this')
        */
        try {
            final RPCRequest rpcRequest = RPC.decodeRequest(payload, getCurrentHandler().getClass()
                    /* this.getClass() */, this);
            return RPC.invokeAndEncodeResponse(getCurrentHandler() /* this */, rpcRequest.getMethod(),
                    rpcRequest.getParameters(), rpcRequest.getSerializationPolicy());
        }
        catch (final IncompatibleRemoteServiceException e) {
            LOG.warn("processCall: " + TraceUtil.formatObj(e), e);
            return RPC.encodeResponseForFailure(null, e);
        }
        catch (final Exception e) {
            LOG.warn("processCall: " + TraceUtil.formatObj(e), e);
            return RPC.encodeResponseForFailure(null, e);
        }
    }

    /**
     * Setter method for the <b>applicationContext</b> property.
     */
    public void setApplicationContext(final ApplicationContext applicationContext) {this.applicationContext = applicationContext;}

    /**
     * Getter method for the <b>servletContext</b> property.
     */
    @Override
    public ServletContext getServletContext()
    {
        return ((WebApplicationContext)applicationContext).getServletContext();
    }

    /**
     * Getter method for the <b>servletName</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getServletName() {return "abc";}

}