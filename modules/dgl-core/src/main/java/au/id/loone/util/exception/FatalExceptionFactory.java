package au.id.loone.util.exception;

/**
 * Indicates that the exception class is capable of being converted to a {@link FatalException}.
 *
 * @author David G Loone
 */
public interface FatalExceptionFactory
{

    /**
     * Generate a fatal exception from this exception.
     */
    public FatalException asFatalException();

}