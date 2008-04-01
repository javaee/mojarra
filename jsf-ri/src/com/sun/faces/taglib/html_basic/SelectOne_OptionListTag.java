/*
 * $Id: SelectOne_OptionListTag.java,v 1.2 2001/12/13 00:15:59 rogerk Exp $
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

// SelectOne_OptionListTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WSelectOne;
import javax.faces.ObjectTable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

/**
 *
 *  <B>SelectOne_OptionListTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOne_OptionListTag.java,v 1.2 2001/12/13 00:15:59 rogerk Exp $
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

    private String name = null;
    private String model = null;
    private String selectedValueModel = null;
    private String valueChangeListener = null;

// Relationship Instance Variables

    private Collection items;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

protected Collection getItems() {
    if (null == items) {
	items = new HashSet();
    }
    return items;
}

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
     * Creates a WSelectOne component and sets renderer specific
     * properties.
     *
     * @param rc renderContext client information
     */
protected WSelectOne createComponent(RenderContext renderContext) 
    throws JspException {
    WSelectOne wSelectOne = new WSelectOne();
    
    // set renderer specific properties
    wSelectOne.setAttribute(renderContext, "name", getName());
    // PENDING(edburns): assert that model and selectedValueModel
    // are either both non-null or both null.
    if ( null != model && null != selectedValueModel) {
	wSelectOne.setModel(model);
	wSelectOne.setSelectedValueModel(selectedValueModel);
    } 
    
    return wSelectOne;
}

    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param c WComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(WSelectOne c, ObjectTable ot) {
   
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), name, c);

        if ( valueChangeListener != null ) {
            String lis_name = name.concat(Constants.REF_VALUECHANGELISTENERS);
            Vector listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectTable. We do this
            // because if the listeners are stored in the components, then
            // they have to exist for the event listeners to be dispatched
            // at the time we process the events.
            // According to the spec, listeners should be dispatched
            // independent of components.
            listeners.add(valueChangeListener);
            ot.put(pageContext.getSession(),lis_name, listeners);
        }
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
    
    // 1. Get or create the component instance.
    //
    WSelectOne wSelectOne = (WSelectOne) 
	ot.get(pageContext.getRequest(), name);
    if (wSelectOne == null) {
	wSelectOne = createComponent(renderContext);
	addToScope(wSelectOne, ot);
    }

    // 2. Render the component.
    //
    try {
        wSelectOne.setRendererName(renderContext,
            "OptionListRenderer");
        wSelectOne.render(renderContext);
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
    ObjectTable ot = (ObjectTable) pageContext.getServletContext().
	getAttribute(Constants.REF_OBJECTTABLE);
    Assert.assert_it( ot != null );
    RenderContext renderContext = 
	(RenderContext)ot.get(pageContext.getSession(),
			      Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );
    
//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
    WSelectOne wSelectOne = (WSelectOne)ot.get(pageContext.getRequest(), name);
    Assert.assert_it(null != wSelectOne);
    
    // The magic method: setting the collection into the component
    wSelectOne.setItems(renderContext, items);

    // Complete the rendering process
    //
    try {
//PENDING(rogerk)we need to reset the renderer name for WSelectOne, becuase
//it is a tag has enclosing component of the same type (WSelectOne) - 
//sharing same attribute list (renderer name is set in attributeList of
//WComponent.
//
        wSelectOne.setRendererName(renderContext,
            "OptionListRenderer");
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

        name = null;
        model = null;
        selectedValueModel = null;
        valueChangeListener = null;
    }

} // end of class SelectOne_OptionListTag
