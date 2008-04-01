/*
 * $Id: NavigationMapTag.java,v 1.2 2002/02/26 21:24:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// NavigationMapTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.NavigationMap;
import javax.faces.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.faces.util.Util;

/**
 *
 *  <B>NavigationMapTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: NavigationMapTag.java,v 1.2 2002/02/26 21:24:48 eburns Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class NavigationMapTag extends TagSupport
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
    private String scope = null;
    private String className = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public NavigationMapTag()
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
        
        if ( id != null ) {
            NavigationMap navigationMap = (NavigationMap) 
                    ot.get(pageContext.getRequest(), id);
            if ( navigationMap == null) {
                navigationMap = createComponent();
                addToScope(navigationMap, ot);    
            }    
        }
        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the navigationMap object to the ObjectManager
     * in the appropriate scope
     *
     * @param navMap navigationMap object to be stored in namescope
     * @param ot ObjectManager Table that stores the components
     */
    public void addToScope(NavigationMap navMap, ObjectManager objectManager) {
        
        if ("request".equals(scope)) {
            objectManager.put(pageContext.getRequest(), id, navMap); 
        }    
        else if ("session".equals(scope)) {
          objectManager.put(pageContext.getSession(),id, navMap);
        }
        else if ("application".equals(scope)){
             objectManager.put(pageContext.getServletContext(),id, navMap);
        } else {
            // PENDING ( visvan )
            // currently all components are in stored in session scope.
            // later we should change this to default scope which is request.
            objectManager.put(pageContext.getSession(),id, navMap);
        }
    }

    /**
     * Creates an NavigationMap object. 
     */
    protected NavigationMap createComponent() 
        throws JspException {
            
        NavigationMap navMap_obj = null;
        if ( className == null ) {
            className = "com.sun.faces.NavigationMapImpl";
        }    
        try {
            Class navMap_class = Util.loadClass(className);
            navMap_obj = (NavigationMap) navMap_class.newInstance();
        }catch (IllegalAccessException iae) {
            throw new JspException("Can't create instance for " +
                                 className + ": " + iae.getMessage());
        }
        catch (InstantiationException ie) {
            throw new JspException("Can't create instance for " +
                                 className + ": " + ie.getMessage());
        }
        catch (ClassNotFoundException cfe) {
             throw new JspException("Can't find class for " +
                                 className + ": " + cfe.getMessage());
        }
        return navMap_obj;
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

} // end of class NavigationMapTag
