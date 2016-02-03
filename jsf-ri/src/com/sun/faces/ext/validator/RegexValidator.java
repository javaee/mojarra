package com.sun.faces.ext.validator;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.regex.*;
import java.io.Serializable;
import javax.el.ValueExpression;
import javax.el.ELContext;

/**
 * A Validator that checks against a Regular Expression (which is the pattern 
 * property).  The pattern must resolve to a String that follows the java.util.regex
 * standards.  
 * @author driscoll
 */
public class RegexValidator implements Validator, Serializable {

    private static final long serialVersionUID = 1961950699958181806L;

    private String regex;


    /**
     * <p>The Regular Expression property to validate against.</p>
     *
     * @param pattern a regular expression pattern
     */
    public void setPattern(String pattern) {
        this.regex = pattern;
    }

    /**
     * Validate a String against a regular expression pattern...  The full regex
     * pattern must be matched in order to pass the validation.
     * @param context Context of this request
     * @param component The component wrapping this validator 
     * @param obj A string which will be compared to the pattern property of this validator.  Must be a string.
     */
    public void validate(FacesContext context, UIComponent component, Object obj) {

        FacesMessage fmsg;

        Locale locale = context.getViewRoot().getLocale();

        if (regex == null || regex.length() == 0) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.PATTERN_NOT_SET",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher((String) obj);
            if (!matcher.matches()) {
                Object[] params = { regex };
                fmsg = MojarraMessageFactory.getMessage(locale,
                        "com.sun.faces.ext.validator.regexValidator.NOT_MATCHED",
                        params);
                throw new ValidatorException(fmsg);
            }
        } catch (PatternSyntaxException pse) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.EXP_ERR",
                    (Object) null);
            throw new ValidatorException(fmsg, pse);
        }
    }
}
