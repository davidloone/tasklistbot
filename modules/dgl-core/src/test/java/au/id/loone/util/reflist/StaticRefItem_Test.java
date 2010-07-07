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

import au.id.loone.util.tracing.TraceUtil;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *  Test class for {@link StaticRefItem}.
 *
 *  @author David G Loone
 */
public class StaticRefItem_Test
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(StaticRefItem_Test.class);

    /**
     */
    @Before
    public void before()
    {
        RefListManager.ensureLoaded(StaticRefItem_Test.YesNoRefItem.class);
    }

    /**
     */
    @Test
    public void test_00()
    {
        final YesNoRefItem y = YesNoRefItem.YES;
        assertEquals("YES", y.getCode());
    }

    /**
     */
    @Test
    public void test_01()
    {
        final String s = TraceUtil.formatObj(YesNoRefItem.getAllItems());
        assertEquals("{No, Yes, Unknown}", s);
    }

    /**
     */
    @Test
    public void test_02()
    {
        final String s = TraceUtil.formatObj(YesNoRefItem.getItems());
        assertEquals("{No, Yes}", s);
    }

    /**
     */
    @Test
    public void test_03()
    {
        final String s = TraceUtil.formatObj(YesNoRefItem.getItems(YesNoRefItem.UNKNOWN));
        assertEquals("{No, Yes, Unknown}", s);
    }

    /**
     *  Yes/No reference list.
     */
    public static class YesNoRefItem
            extends StaticRefItem
            implements RefItem
    {

        /**
         * Log4j logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(YesNoRefItem.class);

        /**
         */
        @RefItemInstance
        public static final YesNoRefItem NO = new YesNoRefItem("NO", "No", false, new String[] {"N"});

        /**
         */
        @RefItemInstance
        public static final YesNoRefItem YES = new YesNoRefItem("YES", "Yes", false, new String[] {"Y", "y"});

        /**
         */
        @RefItemInstance
        public static final YesNoRefItem UNKNOWN = new YesNoRefItem("UNKNOWN", "Unknown", true);

        /**
         */
        public YesNoRefItem(
                final String code,
                final String description,
                final boolean obsolete
        )
        {
            super(code, description, obsolete);
            LOG.info("YesNoRefItem(" +
                    TraceUtil.formatObj(code) + ", " +
                    TraceUtil.formatObj(description) + ", " +
                    TraceUtil.formatObj(obsolete) + ")");
        }

        /**
         */
        public YesNoRefItem(
                final String code,
                final String description,
                final boolean obsolete,
                final String[] aliases
        )
        {
            super(code, description, obsolete, aliases);
            LOG.info("YesNoRefItem(" +
                    TraceUtil.formatObj(code) + ", " +
                    TraceUtil.formatObj(description) + ", " +
                    TraceUtil.formatObj(obsolete) + ", " +
                    TraceUtil.formatObj(aliases) + ")");
        }

        public static YesNoRefItem factory(
                final String code
        )
        {
            return (YesNoRefItem)StaticRefItem.getItemByCode(YesNoRefItem.class, code);
        }

        /**
         */
        public static Collection<YesNoRefItem> getItems()
        {
            return StaticRefItem.getItems(YesNoRefItem.class);
        }

        /**
         */
        public static Collection<YesNoRefItem> getItems(
                final YesNoRefItem currentValue
        )
        {
            return StaticRefItem.getItems(YesNoRefItem.class, currentValue);
        }

        /**
         */
        public static Collection<YesNoRefItem> getAllItems()
        {
            return StaticRefItem.getAllItems(YesNoRefItem.class);
        }

    }

}
