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

import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationException;

/**
 * GWT custom serializer for {@link au.id.loone.util.reflists.PlanetRI}.
 *
 * @author David G Loone
 */
public class PlanetRI_CustomFieldSerializer
{

    /**
     */
    public PlanetRI_CustomFieldSerializer()
    {
        super();
    }

    /**
     */
    public static PlanetRI instantiate(
            final SerializationStreamReader reader
    )
            throws SerializationException
    {
        return PlanetRI.factory(reader.readString());
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void serialize(
            final SerializationStreamWriter writer,
            final PlanetRI instance
    )
            throws SerializationException
    {
        writer.writeString(instance.getCode());
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void deserialize(
            final SerializationStreamReader reader,
            final PlanetRI instance
    )
            throws SerializationException
    {
        // Nothing to do.
    }

}