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

package javax.faces.validator;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidatorContext;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

/**
 * <p class="changed_added_2_0"><span
 * class="changed_modified_2_0_rev_a changed_modified_2_3">A Validator</span> that delegates
 * validation of the bean property to the Bean Validation API.</p>
 * @since 2.0
 */
public class BeanValidator implements Validator, PartialStateHolder {
    
    private static final Logger LOGGER =
         Logger.getLogger("javax.faces.validator", "javax.faces.LogStrings");

    private String validationGroups;

    private transient Class[] cachedValidationGroups;

    /**
     * <p class="changed_added_2_0">The standard validator id for this
     * validator, as defined by the JSF specification.</p>
     */
    public static final String VALIDATOR_ID = "javax.faces.Bean";
    
    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * a constraint failure is found.  The message format string for
     * this message may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the interpolated message from Bean Validation.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul>
     * <p>The message format string provided by the default implementation should be a the placeholder {0},
     * thus fully delegating the message handling to Bean Validation. A developer can override this message
     * format string to make it conform to other JSF validator messages (i.e., by including the component label)</p>
     */
    public static final String MESSAGE_ID = "javax.faces.validator.BeanValidator.MESSAGE";

    /**
     * <p class="changed_added_2_0">The name of the servlet context
     * attribute which holds the object used by JSF to obtain Validator
     * instances.  If the servlet context attribute is missing or
     * contains a null value, JSF is free to use this servlet context
     * attribute to store the ValidatorFactory bootstrapped by this
     * validator.</p>
     */
    public static final String VALIDATOR_FACTORY_KEY = "javax.faces.validator.beanValidator.ValidatorFactory";

    /**
     * <p class="changed_added_2_0">The delimiter that is used to
     * separate the list of fully-qualified group names as strings.</p>
     */
    public static final String VALIDATION_GROUPS_DELIMITER = ",";

    /**
     * <p class="changed_added_2_0">The regular expression pattern that
     * identifies an empty list of validation groups.</p>
     */
    public static final String EMPTY_VALIDATION_GROUPS_PATTERN = "^[\\W" + VALIDATION_GROUPS_DELIMITER + "]*$";
    
    /**
     * <p class="changed_added_2_0">If this param is defined, and
     * calling <code>toLowerCase().equals(&#8220;true&#8221;)</code> on a 
     * <code>String</code> representation of its value returns 
     * <code>true</code>, the runtime must not automatically add the
     * validator with validator-id equal to the value of the symbolic
     * constant {@link #VALIDATOR_ID} to the list of default validators.  
     * Setting this parameter to <code>true</code> will have the effect 
     * of disabling the automatic installation of Bean Validation to 
     * every input component in every view in the application, though 
     * manual installation is still possible.</p>
     * 
     */
    public static final String DISABLE_DEFAULT_BEAN_VALIDATOR_PARAM_NAME =
            "javax.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR";
    
    /**
     * <p class="changed_added_2_3">If this param is set, and calling 
     * toLowerCase().equals("true") on a
     * String representation of its value returns {@code true} take
     * the additional actions relating to <code>&lt;validateWholeBean /&gt;</code>
     * specified in {@link #validate}.</p>
     * 
     * @since 2.3
     */
    public static final String ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME = 
            "javax.faces.validator.ENABLE_VALIDATE_WHOLE_BEAN";
    
    //----------------------------------------------------------- multi-field validation
    
    private static final String MULTI_FIELD_VALIDATION_CANDIDATES =
            VALIDATOR_ID + ".MULTI_FIELD_VALIDATION_CANDIDATES";
    
    /**
     * <p class="changed_added_2_0">A comma-separated list of validation
     * groups which are used to filter which validations get checked by
     * this validator. If the validationGroupsArray attribute is omitted or
     * is empty, the validation groups will be inherited from the branch
     * defaults or, if there are no branch defaults, the {@link
     * javax.validation.groups.Default} group will be used.</p>
     *
     * @param validationGroups comma-separated list of validation groups
     * (string with only spaces and commas treated as null)
     */

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

    /**
     * <p class="changed_added_2_0">Return the validation groups passed
     * to the Validation API when checking constraints.  If the
     * validationGroupsArray attribute is omitted or empty, the validation
     * groups will be inherited from the branch defaults, or if there
     * are no branch defaults, the {@link
     * javax.validation.groups.Default} group will be used.</p>
     * 
     * @return the value of the {@code validatinGroups} attribute.
     */
    public String getValidationGroups() {
        return validationGroups;
    }

    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_3">Verify</span>
     * that the value is valid according to the Bean Validation constraints.</p>
     *
     * <div class="changed_added_2_0">

     * <p>Obtain a {@link ValidatorFactory} instance by calling {@link
     * javax.validation.Validation#buildDefaultValidatorFactory}.</p>

     * <p>Let <em>validationGroupsArray</em> be a <code>Class []</code>
     * representing validator groups set on the component by the tag
     * handler for this validator.  The first search component
     * terminates the search for the validation groups value.  If no
     * such value is found use the class name of {@link
     * javax.validation.groups.Default} as the value of the validation
     * groups.</p>

     * <p>Let <em>valueExpression</em> be the return from calling {@link
     * UIComponent#getValueExpression} on the argument
     * <em>component</em>, passing the literal string
     * &#8220;value&#8221; (without the quotes) as an argument.  If this
     * application is running in an environment with a Unified EL
     * Implementation for Java EE6 or later, obtain the
     * <code>ValueReference</code> from <em>valueExpression</em> and let
     * <em>valueBaseClase</em> be the return from calling
     * <code>ValueReference.getBase()</code> and <em>valueProperty</em>
     * be the return from calling
     * <code>ValueReference.getProperty()</code>.  If an earlier version
     * of the Unified EL is present, use the appropriate methods to
     * inspect <em>valueExpression</em> and derive values for
     * <em>valueBaseClass</em> and <em>valueProperty</em>.</p>

     * <p>If no <code>ValueReference</code> can be obtained, take no
     * action and return.</p>

     * <p>If <code>ValueReference.getBase()</code> return
     * <code>null</code>, take no action and return.</p>

     * <p>Obtain the {@link ValidatorContext} from the {@link
     * ValidatorFactory}.</p>

     * <p>Decorate the {@link MessageInterpolator} returned from {@link
     * ValidatorFactory#getMessageInterpolator} with one that leverages
     * the <code>Locale</code> returned from {@link
     * javax.faces.component.UIViewRoot#getLocale}, and store it in the
     * <code>ValidatorContext</code> using {@link
     * ValidatorContext#messageInterpolator}.</p>

     * <p>Obtain the {@link javax.validation.Validator} instance from
     * the <code>validatorContext</code>.</p>

     * <p>Obtain a <code>javax.validation.BeanDescriptor</code> from the
     * <code>javax.validation.Validator</code>.  If
     * <code>hasConstraints()</code> on the <code>BeanDescriptor</code>
     * returns false, take no action and return.  Otherwise proceed.</p>

     * <p>Call {@link javax.validation.Validator#validateValue}, passing
     * <em>valueBaseClass</em>, <em>valueProperty</em>, the
     * <em>value</em> argument, and <em>validatorGroupsArray</em> as
     * arguments.</p>

     * <p>If the returned <code>Set&lt;{@link
     * ConstraintViolation}&gt;</code> is non-empty, for each element in
     * the <code>Set</code>, create a {@link FacesMessage} where the
     * summary and detail are the return from calling {@link
     * ConstraintViolation#getMessage}.  Capture all such
     * <code>FacesMessage</code> instances into a
     * <code>Collection</code> and pass them to {@link
     * ValidatorException#ValidatorException(java.util.Collection)}.
     * <span class="changed_added_2_3">If the {@link
     * #ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME} application parameter is
     * enabled and this {@code Validator} instance has validation groups
     * other than or in addition to the {@code Default} group, record
     * the fact that this field failed validation.  Call {@link
     * #getMultiFieldValidationCandidates} passing {@code true} as the
     * second argument.  Let the returned {@code Map} be called
     * <em>candidates</em> for discussion.  Get or create an entry in
     * <em>candidates</em> (using the return from <code>ValueReference.getBase()</code>
     * as the key) to represent the bean on
     * which this current validator is validating a single property.
     * This entry is itself a <code>Map</code>, called <em>candidate</em> for
     * discussion.  Add an entry in <em>candidate</em> with the key
     * being <code>ValueReference.getProperty()</code> and the value
     * being a new <code>BeanValidator.ComponentValueTuple</code>, with the
     * argument <code>component</code> as the {@link
     * javax.faces.component.EditableValueHolder}, and {@link
     * ComponentValueTuple#FAILED_FIELD_LEVEL_VALIDATION} as the value.  Regardless of
     * whether or not {@link #ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME} is
     * set, throw the new exception.</span></p>
     * 
     * <p class="changed_added_2_3">If the returned {@code Set} is
     * empty, the {@link #ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME}
     * application parameter is enabled and this {@code Validator}
     * instance has validation groups other than or in addition to the
     * {@code Default} group, get or create an entry in the
     * <em>candidates</em> and a <em>candidate</em> representing this
     * valid property as above, but use the argument {@code value} as
     * the value of the {@link ComponentValueTuple}.</p>
     * 
     * </div>
     * 
     * @param context {@inheritDoc}
     * @param component {@inheritDoc}
     * @param value {@inheritDoc}
     *
     * @throws ValidatorException   {@inheritDoc}
     */
    @Override
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (component == null) {
            throw new NullPointerException();
        }
        ValueExpression valueExpression = component.getValueExpression("value");
        if (valueExpression == null) {
            return;
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
                new JsfAwareMessageInterpolator(context, 
                           validatorFactory.getMessageInterpolator());
        validatorContext.messageInterpolator(jsfMessageInterpolator);
        javax.validation.Validator beanValidator = validatorContext.getValidator();
        Class[] validationGroupsArray = parseValidationGroups(getValidationGroups());
        
        // PENDING(rlubke, driscoll): When EL 1.3 is present, we won't need
        // this.
        
        ValueExpressionAnalyzer expressionAnalyzer = 
                new ValueExpressionAnalyzer(valueExpression);
        
        ValueReference valueReference = expressionAnalyzer.getReference(context.getELContext());
        if (valueReference == null) {
            return;
        }
        if (isResolvable(valueReference, valueExpression)) {
            Set<ConstraintViolation<?>> violations = null;
            try {
                //noinspection unchecked
                violations =
                        beanValidator.validateValue(valueReference.getBaseClass(),
                                                    valueReference.getProperty(),
                                                    value,
                                                    validationGroupsArray);
            } catch (IllegalArgumentException iae) {
                String failureMessage = "Unable to validate expression " +
                        valueExpression.getExpressionString() +
               " using Bean Validation.  Unable to get value of expression. "+
                        " Message from Bean Validation: " + iae.getMessage();
                LOGGER.fine(failureMessage);
            }

            if (violations != null && !violations.isEmpty()) {
                ValidatorException toThrow;
                if (1 == violations.size()) {
                    ConstraintViolation violation = violations.iterator().next();
                    toThrow = new ValidatorException(MessageFactory.getMessage(
                          context,
                          MESSAGE_ID,
                          violation.getMessage(),
                          MessageFactory.getLabel(context, component)));
                } else {
                    Set<FacesMessage> messages = new LinkedHashSet<>(
                          violations.size());
                    for (ConstraintViolation violation : violations) {
                        messages.add(MessageFactory.getMessage(context,
                                                               MESSAGE_ID,
                                                               violation.getMessage(),
                                                               MessageFactory.getLabel(
                                                                     context,
                                                                     component)));
                    }
                    toThrow = new ValidatorException(messages);
                }
                
                
                // Record the fact that this field failed validation, so that multi-field
                // validation is not attempted.
                if (wholeBeanValidationEnabled(context, validationGroupsArray)) {
                    Map<Object, Map<String, ComponentValueTuple>> multiFieldCandidates = getMultiFieldValidationCandidates(context, true);
                    Object val = valueReference.getBase();
                    Map<String, ComponentValueTuple> candidate = multiFieldCandidates.getOrDefault(val, new HashMap<>());
                    ComponentValueTuple tuple = new ComponentValueTuple((EditableValueHolder) component, 
                            ComponentValueTuple.FAILED_FIELD_LEVEL_VALIDATION);
                    candidate.put(valueReference.getProperty(), tuple);
                    multiFieldCandidates.putIfAbsent(val, candidate);
                }
                
                throw toThrow;
            }
        }
        
        // Record the fact that this field passed validation, so that multi-field
        // validation can be performed if desired
        if (wholeBeanValidationEnabled(context, validationGroupsArray)) {
            Map<Object, Map<String, ComponentValueTuple>> multiFieldCandidates = getMultiFieldValidationCandidates(context, true);
            Object val = valueReference.getBase();
            Map<String, ComponentValueTuple> candidate = multiFieldCandidates.getOrDefault(val, new HashMap<>());
            ComponentValueTuple tuple = new ComponentValueTuple((EditableValueHolder) component, value);
            candidate.put(valueReference.getProperty(), tuple);
            multiFieldCandidates.putIfAbsent(val, candidate);
        }
    }
    
   /**
    * <p class="changed_added_2_3">Returns a data structure that stores
    * the information necessary to perform class-level validation by
    * <code>&lt;f:validateWholeBean &gt;</code> components elsewhere in
    * the tree.  The lifetime of this data structure does not extend
    * beyond the current {@code FacesContext}.  The data structure must
    * conform to the following specification.</p>
    * 
    * <div class="changed_added_2_3">
    * 
    * <ul>
    * 
    * <li><p>It is a non-thread-safe {@code Map}.</p></li>
    * 
    * <li><p>Keys are CDI bean instances that are referenced by the
    * {@code value} attribute of <code>&lt;f:validateWholeBean
    * &gt;</code> components.</p></li>
    * 
    * <li>
    * 
    * <p>Values are {@code Map}s that represent the properties to be stored 
    * on the CDI bean instance that is the current key.  The inner {@code Map}
    * must conform to the following specification.</p>
    * 
    * <ul>
    * 
    * <li><p>It is a non-thread-safe {@code Map}.</p></li>
    * 
    * <li><p>Keys are property names.</p></li>
    * 
    * <li><p>Values are {@link ComponentValueTuple} instances.</p></li>
    * 
    * </ul>
    * 
    * </li>
    * 
    * 
    * 
    * </ul>
    * 
    * </div>
    * 
    * @param context the {@link FacesContext} for this request
    * 
    * @param create if {@code true}, the data structure must be created if not present.
    * If {@code false} the data structure must not be created and {@code Collections.emptyMap()}
    * must be returned.
    *
    * @return the data structure representing the multi-field validation candidates
    * 
    * @since 2.3
    */
    public static Map<Object, Map<String, ComponentValueTuple>> getMultiFieldValidationCandidates(FacesContext context, boolean create) {
        Map<Object, Object> attrs = context.getAttributes();
        Map<Object, Map<String, ComponentValueTuple>> result;
        result = (Map<Object, Map<String, ComponentValueTuple>>) attrs.get(MULTI_FIELD_VALIDATION_CANDIDATES);
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
    
    /**
     * <p class="changed_added_2_3">A data structure storing a tuple of 
     * {@link EditableValueHolder} with its potential value.  See 
     * {@link #getMultiFieldValidationCandidates(javax.faces.context.FacesContext, boolean)}</p>
     * 
     * @since 2.3
     */
    public static class ComponentValueTuple {
        private EditableValueHolder component;
        private Object value;
        
        /**
         * <p class="changed_added_2_3">Special value to indicate the proposed value
         * for a property failed field-level validation.  This prevents any attempt
         * to perform class level validation.</p>
         */
        public static final String FAILED_FIELD_LEVEL_VALIDATION = VALIDATOR_ID + ".FAILED_FIELD_LEVEL_VALIDATION";

        public ComponentValueTuple(EditableValueHolder component, Object value) {
            this.component = component;
            this.value = value;
        }
        
        public EditableValueHolder getComponent() {
            return component;
        }

        public void setComponent(EditableValueHolder component) {
            this.component = component;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.component);
            hash = 23 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ComponentValueTuple other = (ComponentValueTuple) obj;
            if (!Objects.equals(this.component, other.component)) {
                return false;
            }
            if (!Objects.equals(this.value, other.value)) {
                return false;
            }
            return true;
        }
        
        
    }
    
    private boolean wholeBeanValidationEnabled(FacesContext context, 
            Class [] validationGroupsArray) {
        boolean result;
        
        Map<Object,Object> attrs = context.getAttributes();
        if (!(attrs.containsKey(ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME) &&
             (Boolean)attrs.get(ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME))) { // NOPMD
            return false;
        }
        
        result = !(1 == validationGroupsArray.length && Default.class == validationGroupsArray[0]);
        
        return result;
    }
    
    private boolean isResolvable(ValueReference ref, 
            ValueExpression valueExpression) {
        Boolean result = null;
        String failureMessage = null;
        
        if (null == valueExpression) {
            failureMessage = "Unable to validate expression using Bean "+ 
                    "Validation.  Expression must not be null.";
            result = false;
        } else if (null == ref) {
            failureMessage = "Unable to validate expression " + 
                    valueExpression.getExpressionString() +
                    " using Bean Validation.  Unable to get value of expression.";
            result = false;
        } else {
            Class baseClass = ref.getBaseClass();

            // case 1, base classes of Map, List, or Array are not resolvable
            if (null != baseClass) {
                if (Map.class.isAssignableFrom(baseClass) ||
                        Collection.class.isAssignableFrom(baseClass) ||
                        Array.class.isAssignableFrom(baseClass)) {
                    failureMessage = "Unable to validate expression " + valueExpression.getExpressionString() +
                            " using Bean Validation.  Expression evaluates to a Map, List or array.";
                    result = false;
                }
            }
        }

        result = ((null != result) ? result : true);
        if (!result) {
            LOGGER.fine(failureMessage);
        }

        return result;
    }
    

    private Class[] parseValidationGroups(String validationGroupsStr) {
        if (cachedValidationGroups != null) {
            return cachedValidationGroups;
        }

        if (validationGroupsStr == null) {
            cachedValidationGroups = new Class[] { Default.class };
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

    // ----------------------------------------------------- StateHolder Methods

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[1];
            values[0] = validationGroups;
            return values;
        }
        return null;
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

    private boolean initialState;
    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

    private boolean transientValue;

    @Override
    public boolean isTransient() {
        return this.transientValue;
    }

    @Override
    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    private static class JsfAwareMessageInterpolator implements MessageInterpolator {

        private FacesContext context;
        private MessageInterpolator delegate;

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

}
