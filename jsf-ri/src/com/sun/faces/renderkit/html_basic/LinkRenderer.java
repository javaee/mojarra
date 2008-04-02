/*
 * $Id: LinkRenderer.java,v 1.11 2005/05/16 20:16:28 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;


/**
 * <B>LinkRenderer</B> acts as superclass for CommandLinkRenderer and
 * OutputLinkRenderer.
 */

public class LinkRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Separator character    


    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    protected CommandLinkRenderer commandLinkRenderer = null;

    protected OutputLinkRenderer outputLinkRenderer = null;

    //
    // Constructors and Initializers
    //

    public LinkRenderer() {
        commandLinkRenderer = new CommandLinkRenderer();
        outputLinkRenderer = new OutputLinkRenderer();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean getRendersChildren() {
        return true;
    }


    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (component instanceof UICommand) {
            commandLinkRenderer.decode(context, component);
        } else if (component instanceof UIOutput) {
            outputLinkRenderer.decode(context, component);
        }
        return;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (component instanceof UICommand) {
            commandLinkRenderer.encodeBegin(context, component);
        } else if (component instanceof UIOutput) {
            outputLinkRenderer.encodeBegin(context, component);
        }
        return;
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (component instanceof UICommand) {
            commandLinkRenderer.encodeEnd(context, component);
        } else if (component instanceof UIOutput) {
            outputLinkRenderer.encodeEnd(context, component);
        }

        return;
    }

} // end of class LinkRenderer
