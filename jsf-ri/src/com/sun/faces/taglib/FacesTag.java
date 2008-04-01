/*
 * $Id: FacesTag.java,v 1.5 2002/03/13 18:04:24 eburns Exp $
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
import javax.faces.TreeNavigator;

import com.sun.faces.util.Util;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: FacesTag.java,v 1.5 2002/03/13 18:04:24 eburns Exp $
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

// PENDING(edburns): not sure if it is safe to have ivars like this

    protected RenderContext renderContext = null;

    /** The UIComponent mapped to this tag
     */
    
    protected UIComponent uiComponent = null;

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

    /**
 
    * This is overridden by subclasses that need to get their component
    * from somewhere else.  For example, SelectOne_RadioTag gets its
    * component from its parent.  Same for SelectOne_Option.

    */ 

    public UIComponent getComponentForTag() {
	return uiComponent;
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

/**

* The next two methods can be overridden by FacesTag subclasses that
* don't want to change the state of the TreeNavigator by calling
* getNextStart() or getNextEnd().  An example of this is the tags
* representing values for a UISelectOne's collection.

*/

public UIComponent getComponentFromStart(ObjectManager objectManager) {
    UIComponent result = null;
    TreeNavigator treeNav = null;
    treeNav = (TreeNavigator)objectManager.get(renderContext.getRequest(), 
					       Constants.REF_TREENAVIGATOR);
    Assert.assert_it(null!= treeNav);
    
    result = treeNav.getNextStart();
    Assert.assert_it(null != result);
    return result;
}

public UIComponent getComponentFromEnd(ObjectManager objectManager) {
    UIComponent result = null;
    TreeNavigator treeNav = null;
    treeNav = (TreeNavigator)objectManager.get(renderContext.getRequest(), 
					       Constants.REF_TREENAVIGATOR);
    Assert.assert_it(null!= treeNav);
    
    result = treeNav.getNextEnd();
    Assert.assert_it(null != result);
    return result;
}

//
// Methods from TagSupport
// 

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
	String rendererType = null;
        ObjectManager objectManager = null;

	objectManager = (ObjectManager) pageContext.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
	
        renderContext = 
	    (RenderContext)objectManager.get(pageContext.getSession(),
					     Constants.REF_RENDERCONTEXT);
        Assert.assert_it(null != renderContext);
        
        uiComponent = getComponentFromStart(objectManager);

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
        ObjectManager objectManager = null;

	objectManager = (ObjectManager) pageContext.getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
	
        renderContext = 
	    (RenderContext)objectManager.get(pageContext.getSession(),
					     Constants.REF_RENDERCONTEXT);
        Assert.assert_it(null != renderContext);
        
        uiComponent = getComponentFromEnd(objectManager);

        // Complete the rendering process
        //
	if (null != (rendererType = getRendererType())) {
	    try {
		uiComponent.renderComplete(renderContext);
	    } catch (java.io.IOException e) {
		throw new JspException("Problem completing rendering: "+
				       e.getMessage());
	    } catch (FacesException f) {
		throw new JspException("Problem completing rendering: "+
				       f.getMessage());
	    }
	}
	renderContext = null;
	uiComponent = null;
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
