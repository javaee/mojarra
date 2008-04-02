/*
 * $Id: LabelRenderer.java,v 1.21 2003/12/17 15:13:55 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LabelRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p><B>LabelRenderer</B> renders Label element.<p>. 
 *
 */
public class LabelRenderer extends HtmlBasicRenderer {
    //
    // Protected Constants
    //
    private static final Log log = LogFactory.getLog(LabelRenderer.class);
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private static final String RENDER_END_ELEMENT="com.sun.faces.RENDER_END_ELEMENT";
    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public LabelRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
	ResponseWriter writer = null;
	String 
	    forValue = null,
	    style = (String) component.getAttributes().get("style"),
	    styleClass = (String) component.getAttributes().get("styleClass");
	
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        writer = context.getResponseWriter();
        Util.doAssert(writer != null );

	forValue = (String) component.getAttributes().get("for");
        if ( forValue == null ) {
            if (log.isErrorEnabled()) {
                log.error(Util.getExceptionMessage(Util.NULL_FORVALUE_ID, 
                    new Object[]{ forValue }));
            }
            return;
        }
        
        UIComponent forComponent = getForComponent(context, forValue, component);
        if ( forComponent == null) {
            return;
        }
        
        // set a temporary attribute on the component to indicate that
        // label end element needs to be rendered.
        component.getAttributes().put(RENDER_END_ELEMENT, "yes");
	writer.startElement("label", component);
        String forClientId = forComponent.getClientId(context);
	writer.writeAttribute("for", forClientId, "for");

        Util.renderPassThruAttributes(writer, component);
	if (null != styleClass ){
            writer.writeAttribute("class", styleClass, "styleClass");
	}
        writer.writeText("\n", null);
        writer.flush();
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // render label end element if RENDER_END_ELEMENT is set.
        String render = (String) component.getAttributes().get(RENDER_END_ELEMENT);
        if ( render != null && render.equals("yes")) {
            component.getAttributes().remove(RENDER_END_ELEMENT);
            ResponseWriter writer = context.getResponseWriter();
            Util.doAssert(writer != null );
	    writer.endElement("label");
        }
    }

    // The testcase for this class is TestRenderResponsePhase.java

} // end of class LabelRenderer
