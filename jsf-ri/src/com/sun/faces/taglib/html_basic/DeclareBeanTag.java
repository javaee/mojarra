/*
 * $Id: DeclareBeanTag.java,v 1.5 2002/02/26 21:24:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// DeclareBeanTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.ObjectManager;
import java.util.EventListener;
import javax.faces.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.faces.util.Util;

/**
 *
 *  <B>DeclareBeanTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DeclareBeanTag.java,v 1.5 2002/02/26 21:24:48 eburns Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class DeclareBeanTag extends TagSupport
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
    private String id = null;
    private String className = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public DeclareBeanTag()
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

    public int doStartTag() throws JspException{

        // PENDING(visvan) use tagext class to validate attributes.
        // get ObjectManager from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
	Class beanClass;
	
	try {
	    beanClass = Util.loadClass(className);
	    
	}
	catch (ClassNotFoundException e) {
	    throw new JspException("Can't find class for " + 
				     className + ": " + e.getMessage());
	}
	addToScope(beanClass, ot);    

        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param el Listener object to be stored in namescope
     * @param ot ObjectManager Table that stores the components
     */
    public void addToScope(Class beanClass, ObjectManager objectManager) {
        
        if ("request".equals(scope)) {
            objectManager.bind(ObjectManager.RequestScope, id, beanClass); 
        }    
        else if ("session".equals(scope)) {
	    objectManager.bind(ObjectManager.SessionScope, id, beanClass);
        }
        else if ("application".equals(scope)){
	    objectManager.bind(ObjectManager.GlobalScope, id, beanClass);
        } 
    }

    /**
     * Returns the value of "id" attribute
     *
     * @return String value of "id" attribute
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the "id" attribute
     * @param id value of "id" attribute 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the value of scope attribute
     *
     * @return String value of scope attribute
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * Sets the scope attribute
     * @param scope value of scope attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Returns the value of className attribute
     *
     * @return String value of className attribute
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Sets "className" attribute
     * @param className value of "className" attribute
     */
    public void setClassName(String classname) {
        this.className = classname;
    }

   

} // end of class DeclareBeanTag
