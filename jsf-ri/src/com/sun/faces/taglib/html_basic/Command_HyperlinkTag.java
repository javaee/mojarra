/*
 * $Id: Command_HyperlinkTag.java,v 1.16 2002/02/05 18:57:03 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Command_HyperlinkTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.UICommand;
import javax.faces.UIComponent;

import javax.servlet.jsp.JspException;

/**
 *
 *  <B>Command_HyperlinkTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Command_HyperlinkTag.java,v 1.16 2002/02/05 18:57:03 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Command_HyperlinkTag extends FacesTag
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

    private String target = null;
    private String image = null;
    private String text = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Command_HyperlinkTag() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//
// Methods from FacesTag
//

    public UIComponent newComponentInstance() {
	return new UICommand();
    }

    public void setAttributes(UIComponent comp) {
	ParameterCheck.nonNull(comp);
        comp.setAttribute("target", getTarget());
        comp.setAttribute("image", getImage());
        comp.setAttribute("text", getText());

    }

    public String getRendererType() {
	return "HyperlinkRenderer";
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        target = null;
        image = null;
        text = null;
    }


} // end of class Command_HyperlinkTag
