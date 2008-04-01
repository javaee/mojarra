/*
 * $Id: FacesTag.java,v 1.4 2002/03/08 00:24:50 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTag.java

package com.sun.faces.taglib;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import javax.faces.UIComponent;
import javax.faces.ObjectManager;
import javax.faces.Constants;
import javax.faces.RenderContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.taglib.html_basic.FormTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.4 2002/03/08 00:24:50 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public abstract class FacesTag extends TagSupport
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

    private String scope = null;
    private String model = null;

// Relationship Instance Variables

    protected RenderContext renderContext = null;
    protected ObjectManager objectManager = null;
//
// Constructors and Initializers    
//

public FacesTag()
{
    super();
}

//
// Class methods
//

// 
// Accessors
//

    /**
     * Returns the value of the scope attribute
     *
     * @return String value of scope attribute
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * Sets scope attribute
     * @param scope value of scope attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Returns the value of the model attribute
     *
     * @return String value of model attribute
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Sets the model attribute
     * @param model value of model attribute
     */
    public void setModel(String model) {
        this.model = model;
    }

//
// General Methods
//

    /** Adds the component to the ObjectManager in the appropriate scope
     *
     * @param c UIComponent to be stored in namescope
     * @param objectManager Object pool
     */
    public void addToScope(UIComponent c, ObjectManager objectManager) {
   
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        objectManager.put(pageContext.getSession(), getId(), c);
    }
    
    public void addToParent(UIComponent c, ObjectManager objectManager ) {
        // get the UIForm component which is the parent
        // of this component.
        FormTag ancestor = null;
        try {
            ancestor = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
            String formId  = ancestor.getId();
            UIComponent parentForm = (UIComponent) objectManager.get(pageContext.getRequest(),
                    formId);
            Assert.assert_it ( parentForm != null );
            parentForm.add(c);
        } catch ( Exception e ) {
            // If form tag cannot be found then model is null
        }
    }   

// 
// Methods to be overridden by subclass
//

/**

 * @return a new UIComponent subclass specific to the tag.  For example,
 * in Command_ButtonTag, we return a UICommand.

*/


public abstract UIComponent newComponentInstance();

/**

 * @return called at the end of doStartTag().

*/

public int getStartCode() {
    return EVAL_BODY_INCLUDE;
}

/**

 * @return called at the end of doEndTag().

*/

public int getEndCode() {
    return EVAL_PAGE;
}

/**

* Set any tag specific component attributes here.

*/

public void setAttributes(UIComponent comp) {}

    /**

    * @return the String for this renderer type, or null, if this tag
    * does not have a renderer.

    */

public abstract String getRendererType();

/**

* Default impl does nothing

*/

public void addListeners(UIComponent comp) throws JspException {}

public void addValidators(UIComponent comp) throws JspException {}

//
// Methods from TagSupport
// 

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
	String rendererType = null;
        objectManager = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
	
        renderContext = (RenderContext)objectManager.get(pageContext.getSession(),
							 Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );
        
        UIComponent uiComponent = null;

        // 1. if we don't have an "id" generate one
        //
        if (getId() == null) {
            String gId = Util.generateId();
            setId(gId);
        }

        // 2. Get or create the component instance.
        //
        uiComponent = (UIComponent) objectManager.get(pageContext.getRequest(),
						      getId());
        if ( uiComponent == null ) {
            uiComponent = newComponentInstance();
            // Id should be set before adding the component to the tree.
            uiComponent.setId(getId());
            addToScope(uiComponent, objectManager);
            addToParent(uiComponent, objectManager);
            // listeners and validators should be added only at the time the 
            // component is created.
            addListeners(uiComponent);
            addValidators(uiComponent);
         }
        
         // Call subclass methods
         // PENDING ( visvan ) attributes should be set only the first except
         // for optionList and Radio Tags ??
	 setAttributes(uiComponent);
         
	 // 3. Render the component, if it has a renderer
        //
	if (null != (rendererType = getRendererType())) {
	    try {
		uiComponent.setRendererType(rendererType);
		uiComponent.render(renderContext);
	    } catch (java.io.IOException e) {
		throw new JspException("Problem rendering component: "+
				       e.getMessage());
	    } catch (FacesException f) {
		throw new JspException("Problem rendering component: "+
				       f.getMessage());
	    }
	}
	renderContext = null;
        return (getStartCode());
    }
    
    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException {
	String rendererType = null;
        // get ObjectManager from ServletContext.
        ObjectManager objectManager = (ObjectManager)pageContext.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
        renderContext = 
            (RenderContext)objectManager.get(pageContext.getSession(),
					     Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );
	
	//PENDING(rogerk)can we eliminate this extra get if wCommand is
	//instance variable? If so, threading issue?

        UIComponent wComponent = (UIComponent) objectManager.get(pageContext.getRequest(), 
								 getId());
        Assert.assert_it( wComponent != null );

        // Complete the rendering process
        //
	if (null != (rendererType = getRendererType())) {
	    try {
		wComponent.renderComplete(renderContext);
	    } catch (java.io.IOException e) {
		throw new JspException("Problem completing rendering: "+
				       e.getMessage());
	    } catch (FacesException f) {
		throw new JspException("Problem completing rendering: "+
				       f.getMessage());
	    }
	}
	renderContext = null;
        return getEndCode();
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        setId(null);
        setScope(null);
    }

} // end of class FacesTag
