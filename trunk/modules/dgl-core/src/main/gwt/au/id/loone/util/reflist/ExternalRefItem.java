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

import java.util.Set;

/**
 *  Base class for reference items whose content is externalised
 *  (<i>ie</i>, whose contents are not defined in the class itself),
 *  and can be refreshed.
 *  The externalised representation of a reference list is defined by xstreamClasses in the
 *  {@link au.id.loone.util.reflist.transobj} package.
 *
 *  @author David G Loone
 */
public abstract class ExternalRefItem
        extends BaseRefItem
        implements RefItem
{

    /**
     */
    public ExternalRefItem(
            final String code,
            final String description,
            final boolean obsolete,
            final Set<String> aliases
    )
    {
        super(code, description, obsolete, aliases);
    }

}
