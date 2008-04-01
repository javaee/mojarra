/*
 * $Id: Command_ButtonTag.java,v 1.13 2002/01/10 22:20:11 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_ButtonTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.WCommand;
import javax.faces.WForm;
import javax.faces.ObjectManager;

import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>Command_ButtonTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_ButtonTag.java,v 1.13 2002/01/10 22:20:11 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_ButtonTag extends TagSupport {
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

    private String image = null;
    private String name = null;
    private String label = null;
    private String commandName = null;
    private String scope = null;
    private String commandListener = null;
    private String command = null;
 
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_ButtonTag()
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
        this.name= name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Returns the value of commandListener attribute
     *
     * @return String value of commandListener attribute
     */
    public String getCommandListener() {
        return this.commandListener;
    }

    /**
     * Sets commandListener attribute
     * @param command_listener value of commandListener attribute
     */
    public void setCommandListener(String command_listener) {
        this.commandListener = command_listener;
    }

    /**
     * Returns the value of commandListener attribute
     *
     * @return String value of commandListener attribute
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Sets command attribute
     * @param command value of command attribute
     */
    public void setCommand(String command) {
        this.command = command;
    }

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
     * Returns the value of the commandName attribute
     *
     * @return String value of commandName attribute
     */
    public String getCommandName() {
        return this.commandName;
    }

    /**
     * Sets commandName attribute
     * @param cmd_name value of commandName attribute
     */
    public void setCommandName(String cmd_name) {
        this.commandName = cmd_name;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Assert.assert_it( pageContext != null );
        ObjectManager objectManager = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
        RenderContext renderContext = 
            (RenderContext)objectManager.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        if (name != null) {

            // 1. Get or create the component instance.
            //
            WCommand wCommand = 
                (WCommand) objectManager.get(pageContext.getRequest(), name);
            if ( wCommand == null ) {
                wCommand = new WCommand();
                addToScope(wCommand, objectManager);
            }
            wCommand.setAttribute(renderContext, "name", getName());
            wCommand.setAttribute(renderContext, "image", getImage());
            wCommand.setAttribute(renderContext, "label", getLabel());
            
            // 2. Render the component.
            //
            try {
                wCommand.setRendererName(renderContext, "ButtonRenderer");
                wCommand.render(renderContext);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering component: "+
                    e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }
        }
         
        return (EVAL_BODY_INCLUDE);
    }
    
    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectManager from ServletContext.
        ObjectManager objectManager = (ObjectManager)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( objectManager != null );
        RenderContext renderContext = 
            (RenderContext)objectManager.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

//PENDING(rogerk)can we eliminate this extra get if wCommand is instance
//variable? If so, threading issue?
//
        WCommand wCommand = (WCommand) objectManager.get(pageContext.getRequest(), name);
        Assert.assert_it( wCommand != null );

        // Complete the rendering process
        //
        try {
            wCommand.renderComplete(renderContext);
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

        image = null;
        name = null;
        label = null;
        commandName = null;
        scope = null;
        commandListener = null;
        command = null;
    }

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param c WComponent to be stored in namescope
     * @param objectManager Object pool
     */
    public void addToScope(WCommand c, ObjectManager objectManager) {
   
        Vector listeners = null; 
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        objectManager.put(pageContext.getSession(), name, c);
  
        if ( commandListener != null ) {
            String lis_name = name.concat(Constants.REF_COMMANDLISTENERS);
            listeners = (Vector) objectManager.get(pageContext.getRequest(), 
						   lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }    
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectManager.
            listeners.add(commandListener);
            objectManager.put(pageContext.getSession(),lis_name, listeners);
        }

        if ( command != null ) {
            // put the "Command" listener in the objectManager
            String cmd_name = name.concat(Constants.REF_COMMAND);
            String cmd = (String) objectManager.get(pageContext.getRequest(), 
						    cmd_name);
            if ( cmd == null) {
                objectManager.put(pageContext.getSession(),cmd_name, command);
            }
        }
    }

    
 
} // end of class Command_ButtonTag
