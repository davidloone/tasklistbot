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

package au.id.loone.util.reflists;

import java.util.Collection;

import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.reflist.StaticRefItem;
import au.id.loone.util.reflist.RefItemInstance;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Reference item for a meridiem (AM/PM).
 *
 * @author David G Loone
 */
public final class MeridiemRI
        extends StaticRefItem
        implements RefItem, IsSerializable
{

    /**
     * Represents a value of <code>FALSE</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MeridiemRI AM = new MeridiemRI("AM", "AM",
            new String[] {"am"});

    /**
     * Represents a value of <code>TRUE</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MeridiemRI PM = new MeridiemRI("PM", "PM",
            new String[] {"pm"});

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final MeridiemRI[] EMPTY_ARRAY = new MeridiemRI[] {};

    /**
     */
    private MeridiemRI(
            final String code,
            final String description,
            final String[] aliases
    )
    {
        super(code, description, aliases);
    }

    /**
     */
    public static MeridiemRI factory(
            final String code
    )
    {
        return factory(MeridiemRI.class, code);
    }

    /**
     */
    public static Collection<MeridiemRI> getItems()
    {
        return getItems(MeridiemRI.class);
    }

    /**
     */
    public static Collection<MeridiemRI> getItems(
            final MeridiemRI currentValue
    )
    {
        return getItems(MeridiemRI.class, currentValue);
    }

    /**
     */
    public static Collection<MeridiemRI> getAllItems()
    {
        return getAllItems(MeridiemRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

}