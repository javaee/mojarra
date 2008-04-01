/*
 * $Id: CommandTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CommandTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.Command;
import javax.faces.ObjectTable;
import javax.faces.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>CommandTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CommandTag.java,v 1.2 2001/12/20 22:26:41 ofung Exp $
 * @author Jayashri Visvanathan
 * 
 *
 */

public class CommandTag extends TagSupport
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
    private String onCompletion = null;
    private String onError = null;
    private String className = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public CommandTag()
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
            Command cmd = (Command) ot.get(pageContext.getRequest(), 
                    name);
            if (cmd == null) {
                cmd = createComponent();
                addToScope(cmd, ot);    
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param cmd Command object to be stored in namescope
     * @param ot ObjectTable Table that stores the components
     */
    public void addToScope(Command cmd, ObjectTable ot) {
        
        if ("request".equals(scope)) {
            ot.put(pageContext.getRequest(), name, cmd); 
        }    
        else if ("session".equals(scope)) {
          ot.put(pageContext.getSession(),name, cmd);
        }
        else if ("application".equals(scope)){
             ot.put(ObjectTable.GlobalScope,name, cmd);
        } else {
            ot.put(pageContext.getRequest(), name, cmd);
        }
    }

    /**
     * Creates an event listener object. 
     */
    protected Command createComponent() 
        throws JspException {
        Command cmd_obj=null;
        try {
            Class cmd_class = Class.forName(className);
            Class[] class_params = new Class[2];
            class_params[0] = onCompletion.getClass();
            class_params[1] =  onError.getClass();
            Constructor cmd_cons = cmd_class.getConstructor(class_params);
            Object[] initargs = new Object[2];
            initargs[0] = onCompletion;
            initargs[1] = onError;
            cmd_obj = (Command) cmd_cons.newInstance(initargs);
        } catch (IllegalAccessException e) {
            throw new JspException("Can't create instance for " +
                                 name + ": " + e.getMessage());
        }
        catch (InstantiationException e) {
            throw new JspException("Can't create instance for " +
                                 name + ": " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            throw new JspException("Can't find class for " +
                                 name + ": " + e.getMessage());
        }
        catch (NoSuchMethodException e) {
            throw new JspException("Can't find class for " +
                                 name + ": " + e.getMessage());
        }
        catch (InvocationTargetException e) {
            throw new JspException("Can't find class for " +
                                 name + ": " + e.getMessage());
        }
        return cmd_obj;
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

    /**
     * Returns the value of onError attribute
     *
     * @return String value of onError attribute
     */
    public String getonError() {
        return this.onError;
    }

    /**
     * Sets onError attribute
     * @param className value of onError attribute
     */
    public void setOnError(String on_error) {
        this.onError = on_error;
    }
    
    /**
     * Returns the value of onCompletion attribute
     *
     * @return String value of onCompletion attribute
     */
    public String getOnCompletion() {
        return this.onCompletion;
    }

    /**
     * Sets onCompletion attribute
     * @param onCompletion value of onCompletion attribute
     */
    public void setOnCompletion(String on_completion) {
        this.onCompletion = on_completion;
    }
   

} // end of class CommandTag
