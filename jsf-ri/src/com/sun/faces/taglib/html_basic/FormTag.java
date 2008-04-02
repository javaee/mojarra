/*
 * $Id: FormTag.java,v 1.50 2003/10/07 20:15:52 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the <code>form</code> custom tag.
 */

public class FormTag extends BaseComponentTag
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
    public String formName_ = null;

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
	formName_ = newFormName;
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
            // This cannot be done in BaseComponentTag since this is specific
            // to FormTag. Since formName is a required attribute,
            // we can be sure that these statements will be executed
            // the first time the tags are processed.
            if (bundle != null) {
                // set it as an attribute on the root component so that
                // it is available to children and doesn't have to be repeated
                // in every tag.
                FacesContext context = FacesContext.getCurrentInstance();
                UIComponent root = context.getViewRoot();
                root.getAttributes().put(RIConstants.BUNDLE_ATTR, bundle);    
            }
            component.getAttributes().put("name", formName);
        }
        
        // action, method, enctype, acceptcharset, accept, target, onsubmit, 
        // onreset
        if (onsubmit != null ) {
            component.getAttributes().put("onsubmit", onsubmit); 
        }
        if (onreset != null ) {
            component.getAttributes().put("onreset", onreset); 
        }
        if (action != null ) {
            component.getAttributes().put("action", action); 
        }
        if (method != null ) {
            component.getAttributes().put("method", method); 
        }
        if (enctype != null ) {
            component.getAttributes().put("enctype", enctype); 
        }
        if (accept != null ) {
            component.getAttributes().put("accept", accept); 
        }
        if (target != null ) {
            component.getAttributes().put("target", target); 
        }
        if (acceptcharset != null ) {
            component.getAttributes().put("acceptcharset", acceptcharset); 
        }        
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (formName_ != null) {
            formName = Util.evaluateElExpression(formName_, pageContext);
   	}
    }


    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // chain to the parent implementation
        return super.doStartTag();
    }


} // end of class FormTag
