/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.24 2002/04/16 21:15:58 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectBoolean_CheckboxTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UISelectBoolean;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>SelectBoolean_CheckboxTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.24 2002/04/16 21:15:58 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectBoolean_CheckboxTag extends FacesTag {
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

    private String checked = null;
    private String value = null;
    private String label = null;
    private String valueChangeListener = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectBoolean_CheckboxTag() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

//
// Methods from FacesTag
//

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     *
     * @param rc facesContext
     */

    public UIComponent newComponentInstance() {
        return new UISelectBoolean();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UISelectBoolean);

	UISelectBoolean uiSelectBoolean = (UISelectBoolean) comp;

        uiSelectBoolean.setAttribute("value", getValue());
        uiSelectBoolean.setAttribute("label", getLabel());
        // If model attribute is not found get it
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( getModelReference() != null ) {
            uiSelectBoolean.setModelReference(getModelReference());
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface ??
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
							   FormTag.class);
		String model_str = ancestor.getModelReference();
		if ( model_str != null ) {
		    setModelReference("${" + model_str + "." + getId() + "}");
		    uiSelectBoolean.setModelReference(getModelReference());
               }
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        if ( checked != null ) {
             boolean state = (Boolean.valueOf(checked)).booleanValue();
             uiSelectBoolean.setSelected(facesContext, state);
        }
    }

    public String getRendererType() {
	return "CheckboxRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
	ParameterCheck.nonNull(comp);
	
	if (null == valueChangeListener) {
	    return;
	}

	Assert.assert_it(comp instanceof UISelectBoolean);
	UISelectBoolean uiSelectBoolean = (UISelectBoolean) comp;
	try {
	    uiSelectBoolean.addValueChangeListener(valueChangeListener);    
	} catch (FacesException fe) {
	    throw new JspException("Listener + " + valueChangeListener +
				   " does not exist or does not implement valueChangeListener " + 
				   " interface" );
	}
    }    

    //
    // General Methods
    //

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        checked = null;
        value = null;
        label = null;
        valueChangeListener = null;
    }

} // end of class SelectBoolean_CheckboxTag
