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
 * Reference list for no/yes values.
 *
 * @author David G Loone
 */
public final class NoYesRI
        extends StaticRefItem
        implements RefItem, IsSerializable
{

    /**
     * Represents a value of <code>NO</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final NoYesRI NO = new NoYesRI("NO", "No",
            new String[] {"no", "N", "n"});

    /**
     * Represents a value of <code>YES</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final NoYesRI YES = new NoYesRI("YES", "Yes",
            new String[] {"yes", "Y", "y"});

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final NoYesRI[] EMPTY_ARRAY = new NoYesRI[] {};

    /**
     */
    private NoYesRI(
            final String code,
            final String description,
            final String[] aliases
    )
    {
        super(code, description, aliases);
    }

    /**
     */
    public static NoYesRI factory(
            final String code
    )
    {
        return factory(NoYesRI.class, code);
    }

    /**
     */
    public static Collection<NoYesRI> getItems()
    {
        return getItems(NoYesRI.class);
    }

    /**
     */
    public static Collection<NoYesRI> getItems(
            final NoYesRI currentValue
    )
    {
        return getItems(NoYesRI.class, currentValue);
    }

    /**
     */
    public static Collection<NoYesRI> getAllItems()
    {
        return getAllItems(NoYesRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

}
