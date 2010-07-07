package au.id.loone.util.servlet;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import au.id.loone.util.tracing.TraceUtil;

/**
 * Servlet for redirecting to private JSP pages.
 *
 * @author David G Loone
 */
public class PrivateJspRedirectServlet
        extends HttpServlet
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(PrivateJspRedirectServlet.class);

    /**
     */
    public PrivateJspRedirectServlet()
    {
        super();
    }

    /**
     */
    public void doGet(
            final HttpServletRequest request,
            final HttpServletResponse response
    )
            throws ServletException, IOException
    {
        final ServletContext servletContext = request.getSession().getServletContext();
        final String privateLocation = "/WEB-INF/content" + request.getServletPath() + ".jsp";
        LOG.trace("doGet: " + TraceUtil.formatObj(request.getServletPath() + " --> " + TraceUtil.formatObj(privateLocation)));
        final URL resourceLocation = servletContext.getResource(privateLocation);
        if (resourceLocation != null) {
            request.getRequestDispatcher(privateLocation).forward(request, response);
        }
        else {
            super.doGet(request, response);
        }
    }


}
