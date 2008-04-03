/*
 * $Id: RegexValidator.java,v 1.2 2008/04/01 22:31:19 driscoll Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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


package com.sun.faces.ext.validator;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.regex.*;
import java.io.Serializable;
import java.text.MessageFormat;
import javax.el.ValueExpression;
import javax.el.ELContext;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.el.ValueBinding;

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
            fmsg = getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.NOT_STRING",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }
        // cast safe, since we just tested
        regexStr = (String) regex.getValue(elcontext);

        if (!(obj instanceof String)) {
            fmsg = getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.NOT_STRING",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }

        if (regexStr.equals("")) {
            fmsg = getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.PATTERN_NOT_SET",
                    (Object) null);
            throw new ValidatorException(fmsg);
        }
        
        try {
            Pattern pattern = Pattern.compile(regexStr);
            Matcher matcher = pattern.matcher((String) obj);
            if (!matcher.matches()) {
                Object[] params = {regexStr};
                fmsg = getMessage(locale,
                        "com.sun.faces.ext.validator.regexValidator.NOT_MATCHED",
                        params);
                throw new ValidatorException(fmsg);
            }
        } catch (PatternSyntaxException pse) {
            Object[] params = {pse.getMessage()};
            fmsg = getMessage(locale,
                    "com.sun.faces.ext.validator.regexValidator.EXP_ERR",
                    (Object) null);
            throw new ValidatorException(fmsg, pse);
        }
    }

    /*
     * This method copied wholesale from MessageFactory.
     * But instead of looking in the system messages after checking for
     * user messages, we look in our private bundle.
     */
    private FacesMessage getMessage(Locale locale,
            String messageId,
            Object... params) {
        String summary = null;
        String detail = null;
        ResourceBundle bundle;
        String bundleName;

        // see if we have a user-provided bundle
        if (null != (bundleName = getApplication().getMessageBundle())) {
            if (null !=
                    (bundle =
                    ResourceBundle.getBundle(bundleName, locale,
                    getCurrentLoader(bundleName)))) {
                // see if we have a hit
                try {
                    summary = bundle.getString(messageId);
                    detail = bundle.getString(messageId + "_detail");
                } catch (MissingResourceException e) {
                    // ignore
                }
            }
        }

        // we couldn't find a summary in the user-provided bundle
        if (null == summary) {
            // see if we have a summary in the app provided bundle
            bundle = ResourceBundle.getBundle("com.sun.faces.ext.validator.regexMessages",
                    locale,
                    getCurrentLoader(this));
            if (null == bundle) {
                throw new NullPointerException();
            }
            // see if we have a hit
            try {
                summary = bundle.getString(messageId);
                // we couldn't find a summary anywhere!  Return null
                if (null == summary) {
                    return null;
                }
                detail = bundle.getString(messageId + "_detail");
            } catch (MissingResourceException e) {
                // ignore
            }
        }
        // At this point, we have a summary and a bundle.     
        FacesMessage ret = new BindingFacesMessage(locale, summary, detail, params);
        ret.setSeverity(FacesMessage.SEVERITY_ERROR);
        return (ret);
    }

    /**
     * Utility method to return the current application for this context.
     * @return application The current application for this context
     */
    private static Application getApplication() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            return (FacesContext.getCurrentInstance().getApplication());
        }
        ApplicationFactory afactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return (afactory.getApplication());
    }

    /**
     * Utility method to get the current classloader.
     * 
     * @param fallbackClass Value to use if the context classloader not set
     * @return classloader The current ClassLoader for this thread's context
     */
    private static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }

    /**
     * This class overrides FacesMessage to provide the evaluation
     * of binding expressions in addition to Strings.
     * It is often the case, that a binding expression may reference
     * a localized property value that would be used as a 
     * substitution parameter in the message.  For example:
     *  <code>#{bundle.userLabel}</code>
     * "bundle" may not be available until the page is rendered.
     * The "late" binding evaluation in <code>getSummary</code> and 
     * <code>getDetail</code> allow the expression to be evaluated
     * when that property is available.
     */
    static class BindingFacesMessage extends FacesMessage {

        BindingFacesMessage(
                Locale locale,
                String messageFormat,
                String detailMessageFormat,
                // array of parameters, both Strings and ValueBindings
                Object[] parameters) {

            super(messageFormat, detailMessageFormat);
            this.locale = locale;
            this.parameters = parameters;
            if (parameters != null) {
                resolvedParameters = new Object[parameters.length];
            }
        }

        @Override
        public String getSummary() {
            String pattern = super.getSummary();
            resolveBindings();
            return getFormattedString(pattern, resolvedParameters);
        }

        @Override
        public String getDetail() {
            String pattern = super.getDetail();
            resolveBindings();
            return getFormattedString(pattern, resolvedParameters);
        }

        private void resolveBindings() {
            FacesContext context = null;
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    Object o = parameters[i];
                    if (o instanceof ValueBinding) {
                        if (context == null) {
                            context = FacesContext.getCurrentInstance();
                        }
                        o = ((ValueBinding) o).getValue(context);
                    }
                    if (o instanceof ValueExpression) {
                        if (context == null) {
                            context = FacesContext.getCurrentInstance();
                        }
                        o = ((ValueExpression) o).getValue(context.getELContext());
                    }
                    // to avoid 'null' appearing in message
                    if (o == null) {
                        o = "";
                    }
                    resolvedParameters[i] = o;
                }
            }
        }

        private String getFormattedString(String msgtext, Object[] params) {
            String localizedStr = null;

            if (params == null || msgtext == null) {
                return msgtext;
            }
            StringBuffer b = new StringBuffer(100);
            MessageFormat mf = new MessageFormat(msgtext);
            if (locale != null) {
                mf.setLocale(locale);
                b.append(mf.format(params));
                localizedStr = b.toString();
            }
            return localizedStr;
        }
        private Locale locale;
        private Object[] parameters;
        private Object[] resolvedParameters;
    }
}
