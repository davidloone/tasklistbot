package au.id.loone.util.dom;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Hibernate user type for {@link Duration}.
 *
 * @author David G Loone
 */
public final class Duration_HibernateUserType
        implements UserType
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(Duration_HibernateUserType.class);

    /**
     *  The SQL types.
     */
    private static final int[] SQL_TYPES = new int[] {
        Types.VARCHAR
    };

    /**
     */
    public Duration_HibernateUserType()
    {
        super();
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
        return Duration.class;
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
        final Duration result;

        final String valueStr = resultSet.getString(names[0]);
        result = DGLStringUtil.isNullOrEmpty(valueStr) ? null : new Duration(valueStr);

        return result;
    }

    /**
     */
    public final void nullSafeSet(
            final PreparedStatement preparedStatement,
            final Object valueObj,
            final int index
    )
            throws HibernateException, SQLException
    {
        final Duration value = (Duration)valueObj;
        preparedStatement.setString(index, value.formatAsHMSS());
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
        return (Duration)value;
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