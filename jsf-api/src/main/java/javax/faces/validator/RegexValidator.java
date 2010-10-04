/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.validator;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.faces.component.PartialStateHolder;

/**
 * <p class="changed_added_2_0"><span
 * class="changed_modified_2_0_rev_a">A Validator</span> that checks
 * against a Regular Expression (which is the pattern property).  The
 * pattern must resolve to a String that follows the java.util.regex
 * standards.</p>
 * @since 2.0
 */
public class RegexValidator implements Validator, PartialStateHolder {

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
        clearInitialState();
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

        if (context == null) {
            throw new NullPointerException();
        }
        if (component == null) {
            throw new NullPointerException();
        }
        
        if (value == null) {
            return;
        }

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

        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[1];
            values[0] = regex;

            return (values);
        }
        return null;

    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {
            Object values[] = (Object[]) state;
            regex = (String) values[0];
        }

    }


    private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }


    private boolean initialState;

    public void markInitialState() {
        initialState = true;
    }

    public boolean initialStateMarked() {
        return initialState;
    }

    public void clearInitialState() {
        initialState = false;
    }
}
