/*
 * $Id: FormTag.java,v 1.46 2003/09/05 14:34:45 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import java.io.IOException;
import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.application.StateManager;

import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;


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

        if (formName != null) {
            // we set the bundle attribute on the root component here  
            // so that we don't set it again during postback. 
            // This cannot be done in FacesTag since this is specific
            // to FormTag. Since formName is a required attribute,
            // we can be sure that these statements will be executed
            // the first time the tags are processed.
            if (bundle != null) {
                // set it as an attribute on the root component so that
                // it is available to children and doesn't have to be repeated
                // in every tag.
                FacesContext context = FacesContext.getCurrentInstance();
                UIComponent root = context.getViewRoot();
                root.setAttribute(RIConstants.BUNDLE_ATTR, bundle);    
            }
            component.setAttribute("name", formName);
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
    }
    
    //
    // Methods from TagSupport
    //
     public int doEndTag() throws JspException {
	// Look up the FacesContext instance for this request
	FacesContext facesContext = FacesContext.getCurrentInstance();
	StateManager stateManager = Util.getStateManager(facesContext);
	try {
	    stateManager.writeStateMarker(facesContext);
	} catch (IOException iox) {
            Object [] params = { "session", iox.getMessage() };
            throw new JspException(
            Util.getExceptionMessage(Util.SAVING_STATE_ERROR_MESSAGE_ID, params), 
            iox);
        }  
	return super.doEndTag();
    }


} // end of class FormTag
