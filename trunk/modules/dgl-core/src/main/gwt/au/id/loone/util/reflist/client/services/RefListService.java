package au.id.loone.util.reflist.client.services;

import au.id.loone.util.reflist.RefList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service for managing reflists.
 *
 * @author David G Loone
 */
@RemoteServiceRelativePath(
        value = "services/RefListService"
)
public interface RefListService
        extends RemoteService
{

    /**
     * Retrieve all the reflists.
     */
    public RefList[] retrieveRefLists();

}