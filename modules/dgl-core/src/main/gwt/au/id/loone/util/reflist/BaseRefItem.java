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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import au.id.loone.util.DGLUtil;

/**
 *  Default implementation of a reference item,
 *  doing most of the heavy lifting for all reference lists in the system.
 *
 *  @author David G Loone
 */
@SuppressWarnings({"CloneDoesntCallSuperClone"})
public abstract class BaseRefItem
        implements RefItem
{

    /**
     *  Current value of the <b>aliases</b> property.
     */
    private Set<String> aliases;

    /**
     *  Current value of the <b>code</b> property.
     */
    private String code;

    /**
     *  Current value of the <b>description</b> property.
     */
    private String description;

    /**
     *  Current value of the <b>obsolete</b> property.
     */
    private boolean obsolete;

    /**
     *  Current value of the <b>ord</b> property.
     */
    int ord;

    /**
     */
    protected BaseRefItem(
            final String code,
            final String description,
            final boolean obsolete
    )
    {
        this(code, description, obsolete, null);
    }

    /**
     */
    protected BaseRefItem(
            final String code,
            final String description,
            final boolean obsolete,
            final Set<String> aliases
    )
    {
        super();

        this.aliases = aliases;
        this.code = code;
        this.description = description;
        this.obsolete = obsolete;
    }

    /**
     */
    public String toString()
    {
        return getDescription();
    }

    /**
     */
    public int hashCode()
    {
        return DGLUtil.hashCode(code);
    }

    /**
     */
    public boolean equals(
            final Object otherObj
    )
    {
        final boolean result;

        if (otherObj == null) {
            result = false;
        }
        else if (otherObj == this) {
            result = true;
        }
        else if (otherObj instanceof BaseRefItem) {
            final BaseRefItem other = (BaseRefItem)otherObj;
            result = DGLUtil.equals(code, other.code);
        }
        else {
            result = false;
        }

        return result;
    }

    /**
     *  @return
     *      The value of comparing the <b>ord</b> properties.
     */
    public int compareTo(
            final RefItem otherObj
    )
    {
        return DGLUtil.compare(ord, ((BaseRefItem)otherObj).ord);
    }

    /**
     */
    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
    public Object clone()
    {
        // Ref item objects are immutable, so it's ok to return the same object.
        return this;
    }

    /**
     *  Getter method for the <b>aliases</b> property.
     */
    public Set<String> getAliases()
    {
        return aliases;
    }

    /**
     *  Getter method for the <b>code</b> property.
     */
    public String getCode()
    {
        return code;
    }

    /**
     *  Getter method for the <b>description</b> property.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     *  Getter method for the <b>obsolete</b> property.
     */
    public boolean getObsolete()
    {
        return obsolete;
    }

    /**
     *  Getter method for the <b>ord</b> property.
     */
    public int getOrd()
    {
        return ord;
    }

    /**
     *  Retrieve a reference item by its code.
     *
     *  @param refItemClass
     *      Identifies the reference list from which to retrieve the item
     *      by the class of its reference items.
     *  @param code
     *      The code string for which to retrieve the reference item.
     *  @return
     *      The reference item corresponding to <code>code</code>,
     *      or <code>null</code> if there is no such item.
     */
    public static <T extends RefItem> T getItemByCode(
            final Class<T> refItemClass,
            final String code
    )
    {
        return RefList.getItemByCode(refItemClass, code);
    }

    /**
     *  Retrieve a reference item by its code/alias.
     *
     *  @param refItemClass
     *      Identifies the reference list from which to retrieve the item
     *      by the class of its reference items.
     *  @param codeOrAlias
     *      The code string string for which to retrieve the reference item.
     *  @return
     *      The reference item corresponding to <code>code</code>,
     *      or <code>null</code> if there is no such item.
     */
    public static <T extends RefItem> T factory(
            final Class<T> refItemClass,
            final String codeOrAlias
    )
    {
        return RefList.factory(refItemClass, codeOrAlias);
    }

    /**
     *  Retrieve a collection of values of a reflist
     *  (excluding obsolete values).
     *
     *  @param refItemClass
     *      Identifies the reference list to retrieve
     *      by the class of its reference items.
     *  @return
     *      An unmodifiable collection of the known values in the reflist
     *      identified by <code>refItemClass</code>
     *      (excluding obsolete values).
     */
    public static <T extends RefItem> Collection<T> getItems(
            final Class<T> refItemClass
    )
    {
        return RefList.getItems(refItemClass);
    }

    /**
     *  Retrieve a collection of values of a reflist
     *  (excluding obsolete values),
     *  ensuring that the given item is included (even if it is obsolete).
     *
     *  @param refItemClass
     *      Identifies the reference list to retrieve
     *      by the class of its reference items.
     *  @param currentValue
     *      A value to include in the result, even if it is obsolete.
     *  @return
     *      An unmodifiable collection of the known values in the reflist
     *      identified by <code>refItemClass</code>
     *      (excluding obsolete values).
     */
    public static <T extends RefItem> Collection<T> getItems(
            final Class<T> refItemClass,
            final T currentValue
    )
    {
        final Collection<T> result;

        result = new LinkedList<T>();
        final Collection<T> items = getAllItems(refItemClass);
        for (T item : items) {
            if (!item.getObsolete() || DGLUtil.equals(item, currentValue)) {
                result.add(item);
            }
        }

        return Collections.unmodifiableCollection(result);
    }

    /**
     *  Retrieve a collection of all values of a reflist
     *  (including obsolete values).
     *
     *  @param refItemClass
     *      Identifies the reference list to retrieve
     *      by the class of its reference items.
     *  @return
     *      An unmodifiable collection of all the known values in the reflist
     *      identified by <code>refItemClass</code>
     *      (including obsolete values).
     */
    public static <T extends RefItem> Collection<T> getAllItems(
            final Class<T> refItemClass
    )
    {
        return RefList.getAllItems(refItemClass);
    }

}
