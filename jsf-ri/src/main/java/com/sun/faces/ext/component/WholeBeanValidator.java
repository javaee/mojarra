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

import static com.sun.faces.ext.component.MultiFieldValidationUtils.FAILED_FIELD_LEVEL_VALIDATION;
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

import static com.sun.faces.util.ReflectionUtils.instance;
import com.sun.faces.util.copier.Copier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import static javax.faces.component.visit.VisitResult.ACCEPT;

class WholeBeanValidator implements Validator {

    private static final Logger LOGGER
            = Logger.getLogger("javax.faces.validator", "javax.faces.LogStrings");    
    
    private static final String ERROR_MISSING_FORM
            = "f:validateWholeBean must be nested directly in an UIForm.";

    private static final String ERROR_COPIER_NAME = "The copier name should be a Java valid simple/qualified name.";

    private static final String COPIER_PREFIX = "com.sun.faces.util.copier.";

    private Class cc = null;

    @Override
    public void validate(FacesContext context, UIComponent c, Object value) throws ValidatorException {        

        // check if the parent of this f:validateWholeBean is a form                  
        UIComponent parent = c.getParent();
        if (!(parent instanceof UIForm)) {
            throw new IllegalArgumentException(ERROR_MISSING_FORM);
        }

        UIForm form = (UIForm) parent;

        ValueExpression beanVE = c.getValueExpression("value");
        if (null == beanVE) {
            throw new FacesException("The \"value\" attribute is required");
        }
		
        Object val = beanVE.getValue(context.getELContext());
        String copierType = (String) c.getAttributes().get("copierType");
        
        // Inspect the status of field level validation
        Map<Object, Map<String, Map<String, Object>>> candidates = MultiFieldValidationUtils.getMultiFieldValidationCandidates(context, false);
        
        /* this should be removed or commented
         if (candidates.isEmpty() || !candidates.containsKey(val)) {
         return;
         }
         */        

        if (context.isValidationFailed()) {
            return;
        }

        Map<String, Map<String, Object>> candidate = null;
        if ((!candidates.isEmpty()) && (candidates.containsKey(val))) {
            candidate = candidates.get(val);
            // Verify that none of the field level properties failed validation
            for (Map.Entry<String, Map<String, Object>> cur : candidate.entrySet()) {
                if (FAILED_FIELD_LEVEL_VALIDATION.equals(cur.getValue().get("value"))) { // NOPMD
                    return;
                }
            }
        }

        AddRemainingCandidateFieldsCallback addRemainingCandidateFieldsCallback = new AddRemainingCandidateFieldsCallback(context, val);
        form.visitTree(VisitContext.createVisitContext(context), addRemainingCandidateFieldsCallback);
        candidate = addRemainingCandidateFieldsCallback.getCandidate();

        if (candidate.isEmpty()) {
            return;
        }
        
        Object valCopy = copyObjectAndPopulateWithCandidateValues(context, beanVE, val, copierType, candidate);
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
            for (Map.Entry<String, Map<String, Object>> cur : candidate.entrySet()) {
                ((EditableValueHolder) cur.getValue().get("component")).setValid(false);
            }
            throw toThrow;
        }
    }

    private Object copyObjectAndPopulateWithCandidateValues(FacesContext context, ValueExpression beanVE,
            Object val, String copierType, Map<String, Map<String, Object>> candidate) {
        // <editor-fold defaultstate="collapsed">

        // Populate the value copy with the validated values from the candidate
        Map<String, Object> propertiesToSet = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> cur : candidate.entrySet()) {
            propertiesToSet.put(cur.getKey(), cur.getValue().get("value"));
        }

        // Copy the value so that class-level validation can be performed
        // without corrupting the real value
		
        // Check if the user has specified a copy strategy         
        Object valCopy = null;
        Copier copier = getCopier(context, copierType);
        
        if (copier != null) {
            valCopy = copier.copy(val);
        } else {
            try {
                NewInstanceCopier nic = new NewInstanceCopier();
                valCopy = nic.copy(val);                
            } catch (IllegalStateException ise2) {
            }
            if (null == valCopy) {
                if (val instanceof Serializable) {
                    try {
                        SerializationCopier sc = new SerializationCopier();
                        valCopy = sc.copy(val);                        
                    } catch (IllegalStateException ise) {
                    }
                } else if (val instanceof Cloneable) {
                    try {
                        CloneCopier cc = new CloneCopier();
                        valCopy = cc.copy(val);                        
                    } catch (IllegalStateException ise) {
                    }
                } else {
                    try {
                        CopyCtorCopier ccc = new CopyCtorCopier();
                        valCopy = ccc.copy(val);                        
                    } catch (IllegalStateException ise) {
                    }
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

    private Copier getCopier(FacesContext context, String copierType) {
        Copier copier = null;

        if (!isEmpty(copierType)) {

            // or should validate only against {"MultiStrategyCopier", "SerializationCopier",
            // "NewInstanceCopier", "CopyCtorCopier", "CloneCopier"} strings?
            if (isCopierTypeSimpleName(copierType)) {                
                copierType = COPIER_PREFIX.concat(copierType);
            } else if (!isName(copierType)) {
                throw new IllegalArgumentException(ERROR_COPIER_NAME);
            }

            Object expressionResult = evaluateExpressionGet(context, copierType);

            if (expressionResult instanceof Copier) {
                copier = (Copier) expressionResult;
            } else if (expressionResult instanceof String) {
                copier = instance((String) expressionResult);
            }
        }

        // default to the hard-coded copy strategy and let MultiStrategyCopier
		// as an user option
        // if (copier == null) {
        //     copier = new MultiStrategyCopier();
        // }
        return copier;
    }

    @SuppressWarnings("unchecked")
    private static <T> T evaluateExpressionGet(FacesContext context, String expression) {
        if (expression == null) {
            return null;
        }

        return (T) context.getApplication().evaluateExpressionGet(context, expression, Object.class);
    }

    private static boolean isCopierTypeSimpleName(String copierType) {
        return (isIdentifier(copierType) && !(isKeyword(copierType)));
    }

    // maybe the following four methods should be moved in com.sun.faces.util   
    private static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private static boolean isName(CharSequence name) {
        String id = name.toString();

        for (String s : id.split("\\.", -1)) {
            if (!isIdentifier(s) || isKeyword(s)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isIdentifier(CharSequence name) {
        String id = name.toString();

        if (id.length() == 0) {
            return false;
        }
        int cp = id.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp)) {
            return false;
        }
        for (int i = Character.charCount(cp);
                i < id.length();
                i += Character.charCount(cp)) {
            cp = id.codePointAt(i);
            if (!Character.isJavaIdentifierPart(cp)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isKeyword(CharSequence s) {
        String keywordOrLiteral = s.toString();
        return keywords.contains(keywordOrLiteral);
    }

    private final static Set<String> keywords;

    static {
        Set<String> s = new HashSet<String>();
        String[] kws = {
            "abstract", "continue", "for", "new", "switch",
            "assert", "default", "if", "package", "synchronized",
            "boolean", "do", "goto", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while",
            // literals
            "null", "true", "false"
        };
        for (String kw : kws) {
            s.add(kw);
        }
        keywords = Collections.unmodifiableSet(s);
    }    

    private static class AddRemainingCandidateFieldsCallback implements VisitCallback {

        private final FacesContext context;
        private final Object base;
        private final Map<String, Map<String, Object>> candidate = new HashMap();

        public AddRemainingCandidateFieldsCallback(final FacesContext context, final Object base) {
            this.context = context;
            this.base = base;
        }

        final Map<String, Map<String, Object>> getCandidate() {
            return candidate;
        }

        @Override
        public VisitResult visit(VisitContext vc, UIComponent uic) {            
            if ((uic instanceof EditableValueHolder) && (uic.isRendered()) && (!(uic instanceof UIValidateWholeBean))) {
                ValueExpression valueExpression = uic.getValueExpression("value");                
                if (valueExpression != null) {

                    ValueExpressionAnalyzer expressionAnalyzer = new ValueExpressionAnalyzer(valueExpression);
                    ValueReference valueReference = expressionAnalyzer.getReference(context.getELContext());                    

                    if ((valueReference != null) && (valueReference.getBase().equals(base))) {
                        Map<String, Object> tuple = new HashMap<>();
                        tuple.put("component", uic);
                        tuple.put("value", (((UIInput) uic).getSubmittedValue() != null) ? ((UIInput) uic).getSubmittedValue() : ((UIInput) uic).getLocalValue());                        
                        candidate.put(valueReference.getProperty(), tuple);
                    }
                }
            }
            return ACCEPT;
        }
    }

    // </editor-fold>
}
