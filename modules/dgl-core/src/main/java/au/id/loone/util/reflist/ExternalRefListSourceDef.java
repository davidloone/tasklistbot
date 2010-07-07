package au.id.loone.util.reflist;

import au.id.loone.util.reflist.transobj.RefListTO;

/**
 * Defines a source of data for an external reflist.
 *
 * @author David G Loone
 */
public interface ExternalRefListSourceDef
{

    /**
     * Retrieve the data fo an external reflist.
     */
    public RefListTO retrieve(
            final Class<? extends ExternalRefItem> refItemClass
    );

}