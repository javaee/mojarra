package javax.faces.validator;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.faces.component.StateHolder;

/**
 * <p class="changed_added_2_0">A Validator that checks against a
 * Regular Expression (which is the pattern property).  The pattern must
 * resolve to a String that follows the java.util.regex standards.</p>
 * @since 2.0
 */
public class RegexValidator implements Validator, StateHolder {

    private String regex;

    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.RegularExpression";

    /**
     * <p>The message identifier of the {@link
     * javax.faces.application.FacesMessage} to be created if the value
     * returned from {@link #getPattern} is <code>null</code> or the
     * empty String.</p>
     */
    public static final String PATTERN_NOT_SET_MESSAGE_ID =
         "javax.faces.validator.RegexValidator.PATTERN_NOT_SET";

    /**
     * <p>The message identifier of the {@link
     * javax.faces.application.FacesMessage} to be created if the act of
     * matching the value against the pattern returned from {@link
     * #getPattern} fails because the value does not match the
     * pattern.</p>
     */
    public static final String NOT_MATCHED_MESSAGE_ID =
         "javax.faces.validator.RegexValidator.NOT_MATCHED";

    /**
     * <p>The message identifier of the {@link
     * javax.faces.application.FacesMessage} to be created if the act of
     * matching the value against the pattern returned from {@link
     * #getPattern} fails because of a
     * <code>PatternSyntaxException</code>.</p>
     */
    public static final String MATCH_EXCEPTION_MESSAGE_ID =
         "javax.faces.validator.RegexValidator.MATCH_EXCEPTION";


    /**
     * <p>The Regular Expression property to validate against.</p>
     *
     * @param pattern a regular expression pattern
     */
    public void setPattern(String pattern) {
        this.regex = pattern;
    }

    /**
     * <p>Return the <code>ValueExpression</code> that yields the
     * regular expression pattern when evaluated.</p>
     */

    public String getPattern() {
        return this.regex;
    }

    /**

     * </p>Validate a String against a regular expression pattern.  The
     * full regex pattern must be matched in order to pass the
     * validation.</p>

     * @param context {@inheritDoc}
     * @param component {@inheritDoc}
     * @param value {@inheritDoc}

     * @throws NullPointerException {@inheritDoc}
     * @throws ValidatorException   {@inheritDoc}

     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) {

        FacesMessage fmsg;

        Locale locale = context.getViewRoot().getLocale();

        if (regex == null || regex.length() == 0) {
            fmsg = MessageFactory.getMessage(locale,
                    PATTERN_NOT_SET_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher((String) value);
            if (!matcher.matches()) {
                Object[] params = { regex };
                fmsg = MessageFactory.getMessage(locale,
                        NOT_MATCHED_MESSAGE_ID,
                        params);
                throw new ValidatorException(fmsg);
            }
        } catch (PatternSyntaxException pse) {
            fmsg = MessageFactory.getMessage(locale,
                    MATCH_EXCEPTION_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg, pse);
        }
    }
    
    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[1];
        values[0] = regex;

        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        regex = (String) values[0];

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }
    
}
