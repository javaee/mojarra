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

package com.sun.faces.ext.component;

import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.copier.CloneCopier;
import com.sun.faces.util.copier.CopyCtorCopier;
import com.sun.faces.util.copier.NewInstanceCopier;
import com.sun.faces.util.copier.SerializationCopier;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator; 
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

class WholeBeanValidator implements Validator {
    private static final Logger LOGGER =
         Logger.getLogger("javax.faces.validator", "javax.faces.LogStrings");

    @Override
    public void validate(FacesContext context, UIComponent c, Object value) throws ValidatorException {
        ValueExpression beanVE = c.getValueExpression("value");
        if (null == beanVE) {
            throw new FacesException("The \"value\" attribute is required");
        }
        Object val = beanVE.getValue(context.getELContext());
        
        // Inspect the status of field level validation
        Map<Object, Map<String, BeanValidator.ComponentValueTuple>> candidates = BeanValidator.getMultiFieldValidationCandidates(context, false);
        if (candidates.isEmpty() || !candidates.containsKey(val)) {
            return;
        }
        Map<String, BeanValidator.ComponentValueTuple> candidate = candidates.get(val);
        // Verify that none of the field level properties failed validation
        for (Map.Entry<String, BeanValidator.ComponentValueTuple> cur : candidate.entrySet()) {
            if (BeanValidator.FAILED_FIELD_LEVEL_VALIDATION.equals(cur.getValue().getValue())) {
                return;
            }
        }
        
        Object valCopy = copyObjectAndPopulateWithCandidateValues(beanVE, val, candidate);
        javax.validation.Validator beanValidator = getBeanValidator(context);
        UIValidateWholeBean component = (UIValidateWholeBean) c;
        
        Class[] validationGroupArray = component.getValidationGroupsArray();
        Set result = null;
        try {
            result = beanValidator.validate(valCopy, validationGroupArray);
        } catch (IllegalArgumentException iae) {
            String failureMessage = "Unable to validate expression " + beanVE.getExpressionString() + " using Bean Validation.  Unable to get value of expression. " + " Message from Bean Validation: " + iae.getMessage();
            LOGGER.fine(failureMessage);
        }
        Set<ConstraintViolation<?>> violations = result;
        if (violations != null && !violations.isEmpty()) {
            ValidatorException toThrow;
            if (1 == violations.size()) {
                ConstraintViolation violation = violations.iterator().next();
                toThrow = new ValidatorException(MessageFactory.getMessage(context, BeanValidator.MESSAGE_ID, violation.getMessage(), MessageFactory.getLabel(context, component)));
            } else {
                Set<FacesMessage> messages = new LinkedHashSet<>(violations.size());
                for (ConstraintViolation violation : violations) {
                    messages.add(MessageFactory.getMessage(context, BeanValidator.MESSAGE_ID, violation.getMessage(), MessageFactory.getLabel(context, component)));
                }
                toThrow = new ValidatorException(messages);
            }
            // Mark the components as invalid to prevent them from receiving
            // values during updateModelValues
            for (Map.Entry<String, BeanValidator.ComponentValueTuple> cur : candidate.entrySet()) {
                cur.getValue().getComponent().setValid(false);
            }
            throw toThrow;
        }
    }
    
    private Object copyObjectAndPopulateWithCandidateValues(ValueExpression beanVE,
            Object val, 
            Map<String, BeanValidator.ComponentValueTuple> candidate) {
        // <editor-fold defaultstate="collapsed">
        
        // Populate the value copy with the validated values from the candidate
        Map<String, Object> propertiesToSet = new HashMap<>();
        for (Map.Entry<String, BeanValidator.ComponentValueTuple> cur : candidate.entrySet()) {
            propertiesToSet.put(cur.getKey(), cur.getValue().getValue());
        }
        // Copy the value so that class-level validation can be performed
        // without corrupting the real value
        Object valCopy = null;
        if (val instanceof Cloneable) {
            try {
                CloneCopier cc = new CloneCopier();
                valCopy = cc.copy(val);
            } catch (IllegalStateException ise) {
            }
        } else if (val instanceof Serializable) {
            try {
                SerializationCopier sc = new SerializationCopier();
                valCopy = sc.copy(val);
            } catch (IllegalStateException ise) {
            }
        } else {
            try {
                CopyCtorCopier ccc = new CopyCtorCopier();
                valCopy = ccc.copy(val);
            } catch (IllegalStateException ise) {
                try {
                    NewInstanceCopier nic = new NewInstanceCopier();
                    valCopy = nic.copy(val);
                } catch (IllegalStateException ise2) {
                }
            }
        }
        if (null == valCopy) {
            throw new FacesException("Unable to copy value from " + beanVE.getExpressionString());
        }
        
        ReflectionUtils.setProperties(valCopy, propertiesToSet);
        return valCopy;
        
        // </editor-fold>
    }
    
    private javax.validation.Validator getBeanValidator(FacesContext context) {
        // <editor-fold defaultstate="collapsed">
        ValidatorFactory validatorFactory;
        Object cachedObject = context.getExternalContext().getApplicationMap().get(BeanValidator.VALIDATOR_FACTORY_KEY);
        if (cachedObject instanceof ValidatorFactory) {
            validatorFactory = (ValidatorFactory) cachedObject;
        } else {
            try {
                validatorFactory = Validation.buildDefaultValidatorFactory();
            } catch (ValidationException e) {
                throw new FacesException("Could not build a default Bean Validator factory", e);
            }
            context.getExternalContext().getApplicationMap().put(BeanValidator.VALIDATOR_FACTORY_KEY, validatorFactory);
        }
        ValidatorContext validatorContext = validatorFactory.usingContext();
        MessageInterpolator jsfMessageInterpolator = new JsfAwareMessageInterpolator(context, validatorFactory.getMessageInterpolator());
        validatorContext.messageInterpolator(jsfMessageInterpolator);
        return validatorContext.getValidator();
        // </editor-fold>
    }

    // <editor-fold defaultstate="collapsed">
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
    // </editor-fold>
    
}
