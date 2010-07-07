package au.id.loone.util.xml.sax;

import java.io.InputStream;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.DGLXMLUtils;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Test class for {@link DocumentContextTracker}.
 *
 * @author David G Loone
 */
public final class DocumentContextTracker_Test
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DocumentContextTracker_Test.class);

    private static final String REFLIST_XSD = "/au/id/loone/xml/reflist_v1.xsd";

    private static final String SCHEMA_XSD = "/org/w3/www/2001/XMLSchema.xsd";

    private static final String PLANET_RI_XML = "/au/id/loone/util/reflists/PlanetRI.xml";

    /**
     */
    public DocumentContextTracker_Test()
    {
        super();
    }

    /**
     *
     */
    @Test
    public void test_00()
            throws Exception
    {
        final DocumentContextTracker documentContextTracker = new DocumentContextTracker();

        final XMLReader reader = DGLXMLUtils.createXMLReader(false);
        reader.setContentHandler(documentContextTracker);
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