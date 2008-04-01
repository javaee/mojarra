/*
 * $Id: ListenerTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ListenerTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.ObjectTable;
import java.util.EventListener;
import javax.faces.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>ListenerTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ListenerTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class ListenerTag extends TagSupport
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
    private String scope = null;
    private String className = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public ListenerTag()
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
        // get ObjectTable from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        
        if ( name != null ) {
            EventListener el = (EventListener) ot.get(pageContext.getRequest(), 
                    name);
            if (el == null) {
                el = createComponent();
                addToScope(el, ot);    
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param el Listener object to be stored in namescope
     * @param ot ObjectTable Table that stores the components
     */
    public void addToScope(EventListener el, ObjectTable ot) {
        
        if ("request".equals(scope)) {
            ot.put(pageContext.getRequest(), name, el); 
        }    
        else if ("session".equals(scope)) {
          ot.put(pageContext.getSession(),name, el);
        }
        else if ("application".equals(scope)){
             ot.put(ObjectTable.GlobalScope,name, el);
        } else {
            ot.put(pageContext.getRequest(),name, el);
        }
    }

    /**
     * Creates an event listener object. 
     */
    protected EventListener createComponent() 
        throws JspException {
        EventListener lis_obj = null;
        try {
            Class lis_class = Class.forName(className);
            lis_obj = (EventListener) lis_class.newInstance();
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
        return lis_obj;
    }

    /**
     * Returns the value of "name" attribute
     *
     * @return String value of "name" attribute
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the "name" attribute
     * @param name value of "name" attribute 
     */
    public void setName(String name) {
        this.name = name;
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

   

} // end of class ListenerTag
