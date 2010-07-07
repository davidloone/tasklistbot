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
import java.util.GregorianCalendar;

import au.id.loone.util.DGLUtil;
import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.reflist.StaticRefItem;
import au.id.loone.util.reflist.RefItemInstance;

/**
 * Reference list of months of the year.
 *
 * @author David G Loone
 */
public final class MonthOfYearRI
        extends StaticRefItem
        implements RefItem
{

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI JANUARY = new MonthOfYearRI("JAN", "January", 31,
            GregorianCalendar.JANUARY, new String[] {"JANUARY"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI FEBRUARY = new MonthOfYearRI("FEB", "February", 28,
            GregorianCalendar.FEBRUARY, new String[] {"FEBRUARY"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI MARCH = new MonthOfYearRI("MAR", "March", 31,
            GregorianCalendar.MARCH, new String[] {"MARCH"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI APRIL = new MonthOfYearRI("APR", "April", 30,
            GregorianCalendar.APRIL, new String[] {"APRIL"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI MAY = new MonthOfYearRI("MAY", "May", 31,
            GregorianCalendar.MAY, new String[] {"MAY"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI JUNE = new MonthOfYearRI("JUN", "June", 30,
            GregorianCalendar.JUNE, new String[] {"JUNE"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI JULY = new MonthOfYearRI("JUL", "July", 31,
            GregorianCalendar.JULY, new String[] {"JULY"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI AUGUST = new MonthOfYearRI("AUG", "August", 31,
            GregorianCalendar.AUGUST, new String[] {"AUGUST"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI SEPTEMBER = new MonthOfYearRI("SEP", "September", 30,
            GregorianCalendar.SEPTEMBER, new String[] {"SEPTEMBER"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI OCTOBER = new MonthOfYearRI("Oct", "October", 31,
            GregorianCalendar.OCTOBER, new String[] {"OCTOBER"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI NOVEMBER = new MonthOfYearRI("NOV", "November", 30,
            GregorianCalendar.NOVEMBER, new String[] {"NOVEMBER"});

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    @RefItemInstance
    public static final MonthOfYearRI DECEMBER = new MonthOfYearRI("DEC", "December", 31,
            GregorianCalendar.DECEMBER, new String[] {"DECEMBER"});

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final MonthOfYearRI[] EMPTY_ARRAY = new MonthOfYearRI[] {};

    /**
     * Current value of the <b>duration</b> property.
     */
    private int itsDuration;

    /**
     * Current value of the <b>number</b> property.
     */
    private int itsNumber;

    /**
     */
    private MonthOfYearRI(
            final String code,
            final String description,
            final int number,
            final int duration,
            final String[] aliases
    )
    {
        super(code, description, aliases);

        itsDuration = duration;
        itsNumber = number;
    }

    /**
     */
    public static MonthOfYearRI factory(
            final String code
    )
    {
        return factory(MonthOfYearRI.class, code);
    }

    /**
     */
    public static Collection<MonthOfYearRI> getItems()
    {
        return getItems(MonthOfYearRI.class);
    }

    /**
     */
    public static Collection<MonthOfYearRI> getItems(
            final MonthOfYearRI currentValue
    )
    {
        return getItems(MonthOfYearRI.class, currentValue);
    }

    /**
     */
    public static Collection<MonthOfYearRI> getAllItems()
    {
        return getAllItems(MonthOfYearRI.class);
    }

    /**
     * Getter method for the <b>duration</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getDuration(
            final int year
    )
    {
        final int result;

        if (DGLUtil.equals(this, FEBRUARY)) {
            if ((new GregorianCalendar()).isLeapYear(year)) {
                result = 29;
            }
            else {
                result = 28;
            }
        }
        else {
            result = itsDuration;
        }

        return result;
    }

    /**
     * Getter method for the <b>number</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public int getNumber()
    {
        return itsNumber;
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

}
