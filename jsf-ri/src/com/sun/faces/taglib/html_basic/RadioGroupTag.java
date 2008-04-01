/*
 * $Id: RadioGroupTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioGroupTag.java

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
 *  <B>RadioGroupTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioGroupTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioGroupTag extends TagSupport
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

public RadioGroupTag()
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
    
    return (EVAL_BODY_INCLUDE);
}

    /**
     * End Tag Processing
     */
public int doEndTag() throws JspException{
    ObjectTable ot = (ObjectTable) pageContext.getServletContext().
	getAttribute(Constants.REF_OBJECTTABLE);
    Assert.assert_it( ot != null );
    RenderContext renderContext = 
	(RenderContext)ot.get(pageContext.getSession(),
			      Constants.REF_RENDERCONTEXT);
    Assert.assert_it( renderContext != null );
    
    WSelectOne wSelectOne = (WSelectOne)ot.get(pageContext.getRequest(), name);
    Assert.assert_it(null != wSelectOne);
    
    // The magic method: setting the collection into the component
    wSelectOne.setItems(renderContext, items);
    
    return EVAL_PAGE;
}

} // end of class RadioGroupTag
