package javax.faces.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**

 * <p class="changed_added_2_0">When this annotation, along with {@link
 * ManagedBean} is found on a class, the runtime must act as if a
 * <code>&lt;managed-bean-scope&lt;application&lt;managed-bean-scope&gt;</code>
 * element was declared for the corresponding managed bean.</p>

 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ApplicationScoped {
}
