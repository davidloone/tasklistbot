package au.id.loone.util.xml.sax;

/**
 * Defines the functionality required by a fragment filter.
 * A fragment filter is simply a fragment handler that keeps track of SAX events
 * and forwards them on to another fragment handler when some condition is satisfied.
 * This interface does not say anything about what that condition should be.
 *
 * <h3>JavaBean Properties</h3>
 *
 * <table align="center" border="1" cellpadding="3" cellspacing="0" width="90%">
 *      <tr>
 *          <th>name</th>
 *          <th>set</th>
 *          <th>get</th>
 *          <th>description</th>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>fragmentHandler</b></td>
 *          <td align="center" valign="top">{@linkplain #setFragmentHandler(FragmentHandler) set}</td>
 *          <td align="center" valign="top">{@linkplain #getFragmentHandler() get}</td>
 *          <td align="left" valign="top">The fragment handler that will be called
 *              when the filter condition is satisfied.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
public interface FragmentFilter
        extends FragmentHandler
{

    /**
     * Setter for the <b>fragmentHandler</b> property.
     */
    public void setFragmentHandler(final FragmentHandler fragmentHandler);

    /**
     * Getter method for the <b>fragmentHandler</b> property.
     */
    public FragmentHandler getFragmentHandler();

}