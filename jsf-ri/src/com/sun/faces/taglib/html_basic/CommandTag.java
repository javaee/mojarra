/*
 * $Id: CommandTag.java,v 1.4 2002/01/16 21:06:34 rogerk Exp $
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
import javax.faces.ObjectManager;
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
 * @version $Id: CommandTag.java,v 1.4 2002/01/16 21:06:34 rogerk Exp $
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
    private String id = null;
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
        // get ObjectManager from ServletContext.
        Assert.assert_it( pageContext != null );
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        
        if ( id != null ) {
            Command cmd = (Command) ot.get(pageContext.getRequest(), 
                    id);
            if (cmd == null) {
                cmd = createComponent();
                addToScope(cmd, ot);    
            }
        }
        return(EVAL_BODY_INCLUDE);
    }

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param cmd Command object to be stored in namescope
     * @param ot ObjectManager Table that stores the components
     */
    public void addToScope(Command cmd, ObjectManager objectManager) {
        
        if ("request".equals(scope)) {
            objectManager.put(pageContext.getRequest(), id, cmd); 
        }    
        else if ("session".equals(scope)) {
          objectManager.put(pageContext.getSession(),id, cmd);
        }
        else if ("application".equals(scope)){
             objectManager.put(ObjectManager.GlobalScope,id, cmd);
        } else {
            objectManager.put(pageContext.getRequest(), id, cmd);
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
                                 id + ": " + e.getMessage());
        }
        catch (InstantiationException e) {
            throw new JspException("Can't create instance for " +
                                 id + ": " + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            throw new JspException("Can't find class for " +
                                 id + ": " + e.getMessage());
        }
        catch (NoSuchMethodException e) {
            throw new JspException("Can't find class for " +
                                 id + ": " + e.getMessage());
        }
        catch (InvocationTargetException e) {
            throw new JspException("Can't find class for " +
                                 id + ": " + e.getMessage());
        }
        return cmd_obj;
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
