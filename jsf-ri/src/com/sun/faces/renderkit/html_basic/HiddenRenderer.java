/*
 * $Id: HiddenRenderer.java,v 1.26 2006/03/29 22:38:36 rlubke Exp $
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

// HiddenRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;

import com.sun.faces.util.MessageUtils;

/**
 * <B>HiddenRenderer</B> is a class that renders the current value of
 * <code>UIInput<code> component as a HTML hidden variable.
 */
public class HiddenRenderer extends HtmlBasicInputRenderer {

    // ------------------------------------------------------------ Constructors


    public HiddenRenderer() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    // ------------------------------------------------------- Protected Methods


    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "hidden", "type");
        String clientId = component.getClientId(context);
        writer.writeAttribute("name", clientId, "clientId");

        // render default text specified
        if (currentValue != null) {
            writer.writeAttribute("value", currentValue, "value");
        }
        writer.endElement("input");

    }

    // The testcase for this class is TestRenderers_3.java 

} // end of class HiddenRenderer


