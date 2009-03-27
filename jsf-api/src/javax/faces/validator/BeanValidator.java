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

package javax.faces.validator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.validation.ConstraintDescriptor;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

/**
 * <p class="changed_added_2_0">A Validator that delegates validation
 * of the bean property to the Bean Validation API.</p>
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
     * <p class="changed_added_2_0">The name of the servlet context
     * attribute which holds the object used by JSF to obtain Validator
     * instances.  If the servlet context attribute is missing or
     * contains a null value, JSF is free to use this servlet context
     * attribute to store the ValidatorFactory bootstrapped by this
     * validator.</p>
     */
    public static final String VALIDATOR_FACTORY_KEY = "javax.faces.validator.beanValidator.ValidatorFactory";

    /**
     * <p class="changed_added_2_0">The name of the component attribute
     * which holds the validation groups defined for a branch of the
     * component tree.</p>
     */
    public static final String VALIDATION_GROUPS_KEY = "javax.faces.validator.beanValidator.ValidationGroups";
    
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
     * calling <code>toLowerCase().equals(&#8220;true&#8221;) on a 
     * <code>String</code> representation of its value returns 
     * <code>true</code>, the runtime must behave as if the
     * validator with validator-id equal to the value of the symbolic
     * constant {@link #VALIDATOR_ID} is not available.  Setting this
     * parameter to true will have the effect of disabling the default 
     * application of Bean Validation to every input component in 
     * every view in the application.</p>
     * 
     */
    public static final String DISABLE_BEAN_VALIDATOR_PARAM_NAME =
            "javax.faces.validator.DISABLE_BEAN_VALIDATOR";

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
        // treat empty list as null
        if (validationGroups != null && validationGroups.matches(EMPTY_VALIDATION_GROUPS_PATTERN)) {
            validationGroups = null;
        }
        
        // only clear cache of validation group classes if value is changing
        if ((validationGroups == null && this.validationGroups != null) ||
            (validationGroups != null && !validationGroups.equals(this.validationGroups))) {
            this.cachedValidationGroups = null;
        }
        
        this.validationGroups = validationGroups;
    }

    /**
     * <p class="changed_added_2_0">Return the validation groups passed
     * to the Validation API when checking constraints.  If the
     * validationGroupsArray attribute is omitted or empty, the validation
     * groups will be inherited from the branch defaults, or if there
     * are no branch defaults, the {@link
     * javax.validation.groups.Default} group will be used.</p>
     */
    public String getValidationGroups() {
        return validationGroups;
    }

    /**
     * <p class="changed_added_2_0">Verify that the value is valid
     * according to the Bean Validation constraints.</p>
     *
     * <div class="changed_added_2_0">

     * <p>Obtain a {@link ValidatorFactory} instance by calling {@link
     * javax.validation.Validation#buildDefaultValidatorFactory}.</p>

     * <p>Let <em>validationGroupsArray</em> be a <code>Class []</code>
     * representing validator groups discovered by looking in the
     * component attribute <code>Map</code> for every ancestor {@link
     * UIComponent} between argument <em>component</em> up to and
     * including the {@link javax.faces.component.UIViewRoot} for a key
     * given by the value of the symbolic constant {@link
     * #VALIDATION_GROUPS_KEY}.  The first search component terminates
     * the search for the validation groups value.  If no such value is
     * found use the class name of {@link
     * javax.validation.groups.Default} as the value of the validation
     * groups.</p>

     * <p>Let <em>valueExpression</em> be the return from calling {@link
     * UIComponent#getValueExpression} on the argument
     * <em>component</em>, passing the literal string
     * &#8220;value&#8221; (without the quotes) as an argument.  If this
     * application is running in an environment with a Unified EL
     * Implementation version 1.3 or greater, obtain the
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
     * ValidatorException#ValidatorException(java.util.Collection)},
     * throwing the new exception.</p>

     * </div>
     *
     * @param context {@inheritDoc}
     * @param component {@inheritDoc}
     * @param value {@inheritDoc}
     *
     * @throws ValidatorException   {@inheritDoc}
     */
    public void validate(FacesContext context,
                         UIComponent component,
                         Object value) {

        ValueExpression valueExpression = component.getValueExpression("value");
        if (valueExpression == null) {
            return;
        }

        ValidatorFactory validatorFactory = null;
        Object cachedObject = context.getExternalContext().getApplicationMap().
	    get(VALIDATOR_FACTORY_KEY);
        if (cachedObject instanceof ValidatorFactory) {
            validatorFactory = (ValidatorFactory) cachedObject;
        }
        else {
            // TODO throw a FacesException if Validation is not
            // available on the classpath QUESTION is this the right way
            // to handle this?
            try {
                validatorFactory = Validation.buildDefaultValidatorFactory();
            }
            catch (ValidatorException e) {
                throw new FacesException("Could not build a default Bean Validator factory", e);
            }
            context.getExternalContext().getApplicationMap().put(VALIDATOR_FACTORY_KEY, validatorFactory);
        }

        if (validationGroups == null) {
            inheritValidationGroups(component);
        }

        ValidatorContext validatorContext = validatorFactory.usingContext();
        MessageInterpolator jsfMessageInterpolator = 
                new JsfAwareMessageInterpolator(context, 
		                   validatorFactory.getMessageInterpolator());
        validatorContext.messageInterpolator(jsfMessageInterpolator);
        javax.validation.Validator beanValidator = validatorContext.getValidator();
        Class [] validationGroupsArray = parseValidationGroups(getValidationGroups());
        
        // PENDING(rlubke, driscoll): When EL 1.3 is present, we won't need
        // this.
        
        ValueExpressionAnalyzer expressionAnalyzer = 
                new ValueExpressionAnalyzer(valueExpression);
        
        ValueReference valueReference = expressionAnalyzer.getReference(context.getELContext());

        if (isResolvable(valueReference, valueExpression)) {
            Set<ConstraintViolation> violations = Collections.EMPTY_SET;
            try {
                violations =
                        beanValidator.validateValue(valueReference.getBaseClass(),
                        valueReference.getProperty(),
                        value, validationGroupsArray);
            } catch (IllegalArgumentException iae) {
                String failureMessage = "Unable to validate expression " +
                        valueExpression.getExpressionString() +
               " using Bean Validation.  Unable to get value of expression. "+
                        " Message from Bean Validation: " + iae.getMessage();
                LOGGER.fine(failureMessage);
            }

            if (!violations.isEmpty()) {
                ValidatorException toThrow = null;
                if (1 == violations.size()) {
                    ConstraintViolation violation = violations.iterator().next();
                    toThrow = new ValidatorException(getMessage(context, component,
                            violation.getMessage(), value));
                } else {
                    Set<FacesMessage> messages = new HashSet<FacesMessage>(violations.size());
                    Iterator<ConstraintViolation> iter = violations.iterator();
                    while (iter.hasNext()) {
                        messages.add(getMessage(context, component,
                                iter.next().getMessage(), value));
                    }
                    toThrow = new ValidatorException(messages);
                }
                throw toThrow;
            }
        } else {
        }
        
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
    
    /**
     * If no validation groups are defined on this validator, search
     * upwards in the component tree to find a branch that defines
     * validation groups.
     */
    private void inheritValidationGroups(UIComponent component) {
        UIComponent branch = findNearestBranchWithValidationGroups(component);
        if (branch != null) {
            // null check and trimming handled in setter
            setValidationGroups((String) branch.getAttributes().get(VALIDATION_GROUPS_KEY));
        }

        // if validation groups is still null, use default to avoid having to do this inheritance check again
        if (validationGroups == null) {
            validationGroups = Default.class.getName();
        }
    }

    private UIComponent findNearestBranchWithValidationGroups(UIComponent component) {
        // TODO might want to check that key does not have bogus value
        if (component.getAttributes().containsKey(VALIDATION_GROUPS_KEY)) {
            return component;
        }
        else if (component.getParent() == null) {
            return null;
        }

        return findNearestBranchWithValidationGroups(component.getParent());
    }

    private Class[] parseValidationGroups(String validationGroupsStr) {
        if (cachedValidationGroups != null) {
            return cachedValidationGroups;
        }

        if (validationGroupsStr == null) {
            cachedValidationGroups = new Class[] { Default.class };
            return cachedValidationGroups;
        }

        List<Class> validationGroupsList = new ArrayList<Class>();
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

        cachedValidationGroups = validationGroupsList.toArray(new Class[0]);
        return cachedValidationGroups;
    }

    /**
     * Return a FacesMessage that uses the message resolved by the Bean Validation API rather than a message key.
     * This method should likely be moved into MessageFactory.
     */
    private FacesMessage getMessage(FacesContext context, UIComponent component, String message, Object invalidValue) {
        // FIXME move locale lookup to a method in MessageFactory
        Locale locale;
        // viewRoot may not have been initialized at this point.
        if (context.getViewRoot() != null) {
            locale = context.getViewRoot().getLocale();
        } else {
            locale = Locale.getDefault();
        }

        if (null == locale) {
            throw new NullPointerException(" locale is null ");
        }

        // QUESTION should we pass invalid value as message replacement parameter
        //String invalidValueAsString = (invalidValue == null ? "[null]" : invalidValue.toString());

        // FIXME if a validator message is missing, BindingFacesMessage is going to be confused by {keyword} syntax in message key
        MessageFactory.BindingFacesMessage facesMessage =
            new MessageFactory.BindingFacesMessage(locale, message, message,
            new Object[] { MessageFactory.getLabel(context, component) });
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        return facesMessage;
    }

    // ----------------------------------------------------- StateHolder Methods

    public Object saveState(FacesContext context) {
        if (!initialStateMarked()) {
            Object values[] = new Object[1];
            values[0] = validationGroups;
            return values;
        }
        return null;
    }

    public void restoreState(FacesContext context, Object state) {
        if (state != null) {
            Object values[] = (Object[]) state;
            validationGroups = (String) values[0];
        }
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

    private boolean transientValue = false;

    public boolean isTransient() {
        return this.transientValue;
    }

    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    class JsfAwareMessageInterpolator implements MessageInterpolator {

        private FacesContext context;
        private MessageInterpolator delegate;

        public JsfAwareMessageInterpolator(FacesContext context, MessageInterpolator delegate) {
            this.context = context;
            this.delegate = delegate;
        }

        public String interpolate(String message, ConstraintDescriptor descriptor, Object value) {
            Locale locale = context.getViewRoot().getLocale();
            if (locale == null) {
                locale = Locale.getDefault();
            }
            return delegate.interpolate(message, descriptor, value, locale);
        }

        public String interpolate(String message, ConstraintDescriptor descriptor, Object value, Locale locale) {
            return delegate.interpolate(message, descriptor, value, locale);
        }

    }
}
