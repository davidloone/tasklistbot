package au.id.loone.util.xml.sax;

import java.util.List;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Defines the functionality required of a fragment filter that filters based on a simple path specification,
 * expressed as an array of {@link ElementSpec} objects.
 *
 * <h3>JavaBean Properties</h3>
 *
 * <table align="center" border="1" cellpadding="3" cellspacing="0" width="100%">
 *      <tr>
 *          <th>name</th>
 *          <th>set</th>
 *          <th>get</th>
 *          <th>description</th>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>pathMatches</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getPathMatches() get}</td>
 *          <td alilgn="left" valign="top">Takes the value <code>true</code> if the filter
 *              is currently within the target fragment,
 *              <code>false</code> otherwise.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>pathSPec</b></td>
 *          <td align="center" valign="top">{@linkplain #setPathSpec(ElementSpec[]) set}</td>
 *          <td align="center" valign="top">{@linkplain #getPathSpec() get}</td>
 *          <td alilgn="left" valign="top">The path specification for the target fragment.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
public class PathFragmentFilter
        extends FragmentHandlerImpl
        implements FragmentFilter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(PathFragmentFilter.class);

    /**
     * Current value of the <b>fragmentHandler</b> property.
     */
    private FragmentHandler fragmentHandler;

    /**
     * Current value of the <b>pathMatches</b> property.
     */
    private boolean pathMatches;

    /**
     * Current value of the <b>pathSpec</b> property.
     */
    private ElementSpec[] pathSpec;

    /**
     * The local context.
     */
    private DocumentContextTracker context;

    /**
     */
    public PathFragmentFilter()
    {
        super();
    }

    /**
     */
    public PathFragmentFilter(
            final FragmentHandler fragmentHandler,
            final ElementSpec ... pathSpec
    )
    {
        this();

        this.fragmentHandler = fragmentHandler;
        this.pathSpec = pathSpec;
    }

    /**
     * See if the local path specifies a document location that is at or under a path specification.
     *
     * @param context
     *      The current document context.
     */
    private void checkPathMatch(
            final DocumentContextTracker context
    )
    {
        final List<DocumentContextTracker.Element> localPath = context.getLocalPath();
        if ((localPath == null) || (localPath.size() == 0)) {
            // Top of document.
            pathMatches = (pathSpec == null) || (pathSpec.length == 0);
        }
        else if (pathSpec.length > localPath.size()) {
            // If the path specification is longer than the local path, there's no hope.
            pathMatches = false;
        }
        else {
            int i = 0;
            pathMatches = true;
            for (final PathFragmentFilter.ElementSpec elementSpec : pathSpec) {
                if ((i >= localPath.size()) ||
                        !elementSpec.matches(localPath.get(i))) {
                    pathMatches = false;
                    break;
                }
                i++;
            }
        }
    }

    /**
     */
    public void startElement(
            final DocumentContextTracker context,
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
    )
            throws SAXException
    {
        final boolean pathMatches = this.getPathMatches();
        checkPathMatch(context);
        if (this.getPathMatches()) {
            if (pathMatches) {
                // Path was matching before, and now. Must be an element within the fragment.
                fragmentHandler.startElement(this.context, uri, localName, qName, attributes);
            }
            else {
                // Path has just started to match, so this element is the matching one. This is the start of
                // the fragment.
                this.context = context.createSlave();
                fragmentHandler.startFragment(this.context, attributes);
            }
        }
    }

    /**
     */
    public void endElement(
            final DocumentContextTracker context,
            final String uri,
            final String localName,
            final String qName
    )
            throws SAXException
    {
        final boolean pathMatches = this.getPathMatches();
        checkPathMatch(context);
        if (pathMatches) {
            if (this.getPathMatches()) {
                // Path was matching before, and now. Must be an element within the fragment.
                fragmentHandler.endElement(this.context, uri, localName, qName);
            }
            else {
                // Path was matching, but is no longer. Must be end of fragment.
                fragmentHandler.endFragment(this.context);
                this.context = null;
            }
        }
    }

    /**
     */
    public void characters(
            final DocumentContextTracker context,
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        if (getPathMatches()) {
            fragmentHandler.characters(this.context,  chars, start, length);
        }
    }

    /**
     */
    public void ignorableWhitespace(
            final DocumentContextTracker context,
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        if (getPathMatches()) {
            fragmentHandler.ignorableWhitespace(this.context,  chars, start, length);
        }
    }

    /**
     */
    public void processingInstruction(
            final DocumentContextTracker context,
            final String target,
            final String data
    )
            throws SAXException
    {
        if (getPathMatches()) {
            fragmentHandler.processingInstruction(this.context, target, data);
        }
    }

    /**
     */
    public void skippedEntity(
            final DocumentContextTracker context,
            final String name
    )
            throws SAXException
    {
        if (getPathMatches()) {
            fragmentHandler.skippedEntity(this.context, name);
        }
    }

    /**
     * Setter for the <b>fragmentHandler</b> property.
     */
    public void setFragmentHandler(final FragmentHandler fragmentHandler) {this.fragmentHandler = fragmentHandler;}

    /**
     * Getter method for the <b>fragmentHandler</b> property.
     */
    public FragmentHandler getFragmentHandler() {return fragmentHandler;}

    /**
     * Getter method for the <b>pathMatches</b> property.
     */
    public boolean getPathMatches() {return pathMatches;}

    /**
     * Setter for the <b>pathSpec</b> property.
     */
    public void setPathSpec(final ElementSpec[] pathSpec) {this.pathSpec = pathSpec;}

    /**
     * Getter method for the <b>pathSpec</b> property.
     */
    public ElementSpec[] getPathSpec() {return pathSpec;}

    /**
     * A specification of a possible element in the path.
     *
     * @author David G Loone
     */
    public static class ElementSpec
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = TraceUtil.getLogger(ElementSpec.class);

        /**
         * Current value of the <b>localName</b> property.
         */
        private String localName;

        /**
         * Current value of the <b>uri</b> property.
         */
        private String uri;

        /**
         */
        public ElementSpec(
                final String uri,
                final String localName
        )
        {
            super();

            this.localName = localName;
            this.uri = uri;
        }

        /**
         */
        public int hashCode()
        {
            return DGLUtil.hashCode(localName) +
                    DGLUtil.hashCode(uri);
        }

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
            else if (otherObj instanceof ElementSpec) {
                final ElementSpec other = (ElementSpec)otherObj;
                result = DGLUtil.equals(localName, other.localName) &&
                        DGLUtil.equals(uri, other.uri);
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
            return "{" + uri + "}" + localName;
        }

        /**
         * See if this element specification matches an actual element.
         *
         * @param element
         *      The element to check against.
         * @return
         *      The value <code>true</code> if the URI and local name of <code>element</code>
         *      match those of this element specification.
         */
        public boolean matches(
                final DocumentContextTracker.Element element
        )
        {
            return (element != null) &&
                    DGLStringUtil.equals(uri, element.getUri()) &&
                    DGLStringUtil.equals(localName, element.getLocalName());
        }

        /**
         * Getter method for the <b>localName</b> property.
         */
        public String getLocalName() {return localName;}

        /**
         * Getter method for the <b>uri</b> property.
         */
        public String getUri() {return uri;}

    }

}