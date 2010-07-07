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
 * Reference list for boolean values (false/true).
 *
 * @author David G Loone
 */
public final class BooleanRI
        extends StaticRefItem
        implements RefItem, IsSerializable
{

    /**
     * Represents a value of <code>FALSE</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final BooleanRI FALSE = new BooleanRI("FALSE", "false",
            new String[] {"false", "F", "f"});

    /**
     * Represents a value of <code>TRUE</code>.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final BooleanRI TRUE = new BooleanRI("TRUE", "true",
            new String[] {"true", "T", "t"});

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final BooleanRI[] EMPTY_ARRAY = new BooleanRI[] {};

    /**
     */
    private BooleanRI(
            final String code,
            final String description,
            final String[] aliases
    )
    {
        super(code, description, aliases);
    }

    /**
     */
    public static BooleanRI factory(
            final String code
    )
    {
        return factory(BooleanRI.class, code);
    }

    /**
     */
    public static Collection<BooleanRI> getItems()
    {
        return getItems(BooleanRI.class);
    }

    /**
     */
    public static Collection<BooleanRI> getItems(
            final BooleanRI currentValue
    )
    {
        return getItems(BooleanRI.class, currentValue);
    }

    /**
     */
    public static Collection<BooleanRI> getAllItems()
    {
        return getAllItems(BooleanRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

}
