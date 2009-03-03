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
@Constraint(validatedBy = CreditCardConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CreditCard {
	// message string should be {constraint.creditCard}
	String message() default "{validator.creditCard}";

	//CreditCardVendor vendor default ANY;

	Class<?>[] groups() default {};
}
