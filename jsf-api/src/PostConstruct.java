package javax.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* Temporary.  Remove when this gets into glassfish */

@Target({ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface PostConstruct{}
