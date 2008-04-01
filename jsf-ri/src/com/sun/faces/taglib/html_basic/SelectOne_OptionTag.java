/*
 * $Id: SelectOne_OptionTag.java,v 1.12 2002/04/05 19:41:18 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_OptionTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UIComponent;
import javax.faces.UISelectOne;
import javax.faces.ObjectManager;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>SelectOne_OptionTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_OptionTag.java,v 1.12 2002/04/05 19:41:18 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_OptionTag extends FacesTag {
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

    private String selected = null;
    private String value = null;
    private String label = null;
    private String description = null;
    private String parentId = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_OptionTag()
{
    super();
}

//
// Class methods
//

//
// General Methods
//
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * We override getId to return the parent's Id.
     */
    public String getId() {
        if (null == parentId) {
            SelectOne_OptionListTag ancestor = null;

            // get the UISelectOne that is our component.
            try {
                ancestor = (SelectOne_OptionListTag)
                    findAncestorWithClass(this, SelectOne_OptionListTag.class);
                parentId = ancestor.getId();
            } catch ( Exception e ) {
                throw new IllegalStateException("Option must be enclosed in a SelectOne_Option tag");
            }
        }
        return parentId;
    }

    public void setId(String id) {
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
        Assert.assert_it(false, "This shouldn't be called, the UISelectOne is already in the OM");
        return null;
    }

    public void setAttributes(UIComponent comp) {
        ParameterCheck.nonNull(comp);
        Assert.assert_it(null != facesContext);
        Assert.assert_it(comp instanceof UISelectOne);

        UISelectOne uiSelectOne = (UISelectOne) comp;
        uiSelectOne.addItem(getValue(), getLabel(), getDescription());

        // if it is selected, make sure the model knows about it.

        // we should update selectedValue only if it is null
        // in the model bean otherwise we would be overwriting
        // the value in model bean, losing any earlier updates. 
        if ( uiSelectOne.getSelectedValue(facesContext) == null &&
                getSelected() != null ) {
            uiSelectOne.setSelectedValue(getValue());
        }
    }

    public String getRendererType() {
        return null;
    }

    public void addListeners(UIComponent comp) throws JspException {
    }

    public UIComponent getComponentFromStart(ObjectManager objectManager) {
	SelectOne_OptionListTag ancestor = null;
	
	// get the UISelectOne that is our component.
	try {
	    ancestor = (SelectOne_OptionListTag) 
		findAncestorWithClass(this, SelectOne_OptionListTag.class);
	} catch ( Exception e ) {
	    throw new IllegalStateException("Option must be enclosed in a SelectOne_Option tag");
	}
	Assert.assert_it(null != ancestor);
	return ancestor.getComponentForTag();
    }

    public UIComponent getComponentFromEnd(ObjectManager objectManager) {
	return getComponentFromStart(objectManager);
    }



    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        selected = null;
        value = null;
        label = null;
    }


} // end of class SelectOne_OptionTag
