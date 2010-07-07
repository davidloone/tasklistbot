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
import java.util.List;
import java.util.Set;

import au.id.loone.util.reflist.ExternalRefItem;
import au.id.loone.util.reflist.ExternalRefListSource;
import au.id.loone.util.reflist.RefItem;
import au.id.loone.util.reflist.RefItemInstance;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Reference list for ISO-3166 countries.
 *
 * @author David G Loone
 */
@ExternalRefListSource(
        sourceClass = au.id.loone.util.reflist.XMLResourceRefListSource.class
)
public final class CountryRI
        extends ExternalRefItem
        implements RefItem, IsSerializable
{

    /**
     * Australia
     */
    @RefItemInstance(
            code = "AU"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI AU;

    /**
     * Australia
     */
    @RefItemInstance(
            code = "AU"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI AUSTRALIA;

    /**
     * United Kingdom
     */
    @RefItemInstance(
            code = "GB"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI GB;

    /**
     * United Kingdom
     */
    @RefItemInstance(
            code = "GB"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI UK;

    /**
     * United Kingdom
     */
    @RefItemInstance(
            code = "GB"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI UNITED_KINGDOM;

    /**
     * United States of America
     */
    @RefItemInstance(
            code = "US"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI US;

    /**
     * United States of America
     */
    @RefItemInstance(
            code = "US"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI USA;

    /**
     * United States of America
     */
    @RefItemInstance(
            code = "US"
    )
    @SuppressWarnings({"UnusedDeclaration"})
    public static CountryRI UNITED_STATES_OF_AMERICA;

    /**
     * Current value of the <b>alpha2Code</b> property.
     */
    private String alpha2Code;

    /**
     * Current value of the <b>alpha3Code</b> property.
     */
    private String alpha3Code;

    /**
     * Current value of the <b>notes</b> property.
     */
    private List<String> notes;

    /**
     * Current value of the <b>numericCode</b> property.
     */
    private String numericCode;

    /**
     * Current value of the <b>since</b> property.
     */
    private String since;

    /**
     * An empty array.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static final CountryRI[] EMPTY_ARRAY = new CountryRI[] {};

    /**
     */
    public CountryRI()
    {
        super(null, null, false, null);
    }

    /**
     */
    CountryRI(
            final String code,
            final String description,
            final boolean obsolete,
            final Set<String> aliases
    )
    {
        super(code, description, obsolete, aliases);
    }

    /**
     */
    public static CountryRI factory(
            final String code
    )
    {
        return factory(CountryRI.class, code);
    }

    /**
     */
    public String toString()
    {
        return CountryRI.class.getName() + "{" +
                super.toString() + "}";
    }

    /**
     */
    public static Collection<CountryRI> getItems()
    {
        return getItems(CountryRI.class);
    }

    /**
     */
    public static Collection<CountryRI> getItems(
            final CountryRI currentValue
    )
    {
        return getItems(CountryRI.class, currentValue);
    }

    /**
     */
    public static Collection<CountryRI> getAllItems()
    {
        return getAllItems(CountryRI.class);
    }

    /**
     * Support for serialization.
     */
    private Object readResolve()
    {
        return factory(getCode());
    }

    /**
     * Pseudo-setter for the <b>alpha2Code</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setAlpha2Code(final String alpha2Code) {this.alpha2Code = alpha2Code;}

    /**
     * Getter method for the <b>alpha2Code</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getAlpha2Code() {return alpha2Code;}

    /**
     * Pseudo-setter for the <b>alpha3Code</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setAlpha3Code(final String alpha3Code) {this.alpha3Code = alpha3Code;}

    /**
     * Getter method for the <b>alpha3Code</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getAlpha3Code() {return alpha3Code;}

    /**
     * Pseudo-setter for the <b>numericCode</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setNumericCode(final String numericCode) {this.numericCode = numericCode;}

    /**
     * Getter method for the <b>numericCode</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getNumericCode() {return numericCode;}

    /**
     * Setter for the <b>notes</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setNotes(final List<String> notes) {this.notes = notes;}

    /**
     * Getter method for the <b>notes</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public List<String> getNotes() {return notes;}

    /**
     * Pseudo-setter for the <b>since</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setSince(final String since) {this.since = since;}

    /**
     * Getter method for the <b>since</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getSince() {return since;}

}