package au.id.loone.util.reflist.server.services;

import au.id.loone.util.reflist.RefList;
import au.id.loone.util.reflist.client.services.RefListService;
import au.id.loone.util.reflists.CountryRI;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.gwtwidgets.server.spring.GWTRequestMapping;

/**
 * Implementation of {@link au.id.loone.util.reflist.client.services.RefListService}.
 *
 * @author David G Loone
 */
@GWTRequestMapping(
        value = "/RefListService"
)
public final class RefListServiceImpl
        implements RefListService
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(RefListServiceImpl.class);

    /**
     */
    public RefListServiceImpl()
    {
        super();
    }

    /**
     */
    public RefList[] retrieveRefLists()
    {
        return new RefList[] {RefList.getRefList(CountryRI.class)};
    }

}