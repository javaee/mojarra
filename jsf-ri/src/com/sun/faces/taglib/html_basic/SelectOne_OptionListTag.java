/*
 * $Id: SelectOne_OptionListTag.java,v 1.12 2002/01/31 20:38:55 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectOne_OptionListTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.util.Util;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.UISelectOne;
import javax.faces.ObjectManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;
import java.util.Vector;

/**
 *
 *  <B>SelectOne_OptionListTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_OptionListTag.java,v 1.12 2002/01/31 20:38:55 rogerk Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectOne_OptionListTag extends TagSupport
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

    private String id = null;
    private String model = null;
    private String selectedValueModel = null;
    private String valueChangeListener = null;

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public SelectOne_OptionListTag()
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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String label) {
        this.model = label;
    }

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
// Methods called by children tags:
//

//
//  Helper Methods for tag methods
//

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
            String class_name = "com.sun.faces.renderkit.html_basic.OptionListRenderer";
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

    /**
     * Creates a UISelectOne component and sets renderer specific
     * properties.
     *
     * @param rc renderContext client information
     */
protected UISelectOne createComponent(RenderContext renderContext) 
    throws JspException {
    UISelectOne uiSelectOne = new UISelectOne();
    
    // set renderer specific properties
    uiSelectOne.setId(getId());
    try {
        uiSelectOne.addValueChangeListener(valueChangeListener);    
    } catch (FacesException fe) {
        throw new JspException("Listener + " + valueChangeListener +
          " doesn't exist or does not implement valueChangeListener interface");
    }
    // PENDING(edburns): assert that model and selectedValueModel
    // are either both non-null or both null.
    if ( null != model && null != selectedValueModel) {
	uiSelectOne.setModelReference(model);
	uiSelectOne.setSelectedModelReference(selectedValueModel);
    } 
    
    return uiSelectOne;
}

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param c UIComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(UISelectOne c, ObjectManager ot) {
   
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), id, c);
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
    
    UISelectOne uiSelectOne = null;

    // 1. if we don't have an "id" generate one
    //
    if (id == null) {
        String gId = Util.generateId();
        setId(gId);
    }

    // 2. Get or create the component instance.
    //
    uiSelectOne = (UISelectOne) ot.get(pageContext.getRequest(), getId());
    if (uiSelectOne == null) {
        uiSelectOne = createComponent(renderContext);
        addToScope(uiSelectOne, ot);
    } 

    //PENDING(rogerk) clear out internal component list (reset)
    uiSelectOne.setItems(new Vector());

    // 2. Render the component.
    //
    try {
        uiSelectOne.setRendererType("OptionListRenderer");
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
public int doEndTag() throws JspException {
    ObjectManager ot = (ObjectManager) pageContext.getServletContext().
	getAttribute(Constants.REF_OBJECTMANAGER);
    Assert.assert_it( ot != null );
    RenderContext renderContext = 
	(RenderContext)ot.get(pageContext.getSession(),
			      Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );
    
//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
    UISelectOne uiSelectOne = (UISelectOne)ot.get(pageContext.getRequest(), id);
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

        id = null;
        model = null;
        selectedValueModel = null;
        valueChangeListener = null;
    }

} // end of class SelectOne_OptionListTag
