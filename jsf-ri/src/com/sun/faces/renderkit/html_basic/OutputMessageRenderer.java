/*
 * $Id: OutputMessageRenderer.java,v 1.15.6.1.2.2 2007/04/27 21:27:46 ofung Exp $
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

// OutputMessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * <B>OutputMessageRenderer</B> is a class that renderes UIOutput
 */

public class OutputMessageRenderer extends HtmlBasicRenderer {

    // Log instance for this class
    protected static final Log log = LogFactory.getLog(
        OutputMessageRenderer.class);

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

    public OutputMessageRenderer() {
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

    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessageString(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
        String
            currentValue = null,
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass");

        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since rendered attribute is set to false");
            }
            return;
        }
        Object currentObj = ((ValueHolder) component).getValue();
        if (currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String) currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        } else {
            // if the value is null, do not output anything.
            return;
        }

        ArrayList parameterList = new ArrayList();

        // get UIParameter children...

        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            //PENDING(rogerk) ignore if child is not UIParameter?

            if (!(kid instanceof UIParameter)) {
                continue;
            }

            parameterList.add(((UIParameter) kid).getValue());
        }

        // If at least one substitution parameter was specified,
        // use the string as a MessageFormat instance.
        String message = null;
        if (parameterList.size() > 0) {
            message = MessageFormat.format
                (currentValue, parameterList.toArray
                               (new Object[parameterList.size()]));
        } else {
            message = currentValue;
        }

        boolean wroteSpan = false;
        if (null != styleClass || null != style ||
            Util.hasPassThruAttributes(component) ||
            shouldWriteIdAttribute(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            wroteSpan = true;

            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            // style is rendered as a passthru attribute
            Util.renderPassThruAttributes(writer, component);
            Util.renderBooleanPassThruAttributes(writer, component);
        }
        Boolean escape = Boolean.TRUE;
        Object val = component.getAttributes().get("escape");
        if (val != null) {
            if (val instanceof Boolean) {
                escape = (Boolean) val;
            } else if (val instanceof String) {
                try {
                    escape = Boolean.valueOf((String) val);
                } catch (Throwable e) {
                }
            }
        }
        if (escape.booleanValue()) {
            writer.writeText(message, "value");
        } else {
            writer.write(message);
        }
        if (wroteSpan) {
            writer.endElement("span");
        }
    }

} // end of class OutputMessageRenderer
