/*
 * $Id: ValidatorTag.java,v 1.24 2005/12/05 16:43:06 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.webapp;

import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;



/**
 * <p><strong>ValidatorTag</strong> is a base class for all JSP custom actions
 * that create and register a <code>Validator</code> instance on the
 * {@link EditableValueHolder} associated with our most immediate surrounding instance
 * of a tag whose implementation class is a subclass of {@link UIComponentTag}.
 * To avoid creating duplicate instances when a page is redisplayed,
 * creation and registration of a {@link Validator} occurs
 * <strong>only</strong> if the corresponding {@link UIComponent} was
 * created (by the owning {@link UIComponentTag}) during the execution of the
 * current page.</p>
 *
 * <p>This class may be used directly to implement a generic validator
 * registration tag (based on the validator-id specified by the
 * <code>id</code> attribute), or as a base class for tag instances that
 * support specific {@link Validator} subclasses.  This <code>id</code>
 * attribute must refer to one of the well known validator-ids, or a
 * custom validator-id as defined in a <code>faces-config.xml</code>
 * file.</p>
 *
 * <p>Subclasses of this class must implement the
 * <code>createValidator()</code> method, which creates and returns a
 * {@link Validator} instance.  Any configuration properties that specify
 * the limits to be enforced by this {@link Validator} must have been
 * set by the <code>createValidator()</code> method.  Generally, this occurs
 * by copying corresponding attribute values on the tag instance.</p>
 *
 * <p>This tag creates no output to the page currently being created.  It
 * is used solely for the side effect of {@link Validator} creation.</p>
 *
 * @deprecated This has been partially replaced by {@link
 * ValidatorELTag}.  The remainder of the functionality, namely, the
 * binding facility and the implementation of the {@link
 * #createValidator} method, is now an implementation detail.
 */

public class ValidatorTag extends TagSupport {


    // ---------------------------------------------------------- Static Members


    private static final long serialVersionUID = -5562623615418158868L;


    // ------------------------------------------------------------- Attributes

    /**
     * <p>The identifier of the {@link Validator} instance to be created.</p>
     */
    private String validatorId = null;
    
    /**
     * <p>The {@link ValueExpression} that evaluates to an object that 
     * implements {@link Validator}.</p>
     */
    private String binding = null;

    /**
     * <p>Set the identifer of the {@link Validator} instance to be created.
     *
     * @param validatorId The new identifier of the validator instance to be
     *                    created.
     */
    public void setValidatorId(String validatorId) {

        this.validatorId = validatorId;

    }

    /**
     * <p>Set the expression that will be used to create a {@link ValueExpression} 
     * that references a backing bean property of the {@link Validator} instance to 
     * be created.</p>
     *
     * @param binding The new expression 
     *
     * @throws JspException if a JSP error occurs
     */
    public void setBinding(String binding) 
        throws JspException {
        if (binding != null && !UIComponentTag.isValueReference(binding)) {
            //PENDING i18n
            throw new JspException("Invalid Expression:"+binding);
        }
        this.binding = binding;
    }
    // --------------------------------------------------------- Public Methods


    /**
     * <p>Create a new instance of the specified {@link Validator}
     * class, and register it with the {@link UIComponent} instance associated
     * with our most immediately surrounding {@link UIComponentTag} instance, if
     * the {@link UIComponent} instance was created by this execution of the
     * containing JSP page.</p>
     *
     * @throws JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {
        
        Validator validator = null;
        
        
        // Locate our parent UIComponentTag
        UIComponentClassicTagBase tag =
            UIComponentTag.getParentUIComponentClassicTagBase(pageContext);
        if (tag == null) { 
       	    //PENDING i18n
            throw new JspException("Not nested in a UIComponentTag Error for tag with handler class:"+
                    this.getClass().getName());
        }

        // Nothing to do unless this tag created a component
        if (!tag.getCreated()) {
            return (SKIP_BODY);
        }
        
        UIComponent component = tag.getComponentInstance();
        if (component == null) {            
            //PENDING i18n
            throw new JspException("Can't create Component from tag.");
        }
        if (!(component instanceof EditableValueHolder)) {
            // PENDING i18n
            throw new JspException("Not nested in a tag of proper type. Error for tag with handler class:"+
                    this.getClass().getName());
        }

        validator = createValidator();
        
        if (validator == null) {
            String validateError = null;
            if (binding != null) {
                validateError = binding;
            }
            if (validatorId != null) {
                if (validateError != null) {
                    validateError += " or " + validatorId;
                } else {
                    validateError = validatorId;
                }
            }
                
            // PENDING i18n
            throw new JspException("Can't create class of type:"+
                "javax.faces.validator.Validator from:"+validateError);
        }

        // Register an instance with the appropriate component
        ((EditableValueHolder)component).addValidator(validator);
        
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.id = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Create and return a new {@link Validator} to be registered
     * on our surrounding {@link UIComponent}.</p>
     *
     * @throws JspException if a new instance cannot be created
     */
    protected Validator createValidator()
        throws JspException {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Validator validator = null;
        ValueExpression vb = null;
        
        // If "binding" is set, use it to create a validator instance.
        if (binding != null) {
            try {
                vb = context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), 
                        binding, Object.class);
                if (vb != null) {
                    validator = (Validator)vb.getValue(context.getELContext());
                    if (validator != null) {
                        return validator;
                    }
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        } 
        // If "validatorId" is set, use it to create the validator
        // instance.  If "validatorId" and "binding" are both set, store the 
        // validator instance in the value of the property represented by
        // the value binding expression.      
        if (validatorId != null) {
            try {
                String validatorIdVal = validatorId;
                if (UIComponentTag.isValueReference(validatorId)) {
                    ValueExpression idBinding = context.getApplication().
                        getExpressionFactory().createValueExpression(context.getELContext(), 
                            validatorId, Object.class);
                    validatorIdVal = (String)idBinding.getValue(context.getELContext());
                }
                validator = context.getApplication().createValidator(validatorIdVal);
                if (validator != null) { 
                    if (vb != null) {
                        vb.setValue(context.getELContext(), validator);
                    }
                }
            } catch (Exception e) {
                throw new JspException(e);
            }
        }
        return validator;
    }


}
