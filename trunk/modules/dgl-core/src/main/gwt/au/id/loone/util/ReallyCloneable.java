package au.id.loone.util;

/**
 * Identifies a class that really is cloneable.
 *
 * @author David G Loone
 */
public interface ReallyCloneable<T>
{

    /**
     */
    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
    public T clone();

}