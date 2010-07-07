/*
 * Copyright 2007, David G Loone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.loone.util.reflist;

import java.io.Serializable;
import java.util.Set;

/**
 *  Base definition for a reference item.
 *
 *  <p>The core distinguishing feature of a Reference Item
 *      is that it has a code and a description.
 *      The code is the value that is usually passed around between systems,
 *      and is stored when a Reference Item is persisted.
 *      The description is the value that is usually presented to a user.
 *      Reference Items usually hang about in groups
 *      called a Reference Lists.
 *      A Reference List is an ordered list of Reference Items,
 *      thus each Reference Item in a Reference List has an ordinal value.
 *      Ordinal values are integers that start at zero
 *      and increase monotonically and without gaps.
 *      Certain Reference Items in a Reference List can also be marked as obsolete,
 *      and Reference Items so marked
 *      do not take part in certain aspects of the Reference List.</p>
 *
 *  @author David G Loone
 */
public interface RefItem
        extends Comparable<RefItem>, Serializable, Cloneable
{

    /**
     * Getter method for the <b>aliases</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public Set<String> getAliases();

    /**
     *  Getter method for the <b>code</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getCode();

    /**
     *  Getter method for the <b>description</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getDescription();

    /**
     *  Getter method for the <b>obsolete</b> property.
     *
     *  <p>As a general rule,
     *      items should not be removed from a reflist.
     *      This is mainly because applications may have saved reference item codes into persistent store,
     *      which then cannot be resolved if that reference item is no longer in the list.
     *      Instead,
     *      mark removed items as obsolete.
     *      The will no longer be returned by the {@link RefList#getItems()},
     *      but will be returned by the {@link RefList#getAllItems()} method.</p>
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public boolean getObsolete();

    /**
     *  Getter method for the <b>ord</b> property.
     *
     *  <p>The ordinal values of items in a reflist always start at zero and increment for each item in the list.
     *      Each item in the list has an ordinal value.
     *      There are no gaps in the ordinal values
     *      (<i>ie</i>, the last item in the list will always have an ordinal value of length&nbsp;-&nbsp;1.
     *      Although ordinal values are unique across the reflist,
     *      they should not be used as keys,
     *      since if the reference list contents changes
     *      (<i>i</i>, adding or removing elements)
     *      the ordinal value allocations will also change.</p>
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getOrd();

}
