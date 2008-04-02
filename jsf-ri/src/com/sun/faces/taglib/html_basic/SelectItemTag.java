/*
 * $Id: SelectItemTag.java,v 1.22 2003/10/07 20:15:54 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;


/**
 * This class is the tag handler that evaluates the 
 * <code>selectitem</code> custom tag.
 */

public class SelectItemTag extends BaseComponentTag
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

    protected String itemValue = null;
    protected String itemValue_ = null;
    protected String itemLabel = null;
    protected String itemLabel_ = null;
    protected String description = null;
    protected String description_ = null;
   
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectItemTag()
    {
        super();
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    public void setItemValue(String value) {
        this.itemValue_ = value;
    }

    public void setItemLabel(String label) {
        this.itemLabel_ = label;
    }

    public void setDescription(String description) {
        this.description_ = description;
    }
    

    //
    // General Methods
    //
    public String getRendererType() { 
        return null;
    }
    public String getComponentType() { 
        return "SelectItem"; 
    }
    
    //
    // Methods from BaseComponentTag
    //

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UISelectItem selectItem = (UISelectItem) component;
	
	if (null != itemValue) {
	    selectItem.setItemValue(itemValue);
	}
	if (null != itemLabel) {
	    selectItem.setItemLabel(itemLabel);
	}
	if (null != description) {
	    selectItem.setItemDescription(description);
	}
        
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (itemValue_ != null) {
            itemValue = Util.evaluateElExpression(itemValue_, pageContext);
   	}
        if (itemLabel_ != null) {
            itemLabel = Util.evaluateElExpression(itemLabel_, pageContext);
   	}
        if (description_ != null) {
            description = Util.evaluateElExpression(description_, pageContext);
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


} // end of class SelectItemTag
