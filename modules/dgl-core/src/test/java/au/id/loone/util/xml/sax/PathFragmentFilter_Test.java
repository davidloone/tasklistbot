package au.id.loone.util.xml.sax;

import java.io.InputStream;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.DGLXMLUtils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Test class for {@link PathFragmentFilter}.
 *
 * @author David G Loone
 */
public final class PathFragmentFilter_Test
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(PathFragmentFilter_Test.class);

    private static final String PLANET_RI_XML = "/au/id/loone/util/reflists/PlanetRI.xml";

    private static final String URI = "http://loone.id.au/xml/refList_v1.xsd";

    /**
     */
    public PathFragmentFilter_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_00()
            throws Exception
    {
        final PathFragmentFilter propertyDefsFilter = new PathFragmentFilter(
                new LoggingFragmentHandler(),
                new PathFragmentFilter.ElementSpec(URI, "refList"),
                new PathFragmentFilter.ElementSpec(URI, "propertyDefs")
        );

        final ContentToFragmentAdapter adapter = new ContentToFragmentAdapter(propertyDefsFilter);

        final XMLReader reader = DGLXMLUtils.createXMLReader(false);
        reader.setContentHandler(adapter);
        InputStream is = null;
        try {
            is = DocumentContextTracker.class.getResourceAsStream(PLANET_RI_XML);
            final InputSource inputSource = new InputSource(is);
            reader.parse(inputSource);
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }

}