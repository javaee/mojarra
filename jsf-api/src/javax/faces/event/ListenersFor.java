package javax.faces.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Container Annotation to specify multiple @ListenerFor annotations
 * on a single component.
 *
 * @since 2.0
 */
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= ElementType.TYPE)
public @interface ListenersFor {

    ListenerFor[] value();
    
}
