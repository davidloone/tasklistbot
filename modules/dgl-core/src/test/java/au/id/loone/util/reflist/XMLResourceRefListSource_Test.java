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
import java.util.Set;

import au.id.loone.util.tracing.TraceUtil;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *  Test class for {@link XMLResourceRefListSource} class.
 *
 *  @author David G Loone
 */
public class XMLResourceRefListSource_Test
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(XMLResourceRefListSource_Test.class);

    /**
     */
    @Before
    public void before()
    {
        RefListManager.ensureLoaded(XMLResourceRefListSource_Test.YesNoRefItem.class);
    }

    /**
     */
    @Test
    public void test_00()
            throws Exception
    {
        YesNoRefItem.getItems();
        assertEquals(2, YesNoRefItem.getItems().size());
        assertEquals(3, YesNoRefItem.getAllItems().size());
        assertEquals("NO", YesNoRefItem.factory("NO").getCode());
        assertEquals("YES", YesNoRefItem.factory("YES").getCode());
        assertEquals("UNKNOWN", YesNoRefItem.factory("UNKNOWN").getCode());
        assertEquals(false, YesNoRefItem.factory("NO").getObsolete());
        assertEquals("YES", YesNoRefItem.factory("Y").getCode());
        assertEquals("YES", YesNoRefItem.factory("y").getCode());
        assertEquals(null, YesNoRefItem.factory("n"));
        assertEquals(true, YesNoRefItem.factory("UNKNOWN").getObsolete());
        assertEquals("YES", YesNoRefItem.YES.getCode());
        assertEquals("NO", YesNoRefItem.X.getCode());
    }

    /**
     */
    @Test
    public void test_01()
            throws Exception
    {
        LOG.debug("test_01: " + TraceUtil.formatObj(Integer.TYPE));
    }

    /**
     *  Yes/No reference list.
     */
    @ExternalRefListSource(
            sourceClass = XMLResourceRefListSource.class
    )
    public static class YesNoRefItem
            extends ExternalRefItem
            implements RefItem
    {

        /**
         */
        @RefItemInstance
        public static YesNoRefItem YES;

        /**
         */
        @RefItemInstance(code = "NO")
        public static YesNoRefItem X;

        /**
         */
        public YesNoRefItem(
                final String code,
                final String description,
                final boolean obsolete,
                final Set<String> aliases
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
            return (YesNoRefItem)StaticRefItem.factory(YesNoRefItem.class, code);
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
