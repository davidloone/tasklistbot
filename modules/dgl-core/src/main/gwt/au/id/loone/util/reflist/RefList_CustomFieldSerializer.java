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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * GWT custom serializer for {@link au.id.loone.util.reflist.RefList}.
 *
 * @author David G Loone
 */
public class RefList_CustomFieldSerializer
{

    /**
     */
    public RefList_CustomFieldSerializer()
    {
        super();
    }

    /**
     */
    public static RefList instantiate(
            final SerializationStreamReader reader
    )
            throws SerializationException
    {
        final String name = reader.readString();
        final String description = reader.readString();
        final RefList refList = new RefList(name, description, null);
        final int size = reader.readInt();
        for (int i = 0; i < size; i++) {
            final RefItem item = (RefItem)reader.readObject();
            refList.addItem(item);
            refList.refItemClass = item.getClass();
        }
        refList.fixate();

        return refList;
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void serialize(
            final SerializationStreamWriter writer,
            final RefList instance
    )
            throws SerializationException
    {
        writer.writeString(instance.getName());
        writer.writeString(instance.getDescription());
        writer.writeInt(instance.getAllItems().size());
        for (final Object item : instance.getAllItems()) {
            writer.writeObject(item);
        }
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void deserialize(
            final SerializationStreamReader reader,
            final RefList instance
    )
            throws SerializationException
    {
        // Nothing to do.
    }

}