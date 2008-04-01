/*
 * $Id: RadioGroupTag.java,v 1.11 2002/01/31 20:38:55 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioGroupTag.java

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
import java.util.HashSet;

/**
 *
 *  <B>RadioGroupTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioGroupTag.java,v 1.11 2002/01/31 20:38:55 rogerk Exp $
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

    private String id = null;
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
               " does not exist or does not implement valueChangeListener " + 
               " interface" );
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
    
    UISelectOne uiSelectOne = (UISelectOne)ot.get(pageContext.getRequest(), id);
    Assert.assert_it(null != uiSelectOne);
    
//PENDING(rogerk) is this really necessary???????
    // The magic method: setting the collection into the component
//    uiSelectOne.setItems(renderContext, items);
    
    return EVAL_PAGE;
}

} // end of class RadioGroupTag
