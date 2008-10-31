package javax.faces.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * RELEASE_PENDING (edburns,rogerk) Please update the docs.
 * <p class="changed_added_2_0">The presence of this annotation along with
 * {@link @ManagedBean} on a class indicates this managed bean is to be
 * <code>request</code> scoped.</p>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RequestScoped {
}
