/*
 * $Id: FormRenderer.java,v 1.89 2005/06/17 19:48:07 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <B>FormRenderer</B> is a class that renders a <code>UIForm<code> as a Form.
 */

public class FormRenderer extends HtmlBasicRenderer {

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

    public FormRenderer() {
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
        // Was our form the one that was submitted?  If so, we need to set
        // the indicator accordingly..
        //
        String clientId = component.getClientId(context);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "Begin decoding component " + component.getId());
        }
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        if (requestParameterMap.containsKey(clientId)) {
            ((UIForm) component).setSubmitted(true);
        } else {
            ((UIForm) component).setSubmitted(false);
        }
       if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, 
                    "End decoding component " + component.getId());
        }
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        String styleClass = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessageString(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
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
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        String clientId = component.getClientId(context);
        // since method and action are rendered here they are not added
        // to the pass through attributes in Util class.
        writer.startElement("form", component);
        writer.writeAttribute("id", clientId, "clientId");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("method", "post", null);
        writer.writeAttribute("action", getActionStr(context), null);
        if (null != (styleClass = (String)
            component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        String acceptcharset = null;
        if (null != (acceptcharset = (String)
            component.getAttributes().get("acceptcharset"))) {
            writer.writeAttribute("accept-charset", acceptcharset, 
                    "acceptcharset");
        }
        
        Util.renderPassThruAttributes(context, writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);
        writer.writeText("\n", null);
    }


    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String getActionStr(FacesContext context) {
        String viewId = context.getViewRoot().getViewId();
        String actionURL =
            context.getApplication().getViewHandler().
            getActionURL(context, viewId);
        return (context.getExternalContext().encodeActionURL(actionURL));
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
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        context.getApplication().getViewHandler().writeState(context);

        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        // this hidden field will be checked in the decode method to
        // determine if this form has been submitted.
        //
        writer.startElement("input", component);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        writer.writeAttribute("value", component.getClientId(context), "value");
        writer.endElement("input");

        writer.endElement("form");
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "End encoding component " + component.getId());
        }
    }

} // end of class FormRenderer
