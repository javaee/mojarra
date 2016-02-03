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

// LineRenderer.java

package renderkits.renderkit.svg;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.logging.Level;

/**
 * <B>LineRenderer</B> is a class that renders an <code>SVG</code>
 * Line.
 */

public class LineRenderer extends BaseRenderer {

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
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component is null");
        }
    }

    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException(
                  "'context' and/or 'component' is null");
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Begin encoding component " +
                                    component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "End encoding component "
                                        +
                                        component.getId()
                                        + " since rendered attribute "
                                        +
                                        "is set to false ");
            }
            return;
        }

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("line", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String x1 = (String) component.getAttributes().get("x1");
        if (x1 != null) {
            writer.writeAttribute("x1", x1, "x1");
        }
        String y1 = (String) component.getAttributes().get("y1");
        if (y1 != null) {
            writer.writeAttribute("y1", y1, "y1");
        }
        String x2 = (String) component.getAttributes().get("x2");
        if (x2 != null) {
            writer.writeAttribute("x2", x2, "x2");
        }
        String y2 = (String) component.getAttributes().get("y2");
        if (y2 != null) {
            writer.writeAttribute("y2", y2, "y2");
        }
        String style = (String) component.getAttributes().get("style");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeText("\n    ", null);
    }

    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException(
                  "'context' and/or 'component' is null");
        }
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("line");
        writer.writeText("\n", null);
    }

    //
    // General Methods
    //

} // end of class LineRenderer
