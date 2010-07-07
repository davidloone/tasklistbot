package au.id.loone.util.reflist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells {@link RefListManager} where to get data from for this external reflist.
 *
 * @author David G Loone
 */
@Retention(
        value = RetentionPolicy.RUNTIME
)
@SuppressWarnings({"UnusedDeclaration"})
@Target(
        value = {ElementType.TYPE}
)
public @interface ExternalRefListSource
{

    Class<? extends ExternalRefListSourceDef> sourceClass();

}