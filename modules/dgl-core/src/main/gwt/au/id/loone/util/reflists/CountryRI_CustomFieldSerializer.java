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

import java.util.List;
import java.util.Set;

import au.id.loone.util.reflist.RefList;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * GWT custom serializer for {@link au.id.loone.util.reflists.CountryRI}.
 *
 * @author David G Loone
 */
public class CountryRI_CustomFieldSerializer
{

    /**
     */
    public CountryRI_CustomFieldSerializer()
    {
        super();
    }

    /**
     */
    public static CountryRI instantiate(
            final SerializationStreamReader reader
    )
            throws SerializationException
    {
        final CountryRI result;

        final String code = reader.readString();
        final String description = reader.readString();
        final boolean obsolete = reader.readBoolean();
        final Set<String> aliases = (Set<String>)reader.readObject();
        final String alpha2Code = reader.readString();
        final String alpha3Code = reader.readString();
        final List<String> notes = (List<String>)reader.readObject();
        final String numericCode = reader.readString();
        final String since = reader.readString();

        if (RefList.getRefList(CountryRI.class) == null) {
            result = new CountryRI(code, description, obsolete, aliases);
            result.setAlpha2Code(alpha2Code);
            result.setAlpha3Code(alpha3Code);
            result.setNotes(notes);
            result.setNumericCode(numericCode);
            result.setSince(since);
        }
        else {
            result = CountryRI.factory(code);
        }

        return result;
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void serialize(
            final SerializationStreamWriter writer,
            final CountryRI instance
    )
            throws SerializationException
    {
        writer.writeString(instance.getCode());
        writer.writeString(instance.getDescription());
        writer.writeBoolean(instance.getObsolete());
        writer.writeObject(instance.getAliases());
        writer.writeString(instance.getAlpha2Code());
        writer.writeString(instance.getAlpha3Code());
        writer.writeObject(instance.getNotes());
        writer.writeString(instance.getNumericCode());
        writer.writeString(instance.getSince());
    }

    /**
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static void deserialize(
            final SerializationStreamReader reader,
            final CountryRI instance
    )
            throws SerializationException
    {
        // Nothing to do.
    }

}