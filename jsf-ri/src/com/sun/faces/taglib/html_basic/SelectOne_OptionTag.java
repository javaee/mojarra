/*
 * $Id: SelectOne_OptionTag.java,v 1.6 2002/01/16 21:06:36 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_OptionTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.UIForm;
import javax.faces.UISelectOne;
import javax.faces.ObjectManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;

/**
 *
 *  <B>SelectOne_OptionTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_OptionTag.java,v 1.6 2002/01/16 21:06:36 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_OptionTag extends TagSupport
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

    private String selected = null;
    private String value = null;
    private String label = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_OptionTag()
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

    /**
     * Returns the appropriate renderer for the tag
     *
     * @param rc RenderContext to obtain renderkit
     */
    public Renderer getRenderer(RenderContext rc ) throws JspException{

        Renderer renderer = null;
        RenderKit renderKit = rc.getRenderKit();
        if (renderKit == null) {
            throw new JspException("Can't determine RenderKit!");
        }
        try {
            String class_name = "com.sun.faces.renderkit.html_basic.OptionRenderer";
            renderer = renderKit.getRenderer(class_name);
        } catch (FacesException e) {
            e.printStackTrace();
            throw new JspException("FacesException " + e.getMessage());
        }

        if (renderer == null) {
            throw new JspException(
                "Could not determine renderer for TextEntry component");
        }
        return renderer;	
    }


//
// Methods from TagSupport
//

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
public int doStartTag() throws JspException {
    ObjectManager ot = (ObjectManager) pageContext.getServletContext().
	getAttribute(Constants.REF_OBJECTMANAGER);
    Assert.assert_it( ot != null );
    RenderContext renderContext = 
	(RenderContext)ot.get(pageContext.getSession(),
			      Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );
    
    // Ascend the tag hierarchy to get the RadioGroup tag
    SelectOne_OptionListTag ancestor = null;
    UISelectOne wSelectOne = null;
    String parentName = null;
    
    // get the UISelectOne that is our component.
    try {
	ancestor = (SelectOne_OptionListTag) 
	    findAncestorWithClass(this, SelectOne_OptionListTag.class);
	parentName = ancestor.getId();
    } catch ( Exception e ) {
	throw new JspException("Option must be enclosed in a SelectOne_Option tag");
    }
    Assert.assert_it(null != ancestor);
    Assert.assert_it(null != parentName);
    
    // by virtue of being inside a RadioGroup there must be a
    // UISelectOne instance under the name.
    wSelectOne = (UISelectOne) ot.get(pageContext.getRequest(), parentName);
    Assert.assert_it(null != wSelectOne);
    
    // These over-write the values from "the last time around", but
    // its ok, since we just use it for rendering.
    wSelectOne.setAttribute(renderContext, "selected", getSelected());
    wSelectOne.setAttribute(renderContext, "value", getValue());
    wSelectOne.setAttribute(renderContext, "label", getLabel());

    // Add this value to the Collection
    ancestor.getItems().add(getLabel());
    // if it is selected, make sure the model knows about it.
    if (null != getSelected()) {
	wSelectOne.setSelectedValue(renderContext, getLabel());
    }
    
    try {
        wSelectOne.setRendererName(renderContext, "OptionRenderer");
        wSelectOne.render(renderContext);
    } catch (java.io.IOException e) {
        throw new JspException("Problem rendering component: "+
            e.getMessage());
    } catch (FacesException f) {
        throw new JspException("Problem rendering component: "+
            f.getMessage());
    }

    wSelectOne.setAttribute(renderContext, "selected", null);
    wSelectOne.setAttribute(renderContext, "value", null);
    wSelectOne.setAttribute(renderContext, "label", null);

    return (EVAL_BODY_INCLUDE);
}

/**
 * End Tag Processing
 */
public int doEndTag() throws JspException{
    ObjectManager ot = (ObjectManager) pageContext.getServletContext().
        getAttribute(Constants.REF_OBJECTMANAGER);
    Assert.assert_it( ot != null );
    RenderContext renderContext =
        (RenderContext)ot.get(pageContext.getSession(),
                              Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );

    // Ascend the tag hierarchy to get the RadioGroup tag
    SelectOne_OptionListTag ancestor = null;
    UISelectOne wSelectOne = null;
    String parentName = null;

    // get the UISelectOne that is our component.
    try {
        ancestor = (SelectOne_OptionListTag)
            findAncestorWithClass(this, SelectOne_OptionListTag.class);
        parentName = ancestor.getId();
    } catch ( Exception e ) {
        throw new JspException("Option must be enclosed in a SelectOne_Option tag");
    }
    Assert.assert_it(null != ancestor);
    Assert.assert_it(null != parentName);

    // by virtue of being inside a RadioGroup there must be a
    // UISelectOne instance under the name.
//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
    wSelectOne = (UISelectOne) ot.get(pageContext.getRequest(), parentName);
    Assert.assert_it(null != wSelectOne);

    // Complete the rendering process
    //
    try {
        wSelectOne.renderComplete(renderContext);
    } catch (java.io.IOException e) {
        throw new JspException("Problem completing rendering: "+
            e.getMessage());
    } catch (FacesException f) {
        throw new JspException("Problem completing rendering: "+
            f.getMessage());
    }

    return EVAL_PAGE;
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
