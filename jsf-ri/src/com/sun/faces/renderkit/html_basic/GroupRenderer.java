/*
 * $Id: GroupRenderer.java,v 1.22.32.2 2007/04/27 21:27:44 ofung Exp $
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

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: GroupRenderer.java,v 1.22.32.2 2007/04/27 21:27:44 ofung Exp $
 */
public class GroupRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(GroupRenderer.class);

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

    public GroupRenderer() {
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

    public boolean getRendersChildren() {
        return true;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " +
                      component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        // Render a span around this group if necessary
        String
            style = (String) component.getAttributes().get("style"),
            styleClass = (String) component.getAttributes().get("styleClass");
        ResponseWriter writer = context.getResponseWriter();

        if (spanned(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        // Render our children recursively
        Iterator kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, (UIComponent) kids.next());
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding children " + component.getId());
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        // Close our span element if necessary
        ResponseWriter writer = context.getResponseWriter();
        if (spanned(component)) {
            writer.endElement("span");
        }
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " +
                      component.getId());
        }

    }


    /**
     * <p>Return true if we need to render a span element around this group.
     *
     * @param component <code>UIComponent</code> for this group
     */
    private boolean spanned(UIComponent component) {
        if (shouldWriteIdAttribute(component) ||
            (component.getAttributes().get("style") != null) ||
            (component.getAttributes().get("styleClass") != null)) {
            return (true);
        } else {
            return (false);
        }

    }


}
