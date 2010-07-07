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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import au.id.loone.util.DGLUtil;
import au.id.loone.util.reflist.client.services.RefListService;
import au.id.loone.util.reflist.client.services.RefListServiceAsync;
import au.id.loone.util.reflists.CountryRI;
import au.id.loone.util.tracing.TraceUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *  Models a single reference list,
 *  providing a container for the reference items
 *  and tools for accessing them.
 *  The tools for accessing the reference list
 *  are generally only used by the concrete implementations of
 *  {@link RefItem}.
 *
 * @author David G Loone
 */
public final class RefList<T extends RefItem>
        implements Comparable<RefList<T>>, IsSerializable
{

    /**
     *  Collection of reference lists.
     */
    private static final Collection<RefList<? extends RefItem>> refLists =
            new TreeSet<RefList<? extends RefItem>>();

    /**
     *  Map of reference list reference item xstreamClasses to reference lists.
     */
    private static final Map<Class<? extends RefItem>, RefList<? extends RefItem>> refListsMapByRefItemClass =
            new HashMap<Class<? extends RefItem>, RefList<? extends RefItem>>();

    /**
     *  Map of reference list names to reference lists.
     */
    private static final Map<String, RefList<? extends RefItem>> refListsMapByName =
            new HashMap<String, RefList<? extends RefItem>>();

    /**
     *  The list of all items in this list.
     */
    private List<T> allItems;

    /**
     *  Current value of the <b>description</b> property.
     */
    private String description;

    /**
     *  The list of items in this list.
     */
    private Collection<T> items;

    /**
     *  A map of codes to items.
     */
    private Map<String, T> itemsMapByCode;

    /**
     *  A map of codes/aliases to items.
     */
    private Map<String, T> itemsMapByCodeOrAlias;

    /**
     *  Current value of the <b>name</b> property.
     */
    private String name;

    /**
     *  Current value of the <b>refItemClass</b> property.
     */
    Class<? extends RefItem> refItemClass;

    /**
     */
    RefList(
            final String name,
            final String description,
            final Class<? extends RefItem> refItemClass
    )
    {
        this.name = name;
        this.refItemClass = refItemClass;

        allItems = new LinkedList<T>();
        this.description = description;
        items = new LinkedList<T>();
        itemsMapByCode = new HashMap<String, T>();
        itemsMapByCodeOrAlias = new HashMap<String, T>();
    }

    /**
     */
    public int hashCode()
    {
        return DGLUtil.hashCode(name);
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
        else if (otherObj instanceof RefList) {
            final RefList other = (RefList)otherObj;
            result = DGLUtil.equals(name, other.name);
        }
        else {
            result = false;
        }

        return result;
    }

    /**
     *  @return
     *      The result of comparing <b>name</b> property.
     */
    public int compareTo(
            final RefList<T> other
    )
    {
        return name.compareTo(other.name);
    }

    /**
     */
    public String toString()
    {
        return "RefList:" + getName();
    }

    /**
     *  Add an item to this reference list.
     *
     *  @param item
     *      The item to add.
     *      This must not be equal to <code>null</code>.
     *  @throws IllegalArgumentException
     *      Thrown if the list already contains an item with the given code or alias.
     */
    public void addItem(
            final T item
    )
            throws IllegalArgumentException
    {
        if (item != null) {
            synchronized (this) {
                if (itemsMapByCodeOrAlias.containsKey(item.getCode())) {
                    throw new IllegalArgumentException("Duplicate code for item " +
                            item.getCode() + " for reference list " + getName() + ".");
                }
                if (item.getAliases() != null) {
                    for (String alias : item.getAliases()) {
                        if (itemsMapByCodeOrAlias.containsKey(alias)) {
                            throw new IllegalArgumentException("Duplicate alias (" +
                                    alias + ") for item " + item.getCode() +
                                    " in reference list " + getName() + ".");
                        }
                    }
                }

                // Add to the "all items" list.
                allItems.add(item);
                // Add to the "normal items" list, but only if not obsolete.
                if (!item.getObsolete()) {
                    items.add(item);
                }
                // Map by code and aliases.
                itemsMapByCode.put(item.getCode(), item);
                itemsMapByCodeOrAlias.put(item.getCode(), item);
                if (item.getAliases() != null) {
                    for (String alias : item.getAliases()) {
                        itemsMapByCodeOrAlias.put(alias, item);
                    }
                }
            }
        }
    }

    /**
     *  Fixate this reference list.
     */
    public void fixate()
    {
        synchronized (this) {
            // Finalise the collections.
            allItems = Collections.unmodifiableList(allItems);
            items = Collections.unmodifiableCollection(items);
            itemsMapByCode = Collections.unmodifiableMap(itemsMapByCode);
            itemsMapByCodeOrAlias = Collections.unmodifiableMap(itemsMapByCodeOrAlias);

            // Fill in the ordinal values.
            int ord = 0;
            for (T item : allItems) {
                ((BaseRefItem)item).ord = ord;
                ord++;
            }

            // Make the new reflist known to the world.
            synchronized (refListsMapByRefItemClass) {
                refLists.add(this);
                refListsMapByName.put(getName(), this);
                refListsMapByRefItemClass.put(getRefItemClass(), this);
            }
        }
    }

    /**
     *  Retrieve all known reference lists.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static Collection<RefList<? extends RefItem>> getRefLists()
    {
        return Collections.unmodifiableCollection(refLists);
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
     *  @throws ClassCastException
     *      Thrown if there is no such reference list.
     */
    public static <T extends RefItem> T getItemByCode(
            final Class<T> refItemClass,
            final String code
    )
            throws ClassCastException
    {
        final RefList<T> refList = getRefListChecked(refItemClass);
        return refList.getItemByCode(code);
    }

    /**
     *  Get an item given its code.
     *
     *  @param code
     *      The code string for which to retrieve the reference item.
     *  @return
     *      The reference item corresponding to <code>code</code>,
     *      or <code>null</code> if there is no such reference item.
     */
    public T getItemByCode(
            final String code
    )
    {
        return itemsMapByCode.get(code);
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
     *  @throws ClassCastException
     *      Thrown if there is no such reference list.
     */
    public static <T extends RefItem> T factory(
            final Class<T> refItemClass,
            final String codeOrAlias
    )
            throws ClassCastException
    {
        final RefList<T> refList = getRefListChecked(refItemClass);
        return refList.factory(codeOrAlias);
    }

    /**
     *  Get an item given its code/alias.
     *
     *  @param codeOrAlias
     *      The code or alias string for which to retrieve the reference item.
     *  @return
     *      The reference item corresponding to <code>codeOrAlias</code>,
     *      or <code>null</code> if there is no such reference item.
     */
    public T factory(
            final String codeOrAlias
    )
    {
        return itemsMapByCodeOrAlias.get(codeOrAlias);
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
            final Class<? extends RefItem> refItemClass
    )
    {
        final RefList<T> refList = getRefListChecked(refItemClass);
        return refList.getItems();
    }

    /**
     *  Getter method for the <b>description</b> property.
     *
     *  @return
     *      The reference list description.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getDescription()
    {
        return description;
    }

    /**
     *  Retrieve a collection of values of this reflist
     *  (excluding obsolete values).
     *
     *  @return
     *      An unmodifiable collection of the known values in this reflist
     *      (excluding obsolete values).
     */
    public Collection<T> getItems()
    {
        return items;
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
            final Class<? extends RefItem> refItemClass
    )
    {
        final RefList<T> refList = getRefListChecked(refItemClass);
        return refList.getAllItems();
    }

    /**
     *  Retrieve a collection of all values of this reflist
     *  (including obsolete values).
     *
     *  @return
     *      An unmodifiable collection of all the known values in this reflist
     *      (including obsolete values).
     */
    public Collection<T> getAllItems()
    {
        return allItems;
    }

    /**
     *  Getter method for the <b>name</b> property.
     */
    public String getName()
    {
        return name;
    }

    /**
     *  Getter method for the <b>refItemClass</b> property.
     */
    public Class<? extends RefItem> getRefItemClass()
    {
        return refItemClass;
    }

    /**
     *  Make a new reflist.
     *
     *  @param name
     *      The name of the reference list.
     *  @param refItemClass
     *      The class of items of this reference list.
     *  @throws IllegalArgumentException
     *      Throw if:
     *      <ul>
     *          <li>A {@link StaticRefItem static reference list} is being refreshed.</li>
     *          <li>A reference list
     *              (as defined by <code>refItemClass</code> is being reloaded,
     *              but the value of <code>name</code> is different from what it was
     *              when it was originally loaded.</li>
     *      </ul>
     */
    public static <T extends RefItem> RefList<T> factory(
            final String name,
            final Class<? extends RefItem> refItemClass
    )
            throws IllegalArgumentException
    {
        return factory(name, null, refItemClass);
    }

    /**
     *  Make a new reflist.
     *
     *  @param name
     *      The name of the reference list.
     *  @param description
     *      The description of the reference list.
     *  @param refItemClass
     *      The class of items of this reference list.
     *  @throws IllegalArgumentException
     *      Throw if:
     *      <ul>
     *          <li>A {@link StaticRefItem static reference list} is being refreshed.</li>
     *          <li>A reference list
     *              (as defined by <code>refItemClass</code> is being reloaded,
     *              but the value of <code>name</code> is different from what it was
     *              when it was originally loaded.</li>
     *      </ul>
     */
    @SuppressWarnings({"unchecked"})
    public static <T extends RefItem> RefList<T> factory(
            final String name,
            final String description,
            final Class<? extends RefItem> refItemClass
    )
            throws IllegalArgumentException
    {
        final RefList<T> result;

        result = new RefList<T>(name, description, refItemClass);

        return result;
    }

    /**
     *  Retrieve a reflist given its ref item class, loading it if necessary.
     *
     *  @param refItemClass
     *      Identifies the reference list to retrieve by the class of its reference items.
     *  @return
     *      The reference list identified by <code>refItemClass</code>,
     *      or <code>null</code> if there is no such reference list.
     */
    @SuppressWarnings({"unchecked"})
    public static <T extends RefItem> RefList<T> getRefList(
            final Class<? extends RefItem> refItemClass
    )
    {
        final RefList<T> result;

        synchronized (refListsMapByRefItemClass) {
            result = (RefList<T>)refListsMapByRefItemClass.get(refItemClass);
        }

        return result;
    }

    /**
     *  Retrieve a reflist given its ref item class.
     *
     *  @param refItemClass
     *      Identifies the reference list to retrieve by the class of its reference items.
     *  @return
     *      The reference list identified by <code>refItemClass</code>.
     *  @throws ClassCastException
     *      Thrown if there is no such reference list.
     */
    @SuppressWarnings({"unchecked"})
    private static <T extends RefItem> RefList<T> getRefListChecked(
            final Class<? extends RefItem> refItemClass
    )
            throws ClassCastException
    {
        final RefList<T> result;

        synchronized (refListsMapByRefItemClass) {
            result = (RefList<T>)refListsMapByRefItemClass.get(refItemClass);
            if (result == null) {
                throw new ClassCastException("No reflist for class " + refItemClass.getName());
            }
        }

        return result;
    }

    /**
     *  Retrieve a reflist given its name.
     *
     *  @param name
     *      Identifies the reference list to retrieve by its name.
     *  @return
     *      The reference list identified by <code>name</code>,
     *      or <code>null</code> if there is no such reference list.
     */
    @SuppressWarnings({"unchecked"})
    public static <T extends RefItem> RefList<T> getRefList(
            final String name
    )
    {
        final RefList<T> result;

        synchronized (refListsMapByRefItemClass) {
            result = (RefList<T>)refListsMapByName.get(name);
        }

        return result;
    }

    /**
     * Initialise the reflists in a GWT client.
     */
    public static void clientInit()
    {
//        final RefListServiceAsync service = GWT.create(RefListService.class);
//        service.retrieveRefLists(new AsyncCallback<RefList[]>() {
//            @Override public void onFailure(final Throwable e) {
//                GWT.log("RefList.clientInit.retrieveRefLists.onFailure: " + TraceUtil.formatObj(e),  e);
//            }
//
//            @Override public void onSuccess(RefList[] refLists) {
//                GWT.log("RefList.clientInit.retrieveRefLists.onSuccess: " + TraceUtil.formatObj(refLists), null);
//                GWT.log("RefList.clientInit.retrieveRefLists.onSuccess: AU = " + TraceUtil.formatObj(CountryRI.factory("AU")), null);
//                GWT.log("RefList.clientInit.retrieveRefLists.onSuccess: AU.aliases = " + TraceUtil.formatObj(CountryRI.factory("AU").getAliases()), null);
//            }
//        });
    }

}
