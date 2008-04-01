/*
 * $Id: TextEntry_InputTag.java,v 1.22 2002/02/14 03:57:41 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_InputTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UITextEntry;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>TextEntry_InputTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_InputTag.java,v 1.22 2002/02/14 03:57:41 edburns Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class TextEntry_InputTag extends FacesTag
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables
    private String value = null;
    private String size = null;
    private String valueChangeListener = null;
    private String maxlength = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TextEntry_InputTag()
    {
        super();
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
        return new UITextEntry();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UITextEntry);

	UITextEntry uiTextEntry = (UITextEntry) comp;

        uiTextEntry.setAttribute("size", size);
        uiTextEntry.setAttribute("maxlength", maxlength);

        // If model attribute is not found get it 
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( getModel() != null ) {
            uiTextEntry.setModelReference(getModel());
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface ??
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
               String model_str = ancestor.getModel();
               if ( model_str != null ) {
                   setModel("$" + model_str + "." + getId());
                   uiTextEntry.setModelReference(getModel());
               } 
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        if ( value != null ) {
            uiTextEntry.setText(value);
        }
    }

    public String getRendererType() {
	return "InputRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
	ParameterCheck.nonNull(comp);
	
	if (null == valueChangeListener) {
	    return;
	}

	Assert.assert_it(comp instanceof UITextEntry);
	UITextEntry uiTextEntry = (UITextEntry) comp;
	try {
	    uiTextEntry.addValueChangeListener(valueChangeListener);    
	} catch (FacesException fe) {
	    throw new JspException("Listener + " + valueChangeListener +
				   " does not exist or does not implement valueChangeListener " + 
				   " interface" );
	}
    }    
    
    //
    // Class methods
    //

    //
    // General Methods
    //

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        value = null;
        size = null;
        maxlength = null;
        valueChangeListener = null;
    }

    /**
     * Returns the value of the "value" attribute
     *
     * @return String value of "value" attribute
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the size attribute
     * @param size value of size attribute
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Returns the value of size attribute
     *
     * @return String value of size attribute
     */
    public String getSize() {
        return this.size;
    }

    /**
     * Sets  maxlength attribute
     * @param  maxlength value of maxlength attribute
     */
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

   /**
     * Returns the value of maxlength attribute
     *
     * @return String value of maxlength attribute
     */
    public String getMaxlength() {
        return this.maxlength;
    }
    
    /**
     * Returns the value of valueChangeListener attribute
     *
     * @return String value of valueChangeListener attribute
     */
    public String getValueChangeListener() {
        return this.valueChangeListener;
    }

    /**
     * Sets valueChangeListener attribute
     * @param change_listener value of formListener attribute
     */
    public void setValueChangeListener(String change_listener) {
        this.valueChangeListener = change_listener;
    }

} // end of class TextEntry_InputTag
