/*
 * Copyright 2010, David G Loone
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

package au.id.loone.util.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

import au.id.loone.util.xstream.XStreamFactory;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * A Hibernate user type that saves the object as an XStream document into a character field.
 *
 * @author David G Loone
 */
@SuppressWarnings({"UnusedDeclaration"})
public final class XStreamUserType
        implements UserType, ParameterizedType
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(XStreamUserType.class);

    /**
     *  The SQL types.
     */
    private static final int[] SQL_TYPES = new int[] {
        Types.LONGVARCHAR
    };

    /**
     * Current value of the <b>objClass</b> property.
     */
    private Class objClass;

    /**
     */
    public XStreamUserType()
    {
        super();
    }

    /**
     */
    public void setParameterValues(
            final Properties parameters
    )
    {
        final String className = parameters.getProperty("className");
        try {
            objClass = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            throw new HibernateException("Class not found", e);
        }
    }

    /**
     */
    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    /**
     */
    public Class returnedClass()
    {
        return objClass;
    }

    /**
     */
    public final boolean equals(
            final Object value1,
            final Object value2
    )
            throws HibernateException
    {
        return DGLUtil.equals(value1, value2);
    }

    /**
     */
    public final int hashCode(
            final Object value
    )
            throws HibernateException
    {
        return (value == null) ? 0 : value.hashCode();
    }

    /**
     */
    public final Object nullSafeGet(
            final ResultSet resultSet,
            final String[] names,
            final Object owner
    )
            throws HibernateException, SQLException
    {
        final Object result;

        final String objStr = resultSet.getString(names[0]);
        if (resultSet.wasNull() ||
                (objStr == null)) {
            result = null;
        }
        else {
            final XStream xstream = xstreamFactory();
            result = xstream.fromXML(objStr);
        }

        return result;
    }

    /**
     */
    public final void nullSafeSet(
            final PreparedStatement preparedStatement,
            final Object value,
            final int index
    )
            throws HibernateException, SQLException
    {
        if (value == null) {
            preparedStatement.setString(index, null);
        }
        else {
            final XStream xstream = xstreamFactory();
            preparedStatement.setString(index, xstream.toXML(value));
        }
    }

    /**
     */
    public final Object deepCopy(
            final Object value
    )
            throws HibernateException
    {
        final Object result;

        if (value == null) {
            result = null;
        }
        else {
            final XStream xstream = xstreamFactory();
            result = xstream.fromXML(xstream.toXML(value));
        }

        return result;
    }

    /**
     */
    public final boolean isMutable()
    {
        return true;
    }

    /**
     */
    public Serializable disassemble(
            final Object value
    )
            throws HibernateException
    {
        return (value == null) ? null : (Serializable)deepCopy(value);
    }

    /**
     */
    public Object assemble(
            final Serializable cached,
            final Object owner
    )
            throws HibernateException
    {
        return (cached == null) ? null : deepCopy(cached);
    }

    /**
     */
    public Object replace(
            final Object original,
            final Object target,
            final Object owner
    )
            throws HibernateException
    {
        return (original == null) ? null : deepCopy(original);
    }

    /**
     */
    private static XStream xstreamFactory()
    {
        return XStreamFactory.factory().getXstream();
    }

}