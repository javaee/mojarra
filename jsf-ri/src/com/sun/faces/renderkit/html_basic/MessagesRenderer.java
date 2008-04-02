/*
 * $Id: MessagesRenderer.java,v 1.1 2003/11/10 21:28:38 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// MessagesRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.util.Assert;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.FacesMessage;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * <p><B>MessagesRenderer</B> handles rendering for the Messages<p>. 
 *
 * @version $Id
 */

public class MessagesRenderer extends HtmlBasicRenderer {
    //
    // Prviate/Protected Constants
    //
    private static final Log log = LogFactory.getLog(MessagesRenderer.class);
   
    //
    // Methods From Renderer
    //

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
        Iterator messageIter = null;        
        FacesMessage curMessage = null;
        ResponseWriter writer = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
       
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        String clientId = (String)component.getAttributes().get("for");
        //"for" attribute optional for Messages
        if (clientId != null ) {
            messageIter = context.getMessages(clientId);
        } else	{
            messageIter = context.getMessages();
        }

        Assert.assert_it(messageIter != null);

        String layout = (String) component.getAttributes().get("layout");
	boolean wroteTable = false;

	//Add style and class attributes to table. If layout attribute is not
	//present or layout is list just do the spans in a linear fashion.
        if ((layout != null) && (layout.equals("table"))) {
            writer.startElement("table", component);
            wroteTable = true;
        }

        while (messageIter.hasNext()) {
            curMessage = (FacesMessage) messageIter.next();

            String
                severityStyle = null,
                severityStyleClass = null;

            if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
                severityStyle = (String) component.getAttributes().get("infoStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("infoStyleClass");
            }
            else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
                severityStyle = (String) component.getAttributes().get("warnStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("warnStyleClass");
            }
            else if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                severityStyle = (String) component.getAttributes().get("errorStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("errorStyleClass");
            }
            else if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
                severityStyle = (String) component.getAttributes().get("fatalStyle");
                severityStyleClass = (String)
                    component.getAttributes().get("fatalStyleClass");
            }

	    String 
	        style = (String) component.getAttributes().get("style"),
	        styleClass = (String) component.getAttributes().get("styleClass");

            // if we have style and severityStyle
            if ((style != null) && (severityStyle != null)) {
                // severityStyle wins
                style = severityStyle;
            }
            // if we have no style, but do have severityStyle
            else if ((style == null) && (severityStyle != null)) {
                // severityStyle wins
                style = severityStyle;
            }

            // if we have styleClass and severityStyleClass
            if ((styleClass != null) && (severityStyleClass != null)) {
                // severityStyleClass wins
                styleClass = severityStyleClass;
            }
            // if we have no styleClass, but do have severityStyleClass
            else if ((styleClass == null) && (severityStyleClass != null)) {
                // severityStyleClass wins
                styleClass = severityStyleClass;
            }

            //Done intializing local variables. Move on to rendering.

            if (wroteTable) {
                writer.startElement("tr", component);
                writer.startElement("td", component);
            }

            boolean wroteSpan = false;

	    if (styleClass != null || style != null) {
                writer.startElement("span", component);
                wroteSpan = true;
	        if (null != styleClass) {
		    writer.writeAttribute("class", styleClass, "styleClass");
	        }
	        if (style != null) {
		    writer.writeAttribute("style", style, "style");
	        }
            } 

            Object tooltip = component.getAttributes().get("tooltip");
            boolean isTooltip = false;
            if (tooltip instanceof Boolean) {
                //if it's not a boolean can ignore it
                isTooltip = ((Boolean)tooltip).booleanValue();
            }

            boolean wroteTooltip = false;
            if (((UIMessages)component).isShowDetail() && 
                ((UIMessages)component).isShowSummary() &&
                isTooltip) {

                if (!wroteSpan) {
                     writer.startElement("span", component);
                     wroteTooltip = true;
                }
                writer.writeAttribute("title", curMessage.getSummary(), "title");
                writer.closeStartTag(component);

	        writer.writeText("\t", null);
	        writer.writeText(curMessage.getDetail(), null);
            } else if (wroteSpan) {
                writer.closeStartTag(component);

	        writer.writeText("\t", null);
	        writer.writeText(curMessage.getSummary(), null);
	        writer.writeText(" ", null);
	        writer.writeText(curMessage.getDetail(), null);
            }

	    if (wroteSpan || wroteTooltip) {
                writer.endElement("span");
	    }

            //close table row if present
            if (wroteTable) {
                writer.endElement("tr");
                writer.endElement("td");
            }

        } //messageIter

        //close table if present
        if (wroteTable) {
            writer.endElement("table");
        }
    }
    
} // end of class MessagesRenderer


