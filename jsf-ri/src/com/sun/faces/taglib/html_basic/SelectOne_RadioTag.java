/*
 * $Id: SelectOne_RadioTag.java,v 1.10 2002/01/31 20:38:55 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
import javax.faces.UIForm;
import javax.faces.UISelectOne;
import javax.faces.ObjectManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;
import java.util.Vector;

/**
 *
 *  <B>SelectOne_RadioTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_RadioTag.java,v 1.10 2002/01/31 20:38:55 rogerk Exp $
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
    private String description = null;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    RadioGroupTag ancestor = null;
    UISelectOne uiSelectOne = null;
    String parentName = null;
    
    // get the UISelectOne that is our component.
    try {
	ancestor = (RadioGroupTag) 
	    findAncestorWithClass(this, RadioGroupTag.class);
	parentName = ancestor.getId();
    } catch ( Exception e ) {
	throw new JspException("Option must be enclosed in a SelectOne_Option tag");
    }
    Assert.assert_it(null != ancestor);
    Assert.assert_it(null != parentName);
    
    // 1. Set up the component
    //
    // by virtue of being inside a RadioGroup there must be a
    // UISelectOne instance under the name.
    uiSelectOne = (UISelectOne) ot.get(pageContext.getRequest(), parentName);
    Assert.assert_it(null != uiSelectOne);
    
    uiSelectOne.setItems(new Vector());
    uiSelectOne.addItem(getValue(), getLabel(), getDescription());

//PENDING(rogerk) not necessary...
    // Add this value to the Collection
//    ancestor.getItems().add(getValue());

    // if it is checked, make sure the model knows about it.
    if (null != getChecked()) {
	uiSelectOne.setSelectedValue(getValue());
    }
    
    // 2. Render the component.
    //
    try {
        uiSelectOne.setRendererType("RadioRenderer");
        uiSelectOne.render(renderContext);
    } catch (java.io.IOException e) {
        throw new JspException("Problem rendering component: "+
            e.getMessage());
    } catch (FacesException f) {
        throw new JspException("Problem rendering component: "+
            f.getMessage());
    }

    return (EVAL_BODY_INCLUDE);
}

    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectManager from ServletContext.
        ObjectManager ot = (ObjectManager)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        RenderContext renderContext =
            (RenderContext)ot.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        // get the UISelectOne that is our component.
        String parentName = null;
        RadioGroupTag ancestor = null;
        try {
            ancestor = (RadioGroupTag)
                findAncestorWithClass(this, RadioGroupTag.class);
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
        UISelectOne uiSelectOne = 
            (UISelectOne) ot.get(pageContext.getRequest(), parentName);
        Assert.assert_it(null != uiSelectOne);

        // Complete the rendering process
        //
        try {
            uiSelectOne.renderComplete(renderContext);
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

        checked = null;
        value = null;
        label = null;
    }


} // end of class SelectOne_RadioTag
