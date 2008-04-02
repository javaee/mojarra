/*
 * $Id: SubviewTag.java,v 1.9 2006/03/29 22:38:42 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;

import java.io.IOException;

import com.sun.faces.application.ViewHandlerResponseWrapper;

public class SubviewTag extends UIComponentELTag {

    // ------------------------------------------------------------ Constructors


    public SubviewTag() {

        super();

    }

    // ---------------------------------------------------------- Public Methods


    public String getComponentType() {

        return "javax.faces.NamingContainer";

    }


    public String getRendererType() {

        return null;

    }

    // ------------------------------------------------------- Protected Methods


    protected UIComponent createVerbatimComponentFromBodyContent() {

        UIOutput verbatim = (UIOutput)
              super.createVerbatimComponentFromBodyContent();
        String value = null;

        Object response = getFacesContext().getExternalContext().getResponse();
        if (response instanceof ViewHandlerResponseWrapper) {
            ViewHandlerResponseWrapper wrapped =
                  (ViewHandlerResponseWrapper) response;
            try {
                if (wrapped.isBytes()) {
                    wrapped.flushContentToWrappedResponse();
                } else if (wrapped.isChars()) {
                    char [] chars = wrapped.getChars();
                    if (null != chars && 0 < chars.length) {
                        if (null != verbatim) {
                            value = (String) verbatim.getValue();
                        }
                        verbatim = super.createVerbatimComponent();
                        if (null != value) {
                            verbatim.setValue(value + new String(chars));
                        } else {
                            verbatim.setValue(new String(chars));
                        }
                    }
                }
                wrapped.clearWrappedResponse();
            } catch (IOException e) {
                throw new FacesException(new JspException(
                      "Can't write content above <f:view> tag"
                      + " " + e.getMessage()));
            }
        }

        return verbatim;

    }

}
