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

package renderkits.renderkit.xul;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: GroupRenderer.java,v 1.3 2005/12/14 22:27:41 rlubke Exp $
 */
public class GroupRenderer extends BaseRenderer {

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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Begin encoding component " +
                                    component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        String
              style = (String) component.getAttributes().get("style"),
              styleClass = (String) component.getAttributes().get("styleClass"),
              captionClass =
                    (String) component.getAttributes().get("captionClass"),
              captionLabel =
                    (String) component.getAttributes().get("captionLabel");
        ResponseWriter writer = context.getResponseWriter();

        writer.writeText("\n", null);
        writer.startElement("groupbox", component);
        writeIdAttributeIfNecessary(context, writer, component);
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        if (captionLabel != null) {
            writer.writeText("\n", null);
            writer.startElement("caption", component);
            writer.writeAttribute("label", captionLabel, "captionLabel");
            if (captionClass != null) {
                writer.writeAttribute("class", captionClass, "captionClass");
            }
            writer.endElement("caption");
        }
        writer.writeText("\n", null);
    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding children " + component.getId());
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
        }

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("groupbox");
        writer.writeText("\n", null);

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "End encoding component " +
                                    component.getId());
        }

    }
}
