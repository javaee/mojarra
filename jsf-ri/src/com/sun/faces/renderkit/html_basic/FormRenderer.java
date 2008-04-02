/*
 * $Id: FormRenderer.java,v 1.72 2004/01/30 21:49:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <B>FormRenderer</B> is a class that renders a <code>UIForm<code> as a Form.
 */

public class FormRenderer extends HtmlBasicRenderer {
    //
    // Protected Constants
    //
    // Log instance for this class
     protected static Log log = LogFactory.getLog(FormRenderer.class);
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
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + clientId);
        }
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey(clientId)) {
	    ((UIForm)component).setSubmitted(true);
	} else {
	    ((UIForm)component).setSubmitted(false);
	}
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + clientId);
        }
    }


    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        String styleClass = null;   
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " + 
                component.getClientId(context));
        }
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                   Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert( writer != null );
        // since method and action are rendered here they are not added
        // to the pass through attributes in Util class.
	writer.startElement("form", component);
	writer.writeAttribute("id", component.getClientId(context), "clientId");
	writer.writeAttribute("method", "post", null);
	writer.writeAttribute("action", getActionStr(context), null);
        if (null != (styleClass = (String) 
		     component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
	}

        Util.renderPassThruAttributes(writer, component);
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

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
             throws IOException{
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }
	
	context.getApplication().getViewHandler().writeState(context);

        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        Util.doAssert(writer != null);

	// this hidden field will be checked in the decode method to determine if
	// this form has been submitted.
	//
        writer.startElement("input", component);
	writer.writeAttribute("type", "hidden", "type");
	writer.writeAttribute("name", component.getClientId(context), "clientId");
        writer.writeAttribute("value", component.getClientId(context), "value");
	writer.endElement("input");

        renderNeededHiddenFields(context, component);

        writer.endElement("form");
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + 
                    component.getClientId(context));
        }
    }


    /**
     * <p>Render any need hidden fields.</p>
     */
    private static void renderNeededHiddenFields(FacesContext context,
                                                 UIComponent component) 
        throws IOException {
      
        ResponseWriter writer = context.getResponseWriter();
        Map map = getHiddenFieldMap(context, false);
        if (map != null) {
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (Boolean.TRUE.equals(entry.getValue())) {
                    writer.startElement("input", component);
                    writer.writeAttribute("type", "hidden", null);
                    writer.writeAttribute("name", entry.getKey(), null);
                    writer.endElement("input");
                }
            }
                
            // Clear the hidden field map
            Map requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(HIDDEN_FIELD_KEY, null);
        }
    }

    /**
     * <p>Remember that we will need a new hidden field.</p>
     */
    public static void addNeededHiddenField(FacesContext context,
                                            String clientId) {
        Map map = getHiddenFieldMap(context, true);
        if (!map.containsKey(clientId)) {
            map.put(clientId, Boolean.TRUE);
        }
    }

    /**
     * <p>Note that a hidden field has already been rendered.</p>
     */
    public static void addRenderedHiddenField(FacesContext context,
                                              String clientId) {
        Map map = getHiddenFieldMap(context, true);
        map.put(clientId, Boolean.FALSE);
    }

    private static Map getHiddenFieldMap(FacesContext context,
                                         boolean createIfNew) {
        Map requestMap = context.getExternalContext().getRequestMap();
        Map map = (Map) requestMap.get(HIDDEN_FIELD_KEY);
        if (map == null) {
            if (createIfNew) {
                map = new HashMap();
                requestMap.put(HIDDEN_FIELD_KEY, map);
            }
        }

        return map;
    }

    private static final String HIDDEN_FIELD_KEY =
      RIConstants.FACES_PREFIX + "FormHiddenFieldMap";

} // end of class FormRenderer
