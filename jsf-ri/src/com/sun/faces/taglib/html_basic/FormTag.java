/*
 * $Id: FormTag.java,v 1.40 2003/08/15 19:15:08 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;


import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;


/**
 * This class is the tag handler that evaluates the <code>form</code> custom tag.
 */

public class FormTag extends FacesTag
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

    public String formName = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public FormTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public String getFormName() { 
        return formName; 
    }
    public void setFormName(String newFormName) { 
	formName = newFormName;
    }

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Form"; 
    }
    public String getComponentType() { 
        return "Form"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIForm form = (UIForm) component;

	if (null == formName) {
	    form.setFormName(formName);
             // we set the bundle attribute on the root component here  
             // so that we don't set it again during postback. 
             // This cannot be done in FacesTag since this is specific
             // to FormTag. Since formName is a required attribute,
             // we can be sure that these statements will be executed
             // the first time the tags are processed.
            if ( bundle != null) { 
                // set it as an attribute on the root component so that
                // it is available to children and doesn't have to be repeated
                // in every tag.
                FacesContext context = FacesContext.getCurrentInstance();
                UIComponent root = context.getTree().getRoot();
                root.setAttribute(RIConstants.BUNDLE_ATTR, bundle);
            }
	}
        // action, method, enctype, acceptcharset, accept, target, onsubmit, 
        // onreset
        if (onsubmit != null ) {
            component.setAttribute("onsubmit", onsubmit); 
        }
        if (onreset != null ) {
            component.setAttribute("onreset", onreset); 
        }
        if (action != null ) {
            component.setAttribute("action", action); 
        }
        if (method != null ) {
            component.setAttribute("method", method); 
        }
        if (enctype != null ) {
            component.setAttribute("enctype", enctype); 
        }
        if (accept != null ) {
            component.setAttribute("accept", accept); 
        }
        if (target != null ) {
            component.setAttribute("target", target); 
        }
        if (acceptcharset != null ) {
            component.setAttribute("acceptcharset", acceptcharset); 
        }
        if (formClass != null ) {
            component.setAttribute("formClass", formClass); 
        }
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class FormTag
