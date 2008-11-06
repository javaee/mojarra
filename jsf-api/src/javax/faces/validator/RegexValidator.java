package javax.faces.validator;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.faces.component.StateHolder;

/**
 * <p class="changed_added_2_0">A Validator that checks against a
 * Regular Expression (which is the pattern property).  The pattern must
 * resolve to a String that follows the java.util.regex standards.</p>
 * @since 2.0
 */
public class RegexValidator implements Validator, StateHolder {

    private ValueExpression regex;

    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.RegularExpression";

    /**
     * <p>The message identifier of the {@link
     * javax.faces.application.FacesMessage} to be created if the
     * <code>ValueExpression</code> returned from {@link #getPattern}
     * does not evaluate to a String.</p>
     */
    public static final String NOT_STRING_MESSAGE_ID =
         "javax.faces.validator.RegexValidator.NOT_STRING";

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
     * <p>The Regular Expression property to validate against.  This
     * property must be a ValueExpression that resolves to a String in
     * the format of the java.util.regex patterns.</p>
     * @param pattern a <code>ValueExpression</code> that evaluates to a
     * String that is the regular expression pattern
     */
    public void setPattern(ValueExpression pattern) {
        this.regex = pattern;
    }

    /**
     * <p>Return the <code>ValueExpression</code> that yields the
     * regular expression pattern when evaluated.</p>
     */

    public ValueExpression getPattern() {
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

        ELContext elcontext = context.getELContext();

        // if not String, complain
        String regexStr;
        if (!regex.getType(elcontext).equals(java.lang.String.class)) {
            fmsg = MessageFactory.getMessage(locale,
                    NOT_STRING_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg);
        }
        // cast safe, since we just tested
        regexStr = (String) regex.getValue(elcontext);

        if (null == regexStr) {
            fmsg = MessageFactory.getMessage(locale,
                    PATTERN_NOT_SET_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        if (!(value instanceof String)) {
            fmsg = MessageFactory.getMessage(locale,
                    NOT_STRING_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        if (regexStr.equals("")) {
            fmsg = MessageFactory.getMessage(locale,
                    PATTERN_NOT_SET_MESSAGE_ID,
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        try {
            Pattern pattern = Pattern.compile(regexStr);
            Matcher matcher = pattern.matcher((String) value);
            if (!matcher.matches()) {
                Object[] params = {regexStr};
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

        Object values[] = new Object[2];
        values[0] = (null != regex) ? regex.getExpressionString() : null;
        values[1] = String.class;
        
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        if (null != values[0]) {
            regex = context.getApplication().getExpressionFactory().
                    createValueExpression(context.getELContext(), 
                    (String) values[0], (Class) values[1]);
        }

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }
    
}
