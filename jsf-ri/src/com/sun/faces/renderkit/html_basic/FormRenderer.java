/*
 * $Id: FormRenderer.java,v 1.61 2003/09/24 23:28:51 craigmcc Exp $
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
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;

/**
 *
 *  <B>FormRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormRenderer.java,v 1.61 2003/09/24 23:28:51 craigmcc Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey(clientId)) {
	    ((UIForm)component).setSubmitted(true);
	} else {
	    ((UIForm)component).setSubmitted(false);
	}
    }


    public void encodeBegin(FacesContext context, UIComponent component) 
             throws IOException{
        String styleClass = null;         
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
        Assert.assert_it( writer != null );
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
	updateFormNumber(context, component);
    }

    /**

    * This method keeps track of the number of forms in a page.  This is
    * necessary for any renderer that needs to use Javascript, as in,
    * document.forms[N].

    */

    protected int updateFormNumber(FacesContext context, 
				   UIComponent component) {
        Map requestMap = context.getExternalContext().getRequestMap();
	int numForms = 0;
	Integer formsInt = null;
	// find out the current number of forms in the page.
	if (null != (formsInt = (Integer) 
		     requestMap.get(RIConstants.FORM_NUMBER_ATTR))) {
	    numForms = formsInt.intValue();
	}
	component.getAttributes().put(RIConstants.FORM_NUMBER_ATTR, 
	    formsInt = new Integer(numForms));
	requestMap.put(RIConstants.FORM_NUMBER_ATTR, 
            formsInt = new Integer(++numForms));
	return numForms;
    }
    
    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String getActionStr(FacesContext context) {
        String contextPath = context.getExternalContext().getRequestContextPath();
        StringBuffer sb = new StringBuffer(contextPath);
        sb.append(RIConstants.URL_PREFIX);
	    sb.append(context.getViewRoot().getViewId());
        return (context.getExternalContext().encodeURL(sb.toString()));
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
        Assert.assert_it(writer != null);

	// this hidden field will be checked in the decode method to determine if
	// this form has been submitted.
	//
        writer.startElement("input", component);
	writer.writeAttribute("type", "hidden", "type");
	writer.writeAttribute("name", component.getClientId(context), "clientId");
        writer.writeAttribute("value", component.getClientId(context), "value");

        writer.endElement("form");
    }

} // end of class FormRenderer
