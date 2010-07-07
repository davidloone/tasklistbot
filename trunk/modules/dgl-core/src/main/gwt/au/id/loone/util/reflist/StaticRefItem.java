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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *  A class for reference items whose values are statically defined within the application code.
 *
 *  @author David G Loone
 */
public abstract class StaticRefItem
        extends BaseRefItem
        implements RefItem
{

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     *  @param obsolete
     *      Whether this reference item is obsolete.
     *  @param aliases
     *      The aliases for this reference item.
     */
    @SuppressWarnings({"unchecked"})
    protected StaticRefItem(
            final String code,
            final String description,
            final boolean obsolete,
            final Set<String> aliases
    )
    {
        super(code, description, obsolete, aliases);
    }

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     *  @param obsolete
     *      Whether this reference item is obsolete.
     *  @param aliases
     *      The aliases for this reference item.
     */
    @SuppressWarnings({"unchecked"})
    protected StaticRefItem(
            final String code,
            final String description,
            final boolean obsolete,
            final String[] aliases
    )
    {
        this(code, description, obsolete,
                ((aliases == null) ? null : Collections.unmodifiableSet(new HashSet(Arrays.asList(aliases)))));
    }

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     *  @param obsolete
     *      Whether this reference item is obsolete.
     */
    protected StaticRefItem(
            final String code,
            final String description,
            final boolean obsolete
    )
    {
        this(code, description, obsolete, (Set<String>)null);
    }

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     */
    protected StaticRefItem(
            final String code,
            final String description
    )
    {
        this(code, description, false, (Set<String>)null);
    }

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     *  @param aliases
     *      The aliases for this reference item.
     */
    protected StaticRefItem(
            final String code,
            final String description,
            final Set<String> aliases
    )
    {
        this(code, description, false, aliases);
    }

    /**
     *  Create a new static ref item and add it to the reference list.
     *
     *  @param code
     *      The code value for the reference item.
     *  @param description
     *      The description value for the reference item.
     *  @param aliases
     *      The aliases for this reference item.
     */
    protected StaticRefItem(
            final String code,
            final String description,
            final String[] aliases
    )
    {
        this(code, description, false, Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(aliases))));
    }

}