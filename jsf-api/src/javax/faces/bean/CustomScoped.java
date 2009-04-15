package javax.faces.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;

/**
 * <p class="changed_added_2_0">When this annotation, along with {@link
 * ManagedBean} is found on a class, the runtime must act as if a
 * <code>&lt;managed-bean-scope&gt;VALUE&lt;managed-bean-scope&gt;</code>
 * element was declared for the corresponding managed bean, where VALUE
 * is the value of the {@link #value} attribute, which must be an EL
 * expression that evaluates to a <code>Map</code>.</p>

 * <p class="changed_added_2_0">Developers must take care when using
 * custom scopes to ensure that any object references made to or from a
 * custom scoped bean consider the necessary scope lifetimes.  The
 * runtime is not required to perform any validations for such
 * considerations.</p>


 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CustomScoped {

    public String value();
}
