/*
 * $Id: LinkRenderer.java,v 1.12 2005/08/22 22:10:20 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
