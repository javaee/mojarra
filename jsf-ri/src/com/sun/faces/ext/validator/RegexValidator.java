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

    private ValueExpression regex;

    /**
     * The Regular Expression property to validate against.  This property must be
     * a ValueExpression that resolves to a String in the format of the java.util.regex
     * patterns.
     * @param pattern
     */
    public void setPattern(ValueExpression pattern) {
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

        ELContext elcontext = context.getELContext();

        // if not String, complain
        String regexStr;
        if (!regex.getType(elcontext).equals(java.lang.String.class)) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.NOT_STRING",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }
        // cast safe, since we just tested
        regexStr = (String) regex.getValue(elcontext);

        if (!(obj instanceof String)) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.NOT_STRING",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        if (regexStr.equals("")) {
            fmsg = MojarraMessageFactory.getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.PATTERN_NOT_SET",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        try {
            Pattern pattern = Pattern.compile(regexStr);
            Matcher matcher = pattern.matcher((String) obj);
            if (!matcher.matches()) {
                Object[] params = {regexStr};
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
