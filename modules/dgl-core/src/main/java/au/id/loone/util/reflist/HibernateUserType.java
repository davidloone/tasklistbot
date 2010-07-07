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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 *  Hibernate user type for reference items.
 *
 *  <p>A concrete class (extending this class) is required to be able to persist
 *      reference item objects using Hibernate.
 *      The concrete class should only need to implement methods
 *      {@link #factory(String)} and {@link #returnedClass()}.</p>
 *
 *  @author David G Loone
 */
public final class HibernateUserType
        implements UserType, ParameterizedType
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(HibernateUserType.class);

    /**
     *  The SQL types.
     */
    private static final int[] SQL_TYPES = new int[] {
        Types.VARCHAR
    };

    /**
     * Current value of the <b>objClass</b> property.
     */
    private Class<? extends RefItem> refItemClass;

    /**
     */
    public HibernateUserType()
    {
        super();
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    public void setParameterValues(
            final Properties parameters
    )
    {
        final String refItemClassName = parameters.getProperty("refItemClassName");
        if (DGLStringUtil.isNullOrEmpty(refItemClassName)) {
            throw new IllegalArgumentException("No value for \"refItemClassName\" parameter.");
        }
        try {
            final Class returnedType = Class.forName(refItemClassName);

            if (!RefItem.class.isAssignableFrom(returnedType)) {
                throw new IllegalArgumentException("Returned class " + returnedType.getName() +
                        " is not a subclass of " + RefItem.class.getName());
            }
            refItemClass = (Class<? extends RefItem>) returnedType;
        }
        catch (ClassNotFoundException e) {
            throw new HibernateException("Reference item class (" + TraceUtil.formatObj(refItemClassName) +
                    ") not found", e);
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
    @Override
    public Class returnedClass()
    {
        return refItemClass;
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
        final RefItem result;

        final String code = resultSet.getString(names[0]);
        result = factory(code);
        if (!resultSet.wasNull() &&
                (result == null)) {
            // Hmmm. We had a non-null value, but it didn't generate a non-null reference item. Must be a
            // problem here.
            throw new IllegalArgumentException("Invalid ref item code (" +
                    TraceUtil.formatObj(code) + ") for ref list " + returnedClass().getName() + ".");
        }

        return result;
    }

    /**
     *  Create a reference item of the correct type from a string.
     *  The concrete implementation of this class should simply delegate to the factory method
     *  of the reference item class itself.
     */
    public RefItem factory(
            final String code
    )
    {
        return RefList.factory(refItemClass, code);
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
        final RefItem item = (RefItem)value;
        preparedStatement.setString(index, (item == null) ? null : item.getCode());
    }

    /**
     */
    public final Object deepCopy(
            final Object value
    )
            throws HibernateException
    {
        return value;
    }

    /**
     */
    public final boolean isMutable()
    {
        return false;
    }

    /**
     */
    public Serializable disassemble(
            final Object value
    )
            throws HibernateException
    {
        // Object is immutable, so just return the original.
        return (RefItem)value;
    }

    /**
     */
    public Object assemble(
            final Serializable cached,
            final Object owner
    )
            throws HibernateException
    {
        // Object is immutable, so just return the original.
        return cached;
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
        // Object is immutable, so just return the original.
        return original;
    }

}
