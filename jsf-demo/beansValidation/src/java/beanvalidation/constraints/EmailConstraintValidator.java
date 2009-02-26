package beanvalidation.constraints;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Dan Allen
 */
public class EmailConstraintValidator implements ConstraintValidator<Email, String> {

	private Pattern pattern;

	public void initialize(Email parameters) {
		// credit goes to http://www.regular-expressions.info/email.html
		pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
	}

	public boolean isValid(String value, ConstraintValidatorContext ctxt) {
		if (value == null || value.length() == 0) {
			return true;
		}

		return pattern.matcher(value).matches();
	}

}
