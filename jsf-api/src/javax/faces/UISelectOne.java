/*
 * $Id: UISelectOne.java,v 1.10 2002/03/07 23:44:04 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package javax.faces;

import java.util.Collection;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletRequest;

/**
 * Class for representing a user-interface component which allows
 * the user to select one value from many.
 * <p>
 * A UISelectOne has two pieces of application-specific data:
 * <ol>
 * <li>The set of selectable items
 * <li>The selected value
 * </ol>
 * Each of these can either be connected to an application-specific
 * model object (via the model-reference properties) or set locally
 * as attributes directly on the component.
 * <p>
 * An item is comprised of three pieces of data:
 * <ol>
 * <li>value (required): uniquely identifies the item within the set
 * <li>label (optional): the label which displays the item to the user
 *     (defaults to null)
 * <li>description (optional): a text description of the item (may
 *     be used on clients supporting &quot;tooltips&quot; or accessibility
 *     (defaults to null)
 * </ol>
 * If a model-reference is set for the selectable items, it must resolve
 * to a Collection object containing the values of the selectable items.
 * <p>
 * The selected value corresponds to the value property of the currently
 * selected item.
 */
public class UISelectOne extends UIComponent implements EventDispatcher, Validatible {
    private static String TYPE = "SelectOne";
    // PENDING(edburns): don't cast these to Strings all over the place.
    private String selectedModelReference = null;
    private String messageModelReference = null;

    private Vector valueChangeListeners = null;

    /** 
     * Returns a String representing the select-one type.  
     *
     * @return a String object containing &quot;SelectOne&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The selected-model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for the selected item on this component.  
     * The model-reference must resolve to an object which implements 
     * one of the following types:
     * <ul>
     * <li><code>java.lang.String</code> corresponding to the id of the
     *     selected item
     * </ul>  
     * @see #setSelectedModelReference  
     * @return String containing the selected-model-reference for this component
     */
    public String getSelectedModelReference() {
        return selectedModelReference;
    }

    /**
     * Sets the selected-model-reference property on this data-bound component.
     * @see #getSelectedModelReference
     * @param selectedModelReference the String which contains a reference 
     *        to the
     *        object which acts as the data-source for selectable items
     *        for this component
     */
    public void setSelectedModelReference(String selectedModelReference) {
        this.selectedModelReference = selectedModelReference;
    } 

    /**
     * The &quot;items&quot; attribute which represents the set of
     * selectable items.  
     * If a local attribute value has been set, that value is returned,
     * else if the model-reference has been set, that reference is
     * resolved and an Iterator containing UISelectOne.Item objects
     * corresponding to that jmodel is returned, else null is returned.
     * @see #setItems
     * @param rc the render context used to render this component
     * @return Iterator containing UISelectOne.Item objects corresponding
     *         to the selectable items
     */
    public Iterator getItems(RenderContext rc) {

        List selectItems = (List)getAttribute(rc, "items");
        Collection coll = null;

        // If we've got a local
        //
        if (selectItems != null) {
            return selectItems.iterator();

        // Otherwise, look to the model
        //
        } else if (getModelReference() != null) {
            try {
                coll = (Collection) rc.getObjectAccessor().
                    getObject(rc.getRequest(), getModelReference());
//PENDING(rogerk) at this point, we have a collection of item values
// (no labels or descriptions).  We would want to get the localized
// labels and descriptions from a resource bundle given the item value
// as he "key", then build a collection of UISelectItem.Item objects to
// be returned.
            } catch ( FacesException e ) {
            }
        }
        
        if (coll != null) {
            return coll.iterator();
        } else {
            return null;
        }
    }

    /**
     * Returns the item corresponding to the specified index.
     * This method will use the Locale contained in the render context 
     * to look up localized values for the item's label and description
     * properties.  For each property, if a localized value found, it
     * will be set in the Item object returned from this method.
     * @param rc the render context used to render this component
     * @param index integer containing index of item
     * @throws IndexOutOfBoundsException if index < 0 or index >= itemCount
     * @return UISelectOne.Item object representing the item at the specified index
     */
    public UISelectOne.Item getItem(RenderContext rc, int index) 
        throws IndexOutOfBoundsException {
        List selectItems = (List)getAttribute(rc, "items");
        if (selectItems == null) {
            return null;
        }

        UISelectOne.Item item = null;
        try {
            item = (UISelectOne.Item)selectItems.get(index); 
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("specified index out of bounds.");
        }
        
	return item;
    }


    /**
     * Sets the local copy of selectable items attribute.  The List
     * must contain UISelectOne.Item objects corresponding to the selectable items.
     * @see #getItems
     * @param items List containing UISelectOne.Item objects.  
     */
    public void setItems(List items) {
        setAttribute("items", items);
    }

    /**
     * Sets the selectable items attribute to correspond to the
     * item values specified in the List.  The List
     * must contain Strings corresponding to the selectable items values.
     * This method sets only the value property of the selectable items; the
     * label and description item properties will be null.
     * @see #getItems
     * @param items List containing String objects which contain the item values
     */
    public void setItemValues(List itemValues) {

        // PENDING(aim): this should probably build the set of
	// UISelectOne items and load them with the values

        if (itemValues != null) {
            Vector items = new Vector();
            for (int i=0; i<itemValues.size(); i++) {
                UISelectOne.Item item = new UISelectOne.Item(
                    (String)itemValues.get(i), null, null); 
                items.add(item);
            }     
            setAttribute("items", items);
        }
    }

    /**
     * Sets the item at the specified index to be the specified item.
     * @param index integer containing index of item
     * @param item UISelectOne.Item object representing the item being set
     * @throws IndexOutOfBoundsException if index < 0 or index >= itemCount 
     */
    public void setItem(int index, UISelectOne.Item item) {
        List selectItems = (List)getAttribute(null, "items");
        if (selectItems == null || index < 0 || 
            index >= selectItems.size()) {
            throw new IndexOutOfBoundsException("Select items list is null or index is out of range.");
        }
        selectItems.add(index, item);
    }

    /**
     * Adds the specified item to the set of selectable items.
     * The value parameter may not be null; label and description may
     * be null. The item is added to the end of the sequence of items.
     * @see #removeItem
     * @param value the String containing the item's value
     * @param label String containing the item's label
     * @param description String containing the item's description
     * @throws NullPointerException if the value parameter is null
     */
    public void addItem(String value, String label, String description) 
        throws FacesException {
        if (value == null) {
            throw new NullPointerException("value parameter cannot be null.");
        }

        UISelectOne.Item selectItem = new UISelectOne.Item(value, label, description);
        List selectItems = (List)getAttribute(null, "items");

        if (selectItems == null) {
            selectItems = new Vector();
            selectItems.add(selectItem);
            setAttribute("items", selectItems);
        } else {
            selectItems.add(selectItem);
        }
    }

    /**
     * Removes the specified item from the set of selectable items.
     * @see #addItem
     * @param value String containing the item's value
     * @throws IllegalParameterException if the item does not exist
     */
    public void removeItem(String value) {

        List selectItems = (List)getAttribute(null, "items");
        if (selectItems == null || selectItems.size() == 0) {
            return;
        }

        boolean foundItem = false;
        for (int i=0; i<selectItems.size(); i++) {
            if (((UISelectOne.Item)selectItems.get(i)).value.equals(value)) {
                foundItem = true;
                selectItems.remove(i);
                break;
            }
        }

        if (!foundItem) {
            throw new FacesException("Item does not exist.");
        }
    }

    /**
     * Removes the item corresponding to the specified index from the
     * set of selectable items.
     * @param index integer containing index of item
     * @throws IndexOutOfBoundsException if index < 0 or index >= itemCount
     */ 
    public void removeItem(int index) {
        List selectItems = (List)getAttribute(null, "items");
        if (selectItems == null || selectItems.size() == 0) {
            return;
        }
        if (index < 0 || index >= selectItems.size()) {
            throw new IndexOutOfBoundsException("Index parameter is out of bounds.");
        }
        selectItems.remove(index);
    }

    /**
     * The &quot;selectedValue&quot; attribute which corresponds to the
     * value of the item which is currently selected.
     * If a local attribute value has been set, that value is returned,
     * else if the selected-model-reference has been set, that reference is
     * resolved and the associated model object is returned, 
     * else <code>null</code> is returned.
     * @see #setSelectedValue
     * @param rc the render context used to render this component
     * @return String containing the value of the selected item, or null
     *         if no items are selected
     */
    public String getSelectedValue(RenderContext rc) {
        return (String) getValue(rc);
    }

    /**
     * Sets the local value of the &quot;selectedValue&quot; attribute.
     * @see #getSelectedItem
     * @param value String containing the value of the selected item
     * @throws IllegalArgumentException if item does not exist
     */
    public void setSelectedValue(String value) {
        setValue(value);
    }

    /**
     * Registers the specified listener id as a value-change listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listenerId
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerId) throws FacesException {
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
     * @param listenerId the id of the value-change listener
     * @throws FacesException if listenerId is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerId) throws FacesException {
        if (valueChangeListeners == null) {
            throw new FacesException("Listener Id is not registered.");
        }
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
    public void dispatch(EventObject e) {

        // PENDING ( visvan ) except the method to set
        // the values locally, rest of the code is the same
        // for all components that support value change events.
        // TODO: Is it feasible to move the common code to
        // some util class ??

        ValueChangeEvent value_event = null;
        if ( e instanceof ValueChangeEvent)  {
            value_event = (ValueChangeEvent) e;
        } else {
            throw new FacesException("Invalid event type. " +
                    "Expected ValueChangeEvent");
        }

        String new_value = (String) value_event.getNewValue();

        EventContext eventContext = value_event.getEventContext();
        // Assert.assert_it( eventContext != null );

        ObjectManager ot = eventContext.getObjectManager();
        // Assert.assert_it( ot != null );

        ServletRequest request = eventContext.getRequest();

        RenderContext rc = (RenderContext)ot.get(request,
                Constants.REF_RENDERCONTEXT);
        // Assert.assert_it( rc != null );

	setSelectedValue((String)value_event.getNewValue());
	pushValueToModel(rc);

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
    }

    /**
     * @return Iterator containing the Validator instances registered
     *         for this component
     */
    public Iterator getValidators() {
	return null; //compile
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
    }

    /**
     * A class for encapsulating the information associated with an item.
     */
    public static class Item extends Object {
	// package-private to allow internal modification
        String value = null;
        String label = null;
        String description = null;

        public Item(String value, String label, String description) {
            this.value = value;
            this.label = label;
            this.description = description;
        }
  
        public Item(String value) {
	    this(value, null, null);
	}

        public String getValue() {
	    return value;
	}

        public String getLabel() {
	    return label;
	}

        public String getDescription() {
	    return description;
	}

        void clear() {
            value = null;
            label = null;
            description = null;
        }
    }
}
