package au.id.loone.util.xml.sax;

import java.io.InputStream;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.DGLXMLUtils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;

/**
 * Test class for {@link ContentToFragmentAdapter}.
 *
 * @author David G Loone
 */
public final class ContentToFragmentAdapter_Test
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(ContentToFragmentAdapter_Test.class);

    private static final String PLANET_RI_XML = "/au/id/loone/util/reflists/PlanetRI.xml";

    /**
     */
    public ContentToFragmentAdapter_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_00()
            throws Exception
    {
        final ContentToFragmentAdapter adapter = new ContentToFragmentAdapter(new LoggingFragmentHandler());

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