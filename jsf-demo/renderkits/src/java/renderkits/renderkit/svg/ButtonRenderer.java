/*
 * $Id: ButtonRenderer.java,v 1.1 2005/06/14 20:12:49 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ButtonRenderer.java

package renderkits.renderkit.svg;

import javax.faces.component.NamingContainer;
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
        // we don' have to worry about getting the value from request parameter
        // because we just need to know if this command caused the submission. We
        // can get the command name by calling currentValue. This way we can 
        // get around the IE bug.
        String clientId = component.getClientId(context);
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        String value = (String) requestParameterMap.get(clientId);
        if (value == null) {
            if (requestParameterMap.get(clientId + ".x") == null &&
                requestParameterMap.get(clientId + ".y") == null) {
                return;
            }
        }

        String type = (String) component.getAttributes().get("type");
        if ((type != null) && (type.toLowerCase().equals("reset"))) {
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
        
        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = (String) component.getAttributes().get("type");
        if (type == null) {
            type = "submit";
            // This is needed in the decode method
            component.getAttributes().put("type", type);
        }

        ResponseWriter writer = context.getResponseWriter();

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }
        writer.startElement("g", component);
        writeIdAttributeIfNecessary(context, writer, component);
        
	UIComponent root = context.getViewRoot();
        UIComponent myForm = component;
        while (!(myForm instanceof UIForm) && root != myForm) {
            myForm = myForm.getParent();
        }
        String formMethodName = myForm.getClientId(context)+"_post(evt)";

        // for text with rect positioning..
        int dxi = 0, dyi = 0, xi = 0, yi = 0, heighti = 0;

        writer.writeAttribute("onclick", formMethodName, "onclick"); 
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
        writer.writeText(label, null);
        writer.endElement("text");
        writer.writeText("\n", null);
        writer.endElement("g");
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
    }

    //
    // General Methods
    //

} // end of class ButtonRenderer
