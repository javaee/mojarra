/*
 * $Id: Command_ButtonTag.java,v 1.10 2001/12/08 00:33:52 rogerk Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
import javax.faces.ObjectTable;

import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>Command_ButtonTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_ButtonTag.java,v 1.10 2001/12/08 00:33:52 rogerk Exp $
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
        ObjectTable ot = (ObjectTable) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it( ot != null );
        RenderContext renderContext = 
            (RenderContext)ot.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        if (name != null) {

            // 1. Get or create the component instance.
            //
            WCommand wCommand = 
                (WCommand) ot.get(pageContext.getRequest(), name);
            if ( wCommand == null ) {
                wCommand = new WCommand();
                addToScope(wCommand, ot);
            }
            wCommand.setAttribute(renderContext, "name", getName());
            wCommand.setAttribute(renderContext, "image", getImage());
            wCommand.setAttribute(renderContext, "label", getLabel());
            
            // 2. Get a RenderKit and associated Renderer for this
            //    component.
            //
            RenderKit renderKit = renderContext.getRenderKit();
            if (renderKit == null) {
                throw new JspException("Can't determine RenderKit!");
            }

            Renderer renderer = null;
            try {
                renderer = renderKit.getRenderer(
                    "com.sun.faces.renderkit.html_basic.ButtonRenderer");
            } catch (FacesException e) {
                throw new JspException(
                    "FacesException!!! " + e.getMessage());
            } 
             
            if (renderer == null) {
                throw new JspException(
                    "Could not determine 'renderer' for component");
            }
            
            // 3. Render the component. (Push the component on
            //    the render stack first).
            //
            try {
                renderContext.pushChild(wCommand);
                renderer.renderStart(renderContext, wCommand);  
//PENDING(rogerk) complet/pop should be done in doEndTag
//
                renderer.renderComplete(renderContext, wCommand);
                renderContext.popChild();
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

        return EVAL_PAGE;
    }

    /** Adds the component and listener to the ObjectTable
     * in the appropriate scope
     *
     * @param c WComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(WCommand c, ObjectTable ot) {
   
        Vector listeners = null; 
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), name, c);
  
        if ( commandListener != null ) {
            String lis_name = name.concat(Constants.REF_COMMANDLISTENERS);
            listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }    
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectTable.
            listeners.add(commandListener);
            ot.put(pageContext.getSession(),lis_name, listeners);
        }

        if ( command != null ) {
            // put the "Command" listener in the objectTable
            String cmd_name = name.concat(Constants.REF_COMMAND);
            String cmd = (String) ot.get(pageContext.getRequest(), cmd_name);
            if ( cmd == null) {
                ot.put(pageContext.getSession(),cmd_name, command);
            }
        }
    }

    
 
} // end of class Command_ButtonTag
