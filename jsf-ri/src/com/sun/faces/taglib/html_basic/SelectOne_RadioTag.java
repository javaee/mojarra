/*
 * $Id: SelectOne_RadioTag.java,v 1.3 2001/12/12 00:24:42 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// SelectOne_RadioTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WForm;
import javax.faces.WSelectOne;
import javax.faces.ObjectTable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;

/**
 *
 *  <B>SelectOne_RadioTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_RadioTag.java,v 1.3 2001/12/12 00:24:42 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_RadioTag extends TagSupport
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

    private String checked = null;
    private String value = null;
    private String label = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_RadioTag()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
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

//
// Methods from TagSupport
//

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
public int doStartTag() throws JspException {
    ObjectTable ot = (ObjectTable) pageContext.getServletContext().
	getAttribute(Constants.REF_OBJECTTABLE);
    Assert.assert_it( ot != null );
    RenderContext renderContext = 
	(RenderContext)ot.get(pageContext.getSession(),
			      Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );
    
    // Ascend the tag hierarchy to get the RadioGroup tag
    RadioGroupTag ancestor = null;
    WSelectOne wSelectOne = null;
    String parentName = null;
    
    // get the WSelectOne that is our component.
    try {
	ancestor = (RadioGroupTag) 
	    findAncestorWithClass(this, RadioGroupTag.class);
	parentName = ancestor.getName();
    } catch ( Exception e ) {
	throw new JspException("Option must be enclosed in a SelectOne_Option tag");
    }
    Assert.assert_it(null != ancestor);
    Assert.assert_it(null != parentName);
    
    // by virtue of being inside a RadioGroup there must be a
    // WSelectOne instance under the name.
    wSelectOne = (WSelectOne) ot.get(pageContext.getRequest(), parentName);
    Assert.assert_it(null != wSelectOne);
    
    // These over-write the values from "the last time around", but
    // its ok, since we just use it for rendering.
    wSelectOne.setAttribute(renderContext, "checked", getChecked());
    wSelectOne.setAttribute(renderContext, "value", getValue());
    wSelectOne.setAttribute(renderContext, "label", getLabel());

    // Add this value to the Collection
    ancestor.getItems().add(getValue());
    // if it is checked, make sure the model knows about it.
    if (null != getChecked()) {
	wSelectOne.setSelectedValue(renderContext, getValue());
    }
    
    RenderKit renderKit = renderContext.getRenderKit();
    if (renderKit == null) {
	throw new JspException("Can't determine RenderKit!");
    }
    
    Renderer renderer = null;
    try {
	renderer = renderKit.getRenderer("com.sun.faces.renderkit.html_basic.RadioRenderer");
    } catch (FacesException e) {
	throw new JspException(
			       "FacesException!!! " + e.getMessage());
    }
    
    if (renderer == null) {
	throw new JspException(
			       "Could not determine 'renderer' for component");
    }
    
    // 3. Render the component. (Push the component on
    //    the render stack first).
    //
    try {
	renderContext.pushChild(wSelectOne);
	renderer.renderStart(renderContext, wSelectOne);
	//PENDING(rogerk) complet/pop should be done in doEndTag
	//
	renderer.renderComplete(renderContext, wSelectOne);
	renderContext.popChild();
    } catch (java.io.IOException e) {
	throw new JspException("Problem rendering component: "+
			       e.getMessage());
    } catch (FacesException f) {
	throw new JspException("Problem rendering component: "+
			       f.getMessage());
    }

    wSelectOne.setAttribute(renderContext, "checked", null);
    wSelectOne.setAttribute(renderContext, "value", null);
    wSelectOne.setAttribute(renderContext, "label", null);

    return (EVAL_BODY_INCLUDE);
}

    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        return EVAL_PAGE;
    }

} // end of class SelectOne_RadioTag
