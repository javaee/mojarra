/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.ext.component;

import com.sun.faces.util.copier.CloneCopier;
import com.sun.faces.util.copier.CopyCtorCopier;
import com.sun.faces.util.copier.NewInstanceCopier;
import com.sun.faces.util.copier.SerializationCopier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import static javax.faces.validator.BeanValidator.EMPTY_VALIDATION_GROUPS_PATTERN;
import static javax.faces.validator.BeanValidator.VALIDATION_GROUPS_DELIMITER;
import static javax.faces.validator.BeanValidator.VALIDATOR_ID;
import static com.sun.faces.util.ReflectionUtils.setProperties;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponentBase;
import static javax.faces.validator.BeanValidator.MESSAGE_ID;
import static javax.faces.validator.BeanValidator.VALIDATOR_FACTORY_KEY;
import javax.faces.validator.ValidatorException;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

public class UIValidateWholeBean extends UIComponentBase implements PartialStateHolder {
    
    private static final Logger LOGGER =
         Logger.getLogger("javax.faces.validator", "javax.faces.LogStrings");

    public static final String FAMILY = "com.sun.faces.ext.validateWholeBean";
    
    private transient String validationGroups;

    private transient Class[] cachedValidationGroups;
    
    enum PropertyKeys {
        /**
         * <p>Custom message to be displayed when validation fails.</p>
         */
        validatorMessage,
    }
    
    
            
    @Override
    public String getFamily() {
        return FAMILY;
    }
    
    public void setValidationGroups(String validationGroups) {

        clearInitialState();        
        String newValidationGroups = validationGroups;
        
        // treat empty list as null
        if (newValidationGroups != null && newValidationGroups.matches(EMPTY_VALIDATION_GROUPS_PATTERN)) {
            newValidationGroups = null;
        }
        
        // only clear cache of validation group classes if value is changing
        if (newValidationGroups == null && validationGroups != null) {
            cachedValidationGroups = null;
        }
        
        if (newValidationGroups != null && validationGroups != null && !newValidationGroups.equals(validationGroups)) {
            cachedValidationGroups = null;
        }
        
        if (newValidationGroups != null && validationGroups == null) {
            cachedValidationGroups = null;
        }
        
        this.validationGroups = newValidationGroups;
    }

    public String getValidationGroups() {
        return validationGroups;
    }
    
    /**
     * <p>If there has been a call to {@link #setValidatorMessage} on this
     * instance, return the message.  Otherwise, call {@link #getValueExpression}
     * passing the key "validatorMessage", get the result of the expression, and return it.
     * Any {@link ELException}s thrown during the call to <code>getValue()</code>
     * must be wrapped in a {@link FacesException} and rethrown.
     * 
     * @return the validator message.
     */

    public String getValidatorMessage() {

        return (String) getStateHelper().eval(PropertyKeys.validatorMessage);

    }

    /**
     * <p>Override any {@link ValueExpression} set for the "validatorMessage"
     * with the literal argument provided to this method.  Subsequent calls
     * to {@link #getValidatorMessage} will return this value;</p>
     *
     * @param message the literal message value to be displayed in the event
     *                validation fails.
     */

    public void setValidatorMessage(String message) {

        getStateHelper().put(PropertyKeys.validatorMessage, message);

    }
    

    
    @Override
    public void processValidators(FacesContext context) {
        try {
            executeWholeBeanValidation(context);
        }
        catch (ValidatorException ve) {
            FacesMessage message;
            String validatorMessageString = getValidatorMessage();
            
            if (null != validatorMessageString) {
                message =
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                validatorMessageString,
                                validatorMessageString);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
            } else {
                Collection<FacesMessage> messages = ve.getFacesMessages();
                if (null != messages) {
                    message = null;
                    String cid = getClientId(context);
                    for (FacesMessage m : messages) {
                        context.addMessage(cid, m);
                    }
                } else {
                    message = ve.getFacesMessage();
                }
            }
            if (message != null) {
                context.addMessage(getClientId(context), message);
            }
        }
    }
    
    private void executeWholeBeanValidation(FacesContext context) throws ValidatorException {
        ValueExpression beanVE = this.getValueExpression("value");
        if (null == beanVE) {
            throw new FacesException("The \"value\" attribute is required");
        }
        Object val = beanVE.getValue(context.getELContext());
        
        // Inspect the status of field level validation
        Map<Object, Map<String, Object>> candidates = getCandidates(context, false);
        if (candidates.isEmpty()) {
            return;
        }
        if (!candidates.containsKey(val)) {
            return;
        }
        Map<String, Object> candidate = candidates.get(val);
        
        // Verify that none of the field level properties failed validation
        for (Map.Entry<String, Object> cur : candidate.entrySet()) {
            if (FAILED_FIELD_LEVEL_VALIDATION.equals(cur.getValue())) {
                return;
            }
        }

        // Copy the value so that class-level validation can be performed
        // without corrupting the real value
        Object valCopy = null;
        if (val instanceof Cloneable) {
            try {
                CloneCopier cc = new CloneCopier();
                valCopy = cc.copy(val);
            } catch (IllegalStateException ise) { }
        } else if (val instanceof Serializable) {
            try {
                SerializationCopier sc = new SerializationCopier();
                valCopy = sc.copy(val);
            } catch (IllegalStateException ise) { }
        } else {
            try {
                CopyCtorCopier ccc = new CopyCtorCopier();
                valCopy = ccc.copy(val);
            } catch (IllegalStateException ise) {
                try {
                    NewInstanceCopier nic = new NewInstanceCopier();
                    valCopy = nic.copy(val);
                } catch (IllegalStateException ise2) { }
            }
        }
        if (null == valCopy) {
            throw new FacesException("Unable to copy value from " + beanVE.getExpressionString());
        }
        
        String groups = getValidationGroups();
        if (null == groups || 0 == groups.length()) {
            throw new FacesException("The \"validationGroups\" attribute is required");
        }

        ValidatorFactory validatorFactory;
        Object cachedObject = context.getExternalContext().getApplicationMap().get(VALIDATOR_FACTORY_KEY);
        if (cachedObject instanceof ValidatorFactory) {
            validatorFactory = (ValidatorFactory) cachedObject;
        }
        else {
            try {
                validatorFactory = Validation.buildDefaultValidatorFactory();
            }
            catch (ValidationException e) {
                throw new FacesException("Could not build a default Bean Validator factory", e);
            }
            context.getExternalContext().getApplicationMap().put(VALIDATOR_FACTORY_KEY, validatorFactory);
        }

        ValidatorContext validatorContext = validatorFactory.usingContext();
        MessageInterpolator jsfMessageInterpolator = 
                new UIValidateWholeBean.JsfAwareMessageInterpolator(context, 
                           validatorFactory.getMessageInterpolator());
        validatorContext.messageInterpolator(jsfMessageInterpolator);
        javax.validation.Validator beanValidator = validatorContext.getValidator();
        
        Class [] validationGroupArray;
        validationGroupArray = parseValidationGroups(groups);
        // Populate the value copy with the validated values from the candidate
        setProperties(valCopy, candidate);
        Set result = null;
        try {
            result = beanValidator.validate(valCopy, validationGroupArray);
        } catch (IllegalArgumentException iae) {
                String failureMessage = "Unable to validate expression " +
                        beanVE.getExpressionString() +
               " using Bean Validation.  Unable to get value of expression. "+
                        " Message from Bean Validation: " + iae.getMessage();
                LOGGER.fine(failureMessage);
        }
        Set<ConstraintViolation<?>> violations = result;

        if (violations != null && !violations.isEmpty()) {
            ValidatorException toThrow;
            if (1 == violations.size()) {
                ConstraintViolation violation = violations.iterator().next();
                    toThrow = new ValidatorException(MessageFactory.getMessage(
                          context,
                          MESSAGE_ID,
                          violation.getMessage(),
                          MessageFactory.getLabel(context, this)));
                } else {
                    Set<FacesMessage> messages = new LinkedHashSet<>(
                          violations.size());
                    for (ConstraintViolation violation : violations) {
                        messages.add(MessageFactory.getMessage(context,
                                                               MESSAGE_ID,
                                                               violation.getMessage(),
                                                               MessageFactory.getLabel(
                                                                     context,
                                                                     this)));
                    }
                    toThrow = new ValidatorException(messages);
                }
                
            throw toThrow;
        }
    }
    
    private static class JsfAwareMessageInterpolator implements MessageInterpolator {

        private final FacesContext context;
        private final MessageInterpolator delegate;

        public JsfAwareMessageInterpolator(FacesContext context, MessageInterpolator delegate) {
            this.context = context;
            this.delegate = delegate;
        }

        @Override
        public String interpolate(String message, MessageInterpolator.Context context) {
            Locale locale = this.context.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
            return delegate.interpolate(message, context, locale);
        }

        @Override
        public String interpolate(String message, MessageInterpolator.Context context, Locale locale) {
            return delegate.interpolate(message, context, locale);
        }

    }
    
    
    // ----------------------------------------------------- StateHolder Methods

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        Object[] result = null;
        if (!initialStateMarked()) {
            Object values[] = new Object[1];
            values[0] = validationGroups;
            return values;
        }
        
        return (result);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (state != null) {
            Object values[] = (Object[]) state;
            validationGroups = (String) values[0];
        }
    }
    
    
    
    private Class[] parseValidationGroups(String validationGroupsStr) {
        if (cachedValidationGroups != null) {
            return cachedValidationGroups;
        }

        List<Class> validationGroupsList = new ArrayList<>();
        String[] classNames = validationGroupsStr.split(VALIDATION_GROUPS_DELIMITER);
        for (String className : classNames) {
            className = className.trim();
            if (className.length() == 0) {
                continue;
            }

            if (className.equals(Default.class.getName())) {
                validationGroupsList.add(Default.class);
            }
            else {
                try {
                    validationGroupsList.add(Class.forName(className, false, Thread.currentThread().getContextClassLoader()));
                } catch (ClassNotFoundException e1) {
                    try {
                        validationGroupsList.add(Class.forName(className));
                    } catch (ClassNotFoundException e2) {
                        throw new FacesException("Validation group not found: " + className);
                    }
                }
            }
        }

        cachedValidationGroups = validationGroupsList.toArray(new Class[validationGroupsList.size()]);
        return cachedValidationGroups;
    }
    
   /*
    * Copied from jsf-api because we can't import private stuff.
    */
     
    private static final String FAILED_FIELD_LEVEL_VALIDATION = VALIDATOR_ID + ".FAILED_FIELD_LEVEL_VALIDATION";
    
    private static final String MULTI_FIELD_VALIDATION_CANDIDATES =
            VALIDATOR_ID + ".MULTI_FIELD_VALIDATION_CANDIDATES";
    
   /*
    * We need to store a data structure in FacesContext attrs that captures the necessary
    * information for multi-field validation to be performed by a UIValidateWholeBean.
    * This is
    *
    * the object instance, a subset of whose properties are the set of fields to be considered
    * in the multi field validation.
    *
    * the name=value pairs for each of the fields
    *
    * FacesContext.getAttributes().get(MULTI_FIELD_VALIDATION_CANDIDATES) returns
    * Map<Object, Map<String, Object>>.
    */
    private Map<Object, Map<String, Object>> getCandidates(FacesContext context, boolean create) {
        Map<Object, Map<String, Object>> result;
        Map<Object, Object> attrs = context.getAttributes();
        result = (Map<Object, Map<String, Object>>) attrs.get(MULTI_FIELD_VALIDATION_CANDIDATES);
        if (null == result) {
            if (create) {
                result = new HashMap<>();
                attrs.put(MULTI_FIELD_VALIDATION_CANDIDATES, result);
            } else {
                result = Collections.emptyMap();
            } 
        }
        
        return result;
    }
    
    
}  
