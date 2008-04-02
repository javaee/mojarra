/*
 * $Id: OutputLinkRenderer.java,v 1.11 2004/02/04 23:41:50 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OutputLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 *  <B>OutputLinkRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OutputLinkRenderer.java,v 1.11 2004/02/04 23:41:50 ofung Exp $
 */

public class OutputLinkRenderer extends HtmlBasicRenderer {
    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(OutputLinkRenderer.class);

    // Separator character

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
	    throw new NullPointerException(Util.getExceptionMessage(
				    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	// take no action, this is an Output component.
        if (log.isTraceEnabled()) {
            log.trace("No decoding necessary since the component " 
                + component.getId() + 
                " is not an instance or a sub class of UIInput");
        }
	return;
    }

    public boolean getRendersChildren() {
	return true;
    }

    protected Object getValue(UIComponent component) {
        return ((UIOutput) component).getValue();
    }

    private String clientId = null;
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + component.getId());
        }

	UIOutput output = (UIOutput) component;
	String hrefVal = getCurrentValue(context, component);
        if (log.isTraceEnabled()) {
            log.trace("Value to be rendered " + hrefVal);
        }

        // suppress rendering if "rendered" property on the output is
        // false
        if (!output.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component "
                + component.getId() + " since " +
                "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert( writer != null );
	writer.startElement("a", component);
	writeIdAttributeIfNecessary(context, writer, component);

        // Go no further if  we have no href.
        if (null == hrefVal || 0 == hrefVal.length()) {
            return;
        }

	clientId = output.getClientId(context);

	//Write Anchor attributes

        LinkRenderer.Param paramList[] = getParamList(context, component);
	StringBuffer sb = new StringBuffer();
	int 
	    i = 0,
	    len = paramList.length;
	sb = new StringBuffer();
	sb.append(hrefVal);
	if (0 < len) {
	    sb.append("?");
	}
        for (i = 0; i < len; i++) {
	    if (0 != i) {
		sb.append("&");
	    }
	    sb.append(paramList[i].getName());
	    sb.append("=");
	    sb.append(paramList[i].getValue());
	}	    
	writer.writeURIAttribute("href", 
				 context.getExternalContext().encodeResourceURL(sb.toString()), 
				 "href");
        Util.renderPassThruAttributes(writer, component);
        Util.renderBooleanPassThruAttributes(writer, component);

        //handle css style class
	String styleClass = (String)
            output.getAttributes().get("styleClass");
	if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
	writer.flush();

    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component "
                + component.getId() + " since " +
                "rendered attribute is set to false ");
            }
            return;
        }
	Iterator kids = component.getChildren().iterator();
	while (kids.hasNext()) {
	    UIComponent kid = (UIComponent) kids.next();
	    kid.encodeBegin(context);
	    if (kid.getRendersChildren()) {
		kid.encodeChildren(context);
	    }
	    kid.encodeEnd(context);
	}
    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isTraceEnabled()) {
            log.trace("End encoding " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component "
                + component.getId() + " since " +
                "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert( writer != null );

	//Write Anchor inline elements

        //Done writing Anchor element
        writer.endElement("a");
	return;
    }



} // end of class OutputLinkRenderer
