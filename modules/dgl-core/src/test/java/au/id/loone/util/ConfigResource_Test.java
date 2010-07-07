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

package au.id.loone.util;

import java.util.Properties;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *  Test class for {@link ConfigResource}.
 *
 *  @author David G Loone
 */
public class ConfigResource_Test
{

    /**
     */
    public ConfigResource_Test()
    {
        super();
    }

    /**
     */
    @Test
    public void test_asString_00()
    {
        final ConfigResource configResource =
                ConfigResource.factory(ConfigResource_Test.class, "ConfigResource_Test.properties");
        assertEquals(10, configResource.getContent().length());
    }

    /**
     */
    @Test
    public void test_asPropertiesRaw_00()
    {
        final ConfigResource configResource =
                ConfigResource.factory(ConfigResource_Test.class, "ConfigResource_Test.properties");
        final Properties props = configResource.getProperties();
        assertEquals("b", props.getProperty("a"));
        assertEquals("d", props.getProperty("c"));
    }

}
