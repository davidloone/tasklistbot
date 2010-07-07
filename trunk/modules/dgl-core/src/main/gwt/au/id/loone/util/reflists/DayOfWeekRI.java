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
 * Reference list of days of the week.
 *
 * @author David G Loone
 */
public final class DayOfWeekRI
        extends StaticRefItem
        implements RefItem, IsSerializable
{

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI SUNDAY = new DayOfWeekRI("SUN", "Sunday",
            new String[] {"SUNDAY", "Sunday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI MONDAY = new DayOfWeekRI("MON", "Monday",
            new String[] {"MONDAY", "Monday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI TUESDAY = new DayOfWeekRI("TUE", "Tuesday",
            new String[] {"TUESDAY", "Tuesday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI WEDNESDAY = new DayOfWeekRI("WED", "Wednesday",
            new String[] {"WEDNESDAY", "Wednesday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI THURSDAY = new DayOfWeekRI("THU", "Thursday",
            new String[] {"THURSDAY", "Thursday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI FRIDAY = new DayOfWeekRI("FRI", "Friday",
            new String[] {"FRIDAY", "Friday"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final DayOfWeekRI SATURDAY = new DayOfWeekRI("SAT", "Saturday",
            new String[] {"SATURDAY", "Saturday"});

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final DayOfWeekRI[] EMPTY_ARRAY = new DayOfWeekRI[] {};

    /**
     */
    private DayOfWeekRI(
            final String code,
            final String description,
            final String[] aliases
    )
    {
        super(code, description, aliases);
    }

    /**
     */
    public static DayOfWeekRI factory(
            final String code
    )
    {
        return factory(DayOfWeekRI.class, code);
    }

    /**
     */
    public static Collection<DayOfWeekRI> getItems()
    {
        return getItems(DayOfWeekRI.class);
    }

    /**
     */
    public static Collection<DayOfWeekRI> getItems(
            final DayOfWeekRI currentValue
    )
    {
        return getItems(DayOfWeekRI.class, currentValue);
    }

    /**
     */
    public static Collection<DayOfWeekRI> getAllItems()
    {
        return getAllItems(DayOfWeekRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

}
