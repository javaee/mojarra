/*
 * $Id: UITextEntry.java,v 1.9 2002/03/15 20:49:23 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.ServletRequest;
import java.util.Vector;
import java.util.EventObject;

/**
 * Class for representing a user-interface component accepts
 * text input from a user.  
 * <p>
 * This is a data-bound component, which means that it's value
 * is derived from an application &quot;model&quot; object which
 * lives outside the component, in the scoped namespace.  The
 * component stores a reference to this model object.  Currently
 * the supported reference types for the model are:
 * <ul>
 * <li>String which describes a <bean-name>.<property-name> 
 *     reference in the scoped namespace.
 *     e.g. &quot;user.lastName&quot; would correspond to a bean
 *          named &quot;user&quot; with a property named &quot;lastName&quot;
 * </ul>
 * In order to make it convenient to set/get the text value
 * without interacting directly with this reference, the class
 * provides <code>getText()</code> and <code>setText</code> methods
 * which interact with the model object under the covers.
 *
 */
public class UITextEntry extends UIComponent implements EventDispatcher, 
        Validatible{

    private static String TYPE = "TextEntry";

    private Vector valueChangeListeners = null;
    private Vector validators = null;
    private String messageModelReference = null;
    
    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;TextEntry&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * This is just a strong type enforcing wrapper around getValue()
     *
     * @see UIComponent#getValue
     * @param rc the render context used to render this component
     * @return String containing the current text value 
     */
    public String getText(RenderContext rc) { 
        return (String) getValue(rc);
    }

    /**
     * This is just a strong type enforcing wrapper around setValue()
     * @see UIComponent#setValue
     * @param rc the render context used to render this component
     * @param text String containing the new text value for this component
     */
    public void setText(String text) { 
	setValue(text);
    }

    /**
     * Registers the specified listener id as a value-change listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerId the name of the value-change listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listenerId
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerId) 
            throws FacesException {
        // PENDING ( visvan ) if listener does not implement
        // valueChangeListener interface or if it doesn't exist
        // throw FacesException
        if ( listenerId != null ) {
             if ( valueChangeListeners == null ) {
                 valueChangeListeners = new Vector();
             }
             valueChangeListeners.add(listenerId);
         }
    }

    /**
     * Removes the specified listener id as a value-change listener
     * for this component.  
     * @param listenerId the name of the value-change listener
     * @throws FacesException if listenerId is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerId)
            throws FacesException {
        // Assert.assert_it( valueChangeListeners != null );
        if ( listenerId != null ) {
            valueChangeListeners.remove(listenerId);
        }
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
         return valueChangeListeners == null? null : valueChangeListeners.iterator();
    }
    
    /**
     * The message model-reference property for this validatible component.
     * This property contains a reference to the object which acts
     * as the store for any validation error messages.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.String</code>
     * <li><code>java.util.Collection</code> of <code>String</code> objects
     * </ul>  
     * @see #setMessageModelReference  
     * @return String containing the message model-reference for this component
     */
    public String getMessageModelReference() {
	return messageModelReference;
    }

    /**
     * Sets the message model-reference property on this validatible component.
     * @see #getMessageModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the store for any validation error messages
     */
    public void setMessageModelReference(String modelReference) {
	this.messageModelReference = modelReference;
    }

    /**
     * Dispatches the specified event to any registered listeners.
     * @param e the object describing the event
     */
    public void dispatch(EventObject e) throws FacesException {

        // ParameterCheck.nonNull(e);

        ValueChangeEvent value_event = null;
        if ( e instanceof ValueChangeEvent)  {
            value_event = (ValueChangeEvent) e;
        } else {
            throw new FacesException("Invalid event type. " +
                    "Expected ValueChangeEvent");
        }

        EventContext eventContext = value_event.getEventContext();
        // Assert.assert_it( eventContext != null );

        ObjectManager ot = eventContext.getObjectManager();
        // Assert.assert_it( ot != null );

        ServletRequest request = eventContext.getRequest();

        RenderContext rc = (RenderContext)ot.get(request,
                Constants.REF_RENDERCONTEXT);
        // Assert.assert_it( rc != null );

	setText((String) value_event.getNewValue());
	
	// dispatch value change listeners.
        if ( valueChangeListeners == null ) {
            return;
        }    
        Iterator listeners = getValueChangeListeners();
        // Assert.assert_it( listeners != null );
        while ( listeners.hasNext() ) {
            String listenerName = (String) listeners.next();
            // Assert.assert_it( listenerName != null );

            ValueChangeListener vcl = (ValueChangeListener)ot.get(request,
                    listenerName);
            // Assert.assert_it ( vcl != null );
            vcl.handleValueChange(value_event);
        }
    }
    
    /**
     * Registers the specified validator id as a validator
     * for this validatible component.  The specified validator id must be registered
     * in the scoped namespace, else an exception will be thrown.
     * @see Validator
     * @see #removeValidator
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered in the
     *         scoped namespace or if the object referred to by validatorId
     *         does not implement the <code>Validator</code> interface.
     */
    public void addValidator(String validatorId) {
        // PENDING ( visvan ) if validator does not implement
        // validator interface or if it doesn't exist
        // throw FacesException
        if ( validatorId != null ) {
             if ( validators == null ) {
                 validators = new Vector();
             }
             validators.add(validatorId);
         }
    }

    /**
     * Removes the specified validator id as a validator
     * for this validatible component.
     * @see #addValidator  
     * @param validatorId the id of the validator
     * @throws FacesException if validatorId is not registered as a
     *         validator for this component.
     */
    public void removeValidator(String validatorId) {
        if ( validatorId != null ) {
           validators.remove(validatorId);
        }
    }

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    public Iterator getValidators() {
	return validators == null? null : validators.iterator();
    }
    
    /**
     * The &quot;validState&quot; attribute which describes the current
     * valid state of this component.  Valid state may be one of the
     * following:
     * <ul>
     * <li><code>Validatible.UNVALIDATED</code>
     * <li><code>Validatible.VALID</code>
     * <li><code>Validatible.INVALID</code>
     * </ul>
     * @see #setValidState
     * @return integer containing the current valid state of this
     *         component
     */
    public int getValidState() {
	Integer valid = (Integer)getAttribute(null, "validState");
	return valid != null? valid.intValue() : Validatible.UNVALIDATED;
    }

    /**
     * Sets the &quot;validState&quot; attribute for this component.
     * @see #getValidState
     * @param validState integer containing the valid state of this
     *        component
     * @throws IllegalParameterException if validState is not
     *         UNVALIDATED, VALID, or INVALID
     */
    public void setValidState(int validState) {
         setAttribute("validState", new Integer(validState));
    }
    
    /**
     * Performs validation on the specified value object. 
     * Subclasses must override this method to perform appropriate
     * validation.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void validate(EventContext eventContext) {
        Converter converterObj = null;
        Object obj = null;
     
        ObjectManager objectManager = eventContext.getObjectManager();
        ServletRequest request = eventContext.getRequest();
        RenderContext rc = (RenderContext)objectManager.get(request,
                        Constants.REF_RENDERCONTEXT);
        // Assert.assert_it( objectManager != null );

        // if the converterReference is set, then get the converter directly
        // from ObjectManager. If not based on modelType, get the converter from
        // converterManager. If modelType is null or of type String, no conversion is done.
        String converterReference = (String) getAttribute(null, "converterReference");

        if ( converterReference != null ) {
            converterObj = (Converter)objectManager.get(request, converterReference);
        } else if ( (getAttribute(null, "modelType")) != null ){

            Class converterClass = null;

            Object model_type = getAttribute(null, "modelType");
            if ( model_type instanceof String ) {
                try {
                    converterClass = Class.forName((String)model_type);
                } catch ( ClassNotFoundException cfe ) {
                    // PENDING ( visvan ) throw JSPException or just warn ??
                }
            } else {
                converterClass = (Class)model_type;
            }

            String converterClassName = converterClass.getName();
            // if modelType is String, need not obtain converter
            if ( ! converterClassName.equals("java.lang.String") ) {
                // get converter from converterManager.
                ConverterManager cm = (ConverterManager)
                    objectManager.get(Constants.REF_CONVERTERMANAGER);
                converterObj = cm.getConverter(converterClass);
            }
        }
        if ( converterObj != null ) {
            try {
                obj = converterObj.convertStringToObject(eventContext, this, 
                        (String) getValue(rc));
            } catch ( ValidationException ce ) {
                // if conversion failed don't proceed to validation
                handleError(obj);
                return;
            }    
        } 
        // if no validators are set, if the conversion was successful
        // then values could still be pushed to model.
        if ( validators == null ) {
            setValidState( Validatible.VALID);
            return;
        }    
        Iterator iterator = getValidators();
        while ( iterator.hasNext() ) {
            String validatorName = (String) iterator.next();
            // Assert.assert_it( validatorName != null );
            Validator validator = (Validator)objectManager.get(request,
                    validatorName);
            // Assert.assert_it ( vcl != null );
            if ( validator != null ) {
                // if there is no converter set, then use the local value for
                // validation.
                if ( obj == null ) {
                    obj = getValue(rc);
                }    
                try {
                    validator.validate(eventContext, this,obj);
                    setValidState ( Validatible.VALID);
                    // cache the validated value
                    setValue(obj);
                } catch ( ValidationException ve ) {
                    handleError(obj);    
                }
            } 
        }    
    }
   
    /**
     * Resets the state and converts the value back to string for 
     * rendering purpose.
     */ 
    protected void handleError(Object obj) {
        setValidState ( Validatible.INVALID);
        if ( obj != null ) {
            setValue(obj.toString());
        }
    }             
}
