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

// ButtonRenderer.java

package renderkits.renderkit.svg;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <B>ButtonRenderer</B> is a class that renders the current value of
 * <code>UICommand<code> as a Button.
 */

public class ButtonRenderer extends BaseRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(ButtonRenderer.class);

    //
    // Class Variables
    //
    private static final String FORM_HAS_COMMAND_LINK_ATTR = 
         "com.sun.faces.FORM_HAS_COMMAND_LINK_ATTR";

    private static final String NO_COMMAND_LINK_FOUND_VALUE = 
         "com.sun.faces.NO_COMMAND_LINK_FOUND";

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
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
        
        // Was our command the one that caused this submission?
        String clientId = component.getClientId(context);
        Map<String,String> requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        if (requestParameterMap.get(clientId) == null) {
                return;
        }

        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);

        if (log.isDebugEnabled()) {
            log.debug("This command resulted in form submission " +
                      " ActionEvent queued " + actionEvent);
        }
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + component.getId());
        }
        return;
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " + component.getId() +
                          " since rendered attribute is set to false ");
            }
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();

        // for text with rect positioning..
        int dxi = 0, dyi = 0, xi = 0, yi = 0, heighti = 0;

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }
        writer.startElement("g", component);
        writeIdAttributeIfNecessary(context, writer, component);
        
        String type = (String)component.getAttributes().get("type");
        if (type != null && type.equals("submit")) {
	    UIComponent root = context.getViewRoot();
            UIComponent myForm = component;
            while (!(myForm instanceof UIForm) && root != myForm) {
                myForm = myForm.getParent();
            }
            String formMethodName = myForm.getClientId(context)+"_post(evt)";
            writer.writeAttribute("onclick", formMethodName, "onclick"); 
        } else {
            String onclick = (String)component.getAttributes().get("onclick");
            if (onclick != null) {
                writer.writeAttribute("onclick", onclick, "onclick");
            }
        }
        writer.writeText("\n    ", null);
        writer.startElement("rect", component);
        String width = (String)component.getAttributes().get("width");
        if (width != null) {
            writer.writeAttribute("width", width, "width");
        }
        String height = (String)component.getAttributes().get("height");
        if (height != null) {
            heighti = Integer.parseInt(height);
            writer.writeAttribute("height", height, "height");
        }
        String x = (String)component.getAttributes().get("x");
        if (x != null) {
            xi = Integer.parseInt(x);
            writer.writeAttribute("x", x, "x");
        }
        String y = (String)component.getAttributes().get("y");
        if (y != null) {
            yi = Integer.parseInt(y);
            writer.writeAttribute("y", y, "y");
        }
        String dx = (String)component.getAttributes().get("dx");
        if (dx != null) {
            dxi = Integer.parseInt(dx);
        }
        String dy = (String)component.getAttributes().get("dy");
        if (dy != null) {
            dyi = Integer.parseInt(dy);
        }
        String rx = (String)component.getAttributes().get("rx");
        if (rx != null) {
            writer.writeAttribute("rx", rx, "rx");
        }
        String ry = (String)component.getAttributes().get("ry");
        if (ry != null) {
            writer.writeAttribute("ry", ry, "ry");
        }
        String style = (String)component.getAttributes().get("style");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.endElement("rect");
        writer.writeText("\n    ", null);
        int tx = xi + dxi;
        int ty = yi + heighti - dyi; 
        writer.startElement("text", component);
        writer.writeAttribute("x", new Integer(tx), null);
        writer.writeAttribute("y", new Integer(ty), null);
        writer.writeAttribute("text-anchor", "middle", null);
        String labelStyle = (String)component.getAttributes().get("labelStyle");
        if (labelStyle != null) {
            writer.writeAttribute("style", labelStyle, "labelStyle");
        }
        writer.writeText(label, null);
        writer.endElement("text");
        writer.writeText("\n", null);

        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            // PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("g");
        writer.writeText("\n", null);
    }

    //
    // General Methods
    //

} // end of class ButtonRenderer
