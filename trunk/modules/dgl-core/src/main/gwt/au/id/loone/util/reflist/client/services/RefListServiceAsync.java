package au.id.loone.util.reflist.client.services;

import au.id.loone.util.reflist.RefList;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async service definition for {@link RefListService}.
 *
 * @author David G Loone
 */
public interface RefListServiceAsync
{

    /**
     */
    public void retrieveRefLists(
            final AsyncCallback<RefList[]> callback
    );

}