/*
 * Copyright 2009, David G Loone
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

package au.id.loone.util.beans.converters;

import javax.naming.ldap.LdapName;
import javax.naming.InvalidNameException;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.exception.FatalException;

import org.apache.commons.beanutils.Converter;

/**
 * Property converter to convert string to {@link javax.naming.ldap.LdapName}.
 *
 * @author David G Loone
 */
public class LdapNameConverter
        implements Converter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(LdapNameConverter.class);

    /**
     */
    public LdapNameConverter()
    {
        super();
    }

    /**
     */
    public Object convert(
            final Class type,
            final Object value
    )
    {
        final LdapName result;

        try {
            result = new LdapName((String)value);
        }
        catch (final InvalidNameException e) {
            final FatalException fe = new FatalException("INVALID_LDAP_NAME", e);
            fe.setAttribute("name", value);
            throw fe;
        }

        return result;
    }

}

