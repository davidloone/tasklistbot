package au.id.loone.util.reflist;

import au.id.loone.util.PackageScanner;
import au.id.loone.util.exception.FatalException;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Bean for initialising reflists.
 *
 * @author David G Loone
 */
public final class RefListInitialiser
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(RefListInitialiser.class);

    /**
     * Current value of the <b>packageScanner</b> property.
     */
    private PackageScanner packageScanner;

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public RefListInitialiser()
    {
        super();
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public RefListInitialiser(
            final PackageScanner packageScanner
    )
    {
        super();

        this.packageScanner = packageScanner;
    }

    /**
     */
    public void initialiseRefLists()
    {
        LOG.trace("initialiseRefLists()");

        if (packageScanner.getRefItemClasses() != null) {
            for (final Class<? extends RefItem> refItemClass : packageScanner.getRefItemClasses()) {
                LOG.trace("initialiseRefLists: " + TraceUtil.formatObj(refItemClass, "refItemClass"));
                try {
                    RefListManager.ensureLoaded(refItemClass);
                }
                catch (final RuntimeException e) {
                    final FatalException fe = FatalException.factory(e);
                    fe.addProperty(refItemClass, "refItemClass");
                    LOG.warn("initialiseRefLists: " + TraceUtil.formatObj(e), e);
                    throw fe;
                }
            }
        }

        LOG.trace("~initialiseRefLists");
    }

    /**
     * Setter for the <b>packageScanner</b> property.
     */
    @Required
    public void setPackageScanner(final PackageScanner packageScanner) {this.packageScanner = packageScanner;}

}