/*
 * $Id: Command_ButtonTag.java,v 1.20 2002/02/05 18:57:03 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_ButtonTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.UICommand;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>Command_ButtonTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_ButtonTag.java,v 1.20 2002/02/05 18:57:03 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_ButtonTag extends FacesTag {
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
    private String label = null;
    private String commandName = null;
    private String commandListener = null;
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_ButtonTag()
    {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
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

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
	return new UICommand();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
        comp.setAttribute("image", getImage());
        comp.setAttribute("label", getLabel());
    }

    public String getRendererType() {
	return "ButtonRenderer";
    }

    public void addListeners(UIComponent comp) throws JspException {
	ParameterCheck.nonNull(comp);
	Assert.assert_it(comp instanceof UICommand);
	try {
            ((UICommand)comp).addCommandListener(commandListener);    
        } catch (FacesException fe) {
            throw new JspException("Listener " + commandListener +
				   " does not exist or does not implement " +
				   "commandListener interface" );
        }
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        image = null;
        label = null;
        commandName = null;
        commandListener = null;
    }
 
} // end of class Command_ButtonTag
