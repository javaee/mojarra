/*
 * $Id: UITextEntry.java,v 1.4 2002/01/25 18:35:07 visvan Exp $
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
public class UITextEntry extends UIComponent implements EventDispatcher {

    private static String TYPE = "TextEntry";
    private Object model = null;
    private String text = null;

    private Vector valueChangeListeners = null;

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
     * The model property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The supported types
     * for this reference:
     * <ul>
     * <li>String containing a model-reference in the scoped namespace
     *     e.g. &quot;user.lastName&quot; refers to an object named 
     *          &quot;user&quot;
     *          with a property named &quot;lastName&quot;.
     * </ul>
     * @return Object describing the data-source for this component
     */
    public Object getModel() {
	return model;
    }

    /**
     * Sets the model property on this data-bound component.
     * @param model the Object which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModel(Object model) {
        this.model = model;
    }

    /**
     * Returns the current text value for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return String containing the current text value 
     */
    public String getText(RenderContext rc) { 

        String value = null;
        if ( model == null )  {
            return text;
        }    
        else { 
            try {
                value =(String)rc.getObjectAccessor().getObject(rc.getRequest(), 
                        (String) model);
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return text;
            }
	    return value;
        }    
    }

    /**
     * Sets the current text value for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it 
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param text String containing the new text value for this component
     */
    public void setText(RenderContext rc, String text) { 
        if ( model == null ) {
            this.text = text;
        } else {
            try {
                rc.getObjectAccessor().setObject(rc.getRequest(), 
						 (String)model, text);    
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                this.text = text;
            }
        }    
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

        String new_value = (String) value_event.getNewValue();
        String srcId = value_event.getSourceId();
        String modelRef = (String) getModel();

        EventContext eventContext = value_event.getEventContext();
        // Assert.assert_it( eventContext != null );

        ObjectManager ot = eventContext.getObjectManager();
        // Assert.assert_it( ot != null );

        ServletRequest request = eventContext.getRequest();

        RenderContext rc = (RenderContext)ot.get(request,
                Constants.REF_RENDERCONTEXT);
        // Assert.assert_it( rc != null );
 
        // PENDING ( visvan ) according to the latest version of the
        // spec, value changes will not not pushed to model object
        // until it is validated. This change will be made along with
        // model object changes.
        if ( modelRef == null ) {
            setText(rc, (String)value_event.getNewValue());
        } else {
            rc.getObjectAccessor().setObject(request, modelRef,
                                             new_value);
        }

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

}
