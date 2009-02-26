package beanvalidation.constraints;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Valid a credit card number using the standard LUHN check. Implementation borrowed from Mojarra's
 * CreditCardValidator.
 *
 * @author Dan Allen
 */
public class CreditCardConstraintValidator implements ConstraintValidator<CreditCard, String> {

	private Pattern basicSyntaxPattern;

	public void initialize(CreditCard parameters) {
		basicSyntaxPattern = Pattern.compile("^[0-9\\ \\-]*$");
	}

	public boolean isValid(String value, ConstraintValidatorContext ctxt) {
		if (value == null || value.length() == 0) {
			return true;
		}

		if (!basicSyntaxPattern.matcher(value).matches()) {
			return false;
		}

		return luhnCheck(stripNonDigits(value));
	}

	private String stripNonDigits(String s) {
        return s.replaceAll(" ", "").replaceAll("-", "");
    }


	private boolean luhnCheck(String number) {
        int sum = 0;

        boolean timestwo = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (timestwo) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            timestwo = !timestwo;
        }
        return sum % 10 == 0;
    }

}
