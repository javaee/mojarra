/*
 * $Id: Output_TextTag.java,v 1.23 2002/03/08 00:24:50 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Output_TextTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UIOutput;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>Output_TextTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Output_TextTag.java,v 1.23 2002/03/08 00:24:50 jvisvanathan Exp $
 * 
 *
 */

public class Output_TextTag extends FacesTag
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

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public Output_TextTag()
    {
        super();
    }

    //
    // Class methods
    //

//
// Methods from FacesTag
//

    /**
     * Creates a Form component and sets renderer specific
     * properties.
     *
     * @param rc renderContext
     */

    public UIComponent newComponentInstance() {
        return new UIOutput();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UIOutput);
	
	UIOutput out = (UIOutput) comp;
	
        // set render independent attributes 
        // If model attribute is not found get it
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.
	
        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( getModel() != null ) {
            out.setModelReference(getModel());
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface. Also at this point we must ensure that
            // the bean has a property with this id and has 
            // accessor methods for it. Need to figure out an
            // efficient way to do that. 
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
							   FormTag.class);
		String model_str = ancestor.getModel();
		if ( model_str != null ) {
		    setModel("$" + model_str + "." + getId());
		    out.setModelReference(getModel());
		}
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        if ( out.getValue(renderContext) == null && value != null ) {
            out.setValue(value);
        }
    }

    public String getRendererType() {
	return "TextRenderer";
    }

    //
    // General Methods
    //

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        value = null;
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
     * Sets the "value" attribute
     * @param value value of "value" attribute
     */
    public void setValue(String value) {
        this.value = value;
    }

} // end of class Output_TextTag
