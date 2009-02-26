package beanvalidation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

/**
 * @author Dan Allen
 */
@Documented
@Constraint(validatedBy = EmailConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
	// message string should be {constraint.email}
	String message() default "{validator.email}";

	Class<?>[] groups() default {};
}
