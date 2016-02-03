/*
 * $Id: TextRenderer.java,v 1.9.30.2 2007/04/27 21:27:58 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TextRenderer.java

package com.sun.faces.systest.render;

import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;

/**
 * <B>TextRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextRenderer.java,v 1.9.30.2 2007/04/27 21:27:58 ofung Exp $
 * @see	Blah
 * @see	Bloo
 */

public class TextRenderer extends Renderer {

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


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TextRenderer() {
        super();
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

    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);

        String styleClass = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }

        writer.writeText("This IS TEXT FROM THE CUSTOM RENDERER", null);
    }


    public String convertClientId(FacesContext context, String clientId) {
        return clientId;
    }

    /* Replaced by convertClientId() above
    public String getClientId(FacesContext context, UIComponent component){
	String clientId = null;
	NamingContainer closestContainer = null;
	UIComponent containerComponent = component;

        // Search for an ancestor that is a naming container
        while (null != (containerComponent = 
                        containerComponent.getParent())) {
            if (containerComponent instanceof NamingContainer) {
                closestContainer = (NamingContainer) containerComponent;
                break;
            }
        }

        // If none is found, see if this is a naming container
        if (null == closestContainer && component instanceof NamingContainer) {
            closestContainer = (NamingContainer) component;
        }

        if (null != closestContainer) {

            // If there is no componentId, generate one and store it
            if (component.getId() == null) {
                // Don't call setId() because it checks for
                // uniqueness.  No need.
                clientId = closestContainer.generateClientId();
            } else {
                clientId = component.getId();
            }

            // build the client side id
            containerComponent = (UIComponent) closestContainer;

            // If this is the root naming container, break
            if (null != containerComponent.getParent()) {
                clientId = containerComponent.getClientId(context) +
                    NamingContainer.SEPARATOR_CHAR + clientId;
            }
        }

        if (null == clientId) {
	    throw new NullPointerException();
	}
	return (clientId);
    }
    */

} // end of class TextRenderer
