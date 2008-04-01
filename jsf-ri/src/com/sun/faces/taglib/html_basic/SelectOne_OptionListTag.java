/*
 * $Id: SelectOne_OptionListTag.java,v 1.13 2002/02/07 04:31:34 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_OptionListTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UIComponent;
import javax.faces.UISelectOne;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>SelectOne_OptionListTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_OptionListTag.java,v 1.13 2002/02/07 04:31:34 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_OptionListTag extends FacesTag {
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

    private String selectedValueModel = null;
    private String valueChangeListener = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_OptionListTag()
{
    super();
}

//
// Class methods
//

//
// General Methods
//
    public String getSelectedValueModel() {
        return selectedValueModel;
    }

    public void setSelectedValueModel(String newVal) {
        this.selectedValueModel = newVal;
    }

    public String getValueChangeListener() {
        return this.valueChangeListener;
    }

    public void setValueChangeListener(String change_listener) {
        this.valueChangeListener = change_listener;
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
        return new UISelectOne();
    }

    public void setAttributes(UIComponent comp) {
        ParameterCheck.nonNull(comp);
        Assert.assert_it(comp instanceof UISelectOne);

        UISelectOne uiSelectOne = (UISelectOne) comp;
        // assert that model and selectedValueModel are either both
        // non-null or both null.
        Assert.assert_it(
                         ((null != getModel() && null != getSelectedValueModel()
) ? true : false)
                         ||
                         ((null == getModel() && null == getSelectedValueModel()
) ? true : false)
                         );

        if ( null != getModel() && null != getSelectedValueModel()) {
            uiSelectOne.setModelReference(getModel());
            uiSelectOne.setSelectedModelReference(getSelectedValueModel());
        }
        uiSelectOne.setItems(new java.util.Vector());
    }
//
// Methods called by children tags:
//

//
//  Helper Methods for tag methods
//

    public String getRendererType() {
        return "OptionListRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
        ParameterCheck.nonNull(comp);

        if (null == valueChangeListener) {
            return;
        }

        Assert.assert_it(comp instanceof UISelectOne);
        UISelectOne uiSelectOne = (UISelectOne) comp;
        try {
            uiSelectOne.addValueChangeListener(valueChangeListener);
        } catch (FacesException fe) {
            throw new JspException("Listener + " + valueChangeListener +
                " does not exist or does not implement valueChangeListener " + 
                    " interface" );
        }
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();
        selectedValueModel = null;
        valueChangeListener = null;
    }

} // end of class SelectOne_OptionListTag
